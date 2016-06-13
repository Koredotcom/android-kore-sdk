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
import kore.botssdk.net.BotRestService;
import kore.botssdk.net.RestRequest;
import kore.botssdk.net.RestResponse;
import kore.botssdk.utils.Constants;

/**
 * Created by Ramachandra Pradeep on 6/1/2016.
 */
public final class SocketWrapper {

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

    private SpiceManager spiceManager = new SpiceManager(BotRestService.class);
    private Context mContext;

    /**
     * Restricting outside object creation
     */
    private SocketWrapper(Context mContext) {
        spiceManager.start(mContext);
        this.mContext = mContext;
    }

    /**
     *
     * @return
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
     *
     * @return
     * @throws CloneNotSupportedException
     */
    @Override
    protected Object clone() throws CloneNotSupportedException {
        return new CloneNotSupportedException("Clone not supported");
    }

    /**
     * Method to invoke connection
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
        if (!spiceManager.isStarted()) {
            spiceManager.start(mContext);
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

        spiceManager.execute(request, new RequestListener<RestResponse.RTMUrl>() {
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
     *
     * @param clientId
     * @param secretKey
     */
    public void connectAnonymous(final String clientId, final String secretKey,SocketConnectionListener socketConnectionListener) {

        this.socketConnectionListener = socketConnectionListener;
        this.accessToken = null;
        this.clientId = clientId;
        this.secretKey = secretKey;
        final String chatBot = "";
        final String taskBotId = "";

        //If spiceManager is not started then start it
        if (!spiceManager.isStarted()) {
            spiceManager.start(mContext);
        }

        RestRequest<RestResponse.RTMUrl> request = new RestRequest<RestResponse.RTMUrl>(RestResponse.RTMUrl.class, null, null) {
            @Override
            public RestResponse.RTMUrl loadDataFromNetwork() throws Exception {
                HashMap<String, Object> hsh = new HashMap<>();
                hsh.put(Constants.KEY_ASSERTION, new AnonymousAssertionModel(clientId, secretKey));

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


        spiceManager.execute(request, new RequestListener<RestResponse.RTMUrl>() {
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
                        if(spiceManager != null && spiceManager.isStarted())
                            spiceManager.shouldStop();
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
     *
     */

    private void reconnect() {
        if (accessToken != null) {
            //Reconnection for valid credential
            reconnectForCertifiedUser();
        } else {
            //Reconnection for anonymous
            reconnectForAnonymousUser();
        }
    }

    /**
     *
     */
    private void reconnectForCertifiedUser() {
        Log.i(LOG_TAG, "Connection lost. Reconnecting....");
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

        spiceManager.execute(request, new RequestListener<RestResponse.RTMUrl>() {
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
     *
     */
    private void reconnectForAnonymousUser() {

        Log.i(LOG_TAG, "Connection lost. Reconnecting....");
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


        spiceManager.execute(request, new RequestListener<RestResponse.RTMUrl>() {
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
            Log.e(LOG_TAG, "Either WebSocketConnection is not initialized or connection is not present.");
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
        if(spiceManager != null && spiceManager.isStarted()){
            spiceManager.shouldStop();
        }
    }

    /**
     *
     * @param url
     */
    private void determineTLSEnability(String url) {
        mTLSEnabled = url.startsWith(Constants.SECURE_WEBSOCKET_PREFIX);
    }

    public boolean isConnected() {
        if (mConnection != null && mConnection.isConnected()) {
            return true;
        } else {
            return false;
        }
    }

}