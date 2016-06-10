package kore.botssdk.websocket;

import kore.botssdk.autobahn.WebSocket;

/**
 * Created by Ramachandra Pradeep on 6/1/2016.
 */
public class SocketListener implements SocketConnectionListener {

    @Override
    public void onOpen() {

    }

    @Override
    public void onClose(WebSocket.WebSocketConnectionObserver.WebSocketCloseNotification code, String reason) {
        //TODO do necessary things on disconnected here
    }

    @Override
    public void onTextMessage(String payload) {
        //TODO you will get the response here
    }

    @Override
    public void onRawTextMessage(byte[] payload) {

    }

    @Override
    public void onBinaryMessage(byte[] payload) {

    }
}
