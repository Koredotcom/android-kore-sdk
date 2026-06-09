package kore.botssdk.voicemode;

import android.Manifest;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.AlertDialog;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;

import java.util.ArrayList;
import java.util.List;

import kore.botssdk.voicemode.listener.VoiceModeCallback;
import kore.botssdk.voicemode.viewmodel.VoiceModeViewModel;

/**
 * Full-screen voice mode activity with WebRTC.
 * Shows animated connecting circle or animated orb based on connection state.
 *
 * <p>Credentials can be passed via Intent extras or by overriding {@link #getWebRTCConfig()}.
 */
public class SampleVoiceActivity extends AppCompatActivity implements VoiceModeCallback {

    private static final int PERMISSION_REQUEST_CODE = 1001;

    public static final String EXTRA_BOT_ID = "bot_id";
    public static final String EXTRA_CLIENT_ID = "client_id";
    public static final String EXTRA_CLIENT_SECRET = "client_secret";
    public static final String EXTRA_IDENTITY = "identity";
    public static final String EXTRA_WEBSOCKET_URL = "websocket_url";
    public static final String EXTRA_JWT_SERVICE_URL = "jwt_service_url";
    public static final String EXTRA_SERVER_URL = "server_url";
    public static final String EXTRA_SIP_DOMAIN = "sip_domain";

    private VoiceModeViewModel viewModel;

    private View rootLayout;
    private TextView tvBotResponse;
    private ImageView ivConnectingCircle;
    private FrameLayout btnClose;
    private TextView tvStatus;
    private ImageView ivVoiceModeIcon;
    private View bottomBar;

    private ObjectAnimator rotationAnimator;
    private AnimationDrawable voiceModeAnimation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupWindow();
        setContentView(R.layout.activity_sample_voice);

        initViews();
        applyEdgeToEdgeInsets(findViewById(android.R.id.content));
        setupClickListeners();
        initViewModel();

        showConnectingState();
        requestPermissionsAndStart();
    }

    private void setupWindow() {
        Window window = getWindow();
        if (window == null) {
            return;
        }

        WindowCompat.setDecorFitsSystemWindows(window, false);
        window.setStatusBarColor(Color.TRANSPARENT);
        window.setNavigationBarColor(Color.TRANSPARENT);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

        WindowInsetsControllerCompat windowInsetsController =
                WindowCompat.getInsetsController(window, window.getDecorView());
        if (windowInsetsController != null) {
            windowInsetsController.setAppearanceLightStatusBars(true);
            windowInsetsController.setAppearanceLightNavigationBars(true);
        }
    }

    private void initViews() {
        rootLayout = findViewById(R.id.rootLayout);
        tvBotResponse = findViewById(R.id.tvBotResponse);
        ivConnectingCircle = findViewById(R.id.ivConnectingCircle);
        btnClose = findViewById(R.id.btnClose);
        tvStatus = findViewById(R.id.tvStatus);
        ivVoiceModeIcon = findViewById(R.id.ivVoiceModeIcon);
        bottomBar = findViewById(R.id.bottomBar);
    }

    private void applyEdgeToEdgeInsets(View view) {
        ViewCompat.setOnApplyWindowInsetsListener(view, (v, windowInsets) -> {
            Insets insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars());

            if (tvBotResponse != null) {
                ViewGroup.MarginLayoutParams responseParams =
                        (ViewGroup.MarginLayoutParams) tvBotResponse.getLayoutParams();
                responseParams.topMargin = insets.top + 60;
                tvBotResponse.setLayoutParams(responseParams);
            }

            if (bottomBar != null) {
                ViewGroup.MarginLayoutParams bottomParams =
                        (ViewGroup.MarginLayoutParams) bottomBar.getLayoutParams();
                bottomParams.bottomMargin = insets.bottom + 32;
                bottomBar.setLayoutParams(bottomParams);
            }

            return WindowInsetsCompat.CONSUMED;
        });
    }

    private void setupClickListeners() {
        btnClose.setOnClickListener(v -> {
            if (viewModel != null) {
                viewModel.stopVoiceMode();
            }
            finish();
        });
    }

    private void initViewModel() {
        viewModel = new VoiceModeViewModel(this);
        viewModel.setVoiceModeCallback(this);
    }

    protected WebRTCConfig getWebRTCConfig() {
        String botId = getIntent().getStringExtra(EXTRA_BOT_ID);
        String clientId = getIntent().getStringExtra(EXTRA_CLIENT_ID);
        String clientSecret = getIntent().getStringExtra(EXTRA_CLIENT_SECRET);
        String identity = getIntent().getStringExtra(EXTRA_IDENTITY);
        String webSocketUrl = getIntent().getStringExtra(EXTRA_WEBSOCKET_URL);
        String jwtServiceUrl = getIntent().getStringExtra(EXTRA_JWT_SERVICE_URL);
        String serverUrl = getIntent().getStringExtra(EXTRA_SERVER_URL);
        String sipDomain = getIntent().getStringExtra(EXTRA_SIP_DOMAIN);

        if (isNullOrEmpty(botId) || isNullOrEmpty(clientId) || isNullOrEmpty(clientSecret)
                || isNullOrEmpty(identity) || isNullOrEmpty(webSocketUrl)
                || isNullOrEmpty(jwtServiceUrl) || isNullOrEmpty(serverUrl)) {
            Toast.makeText(this, R.string.voice_mode_webrtc_not_configured, Toast.LENGTH_LONG).show();
            finish();
            return null;
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

    private void requestPermissionsAndStart() {
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
            startVoiceMode();
        } else {
            ActivityCompat.requestPermissions(this,
                    permissionsNeeded.toArray(new String[0]),
                    PERMISSION_REQUEST_CODE);
        }
    }

    private void startVoiceMode() {
        WebRTCConfig config = getWebRTCConfig();
        if (config == null || isFinishing()) {
            return;
        }

        if (!VoiceModeViewModel.isVoiceModeConfigured(config)) {
            Toast.makeText(this, R.string.voice_mode_webrtc_not_configured, Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        viewModel.startVoiceMode(config);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PERMISSION_REQUEST_CODE) {
            boolean micGranted = false;

            for (int i = 0; i < permissions.length; i++) {
                if (Manifest.permission.RECORD_AUDIO.equals(permissions[i])) {
                    micGranted = grantResults[i] == PackageManager.PERMISSION_GRANTED;
                }
            }

            if (micGranted) {
                startVoiceMode();
            } else {
                boolean shouldShowRationale = ActivityCompat.shouldShowRequestPermissionRationale(
                        this, Manifest.permission.RECORD_AUDIO);

                if (!shouldShowRationale) {
                    new AlertDialog.Builder(this)
                            .setTitle(R.string.voice_permission_title)
                            .setMessage(R.string.voice_permission_message)
                            .setPositiveButton(android.R.string.ok, (dialog, which) -> finish())
                            .show();
                } else {
                    Toast.makeText(this, R.string.voice_status_permission_denied, Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        }
    }

    @Override
    public void onVoiceModeStateChanged(VoiceModeState state) {
        if (isFinishing()) {
            return;
        }
        runOnUiThread(() -> updateUIForState(state));
    }

    @Override
    public void onVoiceModeConnected() {
        if (isFinishing()) {
            return;
        }
        runOnUiThread(this::showConnectedState);
    }

    @Override
    public void onVoiceModeDisconnected(String reason) {
        if (isFinishing()) {
            return;
        }
        runOnUiThread(this::stopAllAnimations);
    }

    @Override
    public void onVoiceModeError(String error) {
        if (isFinishing()) {
            return;
        }
        runOnUiThread(() -> {
            Toast.makeText(this, error, Toast.LENGTH_LONG).show();
            stopAllAnimations();
            tvStatus.setText(R.string.voice_mode_error);
        });
    }

    private void updateUIForState(VoiceModeState state) {
        switch (state) {
            case IDLE:
                showConnectingState();
                tvStatus.setText(R.string.voice_mode_status_ready);
                break;

            case CONNECTING:
                showConnectingState();
                tvStatus.setText(R.string.voice_mode_status_connecting);
                break;

            case REGISTERING:
                showConnectingState();
                tvStatus.setText(R.string.voice_mode_status_registering);
                break;

            case CALLING:
                showConnectingState();
                tvStatus.setText(R.string.voice_mode_status_calling);
                break;

            case IN_PROGRESS:
                showConnectedState();
                break;

            case DISCONNECTING:
                tvStatus.setText(R.string.voice_mode_status_disconnecting);
                break;

            case ERROR:
                tvStatus.setText(R.string.voice_mode_error);
                break;
        }
    }

    private void showConnectingState() {
        tvBotResponse.setVisibility(View.GONE);
        ivConnectingCircle.setVisibility(View.VISIBLE);
        tvStatus.setVisibility(View.VISIBLE);
        ivVoiceModeIcon.setVisibility(View.GONE);

        stopConnectedAnimations();
        startConnectingAnimation();
    }

    private void showConnectedState() {
        tvBotResponse.setVisibility(View.VISIBLE);
        tvBotResponse.setText("");

        ivConnectingCircle.setVisibility(View.GONE);
        tvStatus.setVisibility(View.GONE);
        ivVoiceModeIcon.setVisibility(View.VISIBLE);

        stopConnectingAnimation();
        startConnectedAnimations();
    }

    private void startConnectingAnimation() {
        if (rotationAnimator != null && rotationAnimator.isRunning()) {
            return;
        }

        rotationAnimator = ObjectAnimator.ofFloat(ivConnectingCircle, "rotation", 0f, 360f);
        rotationAnimator.setDuration(3000);
        rotationAnimator.setInterpolator(new LinearInterpolator());
        rotationAnimator.setRepeatCount(ValueAnimator.INFINITE);
        rotationAnimator.start();
    }

    private void stopConnectingAnimation() {
        if (rotationAnimator != null) {
            rotationAnimator.cancel();
            rotationAnimator = null;
        }
    }

    private void startConnectedAnimations() {
        startVoiceModeAnimation();
    }

    private void startVoiceModeAnimation() {
        if (ivVoiceModeIcon != null && ivVoiceModeIcon.getDrawable() instanceof AnimationDrawable) {
            voiceModeAnimation = (AnimationDrawable) ivVoiceModeIcon.getDrawable();
            voiceModeAnimation.start();
        }
    }

    private void stopConnectedAnimations() {
        stopVoiceModeAnimation();
    }

    private void stopVoiceModeAnimation() {
        if (voiceModeAnimation != null && voiceModeAnimation.isRunning()) {
            voiceModeAnimation.stop();
            voiceModeAnimation = null;
        }
    }

    private void stopAllAnimations() {
        stopConnectingAnimation();
        stopConnectedAnimations();
    }

    public void updateBotResponse(String text) {
        if (tvBotResponse != null && !isFinishing()) {
            runOnUiThread(() -> tvBotResponse.setText(text));
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopAllAnimations();
        if (viewModel != null) {
            viewModel.cleanup();
        }
    }

    @Override
    public void onBackPressed() {
        if (viewModel != null && viewModel.isVoiceModeActive()) {
            viewModel.stopVoiceMode();
        }
        super.onBackPressed();
    }

    private static boolean isNullOrEmpty(String value) {
        return value == null || value.trim().isEmpty();
    }
}
