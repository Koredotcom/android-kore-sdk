package kore.botssdk.voicemode.viewmodel;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import java.lang.ref.WeakReference;

import kore.botssdk.voicemode.CallSession;
import kore.botssdk.voicemode.WebRTCClient;
import kore.botssdk.voicemode.WebRTCConfig;
import kore.botssdk.voicemode.WebRTCListener;
import kore.botssdk.voicemode.listener.VoiceModeCallback;
import kore.botssdk.voicemode.utils.LogHelper;

/**
 * ViewModel for voice mode UI.
 * Handles WebRTC connection and voice mode interaction.
 */
public class VoiceModeViewModel {
    private static final String LOG_TAG = VoiceModeViewModel.class.getSimpleName();

    private final WeakReference<Context> context;
    private WebRTCClient webRTCClient;
    private VoiceModeCallback voiceModeCallback;
    private VoiceModeCallback.VoiceModeState currentVoiceModeState = VoiceModeCallback.VoiceModeState.IDLE;
    private final Handler mainHandler = new Handler(Looper.getMainLooper());

    private boolean isMuted = false;
    private boolean isSpeakerOn = true;

    public VoiceModeViewModel(Context context) {
        this.context = new WeakReference<>(context);
    }

    public void setVoiceModeCallback(VoiceModeCallback callback) {
        this.voiceModeCallback = callback;
    }

    public VoiceModeCallback.VoiceModeState getCurrentVoiceModeState() {
        return currentVoiceModeState;
    }

    public static boolean isVoiceModeConfigured(WebRTCConfig config) {
        return config != null
                && !isNullOrEmpty(config.getWebSocketUrl())
                && !isNullOrEmpty(config.getSipDomain());
    }

    public void startVoiceMode(WebRTCConfig config) {
        if (!isVoiceModeConfigured(config)) {
            notifyVoiceModeError("WebRTC is not configured");
            return;
        }

        updateVoiceModeState(VoiceModeCallback.VoiceModeState.CONNECTING);

        try {
            Context ctx = context.get();
            if (ctx == null) {
                notifyVoiceModeError("Context is not available");
                return;
            }

            LogHelper.setDebugEnabled(config.isDebug());
            webRTCClient = new WebRTCClient(ctx);
            webRTCClient.setListener(webRTCListener);
            webRTCClient.initWithCredentials(config);

        } catch (Exception e) {
            LogHelper.e(LOG_TAG, "Failed to start voice mode: " + e.getMessage());
            notifyVoiceModeError("Failed to start voice mode: " + e.getMessage());
        }
    }

    public void stopVoiceMode() {
        updateVoiceModeState(VoiceModeCallback.VoiceModeState.DISCONNECTING);

        if (webRTCClient != null) {
            try {
                webRTCClient.hangup();
                webRTCClient.disconnect();
            } catch (Exception e) {
                LogHelper.e(LOG_TAG, "Error stopping voice mode: " + e.getMessage());
            } finally {
                webRTCClient = null;
            }
        }

        updateVoiceModeState(VoiceModeCallback.VoiceModeState.IDLE);
        notifyVoiceModeDisconnected("Voice mode stopped");
    }

    public boolean isVoiceModeActive() {
        return currentVoiceModeState == VoiceModeCallback.VoiceModeState.IN_PROGRESS
                || currentVoiceModeState == VoiceModeCallback.VoiceModeState.CONNECTING
                || currentVoiceModeState == VoiceModeCallback.VoiceModeState.REGISTERING
                || currentVoiceModeState == VoiceModeCallback.VoiceModeState.CALLING;
    }

    public boolean isInCall() {
        return currentVoiceModeState == VoiceModeCallback.VoiceModeState.IN_PROGRESS;
    }

    public void toggleMute() {
        if (webRTCClient != null) {
            if (isMuted) {
                webRTCClient.unmute();
                isMuted = false;
            } else {
                webRTCClient.mute();
                isMuted = true;
            }
        }
    }

    public boolean isMuted() {
        return isMuted;
    }

    public void toggleSpeaker() {
        if (webRTCClient != null) {
            isSpeakerOn = webRTCClient.toggleSpeaker();
        }
    }

    public boolean isSpeakerOn() {
        return isSpeakerOn;
    }

    private void updateVoiceModeState(VoiceModeCallback.VoiceModeState state) {
        currentVoiceModeState = state;
        mainHandler.post(() -> {
            if (voiceModeCallback != null) {
                voiceModeCallback.onVoiceModeStateChanged(state);
            }
        });
    }

    private void notifyVoiceModeConnected() {
        mainHandler.post(() -> {
            if (voiceModeCallback != null) {
                voiceModeCallback.onVoiceModeConnected();
            }
        });
    }

    private void notifyVoiceModeDisconnected(String reason) {
        mainHandler.post(() -> {
            if (voiceModeCallback != null) {
                voiceModeCallback.onVoiceModeDisconnected(reason);
            }
        });
    }

    private void notifyVoiceModeError(String error) {
        updateVoiceModeState(VoiceModeCallback.VoiceModeState.ERROR);
        mainHandler.post(() -> {
            if (voiceModeCallback != null) {
                voiceModeCallback.onVoiceModeError(error);
            }
        });
    }

    private final WebRTCListener webRTCListener = new WebRTCListener() {
        @Override
        public void onConnected() {
            LogHelper.d(LOG_TAG, "WebRTC connected");
            updateVoiceModeState(VoiceModeCallback.VoiceModeState.REGISTERING);
        }

        @Override
        public void onDisconnected() {
            LogHelper.d(LOG_TAG, "WebRTC disconnected");
            updateVoiceModeState(VoiceModeCallback.VoiceModeState.IDLE);
            notifyVoiceModeDisconnected("Disconnected");
        }

        @Override
        public void onRegistered() {
            LogHelper.d(LOG_TAG, "WebRTC registered");
            updateVoiceModeState(VoiceModeCallback.VoiceModeState.CALLING);
        }

        @Override
        public void onRegistrationFailed(String error) {
            LogHelper.e(LOG_TAG, "WebRTC registration failed: " + error);
            notifyVoiceModeError("Registration failed: " + error);
        }

        @Override
        public void onUnregistered() {
            LogHelper.d(LOG_TAG, "WebRTC unregistered");
        }

        @Override
        public void onCallProgress() {
            LogHelper.d(LOG_TAG, "WebRTC call progress");
            updateVoiceModeState(VoiceModeCallback.VoiceModeState.CALLING);
        }

        @Override
        public void onCallAccepted() {
            LogHelper.d(LOG_TAG, "WebRTC call accepted");
            updateVoiceModeState(VoiceModeCallback.VoiceModeState.IN_PROGRESS);
            notifyVoiceModeConnected();
        }

        @Override
        public void onCallConfirmed() {
            LogHelper.d(LOG_TAG, "WebRTC call confirmed");
            updateVoiceModeState(VoiceModeCallback.VoiceModeState.IN_PROGRESS);
        }

        @Override
        public void onCallEnded(String cause) {
            LogHelper.d(LOG_TAG, "WebRTC call ended: " + cause);
            updateVoiceModeState(VoiceModeCallback.VoiceModeState.IDLE);
            notifyVoiceModeDisconnected(cause);
            webRTCClient = null;
        }

        @Override
        public void onCallFailed(String cause) {
            LogHelper.e(LOG_TAG, "WebRTC call failed: " + cause);
            notifyVoiceModeError("Call failed: " + cause);
            webRTCClient = null;
        }

        @Override
        public void onIncomingCall(CallSession session) {
            LogHelper.d(LOG_TAG, "WebRTC incoming call - auto rejecting");
        }

        @Override
        public void onError(String error) {
            LogHelper.e(LOG_TAG, "WebRTC error: " + error);
            notifyVoiceModeError(error);
        }
    };

    public void cleanup() {
        if (webRTCClient != null) {
            try {
                webRTCClient.disconnect();
            } catch (Exception e) {
                LogHelper.e(LOG_TAG, "Error cleaning up voice mode: " + e.getMessage());
            }
            webRTCClient = null;
        }
        currentVoiceModeState = VoiceModeCallback.VoiceModeState.IDLE;
    }

    private static boolean isNullOrEmpty(String value) {
        return value == null || value.trim().isEmpty();
    }
}
