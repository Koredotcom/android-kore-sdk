package kore.botssdk;

/**
 * Created by Pradeep Mahato on 10-Jun-16.
 */
public class SocketConnectionEvents {

    public static enum SocketConnectionEventStates {
        NOCONNECTION,
        CONNECTING,
        CONNECTED,
        DISCONNECTED,
        RECONNECTING,
        FAILED_TO_CONNECT
    }

    SocketConnectionEventStates socketConnectionEventStates = SocketConnectionEventStates.NOCONNECTION;

    public SocketConnectionEvents(SocketConnectionEventStates socketConnectionEventStates) {
        this.socketConnectionEventStates = socketConnectionEventStates;
    }

    public SocketConnectionEventStates getSocketConnectionEventStates() {
        return socketConnectionEventStates;
    }
}
