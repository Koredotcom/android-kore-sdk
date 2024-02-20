package kore.botssdk.io.crossbar.autobahn.websocket.messages;


import kore.botssdk.io.crossbar.autobahn.websocket.exceptions.WebSocketException;

/// WebSockets reader detected WS protocol violation.
public class ProtocolViolation extends Message {

    public final WebSocketException mException;

    public ProtocolViolation(WebSocketException e) {
        mException = e;
    }
}
