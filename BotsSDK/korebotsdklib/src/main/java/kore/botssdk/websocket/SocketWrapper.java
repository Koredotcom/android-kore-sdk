package kore.botssdk.websocket;

import android.content.Context;
import android.util.Log;

import com.octo.android.robospice.SpiceManager;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;

import kore.botssdk.autobahn.WebSocket;
import kore.botssdk.autobahn.WebSocketConnection;
import kore.botssdk.autobahn.WebSocketException;
import kore.botssdk.models.AnonymousAssertionModel;
import kore.botssdk.models.BotInfoModel;
import kore.botssdk.net.BaseSpiceManager;
import kore.botssdk.net.BotRestService;
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

    private String url;
    private URI uri;
    private boolean mTLSEnabled = false;

    private HashMap<String, Object> optParameterBotInfo;
    private String accessToken;
    private String clientId;
    private String secretKey;

    private Context mContext;

    /**
     * Restricting outside object creation
     */
    private SocketWrapper(Context mContext) {
        start(mContext);
        this.mContext = mContext;
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
     * @param chatBot:      Name of the chat-bot
     * @param taskBotId:    Chat-bot's taskId
     */
    public void connect(String accessToken, String chatBot, String taskBotId,SocketConnectionListener socketConnectionListener) {

        this.socketConnectionListener = socketConnectionListener;
        this.accessToken = accessToken;
        optParameterBotInfo = new HashMap<>();

        final String chatBotArg = (chatBot == null) ? "" : chatBot;
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

                RestResponse.RTMUrl rtmUrl = getService().getRtmUrl(
                        accessTokenHeader(jwtGrant.getAuthorization().getAccessToken()), optParameterBotInfo);
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
     * Method to invoke connection for anonymous
     *
     * These keys are generated from bot admin console
     * @param clientId : generated clientId
     * @param uuid : uuid associated with the specific client
     */
    public void connectAnonymous(final String clientId, final String uuid,SocketConnectionListener socketConnectionListener) {

        this.socketConnectionListener = socketConnectionListener;
        this.accessToken = null;
        this.clientId = clientId;
        this.secretKey = uuid;
        final String chatBot = "";
        final String taskBotId = "";

        //If spiceManager is not started then start it
        if (!isConnected()) {
            start(mContext);
        }

        RestRequest<RestResponse.RTMUrl> request = new RestRequest<RestResponse.RTMUrl>(RestResponse.RTMUrl.class, null, null) {
            @Override
            public RestResponse.RTMUrl loadDataFromNetwork() throws Exception {
                HashMap<String, Object> hsh = new HashMap<>();
                hsh.put(Constants.KEY_ASSERTION, new AnonymousAssertionModel(clientId, uuid));

                BotInfoModel botInfoModel = new BotInfoModel(chatBot, taskBotId);
                hsh.put(Constants.BOT_INFO, botInfoModel);

                RestResponse.BotAuthorization jwtGrant = getService().jwtGrantAnonymous(hsh);

                String userId = jwtGrant.getUserInfo().getId();
                this.accessToken = jwtGrant.getAuthorization().getAccessToken();

                RestResponse.RTMUrl rtmUrl = getService().getRtmUrl(
                        accessTokenHeader(jwtGrant.getAuthorization().getAccessToken()), new HashMap<String, Object>());
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
            determineTLSEnability(url);

            try {
                mConnection.connect(uri, new WebSocket.WebSocketConnectionObserver() {
                    @Override
                    public void onOpen() {
                        Log.d(LOG_TAG, "Connection Open.");
                        if (socketConnectionListener != null) {
                            socketConnectionListener.onOpen();
                        }
                    }

                    @Override
                    public void onClose(WebSocketCloseNotification code, String reason) {
                        Log.d(LOG_TAG, "Connection Lost.");
                        if (socketConnectionListener != null) {
                            socketConnectionListener.onClose(code, reason);
                        }
                        if (isConnected()) {
                            stop();
                        }
                    }

                    @Override
                    public void onTextMessage(String payload) {
                        Log.d(LOG_TAG, "onTextMessage payload :" + payload);
                        if (socketConnectionListener != null) {
                            socketConnectionListener.onTextMessage(payload);
                        }
                    }

                    @Override
                    public void onRawTextMessage(byte[] payload) {
                        Log.d(LOG_TAG, "onRawTextMessage payload:" + payload);
                        if (socketConnectionListener != null) {
                            socketConnectionListener.onRawTextMessage(payload);
                        }
                    }

                    @Override
                    public void onBinaryMessage(byte[] payload) {
                        Log.d(LOG_TAG, "onBinaryMessage payload: " + payload);
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

                RestResponse.RTMUrl rtmUrl = getService().getRtmUrl(
                        accessTokenHeader(jwtGrant.getAuthorization().getAccessToken()), optParameterBotInfo, true);
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
                HashMap<String, Object> hsh = new HashMap<>(2);
                hsh.put(Constants.KEY_ASSERTION, new AnonymousAssertionModel(clientId, secretKey));
                RestResponse.BotAuthorization jwtGrant = getService().jwtGrantAnonymous(hsh);

                String userId = jwtGrant.getUserInfo().getId();
                this.accessToken = jwtGrant.getAuthorization().getAccessToken();

                RestResponse.RTMUrl rtmUrl = getService().getRtmUrl(
                        accessTokenHeader(jwtGrant.getAuthorization().getAccessToken()), new HashMap<String, Object>(), true);
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
     * @param msg : The message object
     * @return Was it able to successfully send the message.
     */
    public boolean sendMessage(String msg) {
        if (mConnection != null && mConnection.isConnected()) {
            mConnection.sendTextMessage(msg);
            return true;
        } else {
            reconnect();
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
    private void determineTLSEnability(String url) {
        mTLSEnabled = url.startsWith(Constants.SECURE_WEBSOCKET_PREFIX);
    }

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

}