package kore.botssdk.voicemode;

/**
 * Listener interface for WebRTC events.
 * Implement this interface to receive callbacks for connection, registration, and call events.
 */
public interface WebRTCListener {

    /**
     * Called when WebSocket connection is established.
     */
    void onConnected();

    /**
     * Called when WebSocket connection is closed.
     */
    void onDisconnected();

    /**
     * Called when SIP registration is successful.
     */
    void onRegistered();

    /**
     * Called when SIP registration fails.
     * @param error The error message
     */
    void onRegistrationFailed(String error);

    /**
     * Called when SIP unregistration is complete.
     */
    void onUnregistered();

    /**
     * Called when an outgoing call is in progress (ringing on remote).
     */
    void onCallProgress();

    /**
     * Called when a call is accepted by remote party.
     */
    void onCallAccepted();

    /**
     * Called when a call is confirmed (media established).
     */
    void onCallConfirmed();

    /**
     * Called when a call ends.
     * @param cause The reason for ending
     */
    void onCallEnded(String cause);

    /**
     * Called when a call fails.
     * @param cause The reason for failure
     */
    void onCallFailed(String cause);

    /**
     * Called when there is an incoming call.
     * @param session The call session
     */
    void onIncomingCall(CallSession session);

    /**
     * Called when an error occurs.
     * @param error The error message
     */
    void onError(String error);
}
