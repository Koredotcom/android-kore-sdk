package kore.botssdk.io.crossbar.autobahn.websocket.messages;

/// WebSockets binary message to send or received.
public class BinaryMessage extends Message {

    public final byte[] mPayload;

    public BinaryMessage(byte[] payload) {
        mPayload = payload;
    }
}
