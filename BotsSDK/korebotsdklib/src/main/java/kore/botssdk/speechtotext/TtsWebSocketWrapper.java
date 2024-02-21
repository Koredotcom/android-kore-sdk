package kore.botssdk.speechtotext;

import android.content.Context;

import com.google.gson.Gson;

import java.util.HashMap;

import kore.botssdk.io.crossbar.autobahn.websocket.WebSocketConnection;
import kore.botssdk.io.crossbar.autobahn.websocket.WebSocketConnectionHandler;
import kore.botssdk.io.crossbar.autobahn.websocket.interfaces.IWebSocket;
import kore.botssdk.net.SDKConfiguration;
import kore.botssdk.utils.LogUtils;
import kore.botssdk.websocket.SocketConnectionListener;

/**
 * Created by Shiva Krishna on 11/23/2017.
 */
@SuppressWarnings("UnKnownNullness")
public class TtsWebSocketWrapper {
    final String LOG_TAG = TtsWebSocketWrapper.class.getSimpleName();
    SocketConnectionListener socketConnectionListener = null;
    private final IWebSocket mConnection = new WebSocketConnection();

    private final Context mContext;

    private TtsWebSocketWrapper(Context mContext) {
        this.mContext = mContext;
    }

    /**
     * The global default SocketWrapper instance
     */
    public static TtsWebSocketWrapper getInstance(Context mContext) {
        return new TtsWebSocketWrapper(mContext);
    }

    /**
     * To prevent cloning
     *
     */
    @Override
    protected Object clone() throws CloneNotSupportedException {
        return new CloneNotSupportedException("Clone not supported");
    }

    /**
     * To connect the user presence
     *
     */
    public void connect(SocketConnectionListener _mListener) {
        if(mConnection.isConnected()) return;
        this.socketConnectionListener = _mListener;

        String url = SDKConfiguration.Server.TTS_WS_URL;
        LogUtils.d(LOG_TAG,"The url is "+ url);
        try {
            mConnection.connect(url, new  WebSocketConnectionHandler() {
                @Override
                public void onOpen() {
                    if (socketConnectionListener != null) {
                        socketConnectionListener.onOpen(false);
                    }
                }

                @Override
                public void onClose(int code, String reason) {
                    LogUtils.d(LOG_TAG, "Connection Lost.");
                    if (socketConnectionListener != null) {
                        socketConnectionListener.onClose(code, reason);
                    }
                }

                @Override
                public void onMessage(String payload) {
                    if (socketConnectionListener != null) {
                        socketConnectionListener.onTextMessage(payload);
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     *
     * @param msg
     * @return
     */
    public boolean sendMessage(String msg,String bearer) {
        if (mConnection.isConnected()) {
            HashMap<String,String> body = new HashMap<>();
            body.put("message",msg);
            body.put("user",SDKConfiguration.Client.bot_name);
            body.put("authorization",bearer);
            Gson gson = new Gson();
            String jsonPayload = gson.toJson(body);
            mConnection.sendMessage(jsonPayload);
            return true;
        } else {
           connect(socketConnectionListener);
            LogUtils.e(LOG_TAG, "Connection is not present. Reconnecting...");
            return false;
        }
    }


    /**
     * For disconnecting user's presence
     * Call this method when the user logged out
     */
    public void disConnect() {
        if (mConnection.isConnected()) {
            try {
                mConnection.sendClose();
            } catch (Exception e) {
                LogUtils.d(LOG_TAG, "Exception while disconnection");
            }
            LogUtils.d(LOG_TAG, "DisConnected successfully");
        } else {
            LogUtils.d(LOG_TAG, "Cannot disconnect.._client is null");
        }
    }

    public boolean isConnected() {
        return mConnection.isConnected();
    }
}