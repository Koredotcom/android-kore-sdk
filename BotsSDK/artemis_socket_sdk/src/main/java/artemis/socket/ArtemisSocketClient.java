package artemis.socket;

import android.os.Handler;
import android.os.Looper;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okhttp3.MediaType;

/**
 * Artemis socket client using the API-key bootstrap and SDK ticket protocol.
 *
 * <p>The flow matches the Artemis Flutter SDK: SDK init, WebSocket ticket,
 * then a ticket-authenticated connection to {@code /ws/sdk}.</p>
 */
public final class ArtemisSocketClient {
    private static final MediaType JSON = MediaType.get("application/json; charset=utf-8");
    private static final Gson GSON = new Gson();

    private final ArtemisSocketConfiguration configuration;
    private final ArtemisSocketListener listener;
    private final OkHttpClient httpClient;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private final Handler mainHandler = new Handler(Looper.getMainLooper());

    private volatile WebSocket webSocket;
    private volatile boolean connected;
    private volatile boolean disconnectRequested;
    private volatile String sessionId;
    private int reconnectAttempts;

    public ArtemisSocketClient(ArtemisSocketConfiguration configuration, ArtemisSocketListener listener) {
        if (configuration == null) {
            throw new IllegalArgumentException("configuration is required");
        }
        if (listener == null) {
            throw new IllegalArgumentException("listener is required");
        }
        this.configuration = configuration;
        this.listener = listener;
        this.httpClient = new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(0, TimeUnit.MILLISECONDS)
                .build();
    }

    public void connect() {
        if (connected || webSocket != null) {
            notifyLog("CLIENT connect ignored: socket is already active");
            return;
        }
        disconnectRequested = false;
        reconnectAttempts = 0;
        notifyLog("CLIENT connect requested for project_id=" + configuration.getProjectId());
        notifyConnecting();
        executor.execute(this::startSession);
    }

    public void disconnect() {
        disconnectRequested = true;
        connected = false;
        notifyLog("CLIENT disconnect requested");
        WebSocket socket = webSocket;
        webSocket = null;
        if (socket != null) {
            socket.close(1000, "Client disconnect");
        }
        notifyDisconnected("Client disconnect");
    }

    public boolean isConnected() {
        return connected;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void sendMessage(String text) {
        if (text == null || text.trim().isEmpty()) {
            throw new IllegalArgumentException("message must not be empty");
        }
        WebSocket socket = webSocket;
        if (!connected || socket == null) {
            throw new IllegalStateException("Artemis socket is not connected");
        }

        JsonObject message = new JsonObject();
        message.addProperty("type", "chat_message");
        message.addProperty("text", text);
        message.addProperty("messageId", UUID.randomUUID().toString());
        if (sessionId != null && !sessionId.isEmpty()) {
            message.addProperty("sessionId", sessionId);
        }
        if (!socket.send(GSON.toJson(message))) {
            throw new IllegalStateException("Artemis socket rejected the message");
        }
        notifyLog("WS SEND " + GSON.toJson(message));
    }

    public void shutdown() {
        disconnect();
        executor.shutdownNow();
        httpClient.dispatcher().executorService().shutdown();
        httpClient.connectionPool().evictAll();
    }

    private void startSession() {
        try {
            notifyLog("AUTH starting Artemis SDK session");
            String sdkToken = requestSdkToken();
            String ticket = requestWebSocketTicket(sdkToken);
            openWebSocket(ticket);
        } catch (Throwable error) {
            notifyLog("AUTH failed: " + error.getMessage());
            notifyError(error);
            scheduleReconnect();
        }
    }

    private String requestSdkToken() throws IOException {
        JsonObject body = new JsonObject();
        if (!isBlank(configuration.getChannelId())) {
            body.addProperty("channelId", configuration.getChannelId());
        } else if (!isBlank(configuration.getChannelName())) {
            body.addProperty("channelName", configuration.getChannelName());
        }

        Request request = new Request.Builder()
                .url(httpEndpoint() + "/api/v1/sdk/init")
                .header("Content-Type", "application/json")
                .header("X-Public-Key", configuration.getApiKey())
                .post(RequestBody.create(GSON.toJson(body), JSON))
                .build();

        notifyLog("API REQUEST POST " + request.url() + "\nHeaders: X-Public-Key=<redacted>\nBody: " + GSON.toJson(body));

        try (Response response = httpClient.newCall(request).execute()) {
            String responseBody = response.body() == null ? "" : response.body().string();
            notifyLog("API RESPONSE " + response.code() + " " + request.url() + "\nBody: " + redactSensitiveJson(responseBody));
            if (!response.isSuccessful()) {
                throw new IOException("SDK init failed (" + response.code() + "): " + responseBody);
            }
            JsonObject payload = parseObject(responseBody, "SDK init");
            String token = stringValue(payload, "token");
            if (isBlank(token)) {
                throw new IOException("SDK init response did not contain a token");
            }
            return token;
        }
    }

    private String requestWebSocketTicket(String sdkToken) throws IOException {
        Request request = new Request.Builder()
                .url(httpEndpoint() + "/api/v1/sdk/ws-ticket")
                .header("Content-Type", "application/json")
                .header("X-SDK-Token", sdkToken)
                .post(RequestBody.create("{}", JSON))
                .build();

        notifyLog("API REQUEST POST " + request.url() + "\nHeaders: X-SDK-Token=<redacted>\nBody: {}");

        try (Response response = httpClient.newCall(request).execute()) {
            String responseBody = response.body() == null ? "" : response.body().string();
            notifyLog("API RESPONSE " + response.code() + " " + request.url() + "\nBody: " + redactSensitiveJson(responseBody));
            if (!response.isSuccessful()) {
                throw new IOException("WebSocket ticket failed (" + response.code() + "): " + responseBody);
            }
            JsonObject payload = parseObject(responseBody, "WebSocket ticket");
            String ticket = stringValue(payload, "ticket");
            if (isBlank(ticket)) {
                throw new IOException("WebSocket ticket response did not contain a ticket");
            }
            return ticket;
        }
    }

    private void openWebSocket(String ticket) {
        Request request = new Request.Builder()
                .url(webSocketEndpoint() + "/ws/sdk")
                .header("Sec-WebSocket-Protocol", "sdk-ticket, " + ticket)
                .build();

        notifyLog("WS CONNECT " + request.url() + "\nSubprotocol: sdk-ticket, <redacted>");

        webSocket = httpClient.newWebSocket(request, new WebSocketListener() {
            @Override
            public void onOpen(WebSocket socket, Response response) {
                notifyLog("WS OPEN HTTP " + response.code());
                notifyConnecting();
            }

            @Override
            public void onMessage(WebSocket socket, String text) {
                notifyLog("WS RECEIVE " + text);
                handleMessage(text);
            }

            @Override
            public void onClosing(WebSocket socket, int code, String reason) {
                notifyLog("WS CLOSING code=" + code + " reason=" + reason);
                socket.close(code, reason);
            }

            @Override
            public void onClosed(WebSocket socket, int code, String reason) {
                notifyLog("WS CLOSED code=" + code + " reason=" + reason);
                connected = false;
                webSocket = null;
                notifyDisconnected(reason);
                scheduleReconnect();
            }

            @Override
            public void onFailure(WebSocket socket, Throwable error, Response response) {
                notifyLog("WS FAILURE " + error.getMessage());
                connected = false;
                webSocket = null;
                notifyError(error);
                scheduleReconnect();
            }
        });
    }

    private void handleMessage(String text) {
        try {
            JsonObject payload = JsonParser.parseString(text).getAsJsonObject();
            if ("session_start".equals(stringValue(payload, "type"))) {
                sessionId = stringValue(payload, "sessionId");
                connected = true;
                reconnectAttempts = 0;
                notifyConnected(sessionId);
                notifyLog("SESSION STARTED session_id=" + sessionId);
            }
        } catch (RuntimeException error) {
            notifyError(error);
        }
        notifyMessage(text);
    }

    private void scheduleReconnect() {
        if (disconnectRequested || !configuration.isReconnectionEnabled()
                || reconnectAttempts >= configuration.getMaxReconnectAttempts()) {
            return;
        }
        long delay = configuration.getReconnectBaseDelayMs() * (1L << Math.min(reconnectAttempts, 30));
        final long reconnectDelay = Math.min(delay, configuration.getReconnectMaxDelayMs());
        reconnectAttempts++;
        notifyLog("RECONNECT scheduled attempt=" + reconnectAttempts + "/"
                + configuration.getMaxReconnectAttempts() + " delay_ms=" + reconnectDelay);
        executor.execute(() -> {
            try {
                Thread.sleep(reconnectDelay);
            } catch (InterruptedException interruptedException) {
                Thread.currentThread().interrupt();
                return;
            }
            if (!disconnectRequested) {
                notifyConnecting();
                startSession();
            }
        });
    }

    private JsonObject parseObject(String body, String operation) throws IOException {
        try {
            JsonElement parsed = JsonParser.parseString(body);
            if (!parsed.isJsonObject()) {
                throw new IOException(operation + " response was not an object");
            }
            return parsed.getAsJsonObject();
        } catch (RuntimeException error) {
            throw new IOException(operation + " response was not valid JSON", error);
        }
    }

    private String httpEndpoint() {
        String endpoint = stripTrailingSlash(configuration.getEndpoint());
        if (endpoint.startsWith("wss://")) {
            return "https://" + endpoint.substring("wss://".length());
        }
        if (endpoint.startsWith("ws://")) {
            return "http://" + endpoint.substring("ws://".length());
        }
        return endpoint;
    }

    private String webSocketEndpoint() {
        String endpoint = httpEndpoint();
        if (endpoint.startsWith("https://")) {
            return "wss://" + endpoint.substring("https://".length());
        }
        return "ws://" + endpoint.substring("http://".length());
    }

    private static String stripTrailingSlash(String value) {
        String endpoint = value == null ? "" : value.trim();
        while (endpoint.endsWith("/")) {
            endpoint = endpoint.substring(0, endpoint.length() - 1);
        }
        return endpoint;
    }

    private static String stringValue(JsonObject object, String key) {
        JsonElement value = object.get(key);
        return value == null || value.isJsonNull() ? "" : value.getAsString();
    }

    private static boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }

    private static String redactSensitiveJson(String body) {
        if (isBlank(body)) {
            return "<empty>";
        }
        try {
            JsonElement parsed = JsonParser.parseString(body);
            redactSensitiveFields(parsed);
            return GSON.toJson(parsed);
        } catch (RuntimeException ignored) {
            return "<non-json response>";
        }
    }

    private static void redactSensitiveFields(JsonElement element) {
        if (element == null || element.isJsonNull()) {
            return;
        }
        if (element.isJsonArray()) {
            for (JsonElement child : element.getAsJsonArray()) {
                redactSensitiveFields(child);
            }
            return;
        }
        if (!element.isJsonObject()) {
            return;
        }
        for (String key : new String[]{"token", "ticket", "api_key", "apiKey", "sdkToken", "bootstrapToken"}) {
            if (element.getAsJsonObject().has(key)) {
                element.getAsJsonObject().addProperty(key, "<redacted>");
            }
        }
        for (JsonElement child : element.getAsJsonObject().asMap().values()) {
            redactSensitiveFields(child);
        }
    }

    private void notifyLog(String message) {
        mainHandler.post(() -> listener.onLog(message));
    }

    private void notifyConnecting() {
        mainHandler.post(listener::onConnecting);
    }

    private void notifyConnected(String id) {
        mainHandler.post(() -> listener.onConnected(id));
    }

    private void notifyDisconnected(String reason) {
        mainHandler.post(() -> listener.onDisconnected(reason));
    }

    private void notifyMessage(String payload) {
        mainHandler.post(() -> listener.onMessage(payload));
    }

    private void notifyError(Throwable error) {
        mainHandler.post(() -> listener.onError(error));
    }
}
