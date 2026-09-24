package com.artemis.socket;

import android.app.AlertDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONObject;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import artemis.socket.ArtemisSocketClient;
import artemis.socket.ArtemisSocketConfiguration;
import artemis.socket.ArtemisSocketListener;

/**
 * Small chat-only host application for verifying the Artemis Socket SDK.
 *
 * Configuration is loaded from the example asset so the screen matches the
 * Flutter example. Long-press the connection status to inspect the SDK trace
 * without making the normal chat experience look like a debug screen.
 */
public class ArtemisSocketDemoActivity extends AppCompatActivity implements ArtemisSocketListener {

    private static final String TAG = "ArtemisSocketDemo";
    private static final int STATE_NOT_CONNECTED = 0;
    private static final int STATE_CONNECTING = 1;
    private static final int STATE_CONNECTED = 2;
    private static final int STATE_ERROR = 3;

    private TextView tvConnectionStatus;
    private View connectionStatusBar;
    private ImageView ivConnectionIcon;
    private ProgressBar pbConnection;
    private EditText etMessage;
    private ImageButton btnSend;
    private RecyclerView rvChatMessages;
    private ChatMessageAdapter chatMessageAdapter;
    private final StringBuilder traceLog = new StringBuilder();
    private final Map<String, StringBuilder> streamingMessages = new HashMap<>();

    private String endpoint = "";
    private String projectId = "";
    private String channelId = "";
    private String channelName = "";
    private String apiKey = "";
    private ArtemisSocketClient socketClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_artemis_socket_demo);

        connectionStatusBar = findViewById(R.id.connectionStatusBar);
        ivConnectionIcon = findViewById(R.id.ivConnectionIcon);
        pbConnection = findViewById(R.id.pbConnection);
        tvConnectionStatus = findViewById(R.id.tvConnectionStatus);
        etMessage = findViewById(R.id.etMessage);
        btnSend = findViewById(R.id.btnSend);
        rvChatMessages = findViewById(R.id.rvChatMessages);
        ImageButton btnReconnect = findViewById(R.id.btnReconnect);

        chatMessageAdapter = new ChatMessageAdapter();
        rvChatMessages.setLayoutManager(new LinearLayoutManager(this));
        rvChatMessages.setAdapter(chatMessageAdapter);

        loadReferenceConfiguration();
        updateConnectionState(STATE_NOT_CONNECTED);

        btnReconnect.setOnClickListener(v -> connectToArtemis());
        btnSend.setOnClickListener(v -> sendMessageToArtemis());
        etMessage.setOnEditorActionListener((v, actionId, event) -> {
            sendMessageToArtemis();
            return true;
        });

        // Diagnostics stay out of the normal chat surface, like the Flutter example.
        connectionStatusBar.setOnLongClickListener(v -> {
            showTraceDialog();
            return true;
        });

        // Match the Flutter example: initialize and connect as soon as the screen opens.
        new Handler(Looper.getMainLooper()).post(this::connectToArtemis);
    }

    private void loadReferenceConfiguration() {
        try (InputStream inputStream = getAssets().open("artemis_example_config.properties")) {
            Properties properties = new Properties();
            properties.load(inputStream);
            loadConfigurationValues(properties);
            appendLog("Loaded Artemis values from artemis_example_config.properties.");

            try (InputStream localInputStream = getAssets().open("artemis_example_config.local.properties")) {
                Properties localProperties = new Properties();
                localProperties.load(localInputStream);
                loadConfigurationValues(localProperties);
                appendLog("Loaded local api_key override.");
            } catch (Exception ignored) {
                appendLog("No local api_key override found.");
            }
        } catch (Exception exception) {
            appendLog("Artemis configuration could not be loaded: " + exception.getMessage());
        }
    }

    private void loadConfigurationValues(Properties properties) {
        endpoint = valueOrCurrent(properties, "endpoint", endpoint);
        projectId = valueOrCurrent(properties, "project_id", projectId);
        channelId = valueOrCurrent(properties, "channel_id", channelId);
        channelName = valueOrCurrent(properties, "channel_name", channelName);
        apiKey = valueOrCurrent(properties, "api_key", apiKey);
    }

    private String valueOrCurrent(Properties properties, String key, String current) {
        String value = properties.getProperty(key, "").trim();
        return value.isEmpty() ? current : value;
    }

    private void connectToArtemis() {
        if (!isConfigured(endpoint) || !isConfigured(projectId) || !isConfigured(apiKey)
                || (!isConfigured(channelId) && !isConfigured(channelName))) {
            updateConnectionState(STATE_ERROR);
            showError(R.string.error_configuration_missing);
            return;
        }

        try {
            updateConnectionState(STATE_CONNECTING);
            ArtemisSocketConfiguration configuration = ArtemisSocketConfiguration.builder()
                    .endpoint(endpoint)
                    .projectId(projectId)
                    .apiKey(apiKey)
                    .channelId(channelId)
                    .channelName(channelName)
                    .reconnection(true, 5, 1000, 30000)
                    .build();

            if (socketClient != null) {
                socketClient.shutdown();
            }
            socketClient = new ArtemisSocketClient(configuration, this);
            appendLog("Starting Artemis SDK init: " + endpoint);
            socketClient.connect();
        } catch (Exception exception) {
            onError(exception);
        }
    }

    private boolean isConfigured(String value) {
        String normalized = value == null ? "" : value.trim();
        return !normalized.isEmpty() && !normalized.startsWith("YOUR_ARTEMIS_");
    }

    private void sendMessageToArtemis() {
        String message = etMessage.getText().toString().trim();
        if (message.isEmpty()) {
            return;
        }
        if (socketClient == null || !socketClient.isConnected()) {
            showError(R.string.error_socket_not_connected);
            return;
        }

        try {
            socketClient.sendMessage(message);
            addChatMessage("You", message, true);
            appendLog("Sent chat_message: " + message);
            etMessage.setText("");
        } catch (Exception exception) {
            onError(exception);
        }
    }

    private void showError(int messageResId) {
        Toast.makeText(this, messageResId, Toast.LENGTH_SHORT).show();
        appendLog(getString(messageResId));
    }

    private void updateConnectionState(int state) {
        runOnUiThread(() -> {
            int backgroundColor;
            int foregroundColor;
            String label;
            boolean showProgress = false;

            switch (state) {
                case STATE_CONNECTED:
                    backgroundColor = R.color.status_connected_background;
                    foregroundColor = R.color.status_connected_foreground;
                    label = getString(R.string.status_connected);
                    ivConnectionIcon.setImageResource(R.drawable.ic_check_circle);
                    break;
                case STATE_CONNECTING:
                    backgroundColor = R.color.status_connecting_background;
                    foregroundColor = R.color.status_connecting_foreground;
                    label = getString(R.string.status_connecting);
                    showProgress = true;
                    break;
                case STATE_ERROR:
                    backgroundColor = R.color.status_error_background;
                    foregroundColor = R.color.status_error_foreground;
                    label = getString(R.string.status_error);
                    ivConnectionIcon.setImageResource(R.drawable.ic_warning);
                    break;
                case STATE_NOT_CONNECTED:
                default:
                    backgroundColor = R.color.status_disconnected_background;
                    foregroundColor = R.color.status_disconnected_foreground;
                    label = getString(R.string.status_not_connected);
                    ivConnectionIcon.setImageResource(R.drawable.ic_warning);
                    break;
            }

            connectionStatusBar.setBackgroundColor(ContextCompat.getColor(this, backgroundColor));
            tvConnectionStatus.setText(label);
            tvConnectionStatus.setTextColor(ContextCompat.getColor(this, foregroundColor));
            ivConnectionIcon.setColorFilter(ContextCompat.getColor(this, foregroundColor));
            ivConnectionIcon.setVisibility(showProgress ? View.GONE : View.VISIBLE);
            pbConnection.setIndeterminateTintList(ContextCompat.getColorStateList(this, foregroundColor));
            pbConnection.setVisibility(showProgress ? View.VISIBLE : View.GONE);

            boolean connected = state == STATE_CONNECTED;
            etMessage.setEnabled(connected);
            btnSend.setEnabled(connected);
            btnSend.setAlpha(connected ? 1f : 0.55f);
        });
    }

    private void appendLog(String log) {
        synchronized (traceLog) {
            traceLog.append(log).append('\n');
        }
        Log.d(TAG, log);
    }

    private void showTraceDialog() {
        TextView traceView = new TextView(this);
        traceView.setText(traceLog.toString());
        traceView.setTextSize(12);
        traceView.setTextColor(ContextCompat.getColor(this, R.color.trace_text));
        traceView.setTypeface(android.graphics.Typeface.MONOSPACE);
        traceView.setPadding(24, 8, 24, 8);
        traceView.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_MULTI_LINE);

        new AlertDialog.Builder(this)
                .setTitle(R.string.logs_title)
                .setView(traceView)
                .setPositiveButton(android.R.string.ok, null)
                .show();
    }

    private void addChatMessage(String sender, String message, boolean userMessage) {
        runOnUiThread(() -> {
            chatMessageAdapter.addMessage(sender, message, userMessage);
            rvChatMessages.scrollToPosition(chatMessageAdapter.getItemCount() - 1);
        });
    }

    private void addIncomingChatMessage(String payload) {
        try {
            JSONObject json = new JSONObject(payload);
            String type = json.optString("type", "");

            // Only response_end becomes an assistant bubble. All transport,
            // status, action, acknowledgement, and lifecycle frames remain
            // diagnostics-only and are never rendered in the chat transcript.
            if ("response_chunk".equals(type)) {
                String messageId = json.optString("messageId", "");
                String chunk = firstString(json, "chunk", "content", "text");
                if (!messageId.isEmpty() && !chunk.isEmpty()) {
                    streamingMessages.computeIfAbsent(messageId, key -> new StringBuilder()).append(chunk);
                }
                return;
            }
            if (!"response_end".equals(type)) {
                return;
            }

            String messageId = json.optString("messageId", "");
            String content = firstString(json, "fullText", "text", "content");
            if (content.isEmpty() && json.has("contentEnvelope")) {
                JSONObject envelope = json.optJSONObject("contentEnvelope");
                if (envelope != null) {
                    content = firstString(envelope, "text", "content");
                }
            }
            if (content.isEmpty()) {
                StringBuilder streamed = streamingMessages.get(messageId);
                content = streamed == null ? "" : streamed.toString();
            }
            streamingMessages.remove(messageId);
            if (content.trim().isEmpty()) {
                return;
            }
            String finalContent = content;
            runOnUiThread(() -> {
                chatMessageAdapter.addMessage("Artemis", finalContent, false);
                rvChatMessages.scrollToPosition(chatMessageAdapter.getItemCount() - 1);
            });
        } catch (Exception ignored) {
            // Malformed or non-chat frames are diagnostics-only.
        }
    }

    private String firstString(JSONObject object, String... keys) {
        for (String key : keys) {
            Object value = object.opt(key);
            if (value instanceof String && !((String) value).trim().isEmpty()) {
                return ((String) value).trim();
            }
        }
        return "";
    }

    @Override
    public void onLog(String message) {
        appendLog(message);
    }

    @Override
    public void onConnecting() {
        updateConnectionState(STATE_CONNECTING);
    }

    @Override
    public void onConnected(String sessionId) {
        updateConnectionState(STATE_CONNECTED);
        appendLog("Artemis socket connected. Session: " + sessionId);
    }

    @Override
    public void onDisconnected(String reason) {
        streamingMessages.clear();
        updateConnectionState(STATE_NOT_CONNECTED);
        appendLog("Artemis socket disconnected: " + reason);
    }

    @Override
    public void onMessage(String payload) {
        addIncomingChatMessage(payload);
    }

    @Override
    public void onError(Throwable error) {
        updateConnectionState(STATE_ERROR);
        appendLog("Artemis socket error: " + error.getMessage());
    }

    @Override
    protected void onDestroy() {
        if (socketClient != null) {
            socketClient.shutdown();
        }
        super.onDestroy();
    }
}
