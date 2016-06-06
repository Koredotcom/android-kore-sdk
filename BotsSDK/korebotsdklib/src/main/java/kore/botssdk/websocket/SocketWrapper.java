package kore.botssdk.websocket;

import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;

import de.tavendo.autobahn.WebSocketConnection;
import de.tavendo.autobahn.WebSocketException;
import de.tavendo.autobahn.WebSocketHandler;
import kore.botssdk.net.BotRequestPool;

/**
 * Created by Ramachandra Pradeep on 6/1/2016.
 */
public final class SocketWrapper {

    public static SocketWrapper pKorePresenceInstance;
    private final String LOG_TAG = SocketWrapper.class.getSimpleName();
    private SocketConnectionListener socketConnectionListener = null;

    private final WebSocketConnection mConnection = new WebSocketConnection();

    private boolean mIsReconnectionAttemptNeeded = true;
    private String url;

    private int mReconnectionCount = 0;

    /**
     * Restricting outside object creation
     */
    private SocketWrapper() {
    }

    /**
     * Singleton Instance *
     */
    public static synchronized SocketWrapper getInstance() {
        if (pKorePresenceInstance == null)
            pKorePresenceInstance = new SocketWrapper();
        return pKorePresenceInstance;
    }

    public void connect(String url) {
        if (url != null) {
            this.url = url;
            try {
                mConnection.connect(url, new WebSocketHandler() {
                    @Override
                    public void onOpen() {
                    }

                    @Override
                    public void onTextMessage(String payload) {
                        Log.d(getClass().getSimpleName(), "Got echo: " + payload);
                        if (socketConnectionListener != null) {
                            socketConnectionListener.onConnected(payload);
                        }
                    }

                    @Override
                    public void onClose(int code, String reason) {
                        if (socketConnectionListener != null) {
                            socketConnectionListener.onDisconnected(reason);
                        }
                        Log.d(getClass().getSimpleName(), "Connection lost.");
                    }
                });
            } catch (WebSocketException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * @param msg : The message object
     * @return Was it able to successfully send the message.
     */
    public void sendMessage(String msg) {
        if (mConnection != null && mConnection.isConnected()) {
            mConnection.sendTextMessage(msg);
        } else {
            Log.e(LOG_TAG, "Either WebSocketConnection is not initialized or connection is not present.");
        }
    }

    /**
     * For disconnecting user's presence
     * Call this method when the user logged out
     */
    public void disConnect() {
        this.mIsReconnectionAttemptNeeded = false;
        if (mConnection != null && mConnection.isConnected()) {
            try {
                mConnection.disconnect();
            } catch (Exception e) {
                Log.d(LOG_TAG, "Exception while disconnection");
            }
            Log.d(LOG_TAG, "DisConnected successfully");
        } else {
            Log.d(LOG_TAG, "Cannot disconnect.._client is null");
        }
    }

    public void setSocketConnectionListener(SocketConnectionListener socketConnectionListener) {
        this.socketConnectionListener = socketConnectionListener;
    }

    public boolean isConnected() {
        if (mConnection != null && mConnection.isConnected()) {
            return true;
        } else {
            return false;
        }
    }

}