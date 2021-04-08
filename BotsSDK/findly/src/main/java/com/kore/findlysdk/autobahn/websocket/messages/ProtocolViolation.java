package com.kore.findlysdk.autobahn.websocket.messages;


import com.kore.findlysdk.autobahn.websocket.exceptions.WebSocketException;

/// WebSockets reader detected WS protocol violation.
public class ProtocolViolation extends Message {

    public WebSocketException mException;

    public ProtocolViolation(WebSocketException e) {
        mException = e;
    }
}
