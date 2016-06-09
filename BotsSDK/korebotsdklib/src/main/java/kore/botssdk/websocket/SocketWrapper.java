package kore.botssdk.websocket;

import android.util.Log;

import com.octo.android.robospice.SpiceManager;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

import java.util.HashMap;

import de.tavendo.autobahn.WebSocketConnection;
import de.tavendo.autobahn.WebSocketException;
import de.tavendo.autobahn.WebSocketHandler;
import kore.botssdk.models.AnonymousAssertionModel;
import kore.botssdk.models.BotInfoModel;
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

    private boolean mIsReconnectionAttemptNeeded = true;
    private String url;

    private HashMap<String, Object> optParameterBotInfo;
    private String accessToken;
    private SpiceManager spiceManager;

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

    /**
     * Method to invoke connection
     *
     * @param accessToken :
     * @param chatBot
     * @param taskBotId
     * @param spiceManager
     */
    public void connect(String accessToken, final String chatBot, final String taskBotId, SpiceManager spiceManager) {

        this.spiceManager = spiceManager;
        this.accessToken = accessToken;
        optParameterBotInfo = new HashMap<>();
        if (chatBot != null && !chatBot.isEmpty() && taskBotId != null && !taskBotId.isEmpty()) {
            BotInfoModel botInfoModel = new BotInfoModel(chatBot, taskBotId);
            optParameterBotInfo.put("botInfo", botInfoModel);
        }

        RestRequest<RestResponse.RTMUrl> request = new RestRequest<RestResponse.RTMUrl>(RestResponse.RTMUrl.class, null, accessToken) {
            @Override
            public RestResponse.RTMUrl loadDataFromNetwork() throws Exception {
                RestResponse.JWTTokenResponse jwtToken = getService().getJWTToken(accessTokenHeader());
                HashMap<String,Object> hsh = new HashMap<>(1);
                hsh.put(Constants.KEY_ASSERTION,jwtToken.getJwt());
                RestResponse.BotAuthorization jwtGrant = getService().jwtGrant(hsh);

                RestResponse.RTMUrl rtmUrl = getService().getRtmUrl(
                        accessTokenHeader(jwtGrant.getAuthorization().getAccessToken()), optParameterBotInfo);
                return rtmUrl;
            }
        } ;

        spiceManager.execute(request, new RequestListener<RestResponse.RTMUrl>() {
            @Override
            public void onRequestFailure(SpiceException e) {
                Log.e(LOG_TAG, e.getMessage());
            }

            @Override
            public void onRequestSuccess(RestResponse.RTMUrl response) {
                SocketWrapper.getInstance().connectToSocket(response.getUrl());
            }
        });

    }

    public void connectAnonymous(SpiceManager spiceManager) {

        this.spiceManager = spiceManager;
        this.accessToken = null;

        RestRequest<RestResponse.RTMUrl> request = new RestRequest<RestResponse.RTMUrl>(RestResponse.RTMUrl.class, null, null) {
            @Override
            public RestResponse.RTMUrl loadDataFromNetwork() throws Exception {
                HashMap<String,Object> hsh = new HashMap<>(2);
                hsh.put(Constants.KEY_ASSERTION, new AnonymousAssertionModel());
                RestResponse.BotAuthorization jwtGrant = getService().jwtGrantAnonymous(hsh);

                String userId = jwtGrant.getUserInfo().getId();
                this.accessToken = jwtGrant.getAuthorization().getAccessToken();

                RestResponse.RTMUrl rtmUrl = getService().getRtmUrl(
                        accessTokenHeader(jwtGrant.getAuthorization().getAccessToken()), new HashMap<String, Object>());
                return rtmUrl;
            }
        } ;


        spiceManager.execute(request, new RequestListener<RestResponse.RTMUrl>() {
            @Override
            public void onRequestFailure(SpiceException e) {
                Log.e(LOG_TAG, e.getMessage());
            }

            @Override
            public void onRequestSuccess(RestResponse.RTMUrl response) {
                SocketWrapper.getInstance().connectToSocket(response.getUrl());
            }
        });
    }


    private void connectToSocket(String url) {
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

    public void reconnect() {
        if (accessToken != null) {
            //Reconnection for valid credential
            reconnectForCertifiedUser();
        } else {
            //Reconnection for anonymous
            reconnectForAnonymousUser();
        }
    }

    private void reconnectForCertifiedUser() {
        Log.i(LOG_TAG, "Connection lost. Reconnecting....");
        RestRequest<RestResponse.RTMUrl> request = new RestRequest<RestResponse.RTMUrl>(RestResponse.RTMUrl.class, null, accessToken) {
            @Override
            public RestResponse.RTMUrl loadDataFromNetwork() throws Exception {
                RestResponse.JWTTokenResponse jwtToken = getService().getJWTToken(accessTokenHeader());
                HashMap<String,Object> hsh = new HashMap<>(1);
                hsh.put(Constants.KEY_ASSERTION,jwtToken.getJwt());
                RestResponse.BotAuthorization jwtGrant = getService().jwtGrant(hsh);

                RestResponse.RTMUrl rtmUrl = getService().getRtmUrl(
                        accessTokenHeader(jwtGrant.getAuthorization().getAccessToken()), optParameterBotInfo, true);
                return rtmUrl;
            }
        } ;

        spiceManager.execute(request, new RequestListener<RestResponse.RTMUrl>() {
            @Override
            public void onRequestFailure(SpiceException e) {
                Log.e(LOG_TAG, e.getMessage());
            }

            @Override
            public void onRequestSuccess(RestResponse.RTMUrl response) {
                SocketWrapper.getInstance().connectToSocket(response.getUrl());
            }
        });
    }

    private void reconnectForAnonymousUser() {
        
        Log.i(LOG_TAG, "Connection lost. Reconnecting....");
        RestRequest<RestResponse.RTMUrl> request = new RestRequest<RestResponse.RTMUrl>(RestResponse.RTMUrl.class, null, null) {
            @Override
            public RestResponse.RTMUrl loadDataFromNetwork() throws Exception {
                HashMap<String,Object> hsh = new HashMap<>(2);
                hsh.put(Constants.KEY_ASSERTION, new AnonymousAssertionModel());
                RestResponse.BotAuthorization jwtGrant = getService().jwtGrantAnonymous(hsh);

                String userId = jwtGrant.getUserInfo().getId();
                this.accessToken = jwtGrant.getAuthorization().getAccessToken();

                RestResponse.RTMUrl rtmUrl = getService().getRtmUrl(
                        accessTokenHeader(jwtGrant.getAuthorization().getAccessToken()), new HashMap<String, Object>(), true);
                return rtmUrl;
            }
        } ;


        spiceManager.execute(request, new RequestListener<RestResponse.RTMUrl>() {
            @Override
            public void onRequestFailure(SpiceException e) {
                Log.e(LOG_TAG, e.getMessage());
            }

            @Override
            public void onRequestSuccess(RestResponse.RTMUrl response) {
                SocketWrapper.getInstance().connectToSocket(response.getUrl());
            }
        });
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