package kore.botssdk.io.crossbar.autobahn.websocket.messages;

/// An exception occured in the WS reader or WS writer.
public class Error extends Message {

    public final Exception mException;

    public Error(Exception e) {
        mException = e;
    }
}
