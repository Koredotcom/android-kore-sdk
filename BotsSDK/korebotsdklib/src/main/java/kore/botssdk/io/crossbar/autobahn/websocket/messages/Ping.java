package kore.botssdk.io.crossbar.autobahn.websocket.messages;

/// WebSockets ping to send or received.
public class Ping extends Message {

    public final byte[] mPayload;

    public Ping() {
        mPayload = null;
    }

    public Ping(byte[] payload) {
        mPayload = payload;
    }
}
