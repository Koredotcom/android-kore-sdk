package com.kore.findlysdk.listners;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.kore.findlysdk.BotDb.BotDataPersister;
import com.kore.findlysdk.bot.BotClient;
import com.kore.findlysdk.events.AuthTokenUpdateEvent;
import com.kore.findlysdk.events.KoreEventCenter;
import com.kore.findlysdk.events.NetworkEvents;
import com.kore.findlysdk.events.SocketDataTransferModel;
import com.kore.findlysdk.models.BotInfoModel;
import com.kore.findlysdk.models.BotRequest;
import com.kore.findlysdk.models.JWTTokenResponse;
import com.kore.findlysdk.models.RestResponse;
import com.kore.findlysdk.models.TokenResponseModel;
import com.kore.findlysdk.models.UserNameModel;
import com.kore.findlysdk.net.BotRestBuilder;
import com.kore.findlysdk.net.SDKConfiguration;
import com.kore.findlysdk.utils.BundleConstants;
import com.kore.findlysdk.utils.DateUtils;
import com.kore.findlysdk.utils.NetworkUtility;
import com.kore.findlysdk.utils.Utils;

import java.util.Date;
import java.util.HashMap;
import java.util.Random;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;
import static com.kore.findlysdk.listners.BaseSocketConnectionManager.CONNECTION_STATE.CONNECTING;
import static com.kore.findlysdk.listners.BaseSocketConnectionManager.CONNECTION_STATE.DISCONNECTED;

/**
 * Created by Ramachandra Pradeep on 03-Jan-18.
 */

public class BotSocketConnectionManager extends BaseSocketConnectionManager {

    private BotClient botClient;
    private static BotSocketConnectionManager botSocketConnectionManager;
    private String accessToken;

    public SocketChatListener getChatListener() {
        return chatListener;
    }

    public void setChatListener(SocketChatListener chatListener) {
        this.chatListener = chatListener;
    }

    private SocketChatListener chatListener;
    private RestResponse.BotCustomData botCustomData;


    private final String LOG_TAG = getClass().getSimpleName();
    private Gson gson = new Gson();
    private boolean isWithAuth;

    public CONNECTION_STATE getConnection_state() {
        return connection_state;
    }

    public void setConnection_state(CONNECTION_STATE connection_state) {
        this.connection_state = connection_state;
    }

    private CONNECTION_STATE connection_state = DISCONNECTED;
    private String botAccessToken, botUserId;
    private String botName, streamId;

    private String userId;

    private BotSocketConnectionManager() {
        KoreEventCenter.register(this);
    }

    public static BotSocketConnectionManager getInstance() {
        if (botSocketConnectionManager == null) {
//            synchronized (BotSocketConnectionManager.class) {
//                if (botSocketConnectionManager == null)
                    botSocketConnectionManager = new BotSocketConnectionManager();
//            }
        }
        return botSocketConnectionManager;
    }


    @Override
    public void onOpen(boolean isReconnection) {
        connection_state = CONNECTION_STATE.CONNECTED;
//        KoreEventCenter.post(connection_state);
        if(chatListener != null){
            chatListener.onConnectionStateChanged(connection_state,isReconnection);
        }
        botAccessToken = botClient.getAccessToken();
        botUserId = botClient.getUserId();
        botClient.sendMessage(null);
    }


    @Override
    public void onClose(int code, String reason) {
        connection_state = CONNECTION_STATE.CONNECTED_BUT_DISCONNECTED;
//        KoreEventCenter.post(connection_state);
        if(chatListener != null){
            chatListener.onConnectionStateChanged(connection_state,false);
        }
    }

    @Override
    public void onTextMessage(String payload) {
//        socketUpdateListener.onMessageUpdate(payload, false, null);
//        KoreEventCenter.post(new SocketDataTransferModel(BaseSocketConnectionManager.EVENT_TYPE.TYPE_TEXT_MESSAGE,payload,null));
        persistBotMessage(payload,false,null);
        if(chatListener != null){
            chatListener.onMessage(new SocketDataTransferModel(EVENT_TYPE.TYPE_TEXT_MESSAGE,payload,null,false));
        }
    }

    public static void killInstance() {
        if (botSocketConnectionManager != null) {
            botSocketConnectionManager.stopDelayMsgTimer();
            botSocketConnectionManager.stopAlertMsgTimer();
            botSocketConnectionManager.shutDownConnection();
        }
    }

    @Override
    public void refreshJwtToken() {
        if (isWithAuth) {
            makeJwtCallWithToken(true);
        } else {
            makeJwtCallWithConfig(true);
//            makeTokenCallForJwt(false);
        }
    }

    private void makeJwtCallWithConfig(final boolean isRefresh) {
        try{
            String jwt = botClient.generateJWT(SDKConfiguration.Client.identity,SDKConfiguration.Client.client_secret,SDKConfiguration.Client.client_id,SDKConfiguration.Server.IS_ANONYMOUS_USER);
            botName = SDKConfiguration.Client.bot_name;
            streamId = SDKConfiguration.Client.bot_id;
            if (!isRefresh) {
                botClient.connectAsAnonymousUser(jwt, botName, streamId, botSocketConnectionManager);
            } else {
                KoreEventCenter.post(jwt);
            }
        }catch (Exception e){
            e.printStackTrace();
            Toast.makeText(mContext, "Something went wrong in fetching JWT", Toast.LENGTH_SHORT).show();
            connection_state = isRefresh ? CONNECTION_STATE.CONNECTED_BUT_DISCONNECTED : DISCONNECTED;
            if(chatListener != null )
                chatListener.onConnectionStateChanged(connection_state,false);
        }

    }


    public void persistBotMessage(String payload, boolean isSentMessage, BotRequest sentMsg){

        new BotDataPersister(mContext, userId, payload, isSentMessage, sentMsg)
                .loadDataFromNetwork()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Boolean>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }
                    @Override
                    public void onNext(Boolean isSuccess) {
                        Log.d(LOG_TAG, "Persistence success");
                    }
                    @Override
                    public void onError(Throwable e) {
                        Log.d(LOG_TAG, "Persistence fail");
                    }
                    @Override
                    public void onComplete() {

                    }
                });

        /*botsSpiceManager.execute(botDataPersister, new RequestListener<Void>() {
            @Override
            public void onRequestFailure(SpiceException e) {
                Log.d(LOG_TAG, "Persistence fail");
            }

            @Override
            public void onRequestSuccess(Void aVoid) {
                Log.d(LOG_TAG, "Persistence success");
            }
        });*/
    }
    private void makeJwtCallWithToken(final boolean isRefresh) {
//        Call<JWTTokenResponse> jwtTokenCall = BotRestBuilder.getRestAPI().getJWTToken(Utils.accessTokenHeader(accessToken), new HashMap<String, Object>());
//        RestAPIHelper.enqueueWithRetry(jwtTokenCall, new Callback<JWTTokenResponse>() {
//            @Override
//            public void onResponse(Call<JWTTokenResponse> call, Response<JWTTokenResponse> response) {
//                if(response.isSuccessful()){
//                    jwtKeyResponse = response.body();
//                    botName = jwtKeyResponse.getBotName();
//                    streamId = jwtKeyResponse.getStreamId();
//                    if (!isRefresh) {
////                        botClient.connectAsAnonymousUserForKora(accessToken, jwtKeyResponse.getJwt(), jwtKeyResponse.getClientId(),
////                                jwtKeyResponse.getBotName(), jwtKeyResponse.getStreamId(), botSocketConnectionManager);
//                    } else {
//                        KoreEventCenter.post(response.body().getJwt());
//                    }
//                }else{
////                    Log.d("token refresh", t.getMessage());
//                    connection_state = isRefresh ? CONNECTION_STATE.CONNECTED_BUT_DISCONNECTED : DISCONNECTED;
//                }
//            }
//
//            @Override
//            public void onFailure(Call<JWTTokenResponse> call, Throwable t) {
////                if (t instanceof NoNetworkException) {
////                    Toast.makeText(mContext, "No Network", Toast.LENGTH_SHORT).show();
////                } else {
//                    Log.d("token refresh", t.getMessage());
////                }
//                connection_state = isRefresh ? CONNECTION_STATE.CONNECTED_BUT_DISCONNECTED : DISCONNECTED;
//            }
//        });


    }

    @Override
    public void onRawTextMessage(byte[] payload) {
    }

    @Override
    public void onBinaryMessage(byte[] payload) {
    }

    @Override
    public void startAndInitiateConnectionWithAuthToken(Context mContext, String userId, String accessToken,RestResponse.BotCustomData botCustomData) {
        if (connection_state == null || connection_state == DISCONNECTED) {
            this.mContext = mContext;
            this.userId = userId;
            this.accessToken = accessToken;
            connection_state = CONNECTING;
            this.isWithAuth = true;
            KoreEventCenter.post(connection_state);
            if(botCustomData == null) {
                botCustomData = new RestResponse.BotCustomData();
            }
            botCustomData.put("kmUId", userId);
            botCustomData.put("kmToken", accessToken);
            botClient = new BotClient(mContext, botCustomData);

            initiateConnection();
        }
    }

    @Override
    public void startAndInitiateConnectionWithConfig(Context mContext,RestResponse.BotCustomData botCustomData1) {
        this.botCustomData = botCustomData1;
        if (connection_state == null || connection_state == DISCONNECTED) {
            this.mContext = mContext;
            connection_state = CONNECTING;
//            KoreEventCenter.post(connection_state);
            if(chatListener != null ){
                chatListener.onConnectionStateChanged(connection_state,false);
            }
            if(botCustomData == null) {
                botCustomData = new RestResponse.BotCustomData();
            }
            botCustomData.put("kmUId", userId);
            botCustomData.put("kmToken", accessToken);
            this.isWithAuth = false;
            botClient = new BotClient(mContext, botCustomData);
            initiateConnection();
        }
    }

    @Override
    public void startAndInitiateConnection(Context mContext, String userId, String accessToken, UserNameModel userNameModel, String orgId) {

    }


    private void initiateConnection() {
        if (!NetworkUtility.isNetworkConnectionAvailable(mContext)) {
//            Toast.makeText(mContext, "No Network", Toast.LENGTH_SHORT).show();
            connection_state = DISCONNECTED;
//            KoreEventCenter.post(connection_state);
            if(chatListener != null){
                chatListener.onConnectionStateChanged(connection_state,false);
            }
            return;
        }

        if (isWithAuth)
        {
            makeJwtCallWithToken(false);
        }
        else
        {
            makeJwtCallWithConfig(false);
//            makeTokenCallForJwt(false);
        }
    }

    public String getStreamId() {
        return streamId;
    }

    public void setStreamId(String streamId) {
        this.streamId = streamId;
    }

    public void sendInitMessage(String initialMessage) {
        if(botClient != null)
            botClient.sendMessage(initialMessage);
    }

    public void sendMessage(String message, String payLoad) {
        if (payLoad != null)
            botClient.sendMessage(payLoad);
        else
            botClient.sendMessage(message);

        //Update the bot content list with the send message
        RestResponse.BotMessage botMessage = new RestResponse.BotMessage(message);
        RestResponse.BotPayLoad botPayLoad = new RestResponse.BotPayLoad();
        botPayLoad.setMessage(botMessage);
        BotInfoModel botInfo = new BotInfoModel(botName, streamId, null);
        botPayLoad.setBotInfo(botInfo);
        Gson gson = new Gson();
        String jsonPayload = gson.toJson(botPayLoad);

        BotRequest botRequest = gson.fromJson(jsonPayload, BotRequest.class);
        botRequest.setCreatedOn(DateUtils.isoFormatter.format(new Date()));
        persistBotMessage(null,true,botRequest);
        if(chatListener != null ){
            chatListener.onMessage(new SocketDataTransferModel(EVENT_TYPE.TYPE_MESSAGE_UPDATE ,message,botRequest,false));
        }

    }

    public void sendPayload(String message, String payLoad) {
        if (payLoad != null)
            botClient.sendFormData(payLoad, message);
        else
            botClient.sendMessage(message);


        //Update the bot content list with the send message
        RestResponse.BotMessage botMessage = new RestResponse.BotMessage(message);
        RestResponse.BotPayLoad botPayLoad = new RestResponse.BotPayLoad();
        botPayLoad.setMessage(botMessage);
        BotInfoModel botInfo = new BotInfoModel(botName, streamId,null);
        botPayLoad.setBotInfo(botInfo);
        Gson gson = new Gson();
        String jsonPayload = gson.toJson(botPayLoad);

        BotRequest botRequest = gson.fromJson(jsonPayload, BotRequest.class);
        botRequest.setCreatedOn(DateUtils.isoFormatter.format(new Date()));
//        socketUpdateListener.onMessageUpdate(message, true, botRequest);
//        KoreEventCenter.post(new SocketDataTransferModel(BaseSocketConnectionManager.EVENT_TYPE.TYPE_MESSAGE_UPDATE ,message,botRequest));
        persistBotMessage(null,true,botRequest);
        if(chatListener != null ){
            chatListener.onMessage(new SocketDataTransferModel(EVENT_TYPE.TYPE_MESSAGE_UPDATE ,message,botRequest,false));
        }

    }

    public void sendPayloadWithParams(String message, String body, String payLoad) {
        botClient.sendFormData(payLoad, body);

        //Update the bot content list with the send message
        RestResponse.BotMessage botMessage = new RestResponse.BotMessage(message);
        RestResponse.BotPayLoad botPayLoad = new RestResponse.BotPayLoad();
        botPayLoad.setMessage(botMessage);
        botPayLoad.setBotInfo(botClient.getBotInfoModel());
        Gson gson = new Gson();
        String jsonPayload = gson.toJson(botPayLoad);

        BotRequest botRequest = gson.fromJson(jsonPayload, BotRequest.class);
        botRequest.setCreatedOn(DateUtils.isoFormatter.format(new Date()));
//        socketUpdateListener.onMessageUpdate(message, true, botRequest);
//        KoreEventCenter.post(new SocketDataTransferModel(BaseSocketConnectionManager.EVENT_TYPE.TYPE_MESSAGE_UPDATE,message,botRequest));
        persistBotMessage(null,true,botRequest);
        if(chatListener != null){
            chatListener.onMessage(new SocketDataTransferModel(EVENT_TYPE.TYPE_MESSAGE_UPDATE,message,botRequest,false));
        }

    }

    @Override
    public void shutDownConnection() {
        botSocketConnectionManager = null;
        if (botClient != null)
            botClient.disconnect();
    }

    @Override
    public void subscribe() {
        isSubscribed = true;
    }

    @Override
    public void subscribe(SocketChatListener listener) {
        isSubscribed = true;
        this.chatListener = listener;
        if(chatListener != null){
            chatListener.onConnectionStateChanged(connection_state,false);
        }
    }

    @Override
    public void unSubscribe() {
        isSubscribed = false;
    }


    public String getBotUserId() {
        return botUserId;
    }

    public void setBotUserId(String botUserId) {
        this.botUserId = botUserId;
    }

    public String getBotAccessToken() {
        return botAccessToken;
    }

    public void setBotAccessToken(String botAccessToken) {
        this.botAccessToken = botAccessToken;
    }

    public void onEvent(NetworkEvents.NetworkConnectivityEvent event) {
        if (event.getNetworkInfo() != null && event.getNetworkInfo().isConnected()) {
            if(botClient != null && botClient.isConnected())return;
            checkConnectionAndRetry(mContext,false);
        }
    }

    public void onEvent(AuthTokenUpdateEvent ev) {
        if (ev.getAccessToken() != null) {
            accessToken = ev.getAccessToken();
            checkConnectionAndRetry(mContext,false);
        }
    }

    public void checkConnectionAndRetry(Context mContext, boolean isFirstTime) {
        ///here going to refresh jwt token from chat activity and it should not
        if (botClient == null) {
            this.mContext = mContext;
/*            if(botCustomData == null) {
                botCustomData = new RestResponse.BotCustomData();
            }
            botCustomData.put("kmUId", userId);
            botCustomData.put("kmToken", accessToken);*/
            botClient = new BotClient(mContext, botCustomData);
            if(isFirstTime){
                if(chatListener != null ){
                    chatListener.onConnectionStateChanged(CONNECTING,false);
                }
                initiateConnection();
            }else {
                refreshJwtToken();
            }
            return;
        }
        if (connection_state == DISCONNECTED)
            initiateConnection();
        else if (connection_state == CONNECTION_STATE.CONNECTED_BUT_DISCONNECTED) {
            refreshJwtToken();
        }
    }
    public void checkConnectionAndRetryForSignify(Context mContext, boolean isFirstTime) {
        ///here going to refresh jwt token from chat activity and it should not
        if (botClient == null) {
            this.mContext = mContext;
            if(botCustomData == null) {
                final String IDENTITY = "identity";
                final String USERNAME = "userName";
                SharedPreferences preferences = mContext.getSharedPreferences("signify_preferences", MODE_PRIVATE);
                final String identity = preferences.getString(IDENTITY, "");
                final String userName = preferences.getString(USERNAME, "");

                botCustomData = new RestResponse.BotCustomData();
                botCustomData.put(USERNAME, userName);
                botCustomData.put(IDENTITY, identity);
                botCustomData.put("userAgent", System.getProperty("http.agent"));
            }
            /*botCustomData.put("kmUId", userId);
            botCustomData.put("kmToken", accessToken);*/
            botClient = new BotClient(mContext, botCustomData);
            if(!isFirstTime){
                if(chatListener != null){
                    chatListener.onConnectionStateChanged(CONNECTING,false);
                }
                initiateConnection();
            }else {
                refreshJwtToken();
            }
            return;
        }
        if (connection_state == DISCONNECTED)
            initiateConnection();
        else if (connection_state == CONNECTION_STATE.CONNECTED_BUT_DISCONNECTED) {
            refreshJwtToken();
        }
    }


    /**
     * initial reconnection count
     */
    private int mAttemptCount = -1;
    private int mAlertAttemptCount = 0;

    private boolean mIsAttemptNeeded = true;
    private boolean mAlertIsAttemptNeeded = true;
   // private boolean isAlertAlarmReset = false;

    /**
     * The reconnection attempt delay(incremental delay)
     *
     * @return
     */
    private int getDelay() {
        mAttemptCount++;
        if (mAttemptCount > 6) {
            mAttemptCount = -1;
            mIsAttemptNeeded = false;
        }
        Random rint = new Random();
        int delay = (rint.nextInt(5) + 1) * mAttemptCount * 2500;
        if (delay < 3000) delay = 5000;
        return delay;
    }

    private int alertDelay() {
        mAlertAttemptCount++;
        if (mAlertAttemptCount > 2) {
            mAlertAttemptCount = 0;
            mAlertIsAttemptNeeded = false;
        }

        return 55*1000;
    }


    private void postDelayMessage() {
        int mDelay = getDelay();
        try {
            final Handler _handler = new Handler();
            Runnable r = new Runnable() {

                @Override
                public void run() {
                    if (mIsAttemptNeeded) {
//                        KoreEventCenter.post(Utils.buildBotMessage(BundleConstants.DELAY_MESSAGES[mAttemptCount], null));
                        if(chatListener != null){
                            chatListener.onMessage(Utils.buildBotMessage(BundleConstants.DELAY_MESSAGES[mAttemptCount], streamId,botName));
                        }
                        postDelayMessage();
                    }

                }
            };
            _handler.postDelayed(r, mDelay);
        } catch (Exception e) {
            Log.d("KoraSocketConnection", ":: The Exception is " + e.toString());
        }
    }
    private static Handler alertHandler = new Handler();
    private Runnable alertRunnable = new Runnable() {

        @Override
        public void run() {
            if (mAlertIsAttemptNeeded) {
//                        KoreEventCenter.post(Utils.buildBotMessage(BundleConstants.DELAY_MESSAGES[mAttemptCount], null));
                if(chatListener != null){
                    try {
                        chatListener.onMessage(Utils.buildBotMessage(BundleConstants.SESSION_END_ALERT_MESSAGES[mAlertAttemptCount - 1], streamId, botName));
                    }catch (ArrayIndexOutOfBoundsException aiobe){}
                }
                postAlertDelayMessage();
            }

        }
    };
    private void postAlertDelayMessage() {
        int mDelay = alertDelay();
        try {
//            final Handler _handler = new Handler();

            alertHandler.postDelayed(alertRunnable, mDelay);
        } catch (Exception e) {
            Log.d("KoraSocketConnection", ":: The Exception is " + e.toString());
        }
    }

    public void startDelayMsgTimer() {
        mIsAttemptNeeded = true;
        postDelayMessage();
    }
    public void startAlertMsgTimer() {
        mAlertIsAttemptNeeded = true;
        postAlertDelayMessage();
    }


    public void stopDelayMsgTimer() {
        mAttemptCount = -1;
        mIsAttemptNeeded = false;
    }
    public void stopAlertMsgTimer() {
        mAlertAttemptCount = 0;
        alertHandler.removeCallbacks(alertRunnable);
        mAlertIsAttemptNeeded = false;
    }


    public void resetAlertHandler(){
        mAlertAttemptCount = 0;
        alertHandler.removeCallbacks(alertRunnable);
        startAlertMsgTimer();

    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Override
    public void ttsUpdateListener(boolean isTTSEnabled) {

    }

    @Override
    public void ttsOnStop() {

    }
}
