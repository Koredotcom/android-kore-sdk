package websocket.async.http.socketio;

import org.json.JSONArray;
import org.json.JSONObject;

import websocket.http.AsyncHttpClient;
import websocket.http.AsyncHttpClient.SocketIORequest;

import android.content.Context;
import android.os.Handler;
import android.text.TextUtils;
import codebutler.android_websockets.WebSocketClient;

public class SocketIOClient extends EventEmitter {

    boolean connected;
    boolean disconnected;
    Handler handler;
    SocketIOConnection _connection;
    String endpoint;

    private void emitRaw(int type, String message, Acknowledge acknowledge) {
        _connection.emitRaw(type, this, message, acknowledge);
    }

    public void emit(String name, JSONArray args) {
        emit(name, args, null);
    }

    public void emit(final String message) {
        emit(message, (Acknowledge) null);
    }

    public void emit(final JSONObject jsonMessage) {
        emit(jsonMessage, null);
    }

    public void emit(String name, JSONArray args, Acknowledge acknowledge) {
        final JSONObject event = new JSONObject();
        try {
            event.put("name", name);
            event.put("args", args);
            emitRaw(5, event.toString(), acknowledge);
        } catch (Exception e) {
        }
    }

    public void emit(final String message, Acknowledge acknowledge) {
        emitRaw(3, message, acknowledge);
    }

    public void emit(final JSONObject jsonMessage, Acknowledge acknowledge) {
        emitRaw(4, jsonMessage.toString(), acknowledge);
    }

    public static void connect(Context mContext,String uri, final ConnectCallback callback, final Handler handler) {
        connect(mContext,new SocketIORequest(uri), callback, handler,null);
    }

    ConnectCallback connectCallback;

    public static void connect(Context mContext,final SocketIORequest request, final ConnectCallback callback, final Handler handler,CustomCallback _customCallback) {
        final SocketIOConnection connection = new SocketIOConnection(handler, new AsyncHttpClient(mContext), request);
        connection.setCustomCallback(_customCallback);

        final ConnectCallback wrappedCallback = new ConnectCallback() {
            @Override
            public void onConnectCompleted(Exception ex, SocketIOClient client) {
                if (ex != null || TextUtils.isEmpty(request.getEndpoint())) {

                    client.handler = handler;
                    if (callback != null) {
                        callback.onConnectCompleted(ex, client);
                    }

                    return;
                }

                // remove the root client since that's not actually being used.
                connection.clients.remove(client);

                // connect to the endpoint we want
                client.of(request.getEndpoint(), new ConnectCallback() {
                    @Override
                    public void onConnectCompleted(Exception ex, SocketIOClient client) {
                        if (callback != null) {
                            callback.onConnectCompleted(ex, client);
                        }
                    }
                });
            }
        };

        connection.clients.add(new SocketIOClient(connection, "", wrappedCallback));
        connection.reconnect();

    }

    ErrorCallback errorCallback;

    public void setErrorCallback(ErrorCallback callback) {
        errorCallback = callback;
    }

    public ErrorCallback getErrorCallback() {
        return errorCallback;
    }

    DisconnectCallback disconnectCallback;

    public void setDisconnectCallback(DisconnectCallback callback) {
        disconnectCallback = callback;
    }

    public DisconnectCallback getDisconnectCallback() {
        return disconnectCallback;
    }

    ReconnectCallback reconnectCallback;

    public void setReconnectCallback(ReconnectCallback callback) {
        reconnectCallback = callback;
    }

    public ReconnectCallback getReconnectCallback() {
        return reconnectCallback;
    }

    JSONCallback jsonCallback;

    public void setJSONCallback(JSONCallback callback) {
        jsonCallback = callback;
    }

    public JSONCallback getJSONCallback() {
        return jsonCallback;
    }

    StringCallback stringCallback;

    public void setStringCallback(StringCallback callback) {
        stringCallback = callback;
    }

    public StringCallback getStringCallback() {
        return stringCallback;
    }

    

    private SocketIOClient(SocketIOConnection connection, String endpoint,
            ConnectCallback callback) {
        this.endpoint = endpoint;
        this._connection = connection;
        this.connectCallback = callback;
    }

    public boolean isConnected() {
        return connected && !disconnected && _connection.isConnected();
    }

    public void disconnect() {
        _connection.setCustomCallback(null);
        _connection.disconnect(this);
        final DisconnectCallback disconnectCallback = this.disconnectCallback;
        if (disconnectCallback != null) {
            handler.post(new Runnable() {

                @Override
                public void run() {
                    disconnectCallback.onDisconnect(null);

                }
            });

        }
    }

    public void of(String endpoint, ConnectCallback connectCallback) {
        _connection.connect(new SocketIOClient(_connection, endpoint, connectCallback));
    }

    public WebSocketClient getWebSocket() {
        return _connection.webSocketClient;
    }

}
