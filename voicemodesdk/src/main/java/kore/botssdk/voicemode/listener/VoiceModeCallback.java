package kore.botssdk.voicemode.listener;

/**
 * Callback interface for Voice Mode (WebRTC) events.
 * Implement this interface to receive updates on voice mode state changes.
 */
public interface VoiceModeCallback {

    enum VoiceModeState {
        IDLE,
        CONNECTING,
        REGISTERING,
        CALLING,
        IN_PROGRESS,
        DISCONNECTING,
        ERROR
    }

    void onVoiceModeStateChanged(VoiceModeState state);

    void onVoiceModeConnected();

    void onVoiceModeDisconnected(String reason);

    void onVoiceModeError(String error);
}
