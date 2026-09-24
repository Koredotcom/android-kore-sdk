package artemis.socket;

/** Receives lifecycle, message, and error events from the Artemis socket. */
public interface ArtemisSocketListener {
    void onLog(String message);

    void onConnecting();

    void onConnected(String sessionId);

    void onDisconnected(String reason);

    void onMessage(String payload);

    void onError(Throwable error);
}
