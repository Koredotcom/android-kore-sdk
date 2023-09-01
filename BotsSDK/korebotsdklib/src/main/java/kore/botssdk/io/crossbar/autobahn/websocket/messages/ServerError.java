package kore.botssdk.io.crossbar.autobahn.websocket.messages;

public class ServerError extends Message {
    public final int mStatusCode;
    public final String mStatusMessage;

    public ServerError(int statusCode, String statusMessage) {
        mStatusCode = statusCode;
        mStatusMessage = statusMessage;
    }

}
