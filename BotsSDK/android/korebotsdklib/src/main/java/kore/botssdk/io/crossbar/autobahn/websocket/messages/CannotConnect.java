package kore.botssdk.io.crossbar.autobahn.websocket.messages;

public class CannotConnect extends Message {
    public final String reason;

    public CannotConnect(String reason) {
        this.reason = reason;
    }
}
