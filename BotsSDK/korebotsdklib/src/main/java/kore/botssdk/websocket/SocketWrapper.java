package kore.botssdk.websocket;

import android.content.Context;
import android.util.Log;

import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import kore.botssdk.autobahn.WebSocket;
import kore.botssdk.autobahn.WebSocketConnection;
import kore.botssdk.autobahn.WebSocketException;
import kore.botssdk.event.KoreEventCenter;
import kore.botssdk.models.BotInfoModel;
import kore.botssdk.models.CustomData;
import kore.botssdk.net.BaseSpiceManager;
import kore.botssdk.net.RestRequest;
import kore.botssdk.net.RestResponse;
import kore.botssdk.utils.Constants;

/**
 * Created by Ramachandra Pradeep on 6/1/2016.
 * Copyright (c) 2014 Kore Inc. All rights reserved.
 */
public final class SocketWrapper extends BaseSpiceManager {

    private final String LOG_TAG = SocketWrapper.class.getSimpleName();

    public static SocketWrapper pKorePresenceInstance;
    private SocketConnectionListener socketConnectionListener = null;

    private final WebSocketConnection mConnection = new WebSocketConnection();
    private static Timer timer = new Timer();
//    private boolean mIsReconnectionAttemptNeeded = true;

    private String url;
    private URI uri;
//    private boolean mTLSEnabled = false;

    private HashMap<String, Object> optParameterBotInfo;
    private String accessToken;
    private String userAccessToken = null;
    private String clientId;
    private String JWTToken;
    private String uuId;
    private String chatBotName;
    private String taskBotId;
    private String auth;
    private String botUserId;

    private Context mContext;
    /**
     * initial reconnection delay 1 Sec
     */
//    private int mReconnectDelay = 1000;

    /**
     * initial reconnection count
     */
//    private int mReconnectionCount = 0;

    /**
     * Restricting outside object creation
     */
    private SocketWrapper(Context mContext) {
        start(mContext);
        this.mContext = mContext;
        KoreEventCenter.register(this);
    }

    public String getAccessToken(){
        return auth;
    }

    /**
     * The global default SocketWrapper instance
     */
    public static SocketWrapper getInstance(Context mContext) {
        if (pKorePresenceInstance == null) {
            synchronized (SocketWrapper.class) {
                if(pKorePresenceInstance == null) {
                    pKorePresenceInstance = new SocketWrapper(mContext);
                }
            }
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
     * @param taskBotId:    Chat-bot's taskId
     */
    public void connect(String accessToken, String chatBotName, String taskBotId,SocketConnectionListener socketConnectionListener) {

        this.socketConnectionListener = socketConnectionListener;
        this.accessToken = accessToken;
        optParameterBotInfo = new HashMap<>();
//        mIsReconnectionAttemptNeeded = true;

        final String chatBotArg = (chatBotName == null) ? "" : chatBotName;
        final String taskBotIdArg = (taskBotId == null) ? "" : taskBotId;

        BotInfoModel botInfoModel = new BotInfoModel(chatBotArg, taskBotIdArg);
        optParameterBotInfo.put(Constants.BOT_INFO, botInfoModel);

        //If spiceManager is not started then start it
        if (!isConnected()) {
            start(mContext);
        }

        RestRequest<RestResponse.RTMUrl> request = new RestRequest<RestResponse.RTMUrl>(RestResponse.RTMUrl.class, null, accessToken) {
            @Override
            public RestResponse.RTMUrl loadDataFromNetwork() throws Exception {
                RestResponse.JWTTokenResponse jwtToken = getService().getJWTToken(accessTokenHeader());
                HashMap<String, Object> hsh = new HashMap<>();
                hsh.put(Constants.KEY_ASSERTION, jwtToken.getJwt());

                BotInfoModel botInfoModel = new BotInfoModel(chatBotArg, taskBotIdArg);
                hsh.put(Constants.BOT_INFO, botInfoModel);

                RestResponse.BotAuthorization jwtGrant = getService().jwtGrant(hsh);
                auth = jwtGrant.getAuthorization().getAccessToken();
                this.accessToken = jwtGrant.getAuthorization().getAccessToken();
                botUserId = jwtGrant.getUserInfo().getUserId();

                RestResponse.RTMUrl rtmUrl = getService().getRtmUrl(accessTokenHeader(jwtGrant.getAuthorization().getAccessToken()), optParameterBotInfo);
                return rtmUrl;
            }
        };

        getSpiceManager().execute(request, new RequestListener<RestResponse.RTMUrl>() {
            @Override
            public void onRequestFailure(SpiceException e) {
                Log.e(LOG_TAG, e.getMessage());
            }

            @Override
            public void onRequestSuccess(RestResponse.RTMUrl response) {
                try {
                    connectToSocket(response.getUrl());
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    public void ConnectAnonymousForKora(final String userAccessToken, final String sJwtGrant, final String clientId, final String chatBotName, final String taskBotId, final String uuId,SocketConnectionListener socketConnectionListener){
        this.userAccessToken = userAccessToken;
        connectAnonymous(sJwtGrant,clientId,chatBotName,taskBotId,uuId,socketConnectionListener);
    }

    /**
     * Method to invoke connection for anonymous
     *
     * These keys are generated from bot admin console
     * @param clientId : generated clientId
     */
    public void connectAnonymous(final String sJwtGrant, final String clientId, final String chatBotName, final String taskBotId, final String uuId,SocketConnectionListener socketConnectionListener) {

        this.socketConnectionListener = socketConnectionListener;
        this.accessToken = null;
        this.clientId = clientId;
        this.JWTToken = sJwtGrant;
        this.uuId = uuId;
        this.chatBotName = chatBotName;
        this.taskBotId = taskBotId;

        //If spiceManager is not started then start it
        if (!isConnected()) {
            start(mContext);
        }

        RestRequest<RestResponse.RTMUrl> request = new RestRequest<RestResponse.RTMUrl>(RestResponse.RTMUrl.class, null, null) {
            @Override
            public RestResponse.RTMUrl loadDataFromNetwork() throws Exception {

                HashMap<String, Object> hsh = new HashMap<>();
                hsh.put(Constants.KEY_ASSERTION, sJwtGrant);

                BotInfoModel botInfoModel = new BotInfoModel(chatBotName, taskBotId);
                hsh.put(Constants.BOT_INFO, botInfoModel);


                RestResponse.BotAuthorization jwtGrant = getService().jwtGrant(hsh);

                HashMap<String, Object> hsh1 = new HashMap<>();
                hsh1.put(Constants.BOT_INFO, botInfoModel);

                botUserId = jwtGrant.getUserInfo().getUserId();
                this.accessToken = jwtGrant.getAuthorization().getAccessToken();
                auth = jwtGrant.getAuthorization().getAccessToken();

                RestResponse.RTMUrl rtmUrl = getService().getRtmUrl(accessTokenHeader(jwtGrant.getAuthorization().getAccessToken()), hsh1);
                return rtmUrl;
            }
        };


        getSpiceManager().execute(request, new RequestListener<RestResponse.RTMUrl>() {
            @Override
            public void onRequestFailure(SpiceException e) {
                Log.e(LOG_TAG, e.getMessage());
            }

            @Override
            public void onRequestSuccess(RestResponse.RTMUrl response) {
                try {
                    connectToSocket(response.getUrl());
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * To connect through socket
     *
     * @param url : to connect the socket to
     * @throws URISyntaxException
     */
    private void connectToSocket(String url) throws URISyntaxException {
        if (url != null) {
            this.url = url;
            this.uri = new URI(url);

            try {
                mConnection.connect(uri, new WebSocket.WebSocketConnectionObserver() {
                    @Override
                    public void onOpen() {
                        Log.d(LOG_TAG, "Connection Open.");
                        if (socketConnectionListener != null) {
                            socketConnectionListener.onOpen();
                        }
                        startSendingPong();
//                        mReconnectionCount = 1;
//                        mReconnectDelay = 1000;
                    }

                    @Override
                    public void onClose(WebSocketCloseNotification code, String reason) {
                        Log.d(LOG_TAG, "Connection Lost.");
                        if (socketConnectionListener != null) {
                            socketConnectionListener.onClose(code, reason);
                        }
                        if(timer != null){
                            timer.cancel();
                            timer = null;
                        }
                        if (isConnected()) {
                            stop();
                        }
//                        reconnectAttempt();
                    }

                    @Override
                    public void onTextMessage(String payload) {
//                        Log.d(LOG_TAG, "onTextMessage payload :" + payload);
                        if (socketConnectionListener != null) {
                            socketConnectionListener.onTextMessage(payload);
                        }
                    }

                    @Override
                    public void onRawTextMessage(byte[] payload) {
//                        Log.d(LOG_TAG, "onRawTextMessage payload:" + payload);
                        if (socketConnectionListener != null) {
                            socketConnectionListener.onRawTextMessage(payload);
                        }
                    }

                    @Override
                    public void onBinaryMessage(byte[] payload) {
//                        Log.d(LOG_TAG, "onBinaryMessage payload: " + payload);
                        if (socketConnectionListener != null) {
                            socketConnectionListener.onBinaryMessage(payload);
                        }
                    }
                });
            } catch (WebSocketException e) {
                e.printStackTrace();
            }
        }
    }
    private void startSendingPong(){
        TimerTask tTask = new TimerTask() {
            @Override
            public void run() {
                try {
                    if (mConnection != null && mConnection.isConnected()) {
                        mConnection.sendTextMessage("pong from the client");
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        };

       try {
           if(timer == null)timer = new Timer();
           timer.scheduleAtFixedRate(tTask, 1000L, 1000L);
       }catch(Exception e){
           e.printStackTrace();
       }
    }

    /**
     *  Reconnect to socket
     */
    private void reconnect() {
        if (accessToken != null) {
            //Reconnection for valid credential
            reconnectForAuthenticUser();
        } else {
            //Reconnection for anonymous
            reconnectForAnonymousUser();
        }
    }

    /**
     * Reconnection for authentic user
     */
    private void reconnectForAuthenticUser() {
        Log.i(LOG_TAG, "Connection lost. Reconnecting....");

        //If spiceManager is not started then start it
        if (!isConnected()) {
            start(mContext);
        }

        RestRequest<RestResponse.RTMUrl> request = new RestRequest<RestResponse.RTMUrl>(RestResponse.RTMUrl.class, null, accessToken) {
            @Override
            public RestResponse.RTMUrl loadDataFromNetwork() throws Exception {
                RestResponse.JWTTokenResponse jwtToken = getService().getJWTToken(accessTokenHeader());
                HashMap<String, Object> hsh = new HashMap<>(1);
                hsh.put(Constants.KEY_ASSERTION, jwtToken.getJwt());
                RestResponse.BotAuthorization jwtGrant = getService().jwtGrant(hsh);

                RestResponse.RTMUrl rtmUrl = getService().getRtmUrl(accessTokenHeader(jwtGrant.getAuthorization().getAccessToken()), optParameterBotInfo, true);
                return rtmUrl;
            }
        };

        getSpiceManager().execute(request, new RequestListener<RestResponse.RTMUrl>() {
            @Override
            public void onRequestFailure(SpiceException e) {
                Log.e(LOG_TAG, e.getMessage());
            }

            @Override
            public void onRequestSuccess(RestResponse.RTMUrl response) {
                try {
                    connectToSocket(response.getUrl().concat("&isReconnect=true"));
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Reconnection for anonymous user
     */
    private void reconnectForAnonymousUser() {

        Log.i(LOG_TAG, "Connection lost. Reconnecting....");

        //If spiceManager is not started then start it
        if (!isConnected()) {
            start(mContext);
        }

        RestRequest<RestResponse.RTMUrl> request = new RestRequest<RestResponse.RTMUrl>(RestResponse.RTMUrl.class, null, null) {
            @Override
            public RestResponse.RTMUrl loadDataFromNetwork() throws Exception {

                HashMap<String, Object> hsh = new HashMap<>();
                hsh.put(Constants.KEY_ASSERTION, JWTToken);

                BotInfoModel botInfoModel = new BotInfoModel(chatBotName, taskBotId);
                hsh.put(Constants.BOT_INFO, botInfoModel);


                RestResponse.BotAuthorization jwtGrant = getService().jwtGrant(hsh);

                HashMap<String, Object> hsh1 = new HashMap<>();
                hsh1.put(Constants.BOT_INFO, botInfoModel);

                this.accessToken = jwtGrant.getAuthorization().getAccessToken();
                auth = jwtGrant.getAuthorization().getAccessToken();
                botUserId = jwtGrant.getUserInfo().getUserId();
                RestResponse.RTMUrl rtmUrl = getService().getRtmUrl(accessTokenHeader(accessToken), hsh1, true);
                return rtmUrl;
            }
        };


        getSpiceManager().execute(request, new RequestListener<RestResponse.RTMUrl>() {
            @Override
            public void onRequestFailure(SpiceException e) {
                Log.e(LOG_TAG, e.getMessage());
            }

            @Override
            public void onRequestSuccess(RestResponse.RTMUrl response) {
                try {
                    connectToSocket(response.getUrl().concat("&isReconnect=true"));
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void onEvent(String token){
        JWTToken = token;
        reconnect();
    }

    /**
     * @param msg : The message object
     * @return Was it able to successfully send the message.
     */
    public boolean sendMessage(String msg) {
        if (mConnection != null && mConnection.isConnected()) {
            mConnection.sendTextMessage(msg);
            return true;
        } else {
            if(userAccessToken != null && socketConnectionListener != null){
                socketConnectionListener.refreshJwtToken();
            }else {
                reconnect();
            }
            Log.e(LOG_TAG, "Connection is not present. Reconnecting...");
            return false;
        }
    }

   /* *//**
     * Method to Reconnection attempt based on incremental delay
     *
     * @reurn
     *//*
    private void reconnectAttempt() {
//        mIsImmediateFetchActionNeeded = true;
        mReconnectDelay = getReconnectDelay();
        try {
            final Handler _handler = new Handler();
            Runnable r = new Runnable() {

                @Override
                public void run() {
                    Log.d(LOG_TAG, "Entered into reconnection post delayed " + mReconnectDelay);
                    if (mIsReconnectionAttemptNeeded && !isConnected()) {
                        reconnect();
                        Toast.makeText(mContext,"SocketDisConnected",Toast.LENGTH_SHORT).show();
//                        mReconnectDelay = getReconnectDelay();
//                        _handler.postDelayed(this, mReconnectDelay);
                        Log.d(LOG_TAG, "#### trying to reconnect");
                    }

                }
            };
            _handler.postDelayed(r, mReconnectDelay);
        } catch (Exception e) {
            Log.d(LOG_TAG, ":: The Exception is " + e.toString());
        }
    }

    *//**
     * The reconnection attempt delay(incremental delay)
     *
     * @return
     *//*
    private int getReconnectDelay() {
        mReconnectionCount++;
        Log.d(LOG_TAG, "Reconnection count " + mReconnectionCount);
        if (mReconnectionCount > 6) mReconnectionCount = 1;
        Random rint = new Random();
        return (rint.nextInt(5) + 1) * mReconnectionCount * 1000;
    }*/
    /**
     * For disconnecting user's presence
     * Call this method when the user logged out
     */
    public void disConnect() {
//        mIsReconnectionAttemptNeeded = false;
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
        if (isConnected()) {
            stop();
        }
    }

    /**
     * Determine the TLS enability of the url.
     * @param url url to connect to (is generally either ws:// or wss://)
     */
   /* private void determineTLSEnability(String url) {
        mTLSEnabled = url.startsWith(Constants.SECURE_WEBSOCKET_PREFIX);
    }*/

    /**
     * To determine wither socket is connected or not
     * @return boolean indicating the connection presence.
     */
    public boolean isConnected() {
        if (mConnection != null && mConnection.isConnected()) {
            return true;
        } else {
            return false;
        }
    }

    public String getBotUserId() {
        return botUserId;
    }

    public void setBotUserId(String botUserId) {
        this.botUserId = botUserId;
    }
}