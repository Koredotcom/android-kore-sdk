package kore.botssdk.speechtotext;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;

import java.net.URI;
import java.util.HashMap;

import kore.botssdk.io.crossbar.autobahn.websocket.WebSocketConnection;
import kore.botssdk.io.crossbar.autobahn.websocket.WebSocketConnectionHandler;
import kore.botssdk.io.crossbar.autobahn.websocket.interfaces.IWebSocket;
import kore.botssdk.net.SDKConfiguration;
import kore.botssdk.websocket.SocketConnectionListener;

/**
 * Created by Shiva Krishna on 11/23/2017.
 */

public final class TtsWebSocketWrapper {


    private final String LOG_TAG = TtsWebSocketWrapper.class.getSimpleName();

    public static TtsWebSocketWrapper pKorePresenceInstance;
    //    private Map<String, Object> mWebSocketConnectionArgs = Collections.emptyMap();
    private SocketConnectionListener socketConnectionListener = null;
    private final IWebSocket mConnection = new WebSocketConnection();

//    private URI uri;

//    private String accessToken;


    private Context mContext;

    /**
     * Restricting outside object creation
     */
    private TtsWebSocketWrapper(Context mContext) {
        this.mContext = mContext;
    }

    /**
     * The global default SocketWrapper instance
     */
    public static TtsWebSocketWrapper getInstance(Context mContext) {
        if (pKorePresenceInstance == null) {
//            synchronized (TtsWebSocketWrapper.class) {
                pKorePresenceInstance = new TtsWebSocketWrapper(mContext);
//            }
        }
        return pKorePresenceInstance;
    }

    /**
     * To prevent cloning
     *
     * @return
     * @throws CloneNotSupportedException
     */
    @Override
    protected Object clone() throws CloneNotSupportedException {
        return new CloneNotSupportedException("Clone not supported");
    }

    /**
     * Method to invoke connection for authenticated user
     *
     * @param accessToken   : AccessToken of the loged user.
     * @param chatBotName:      Name of the chat-bot
     * @param botId:    Chat-bot's taskId
     */
    /**
     * To connect the user presence
     *
     * @param _mListener
     */
    public void connect(SocketConnectionListener _mListener) {
        if(mConnection.isConnected()) return;
        this.socketConnectionListener = _mListener;
//        String host = null;
//        String port = null;
//        Boolean ssl = false;
//        String accessToken = null;

        /**
         * Preparing presence Url
         */

        String url = SDKConfiguration.Server.TTS_WS_URL;
        Log.d(LOG_TAG,"The url is "+ url);
        try {
//            this.uri = new URI(url);
            mConnection.connect(url, new  WebSocketConnectionHandler() {
                @Override
                public void onOpen() {
//                    Log.d(LOG_TAG, "Connection Open.");
                    if (socketConnectionListener != null) {
                        socketConnectionListener.onOpen(false);
                    }
                }

                @Override
                public void onClose(int code, String reason) {
                    Log.d(LOG_TAG, "Connection Lost.");
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

                /*@Override
                public void onRawTextMessage(byte[] payload) {
                    if (socketConnectionListener != null) {
                        socketConnectionListener.onRawTextMessage(payload);
                    }
                }

                @Override
                public void onBinaryMessage(byte[] payload) {
                    if (socketConnectionListener != null) {
                        socketConnectionListener.onBinaryMessage(payload);
                    }
                }*/
            });
        } catch (Exception e) {
            e.printStackTrace();
        }



    }

    /**
     * @param msg : The message object
     * @return Was it able to successfully send the message.
     */
    public boolean sendRawData(byte[] msg) {
        if (mConnection != null && mConnection.isConnected()) {
            mConnection.sendMessage(msg,true);
            return true;
        }
        return false;
    }

    /**
     *
     * @param msg
     * @return
     */
    public boolean sendMessage(String msg,String bearer) {
        if (mConnection != null && mConnection.isConnected()) {
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
            Log.e(LOG_TAG, "Connection is not present. Reconnecting...");
            return false;
        }
    }


    /**
     * For disconnecting user's presence
     * Call this method when the user logged out
     */
    public void disConnect() {
        if (mConnection != null && mConnection.isConnected()) {
            try {
                mConnection.sendClose();
            } catch (Exception e) {
                Log.d(LOG_TAG, "Exception while disconnection");
            }
            Log.d(LOG_TAG, "DisConnected successfully");
        } else {
            Log.d(LOG_TAG, "Cannot disconnect.._client is null");
        }

    }

    public boolean isConnected() {
        if (mConnection != null && mConnection.isConnected()) {
            return true;
        } else {
            return false;
        }
    }

    /*@Override
    public void onDisconnect(Exception e) {
        KoreLogger.debugLog(LOG_TAG, "Disconnect callback ");
        HashMap<String, String> disConnect = new HashMap<>();
        disConnect.put("name", "disconnect");
        try {
            KoreLogger.debugLog(LOG_TAG, "Sending web socket disconnect callback");
            if (mListener != null)
                mListener.onDisconnected(disConnect);
        } catch (Exception e1) {
            KoreLogger.errorLog(LOG_TAG, "onDisconnect", e1);
        }
    }*/
   /* public void sendData(byte[] msg) {

        if (msg != null && msg.length > 0) {
            AudioDataPool.getAudioDataPoolList().add(AudioDataPool.getAudioDataPoolList().size(),new ByteData(msg));
        }

        if (!AudioDataPool.isPoolEmpty()) {
            if (!AudioDataPool.getAudioDataPoolList().isEmpty()) {
                ArrayList<ByteData> audioDataList = AudioDataPool.getAudioDataPoolList();
                int len = audioDataList.size();
                for (int i = 0; i < len; i++) {
                    ByteData requestPayload = audioDataList.get(i);
                    boolean wasSuccessfullySend = sendRawData(requestPayload.data);
                    if (wasSuccessfullySend) {
                        AudioDataPool.getAudioDataPoolList().remove(i);
                        i--; //reset the parameter
                        len--; //reset the length.
                    } else {
                        break;//Break the loop, as re-connection would be attempted from sendMessage(...)
                    }
                }
            }
        }
    }*/
}