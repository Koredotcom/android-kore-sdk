package artemis.socket;

import android.os.Handler;
import android.os.Looper;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.util.Map;
import java.util.LinkedHashMap;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
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
    private static final long DEFAULT_FEEDBACK_TIMEOUT_MS = 10000L;
    private static final MediaType JSON = MediaType.get("application/json; charset=utf-8");
    private static final Gson GSON = new Gson();

    private final ArtemisSocketConfiguration configuration;
    private final ArtemisSocketListener listener;
    private final OkHttpClient httpClient;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private final Handler mainHandler = new Handler(Looper.getMainLooper());
    private final Map<String, PendingFeedback> pendingFeedback =
            new ConcurrentHashMap<>();

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
        failPendingFeedback("DISCONNECTED", "Connection closed before feedback acknowledgement");
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

    /**
     * Sends an interactive action frame. A normal chat message is never used
     * as a fallback for this operation.
     */
    public void submitAction(String actionId, String value,
                             Map<String, String> formData, String renderId) {
        if (actionId == null || actionId.trim().isEmpty()) {
            throw new IllegalArgumentException("actionId must not be empty");
        }
        if (formData != null) {
            for (Map.Entry<String, String> entry : formData.entrySet()) {
                if (entry.getKey() == null || entry.getKey().trim().isEmpty()
                        || entry.getValue() == null) {
                    throw new IllegalArgumentException("formData keys and values must not be null or blank");
                }
            }
        }
        WebSocket socket = requireConnectedSocket();

        JsonObject action = new JsonObject();
        action.addProperty("type", "action_submit");
        action.addProperty("actionId", actionId.trim());
        if (value != null) action.addProperty("value", value);
        if (formData != null) {
            action.add("formData", GSON.toJsonTree(new LinkedHashMap<>(formData)));
        }
        if (renderId != null) action.addProperty("renderId", renderId);
        if (!socket.send(GSON.toJson(action))) {
            throw new IllegalStateException("Artemis socket rejected the action");
        }
        notifyLog("WS action submitted action_id=" + actionId.trim());
    }

    /**
     * Sends feedback and reports its server acknowledgement (10 second default
     * timeout).
     */
    public void submitFeedback(String messageId, String ratingType, int ratingValue,
                               String feedbackText, String actionRenderId,
                               ArtemisFeedbackCallback callback) {
        submitFeedback(messageId, ratingType, ratingValue, feedbackText, actionRenderId,
                DEFAULT_FEEDBACK_TIMEOUT_MS, callback);
    }

    /** Sends feedback with a caller-selected acknowledgement timeout in milliseconds. */
    public void submitFeedback(String messageId, String ratingType, int ratingValue,
                               String feedbackText, String actionRenderId, long timeoutMs,
                               ArtemisFeedbackCallback callback) {
        if (messageId == null || messageId.trim().isEmpty()) {
            throw new IllegalArgumentException("messageId must not be empty");
        }
        if (!"thumbs".equals(ratingType) && !"star".equals(ratingType)) {
            throw new IllegalArgumentException("ratingType must be 'thumbs' or 'star'");
        }
        if (("thumbs".equals(ratingType) && ratingValue != 0 && ratingValue != 1)
                || ("star".equals(ratingType) && (ratingValue < 1 || ratingValue > 10))) {
            throw new IllegalArgumentException("ratingValue is outside the supported range");
        }
        if (timeoutMs <= 0) throw new IllegalArgumentException("timeoutMs must be positive");
        if (callback == null) throw new IllegalArgumentException("callback is required");
        WebSocket socket = requireConnectedSocket();

        String normalizedMessageId = messageId.trim();
        String key = feedbackKey(normalizedMessageId, actionRenderId);
        PendingFeedback pending = new PendingFeedback(callback);
        if (pendingFeedback.putIfAbsent(key, pending) != null) {
            throw new IllegalStateException("Feedback is already pending for this message");
        }
        try {
            synchronized (pending) {
                if (pendingFeedback.get(key) != pending) return;
                pending.timeout = () -> finishFeedback(
                        key, pending, null, "FEEDBACK_TIMEOUT",
                        "Feedback acknowledgement timed out");
                mainHandler.postDelayed(pending.timeout, timeoutMs);
                JsonObject feedback = new JsonObject();
                feedback.addProperty("type", "feedback.submit");
                feedback.addProperty("messageId", normalizedMessageId);
                feedback.addProperty("ratingType", ratingType);
                feedback.addProperty("ratingValue", ratingValue);
                if (feedbackText != null) {
                    feedback.addProperty("feedbackText", feedbackText);
                }
                if (actionRenderId != null) {
                    feedback.addProperty("actionRenderId", actionRenderId);
                }
                if (!socket.send(GSON.toJson(feedback))) {
                    finishFeedback(key, pending, null, "SEND_REJECTED",
                            "Artemis socket rejected the feedback");
                    return;
                }
                notifyLog("WS feedback submitted message_id=" + normalizedMessageId);
            }
        } catch (RuntimeException ignored) {
            finishFeedback(key, pending, null, "SEND_FAILED", "Feedback could not be sent");
        }
    }

    private WebSocket requireConnectedSocket() {
        WebSocket socket = webSocket;
        if (!connected || socket == null) {
            throw new IllegalStateException("Artemis socket is not connected");
        }
        return socket;
    }

    private static String feedbackKey(String messageId, String actionRenderId) {
        String renderId = actionRenderId == null ? "" : actionRenderId;
        return messageId.length() + ":" + messageId + renderId.length() + ":" + renderId;
    }

    private void handleFeedbackAcknowledgement(JsonObject payload) {
        String messageId = stringValue(payload, "messageId");
        if (isBlank(messageId)) return;
        JsonElement rawRenderId = payload.get("actionRenderId");
        String renderId = rawRenderId == null || rawRenderId.isJsonNull()
                ? null : rawRenderId.getAsString();
        String key = feedbackKey(messageId, renderId);
        PendingFeedback pending = pendingFeedback.get(key);
        if (pending == null) return;

        boolean success = payload.has("success") && payload.get("success").getAsBoolean();
        String feedbackId = stringValue(payload, "feedbackId");
        if (success && !isBlank(feedbackId)) {
            finishFeedback(key, pending, feedbackId, null, null);
            return;
        }

        JsonObject error = payload.has("error") && payload.get("error").isJsonObject()
                ? payload.getAsJsonObject("error") : null;
        String code = error == null ? "FEEDBACK_REJECTED" : stringValue(error, "code");
        String message = error == null ? "Feedback was rejected" : stringValue(error, "message");
        finishFeedback(key, pending, null,
                isBlank(code) ? "FEEDBACK_REJECTED" : code,
                isBlank(message) ? "Feedback was rejected" : message);
    }

    private void finishFeedback(String key, PendingFeedback pending,
                                String feedbackId, String errorCode,
                                String errorMessage) {
        synchronized (pending) {
            if (!pendingFeedback.remove(key, pending)) return;
            if (pending.timeout != null) mainHandler.removeCallbacks(pending.timeout);
        }
        mainHandler.post(() -> {
            try {
                if (errorCode == null) pending.callback.onSuccess(feedbackId);
                else pending.callback.onFailure(errorCode, errorMessage);
            } catch (RuntimeException callbackError) {
                notifyLog("FEEDBACK callback failed: " + callbackError.getClass().getSimpleName());
            }
        });
    }

    private void failPendingFeedback(String code, String message) {
        for (Map.Entry<String, PendingFeedback> entry : pendingFeedback.entrySet()) {
            finishFeedback(entry.getKey(), entry.getValue(), null, code, message);
        }
    }

    private static final class PendingFeedback {
        final ArtemisFeedbackCallback callback;
        volatile Runnable timeout;

        PendingFeedback(ArtemisFeedbackCallback callback) {
            this.callback = callback;
        }
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
            if ("feedback.ack".equals(stringValue(payload, "type"))) {
                handleFeedbackAcknowledgement(payload);
            }
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
