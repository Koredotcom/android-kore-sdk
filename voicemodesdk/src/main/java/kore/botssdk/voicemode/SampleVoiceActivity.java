package kore.botssdk.voicemode;

import android.Manifest;
import android.app.AlertDialog;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.WindowCompat;

import java.util.ArrayList;
import java.util.List;

/**
 * Sample Voice Activity demonstrating the WebRTC SDK usage.
 * This activity provides a complete UI for making voice calls with:
 * - Call/Hangup functionality
 * - Mute/Unmute control
 * - Speaker toggle
 * - Connection status display
 *
 * <p>To use this activity, you can either:
 * <ul>
 *   <li>Start it directly with default credentials</li>
 *   <li>Pass custom credentials via Intent extras</li>
 *   <li>Override {@link #getWebRTCConfig()} in a subclass</li>
 * </ul>
 */
public class SampleVoiceActivity extends AppCompatActivity {

    private static final String TAG = "SampleVoiceActivity";
    private static final int PERMISSION_REQUEST_CODE = 1001;

    // Intent extra keys
    public static final String EXTRA_BOT_ID = "bot_id";
    public static final String EXTRA_CLIENT_ID = "client_id";
    public static final String EXTRA_CLIENT_SECRET = "client_secret";
    public static final String EXTRA_IDENTITY = "identity";
    public static final String EXTRA_WEBSOCKET_URL = "websocket_url";
    public static final String EXTRA_JWT_SERVICE_URL = "jwt_service_url";
    public static final String EXTRA_SERVER_URL = "server_url";
    public static final String EXTRA_SIP_DOMAIN = "sip_domain";

    // UI Components
    private View statusIndicator;
    private TextView tvStatus;
    private FrameLayout avatarFrame;
    private LinearLayout btnMute;
    private TextView tvMuteIcon;
    private TextView tvMuteLabel;
    private LinearLayout btnSpeaker;
    private TextView tvSpeakerIcon;
    private TextView tvSpeakerLabel;
    private LinearLayout btnCall;
    private TextView tvCallIcon;
    private TextView tvCallLabel;

    // State
    private boolean isInCall = false;
    private boolean isConnecting = false;
    private boolean isMuted = false;
    private boolean isSpeakerOn = true;

    // WebRTC Client
    private WebRTCClient webRTCClient;
    private CallSession currentSession;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setupWindow();
        setContentView(R.layout.activity_sample_voice);

        initViews();
        setupClickListeners();

        webRTCClient = new WebRTCClient(this);
        webRTCClient.setListener(new WebRTCEventListener());
        webRTCClient.setDebugEnabled(true);
    }

    private void setupWindow() {
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.voice_background));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            WindowCompat.setDecorFitsSystemWindows(window, false);
        }
    }

    private void initViews() {
        statusIndicator = findViewById(R.id.statusIndicator);
        tvStatus = findViewById(R.id.tvStatus);
        avatarFrame = findViewById(R.id.avatarFrame);

        btnMute = findViewById(R.id.btnMute);
        tvMuteIcon = findViewById(R.id.tvMuteIcon);
        tvMuteLabel = findViewById(R.id.tvMuteLabel);

        btnSpeaker = findViewById(R.id.btnSpeaker);
        tvSpeakerIcon = findViewById(R.id.tvSpeakerIcon);
        tvSpeakerLabel = findViewById(R.id.tvSpeakerLabel);

        btnCall = findViewById(R.id.btnCall);
        tvCallIcon = findViewById(R.id.tvCallIcon);
        tvCallLabel = findViewById(R.id.tvCallLabel);
    }

    private void setupClickListeners() {
        btnCall.setOnClickListener(v -> handleCallToggle());
        btnMute.setOnClickListener(v -> handleMuteToggle());
        btnSpeaker.setOnClickListener(v -> handleSpeakerToggle());
    }

    /**
     * Get the WebRTC configuration.
     * Override this method in a subclass to provide custom credentials,
     * or pass credentials via Intent extras.
     *
     * @return WebRTCConfig instance
     */
    protected WebRTCConfig getWebRTCConfig() {
        String botId = getIntent().getStringExtra(EXTRA_BOT_ID);
        String clientId = getIntent().getStringExtra(EXTRA_CLIENT_ID);
        String clientSecret = getIntent().getStringExtra(EXTRA_CLIENT_SECRET);
        String identity = getIntent().getStringExtra(EXTRA_IDENTITY);
        String webSocketUrl = getIntent().getStringExtra(EXTRA_WEBSOCKET_URL);
        String jwtServiceUrl = getIntent().getStringExtra(EXTRA_JWT_SERVICE_URL);
        String serverUrl = getIntent().getStringExtra(EXTRA_SERVER_URL);
        String sipDomain = getIntent().getStringExtra(EXTRA_SIP_DOMAIN);

        // Use defaults if not provided via intent

        if (botId == null || botId.isEmpty() || clientId == null || clientId.isEmpty() || clientSecret == null || clientSecret.isEmpty() ||
                identity == null || identity.isEmpty() ||
                webSocketUrl == null || webSocketUrl.isEmpty() ||
                jwtServiceUrl == null || jwtServiceUrl.isEmpty() ||
                serverUrl == null || serverUrl.isEmpty()
        ) {
            Toast.makeText(this, "Please check the bot configuration!", Toast.LENGTH_LONG).show();
            finish();
        }

        return new WebRTCConfig.Builder()
                .botId(botId)
                .clientId(clientId)
                .clientSecret(clientSecret)
                .identity(identity)
                .webSocketUrl(webSocketUrl)
                .jwtServiceUrl(jwtServiceUrl)
                .serverUrl(serverUrl)
                .sipDomain(sipDomain)
                .debug(true)
                .autoRegister(true)
                .autoCall(true)
                .build();
    }

    private void handleCallToggle() {
        if (isConnecting) {
            return;
        }

        if (isInCall) {
            handleHangup();
        } else {
            requestPermissionsAndCall();
        }
    }

    private void requestPermissionsAndCall() {
        List<String> permissionsNeeded = new ArrayList<>();

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)
                != PackageManager.PERMISSION_GRANTED) {
            permissionsNeeded.add(Manifest.permission.RECORD_AUDIO);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT)
                    != PackageManager.PERMISSION_GRANTED) {
                permissionsNeeded.add(Manifest.permission.BLUETOOTH_CONNECT);
            }
        }

        if (permissionsNeeded.isEmpty()) {
            initializeCall();
        } else {
            ActivityCompat.requestPermissions(this,
                    permissionsNeeded.toArray(new String[0]),
                    PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PERMISSION_REQUEST_CODE) {
            boolean micGranted = false;
            boolean bluetoothGranted = true;

            for (int i = 0; i < permissions.length; i++) {
                if (Manifest.permission.RECORD_AUDIO.equals(permissions[i])) {
                    micGranted = grantResults[i] == PackageManager.PERMISSION_GRANTED;
                }
                if (Manifest.permission.BLUETOOTH_CONNECT.equals(permissions[i])) {
                    bluetoothGranted = grantResults[i] == PackageManager.PERMISSION_GRANTED;
                }
            }

            Log.d(TAG, "Permission results: mic=" + micGranted + ", bluetooth=" + bluetoothGranted);

            if (micGranted) {
                initializeCall();
            } else {
                boolean shouldShowRationale = ActivityCompat.shouldShowRequestPermissionRationale(
                        this, Manifest.permission.RECORD_AUDIO);

                if (!shouldShowRationale) {
                    showPermissionDeniedDialog();
                } else {
                    updateStatus(getString(R.string.voice_status_permission_denied));
                }
            }
        }
    }

    private void showPermissionDeniedDialog() {
        new AlertDialog.Builder(this)
                .setTitle(R.string.voice_permission_title)
                .setMessage(R.string.voice_permission_message)
                .setPositiveButton(android.R.string.ok, null)
                .show();
    }

    private void initializeCall() {
        isConnecting = true;
        updateStatus(getString(R.string.voice_status_initializing));
        updateCallButton();

        try {
            WebRTCConfig config = getWebRTCConfig();
            webRTCClient.initWithCredentials(config);
        } catch (Exception e) {
            Log.e(TAG, "Failed to initialize call", e);
            isConnecting = false;
            updateStatus("Failed: " + e.getMessage());
            updateCallButton();
        }
    }

    private void handleHangup() {
        if (webRTCClient != null) {
            webRTCClient.hangup();
        }

        resetCallState();
        updateStatus(getString(R.string.voice_status_ready));
    }

    private void handleMuteToggle() {
        if (!isInCall || webRTCClient == null) {
            return;
        }

        if (isMuted) {
            webRTCClient.unmute();
            isMuted = false;
        } else {
            webRTCClient.mute();
            isMuted = true;
        }

        updateMuteButton();
    }

    private void handleSpeakerToggle() {
        if (!isInCall || webRTCClient == null) {
            return;
        }

        isSpeakerOn = webRTCClient.toggleSpeaker();
        updateSpeakerButton();
    }

    private void resetCallState() {
        isInCall = false;
        isConnecting = false;
        isMuted = false;
        currentSession = null;

        updateUI();
    }

    private void updateUI() {
        updateStatusIndicator();
        updateAvatarFrame();
        updateMuteButton();
        updateSpeakerButton();
        updateCallButton();
    }

    private void updateStatus(String status) {
        runOnUiThread(() -> tvStatus.setText(status));
    }

    private void updateStatusIndicator() {
        runOnUiThread(() -> {
            if (isInCall) {
                statusIndicator.setBackgroundResource(R.drawable.bg_status_indicator_active);
            } else {
                statusIndicator.setBackgroundResource(R.drawable.bg_status_indicator_inactive);
            }
        });
    }

    private void updateAvatarFrame() {
        runOnUiThread(() -> {
            if (isInCall) {
                avatarFrame.setBackgroundResource(R.drawable.bg_avatar_active);
            } else {
                avatarFrame.setBackgroundResource(R.drawable.bg_avatar_inactive);
            }
        });
    }

    private void updateMuteButton() {
        runOnUiThread(() -> {
            if (isInCall) {
                btnMute.setEnabled(true);
                btnMute.setAlpha(1.0f);

                if (isMuted) {
                    btnMute.setBackgroundResource(R.drawable.bg_control_button_active);
                    tvMuteIcon.setText("🔇");
                    tvMuteLabel.setText(R.string.voice_btn_unmute);
                } else {
                    btnMute.setBackgroundResource(R.drawable.bg_control_button_inactive);
                    tvMuteIcon.setText("🎤");
                    tvMuteLabel.setText(R.string.voice_btn_mute);
                }
                tvMuteLabel.setTextColor(ContextCompat.getColor(this, R.color.voice_text_white));
            } else {
                btnMute.setEnabled(false);
                btnMute.setAlpha(0.5f);
                btnMute.setBackgroundResource(R.drawable.bg_control_button_disabled);
                tvMuteIcon.setText("🎤");
                tvMuteLabel.setText(R.string.voice_btn_mute);
                tvMuteLabel.setTextColor(ContextCompat.getColor(this, R.color.voice_text_disabled));
            }
        });
    }

    private void updateSpeakerButton() {
        runOnUiThread(() -> {
            if (isInCall) {
                btnSpeaker.setEnabled(true);
                btnSpeaker.setAlpha(1.0f);

                if (isSpeakerOn) {
                    btnSpeaker.setBackgroundResource(R.drawable.bg_control_button_active);
                    tvSpeakerIcon.setText("🔊");
                    tvSpeakerLabel.setText(R.string.voice_btn_speaker_on);
                } else {
                    btnSpeaker.setBackgroundResource(R.drawable.bg_control_button_inactive);
                    tvSpeakerIcon.setText("🔈");
                    tvSpeakerLabel.setText(R.string.voice_btn_speaker_off);
                }
                tvSpeakerLabel.setTextColor(ContextCompat.getColor(this, R.color.voice_text_white));
            } else {
                btnSpeaker.setEnabled(false);
                btnSpeaker.setAlpha(0.5f);
                btnSpeaker.setBackgroundResource(R.drawable.bg_control_button_disabled);
                tvSpeakerIcon.setText("🔊");
                tvSpeakerLabel.setText(R.string.voice_btn_speaker_on);
                tvSpeakerLabel.setTextColor(ContextCompat.getColor(this, R.color.voice_text_disabled));
            }
        });
    }

    private void updateCallButton() {
        runOnUiThread(() -> {
            if (isConnecting) {
                btnCall.setBackgroundResource(R.drawable.bg_connecting_button);
                tvCallIcon.setText("⏳");
                tvCallLabel.setText(R.string.voice_btn_connecting);
                btnCall.setEnabled(false);
            } else if (isInCall) {
                btnCall.setBackgroundResource(R.drawable.bg_hangup_button);
                tvCallIcon.setText("📞");
                tvCallLabel.setText(R.string.voice_btn_hangup);
                btnCall.setEnabled(true);
            } else {
                btnCall.setBackgroundResource(R.drawable.bg_call_button);
                tvCallIcon.setText("📞");
                tvCallLabel.setText(R.string.voice_btn_call);
                btnCall.setEnabled(true);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (webRTCClient != null) {
            webRTCClient.disconnect();
            webRTCClient = null;
        }
    }

    @Override
    public void onBackPressed() {
        if (isInCall) {
            new AlertDialog.Builder(this)
                    .setTitle("End Call?")
                    .setMessage("Do you want to end the current call?")
                    .setPositiveButton("End Call", (dialog, which) -> {
                        handleHangup();
                        super.onBackPressed();
                    })
                    .setNegativeButton("Cancel", null)
                    .show();
        } else {
            super.onBackPressed();
        }
    }

    /**
     * WebRTC Event Listener implementation
     */
    private class WebRTCEventListener extends WebRTCListenerAdapter {

        @Override
        public void onConnected() {
            Log.d(TAG, "WebSocket connected");
        }

        @Override
        public void onDisconnected() {
            Log.d(TAG, "WebSocket disconnected");
            runOnUiThread(() -> {
                if (!isFinishing()) {
                    resetCallState();
                    updateStatus(getString(R.string.voice_status_ready));
                }
            });
        }

        @Override
        public void onRegistered() {
            Log.d(TAG, "SIP registered");
            runOnUiThread(() -> {
                if (!isFinishing()) {
                    updateStatus(getString(R.string.voice_status_registered));
                }
            });
        }

        @Override
        public void onRegistrationFailed(String error) {
            Log.e(TAG, "Registration failed: " + error);
            runOnUiThread(() -> {
                if (!isFinishing()) {
                    isConnecting = false;
                    updateStatus("Registration failed: " + error);
                    updateCallButton();
                }
            });
        }

        @Override
        public void onCallProgress() {
            Log.d(TAG, "Call progress - ringing");
            runOnUiThread(() -> {
                if (!isFinishing()) {
                    isConnecting = false;
                    isInCall = true;
                    updateStatus(getString(R.string.voice_status_ringing));
                    updateUI();
                }
            });
        }

        @Override
        public void onCallAccepted() {
            Log.d(TAG, "Call accepted");
            runOnUiThread(() -> {
                if (!isFinishing()) {
                    isConnecting = false;
                    isInCall = true;
                    updateStatus(getString(R.string.voice_status_in_call));
                    updateUI();
                }
            });
        }

        @Override
        public void onCallConfirmed() {
            Log.d(TAG, "Call confirmed - media established");
            runOnUiThread(() -> {
                if (!isFinishing()) {
                    isConnecting = false;
                    isInCall = true;
                    updateStatus(getString(R.string.voice_status_connected));
                    updateUI();
                }
            });
        }

        @Override
        public void onCallEnded(String cause) {
            Log.d(TAG, "Call ended: " + cause);
            runOnUiThread(() -> {
                if (!isFinishing()) {
                    resetCallState();
                    updateStatus(getString(R.string.voice_status_call_ended));
                    updateUI();
                }
            });
        }

        @Override
        public void onCallFailed(String cause) {
            Log.e(TAG, "Call failed: " + cause);
            runOnUiThread(() -> {
                if (!isFinishing()) {
                    isConnecting = false;
                    isInCall = false;
                    updateStatus("Failed: " + cause);
                    updateUI();
                }
            });
        }

        @Override
        public void onIncomingCall(CallSession session) {
            Log.d(TAG, "Incoming call from: " + session.getRemoteUri());
            currentSession = session;
        }

        @Override
        public void onError(String error) {
            Log.e(TAG, "Error: " + error);
            runOnUiThread(() -> {
                if (!isFinishing()) {
                    isConnecting = false;
                    updateStatus("Error: " + error);
                    updateCallButton();
                }
            });
        }
    }
}
