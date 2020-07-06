package com.kore.ai.widgetsdk.websocket;

import android.content.Context;
import android.os.Handler;
import android.util.Log;

import com.kore.ai.widgetsdk.koranet.KORestBuilder;
import com.kore.ai.widgetsdk.models.KoreSToken;
import com.kore.ai.widgetsdk.net.KaRestAPIHelper;
import com.kore.ai.widgetsdk.net.KaRestBuilder;
import com.kore.ai.widgetsdk.utils.Utils;

import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Random;

import javax.net.ssl.HttpsURLConnection;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Ramachandra Pradeep on 11-Dec-18.
 */

public class KoraPresenceWrapper  {
    private String userId = null;
//    private int respCode = -1;
    private boolean isConnecting = false;
    private final String LOG_TAG = KoraPresenceWrapper.class.getSimpleName();
    private PresenceConnectionListener mListener = null;

    private static KoraPresenceWrapper pKorePresenceInstance;
    private final int CHANNEL_COUNT = 7;

    private String host;
    private String port;
    private boolean ssl;
    private String accessToken;
    /**
     * Web Socket
     */
//    private Socket mSocket;
    /**
     * Web Socket
     */
    private boolean mIsReconnectionAttemptNeeded = true;
    /**
     * initial reconnection delay 1 Sec
     */
    private int mReconnectDelay = 1000;

    /**
     * initial reconnection count
     */
    private int mReconnectionCount = 0;

    private Context mContext;


    public static KoraPresenceWrapper getInstance() {
        if (pKorePresenceInstance == null)
            pKorePresenceInstance = new KoraPresenceWrapper();
        return pKorePresenceInstance;
    }
    public void initializePresence(String host, String port, boolean ssl, String userId, String accessToken) {
        mContext = KORestBuilder.mContext;
        this.userId = userId;
//        this.respCode = respCode;
        mListener = KoraPresenceListener.getInstance();
        this.host = host;
        this.port = port;
        this.ssl = ssl;
        this.accessToken = accessToken;
//        this.mIsImmediateFetchActionNeeded = mIsImmediateFetchActionNeeded;

        Call<KoreSToken> sTokenCall = KaRestBuilder.getKaRestAPI().getSToken(Utils.ah(accessToken),new Object());
        KaRestAPIHelper.enqueueWithRetry(sTokenCall, new Callback<KoreSToken>() {
            @Override
            public void onResponse(Call<KoreSToken> call, Response<KoreSToken> response) {
                if(response.isSuccessful()){
                    connect(response.body());
                }
            }

            @Override
            public void onFailure(Call<KoreSToken> call, Throwable t) {

            }
        });


    }

    private void connect(KoreSToken sToken) {
//        if (sToken != null && sToken.getRESP_CODE() != HttpsURLConnection.HTTP_NOT_FOUND && !isConnected()) {
//            if(isConnecting || isConnected()) return;
//            isConnecting = true;
////            AsyncHttpClient.SocketIORequest req;
//
//            String url = host + ":" + port + "?" + "userid=" + userId + "&" + "sToken=" + sToken.getSToken() + "&channels=" + CHANNEL_COUNT;
//            /**
//             * ssl : isSecureLayer
//             */
//            if (ssl) {
//                url = "https://" + url;
////                req = new AsyncHttpClient.SocketIORequest("https://" + url);
//            } else {
//                url = "http://" + url;
////                req = new AsyncHttpClient.SocketIORequest("http://" + url);
//            }
//            IO.Options opts = new IO.Options();
//            opts.forceNew = true;
//            opts.transports = new String[]{"websocket"};
//            try {
//                mSocket = IO.socket(url,opts);
//            } catch (URISyntaxException e) {
//                throw new RuntimeException(e);
//            }
//            mSocket.on(Socket.EVENT_CONNECT,onConnect);
//            mSocket.on(Socket.EVENT_DISCONNECT,onDisconnect);
//            mSocket.on(Socket.EVENT_CONNECT_ERROR, onConnectError);
//            mSocket.on(Socket.EVENT_CONNECT_TIMEOUT, onConnectError);
//            mSocket.on(Socket.EVENT_MESSAGE,onMessage);
//            mSocket.on("kora",onMessage);
//            mSocket.connect();
            /*SocketIOClient.connect(mContext, req, new ConnectCallback() {

                @Override
                public void onConnectCompleted(Exception ex, SocketIOClient client) {

                    _client = client;
                    mIsReconnectionAttemptNeeded = true;
                    if (ex != null) {
                        KoreLogger.debugLog(LOG_TAG, "WebSocket:: The Exception is " + ex.toString());
                        //Toast.makeText(.this, ex.toString(), Toast.LENGTH_SHORT).show();
                        isConnecting = false;
                        return;
                    }
                    KoreLogger.debugLog(LOG_TAG, "Presence initiated");
                    *//*Subscription will always be called before Sending StatusAll to Presence server
                    - Required By Server Design*//*
                    //Sometimes sync last event service returning 404, dont want to repeat the loop

                    *//*if (mIsReconnectionAttemptNeeded)
                        mIsReconnectionAttemptNeeded = false;*//*

                    mReconnectionCount = 1;
                    mReconnectDelay = 1000;
                    isConnecting = false;

                    *//*if(mIsImmediateFetchActionNeeded) {
                        initiatePresenceOnConnect(userId);
                        mIsImmediateFetchActionNeeded = false;
                    }*//*


                    *//** Save the returned SocketIOClient instance into a variable so you can disconnect it later *//*
                    _client.setDisconnectCallback(KoraPresenceWrapper.this);
                    _client.setErrorCallback(new ErrorCallback() {

                        @Override
                        public void onError(String error) {
                            KoreLogger.debugLog(LOG_TAG, "Inside OnError The Exception is " + error.toString());

                        }
                    });
                    _client.setJSONCallback(new JSONCallback() {

                        @Override
                        public void onJSON(JSONObject json, Acknowledge acknowledge) {
                            try {
                                KoreLogger.debugLog(LOG_TAG, "WebSocket :json:" + json.toString(2));
                            } catch (JSONException e) {
                                KoreLogger.debugLog(LOG_TAG, "WebSocket:: The Exception is " + e.toString());
                            }

                        }
                    });
                    _client.setStringCallback(new StringCallback() {

                        @Override
                        public void onString(String string, Acknowledge acknowledge) {
                            KoreLogger.debugLog(LOG_TAG, "WebSocket:: The String is is " + string);

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
                            _client.setDisconnectCallback(KoraPresenceWrapper.this);

                            _client.addListener("a message", new EventCallback() {

                                @Override
                                public void onEvent(String event, JSONArray argument, Acknowledge acknowledge) {
                                    KoreLogger.debugLog(LOG_TAG, event);
                                }
                            });

                        }
                    });

                }
            }, new Handler(), new CustomCallback() {

                @Override
                public void onStringRecived(HashMap _data) {
                    if (mListener != null && isConnected()) {
                        mListener.onConnected(_data);
                    }
                }
            });*/

//        }else if(sToken != null && sToken.getRESP_CODE() == HttpsURLConnection.HTTP_NOT_FOUND){
////            reconnectAttempt();
////        }
    }

//    private Emitter.Listener onConnect = new Emitter.Listener() {
//        @Override
//        public void call(Object... args) {
//           Log.d("IKIDO","On connect called");
//        }
//    };
//
//    private Emitter.Listener onDisconnect = new Emitter.Listener() {
//        @Override
//        public void call(Object... args) {
//            Log.d("IKIDO","On disconnect called");
//        }
//    };
//
//    private Emitter.Listener onConnectError = new Emitter.Listener() {
//        @Override
//        public void call(Object... args) {
//            Log.d("IKIDO","On connect error called");
//        }
//    };
//    private Emitter.Listener onMessage = new Emitter.Listener() {
//        @Override
//        public void call(Object... args) {
////            JSONObject obj = (JSONObject)args[0];
//            if (mListener != null && isConnected()) {
//                mListener.onConnected((HashMap<String, String>) args[0]);
//            }
//        }
//    };
//
//    public boolean isConnected() {
//        if (mSocket != null && mSocket.connected())
//            return true;
//        return false;
//    }

    /*@Override
    public void onDisconnect(Exception e) {
        isConnecting = false;
        HashMap<String, String> disConnect = new HashMap<>();
        disConnect.put("name", "disconnect");
        try {
            KoreLogger.debugLog(LOG_TAG, "Sending web socket disconnect callback");
            if (mListener != null)
                mListener.onDisconnected(disConnect);

            if (mIsReconnectionAttemptNeeded) {
//                mIsReconnectionAttemptNeeded = true;
                reconnectAttempt();
            }
        } catch (Exception e1) {
            KoreLogger.errorLog(LOG_TAG, "onDisconnect", e1);
        }
    }*/

    /**
     * Method to Reconnection attempt based on incremental delay
     *
     * @reurn
     */
    private void reconnectAttempt() {
        mReconnectDelay = getReconnectDelay();
        try {
            final Handler _handler = new Handler();
            Runnable r = new Runnable() {

                @Override
                public void run() {
//                    KoreLogger.debugLog(LOG_TAG, "Entered into reconnection post delayed " + mReconnectDelay);
//                    if (mIsReconnectionAttemptNeeded && !isConnected()) {
//                        initializePresence(host,port,ssl,userId,accessToken);
////                        mReconnectDelay = getReconnectDelay();
////                        _handler.postDelayed(this, mReconnectDelay);
//                        KoreLogger.debugLog(LOG_TAG, "#### trying to reconnect");
//                    }

                }
            };
            _handler.postDelayed(r, mReconnectDelay);
        } catch (Exception e) {
//            KoreLogger.debugLog(LOG_TAG, ":: The Exception is " + e.toString());
        }
    }

    /**
     * For disconnecting user's presence
     * Call this method when the user logged out
     */
    public void disConnect() {
//        isConnecting = false;
        this.mIsReconnectionAttemptNeeded = false;
//        if (mSocket != null) {
//            try {
//                mSocket.disconnect();
//                mSocket = null;
//            } catch (Exception e) {
//                KoreLogger.debugLog(LOG_TAG, "Exception while disconnection");
//            }
//            KoreLogger.debugLog(LOG_TAG, "DisConnected successfully");
//        } else {
//            KoreLogger.debugLog(LOG_TAG, "Cannot disconnect.._client is null");
//        }
    }

    /**
     * The reconnection attempt delay(incremental delay)
     *
     * @return
     */
    private int getReconnectDelay() {
        mReconnectionCount++;
//        KoreLogger.debugLog(LOG_TAG, "Reconnection count " + mReconnectionCount);
        if (mReconnectionCount > 6) mReconnectionCount = 1;
        Random rint = new Random();
        return (rint.nextInt(5) + 1) * mReconnectionCount * 1000;
    }

}
