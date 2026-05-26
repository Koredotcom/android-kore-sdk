package kore.botssdk.voicemode;

/**
 * Adapter class for WebRTCListener with empty default implementations.
 * Extend this class and override only the methods you need.
 */
public abstract class WebRTCListenerAdapter implements WebRTCListener {

    @Override
    public void onConnected() {
    }

    @Override
    public void onDisconnected() {
    }

    @Override
    public void onRegistered() {
    }

    @Override
    public void onRegistrationFailed(String error) {
    }

    @Override
    public void onUnregistered() {
    }

    @Override
    public void onCallProgress() {
    }

    @Override
    public void onCallAccepted() {
    }

    @Override
    public void onCallConfirmed() {
    }

    @Override
    public void onCallEnded(String cause) {
    }

    @Override
    public void onCallFailed(String cause) {
    }

    @Override
    public void onIncomingCall(CallSession session) {
    }

    @Override
    public void onError(String error) {
    }
}
