package com.kore.findlysdk.autobahn.websocket.messages;

public class CannotConnect extends Message {
    public final String reason;

    public CannotConnect(String reason) {
        this.reason = reason;
    }
}
