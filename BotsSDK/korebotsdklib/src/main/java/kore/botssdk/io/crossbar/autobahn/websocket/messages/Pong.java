package kore.botssdk.io.crossbar.autobahn.websocket.messages;

/// WebSockets pong to send or received.
public class Pong extends Message {

    public final byte[] mPayload;

    public Pong() {
        mPayload = null;
    }

    public Pong(byte[] payload) {
        mPayload = payload;
    }
}
