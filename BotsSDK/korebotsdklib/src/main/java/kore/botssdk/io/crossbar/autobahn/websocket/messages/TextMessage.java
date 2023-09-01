package kore.botssdk.io.crossbar.autobahn.websocket.messages;

/// WebSockets text message to send or received.
public class TextMessage extends Message {

    public final String mPayload;

    public TextMessage(String payload) {
        mPayload = payload;
    }
}
