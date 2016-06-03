package kore.botssdk.websocket;

import android.content.Context;
import android.util.Log;

import java.util.HashMap;
import java.util.Random;

import de.tavendo.autobahn.WebSocketConnection;
import de.tavendo.autobahn.WebSocketException;
import de.tavendo.autobahn.WebSocketHandler;

/**
 * Created by Ramachandra Pradeep on 6/1/2016.
 */
public final class KorePresenceWrapper {

    public static KorePresenceWrapper pKorePresenceInstance;
    private final String LOG_TAG = KorePresenceWrapper.class.getSimpleName();
    private PresenceConnectionListener presenceConnectionListener = null;

    private final WebSocketConnection mConnection = new WebSocketConnection();

    private boolean mIsReconnectionAttemptNeeded = true;
    private String Url;

    /**
     * initial reconnection delay 1 Sec
     */
    private int mReconnectDelay = 1000;

    /**
     * initial reconnection count
     */
    private int mReconnectionCount = 0;

    private Context mContext;

    /**
            * Restricting outside object creation
    */
    private KorePresenceWrapper() {
    }

    /**
     * Singleton Instance *
     */

    public static synchronized KorePresenceWrapper getInstance() {
        if (pKorePresenceInstance == null)
            pKorePresenceInstance = new KorePresenceWrapper();
        return pKorePresenceInstance;
    }

    /*public void connect(String Url) {

        this.Url = Url;
        AsyncHttpClient.SocketIORequest req;

        if (Url!= null) {

            req = new AsyncHttpClient.SocketIORequest("http"+Url.substring(2));

            SocketIOClient.connect(mContext, req, new ConnectCallback() {

                @Override
                public void onConnectCompleted(Exception ex, SocketIOClient client) {

                    _client = client;
                    if (ex != null) {
                        Log.d(LOG_TAG, "WebSocket:: The Exception is " + ex.toString());
                        //Toast.makeText(.this, ex.toString(), Toast.LENGTH_SHORT).show();
                        return;
                    }


                    mReconnectionCount = 0;
                    mReconnectDelay = 1000;

                    *//*if(mIsImmediateFetchActionNeeded) {
                        initiatePresenceOnConnect(userId);
                        mIsImmediateFetchActionNeeded = false;
                    }*//*


                    *//** Save the returned SocketIOClient instance into a variable so you can disconnect it later *//*
                    _client.setDisconnectCallback(KorePresenceWrapper.this);
                    _client.setErrorCallback(new ErrorCallback() {

                        @Override
                        public void onError(String error) {
                            Log.d(LOG_TAG, "Inside OnError The Exception is " + error.toString());

                        }
                    });
                    _client.setJSONCallback(new JSONCallback() {

                        @Override
                        public void onJSON(JSONObject json, Acknowledge acknowledge) {
                            try {
                                Log.d(LOG_TAG, "WebSocket :json:" + json.toString(2));
                            } catch (JSONException e) {
                                Log.d(LOG_TAG, "WebSocket:: The Exception is " + e.toString());
                            }

                        }
                    });
                    _client.setStringCallback(new StringCallback() {

                        @Override
                        public void onString(String string, Acknowledge acknowledge) {
                            Log.d(LOG_TAG, "WebSocket:: The String is is " + string);

                        }
                    });

                    _client.of("/chat", new ConnectCallback() {

                        @Override
                        public void onConnectCompleted(Exception ex, SocketIOClient client) {

                            if (ex != null) {
                                ex.printStackTrace();
                                return;
                            }

                            //This client instance will be using the same websocket as the original client,
                            //but will point to the indicated endpoint
                            _client.setDisconnectCallback(KorePresenceWrapper.this);

                            _client.addListener("a message", new EventCallback() {

                                @Override
                                public void onEvent(String event, JSONArray argument, Acknowledge acknowledge) {
                                    Log.d(LOG_TAG, event);
                                }
                            });

                        }
                    });

                }
            }, new Handler(), new CustomCallback() {

                @Override
                public void onStringRecived(HashMap _data) {
//                    KoreLogger.debugLog(LOG_TAG, _data.toString());
//                    KoreLogger.debugLog(LOG_TAG, "Sending web socket callback");
                    if (presenceConnectionListener != null) {
                        presenceConnectionListener.onConnected(_data);
                    }
                }
            });
        }//if
    }*/

    public void connect(String Url) {
        if(Url != null){
            this.Url = Url;
            try {
                mConnection.connect(Url,new WebSocketHandler(){
                    @Override
                    public void onOpen() {
//                        super.onOpen();
                    }
                    @Override
                    public void onTextMessage(String payload) {
                        Log.d(getClass().getSimpleName(), "Got echo: " + payload);
                        if (presenceConnectionListener != null) {
                            presenceConnectionListener.onConnected(payload);
                        }
                    }

                    @Override
                    public void onClose(int code, String reason) {
                        HashMap<String, String> disConnect = new HashMap<>();
                        disConnect.put("name", "disconnect");
                        if (presenceConnectionListener != null){
                            presenceConnectionListener.onDisconnected(reason);
                        }
                        Log.d(getClass().getSimpleName(), "Connection lost.");
                    }
                });
            } catch (WebSocketException e) {
                e.printStackTrace();
            }
        }
    }

    public void sendMessage(String msg){
        if(mConnection != null && mConnection.isConnected()){
            mConnection.sendTextMessage(msg);
        }
    }


   /* @Override
    public void onDisconnect(Exception e) {
        HashMap<String, String> disConnect = new HashMap<>();
        disConnect.put("name", "disconnect");
        try {
            if (presenceConnectionListener != null){
                presenceConnectionListener.onDisconnected(disConnect);
            }
            if (mIsReconnectionAttemptNeeded) {
//                mIsReconnectionAttemptNeeded = true;
                reconnectAttempt();
            }
        } catch (Exception e1) {
            Log.d(LOG_TAG, "onDisconnect", e1);
        }

    }*/

    /**
     * Method to Reconnection attempt based on incremental delay
     *
     * @reurn
     */
   /* private void reconnectAttempt() {
//        mIsImmediateFetchActionNeeded = true;
        mReconnectDelay = getReconnectDelay();
        try {
            final Handler _handler = new Handler();
            Runnable r = new Runnable() {

                @Override
                public void run() {
                    Log.d(LOG_TAG, "Entered into reconnection post delayed " + mReconnectDelay);
                    if (mIsReconnectionAttemptNeeded && !isConnected()) {
                        connect(Url);
                        mReconnectDelay = getReconnectDelay();
                        _handler.postDelayed(this, mReconnectDelay);
                        Log.d(LOG_TAG, "#### trying to reconnect");
                    }

                }
            };
            _handler.postDelayed(r, mReconnectDelay);
        } catch (Exception e) {
            Log.d(LOG_TAG, ":: The Exception is " + e.toString());
        }
    }
*/
    /**
     * For disconnecting user's presence
     * Call this method when the user logged out
     */
    /*public void disConnect() {
        this.mIsReconnectionAttemptNeeded = false;
        if (_client != null && _client.getWebSocket() != null) {
            try {
                _client.getWebSocket().disconnect();
                _client.disconnect();
                _client = null;
            } catch (Exception e) {
                Log.d(LOG_TAG, "Exception while disconnection");
            }
            Log.d(LOG_TAG, "DisConnected successfully");
        } else {
            Log.d(LOG_TAG, "Cannot disconnect.._client is null");
        }
    }
    public boolean isConnected() {
        if (_client != null && _client.isConnected())
            return true;
        return false;
    }*/

    /**
     * The reconnection attempt delay(incremental delay)
     *
     * @return
     */
    private int getReconnectDelay() {
        mReconnectionCount++;
        Log.d(LOG_TAG, "Reconnection count " + mReconnectionCount);
        if (mReconnectionCount > 10) mReconnectionCount = 1;
        Random rint = new Random();
        return (rint.nextInt(5) + 1) * mReconnectionCount * 1000;
    }

    public PresenceConnectionListener getPresenceConnectionListener() {
        return presenceConnectionListener;
    }

    public void setPresenceConnectionListener(PresenceConnectionListener presenceConnectionListener) {
        this.presenceConnectionListener = presenceConnectionListener;
    }
}