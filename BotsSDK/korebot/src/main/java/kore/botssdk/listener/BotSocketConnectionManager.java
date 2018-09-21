package kore.botssdk.listener;

import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.octo.android.robospice.exception.NoNetworkException;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

import java.util.Date;
import java.util.Random;
import java.util.UUID;

import kore.botssdk.bot.BotClient;
import kore.botssdk.event.KoreEventCenter;
import kore.botssdk.events.AuthTokenUpdateEvent;
import kore.botssdk.events.NetworkEvents;
import kore.botssdk.events.SocketDataTransferModel;
import kore.botssdk.models.BotInfoModel;
import kore.botssdk.models.BotRequest;
import kore.botssdk.models.BotResponse;
import kore.botssdk.models.JWTTokenResponse;
import kore.botssdk.net.JWTGrantRequest;
import kore.botssdk.net.KaJwtRequest;
import kore.botssdk.net.RestResponse;
import kore.botssdk.net.SDKConfiguration;
import kore.botssdk.utils.BundleConstants;
import kore.botssdk.utils.DateUtils;
import kore.botssdk.utils.NetworkUtility;
import kore.botssdk.utils.TTSSynthesizer;
import kore.botssdk.utils.Utils;

import static kore.botssdk.listener.BaseSocketConnectionManager.CONNECTION_STATE.CONNECTING;
import static kore.botssdk.listener.BaseSocketConnectionManager.CONNECTION_STATE.DISCONNECTED;

/**
 * Created by Ramachandra Pradeep on 03-Jan-18.
 */

public class BotSocketConnectionManager extends BaseSocketConnectionManager {

    private BotClient botClient;
    private TTSSynthesizer ttsSynthesizer;
    private static BotSocketConnectionManager botSocketConnectionManager;
    private String accessToken;
    private SocketChatListener chatListener;
    private RestResponse.BotCustomData botCustomData;


    private final String LOG_TAG = getClass().getSimpleName();
    private Gson gson = new Gson();

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
    public void onOpen() {
        connection_state = CONNECTION_STATE.CONNECTED;
//        KoreEventCenter.post(connection_state);
        if(chatListener != null && isSubscribed){
            chatListener.onConnectionStateChanged(connection_state);
        }
        botAccessToken = botClient.getAccessToken();
        botUserId = botClient.getUserId();
        botClient.sendMessage(null);
//        socketUpdateListener.onConnectionUpdated();

    }


    @Override
    public void onClose(int code, String reason) {
        connection_state = CONNECTION_STATE.CONNECTED_BUT_DISCONNECTED;
//        KoreEventCenter.post(connection_state);
        if(chatListener != null && isSubscribed){
            chatListener.onConnectionStateChanged(connection_state);
        }
    }

    @Override
    public void onTextMessage(String payload) {
//        socketUpdateListener.onMessageUpdate(payload, false, null);
//        KoreEventCenter.post(new SocketDataTransferModel(BaseSocketConnectionManager.EVENT_TYPE.TYPE_TEXT_MESSAGE,payload,null));
        if(chatListener != null){
            chatListener.onMessage(new SocketDataTransferModel(BaseSocketConnectionManager.EVENT_TYPE.TYPE_TEXT_MESSAGE,payload,null));
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
        /*if (isWithAuth) {
            makeJwtCallWithToken(true);
        } else {*/
            makeJwtCallWithConfig(true);
//        }
    }

    private void makeJwtCallWithConfig(final boolean isRefresh) {
        Log.d("IKIDO","clientid = "+SDKConfiguration.Client.client_id);
        Log.d("IKIDO","Client Secret is "+ SDKConfiguration.Client.client_secret);
        Log.d("IKIDO","is botsSpiceManager is null "+ (botsSpiceManager == null));
        if(botsSpiceManager != null) {
            if(!botsSpiceManager.isStarted())botsSpiceManager.start(mContext);
            Log.d("IKIDO", "is botsSpiceManager is started " + botsSpiceManager.isStarted());
        }
        JWTGrantRequest request = new JWTGrantRequest(SDKConfiguration.Client.client_id,
                SDKConfiguration.Client.client_secret, SDKConfiguration.Server.IS_ANONYMOUS_USER ? UUID.randomUUID().toString() : SDKConfiguration.Client.identity, SDKConfiguration.Server.IS_ANONYMOUS_USER);
        botsSpiceManager.execute(request, new RequestListener<JWTTokenResponse>() {
            @Override
            public void onRequestFailure(SpiceException e) {
                if (e instanceof NoNetworkException) {
//                    Toast.makeText(mContext, "No Network", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(mContext, "Something went wrong", Toast.LENGTH_SHORT).show();
                }
                connection_state = isRefresh ? CONNECTION_STATE.CONNECTED_BUT_DISCONNECTED : DISCONNECTED;
                if(chatListener != null && isSubscribed)
                    chatListener.onConnectionStateChanged(connection_state);
            }

            @Override
            public void onRequestSuccess(JWTTokenResponse jwt) {
                jwtKeyResponse = jwt;
                botName = SDKConfiguration.Client.bot_name;
                streamId = SDKConfiguration.Client.bot_id;
                if (!isRefresh) {
                    botClient.connectAsAnonymousUser(jwt.getJwt(), SDKConfiguration.Client.client_id, botName, streamId, botSocketConnectionManager);
                } else {
                    KoreEventCenter.post(jwt.getJwt());
                }
            }
        });
    }

    private void makeJwtCallWithToken(final boolean isRefresh) {
        KaJwtRequest request = new KaJwtRequest(accessToken, true);
        botsSpiceManager.execute(request, new RequestListener<JWTTokenResponse>() {
            @Override
            public void onRequestFailure(SpiceException e) {
                if (e instanceof NoNetworkException) {
//                    Toast.makeText(mContext, "No Network", Toast.LENGTH_SHORT).show();
                } else {
                    Log.d("token refresh", e.getMessage());
                }
                connection_state = isRefresh ? CONNECTION_STATE.CONNECTED_BUT_DISCONNECTED : DISCONNECTED;
            }

            @Override
            public void onRequestSuccess(JWTTokenResponse jwt) {
                jwtKeyResponse = jwt;
                botName = jwtKeyResponse.getBotName();
                streamId = jwtKeyResponse.getStreamId();
                if (!isRefresh) {
                    botClient.connectAsAnonymousUserForKora(accessToken, jwtKeyResponse.getJwt(), jwtKeyResponse.getClientId(),
                            jwtKeyResponse.getBotName(), jwtKeyResponse.getStreamId(), botSocketConnectionManager);
                } else {
                    KoreEventCenter.post(jwt.getJwt());
                }
            }
        });
    }

    @Override
    public void onRawTextMessage(byte[] payload) {
    }

    @Override
    public void onBinaryMessage(byte[] payload) {
    }

    /*@Override
    public void startAndInitiateConnectionWithAuthToken(Context mContext, String userId, String accessToken,RestResponse.BotCustomData botCustomData) {
        if (connection_state == null || connection_state == DISCONNECTED) {
            this.mContext = mContext;
            this.userId = userId;
            this.accessToken = accessToken;
            connection_state = CONNECTION_STATE.CONNECTING;
            this.isWithAuth = true;
            KoreEventCenter.post(connection_state);
            if(botCustomData == null) {
                botCustomData = new RestResponse.BotCustomData();
            }
            botCustomData.put("kmUId", userId);
            botCustomData.put("kmToken", accessToken);
            botClient = new BotClient(mContext, botCustomData);
            ttsSynthesizer = new TTSSynthesizer(mContext);
//            this.socketUpdateListener = socketUpdateListener;
            if (!botsSpiceManager.isStarted())
                botsSpiceManager.start(this.mContext);
            initiateConnection();
        }
    }*/

    @Override
    public void startAndInitiateConnectionWithConfig(Context mContext,RestResponse.BotCustomData botCustomData1) {
        this.botCustomData = botCustomData1;
        if (connection_state == null || connection_state == DISCONNECTED) {
            this.mContext = mContext;
            connection_state = CONNECTION_STATE.CONNECTING;
//            KoreEventCenter.post(connection_state);
            if(chatListener != null && isSubscribed){
                chatListener.onConnectionStateChanged(connection_state);
            }
            if(botCustomData == null) {
                botCustomData = new RestResponse.BotCustomData();
            }
            botCustomData.put("kmUId", userId);
            botCustomData.put("kmToken", accessToken);
//            this.isWithAuth = false;
            botClient = new BotClient(mContext, botCustomData);
            ttsSynthesizer = new TTSSynthesizer(mContext);
//            this.socketUpdateListener = socketUpdateListener;
            if (!botsSpiceManager.isStarted())
                botsSpiceManager.start(this.mContext);
            initiateConnection();
        }
    }


    private void initiateConnection() {
        if (!NetworkUtility.isNetworkConnectionAvailable(mContext)) {
//            Toast.makeText(mContext, "No Network", Toast.LENGTH_SHORT).show();
            connection_state = DISCONNECTED;
//            KoreEventCenter.post(connection_state);
            if(chatListener != null && isSubscribed){
                chatListener.onConnectionStateChanged(connection_state);
            }
            return;
        }
        /*if (isWithAuth) {
            makeJwtCallWithToken(false);
        } else {*/
            makeJwtCallWithConfig(false);
//        }
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
        stopTextToSpeech();
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
//        socketUpdateListener.onMessageUpdate(message, true, botRequest);
//        KoreEventCenter.post(new SocketDataTransferModel(BaseSocketConnectionManager.EVENT_TYPE.TYPE_MESSAGE_UPDATE ,message,botRequest));
        if(chatListener != null ){
            chatListener.onMessage(new SocketDataTransferModel(BaseSocketConnectionManager.EVENT_TYPE.TYPE_MESSAGE_UPDATE ,message,botRequest));
        }

    }

    public void sendPayload(String message, String payLoad) {
        stopTextToSpeech();
        if (payLoad != null)
            botClient.sendFormData(payLoad, message);
        else
            botClient.sendMessage(message);


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
//        KoreEventCenter.post(new SocketDataTransferModel(BaseSocketConnectionManager.EVENT_TYPE.TYPE_MESSAGE_UPDATE ,message,botRequest));
        if(chatListener != null ){
            chatListener.onMessage(new SocketDataTransferModel(BaseSocketConnectionManager.EVENT_TYPE.TYPE_MESSAGE_UPDATE ,message,botRequest));
        }

    }

    public void sendPayloadWithParams(String message, String body, String payLoad) {
        stopTextToSpeech();
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
        if(chatListener != null){
            chatListener.onMessage(new SocketDataTransferModel(BaseSocketConnectionManager.EVENT_TYPE.TYPE_MESSAGE_UPDATE,message,botRequest));
        }

    }

    @Override
    public void shutDownConnection() {
        if (botsSpiceManager.isStarted())
            botsSpiceManager.shouldStop();
        botSocketConnectionManager = null;
        if (botClient != null)
            botClient.disconnect();
        if (ttsSynthesizer != null) {
            ttsSynthesizer.stopTextToSpeech();

        }
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
            chatListener.onConnectionStateChanged(connection_state);
        }
    }

    @Override
    public void unSubscribe() {
        isSubscribed = false;
        stopTextToSpeech();
    }

    @Override
    public void ttsUpdateListener(boolean isTTSEnabled) {
        stopTextToSpeech();
    }

    @Override
    public void ttsOnStop() {
        stopTextToSpeech();
    }

    public void stopTextToSpeech() {
        try {
            ttsSynthesizer.stopTextToSpeech();
        } catch (IllegalArgumentException | NullPointerException exception) {
            exception.printStackTrace();
        }
    }

    public void setTtsEnabled(boolean ttsEnabled) {
        if(ttsSynthesizer != null) {
            ttsSynthesizer.setTtsEnabled(ttsEnabled);
        }
    }

    public boolean isTTSEnabled() {
        return ttsSynthesizer != null && ttsSynthesizer.isTtsEnabled();
    }

    public void startSpeak(String text) {
        if (text != null && !text.isEmpty() && isSubscribed) {
            ttsSynthesizer.speak(text.replaceAll("\\<.*?>", ""), botClient.getAccessToken());
        }
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
        Log.d("IKIDO","The state of the activity is "+isFirstTime);
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
                if(chatListener != null && isSubscribed){
                    chatListener.onConnectionStateChanged(CONNECTING);
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
    private int mAlertAttemptCount = -1;

    private boolean mIsAttemptNeeded = true;
    private boolean mAlertIsAttemptNeeded = true;
    private boolean isAlertAlarmReset = false;

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
        if (mAlertAttemptCount > 3) {
            mAlertAttemptCount = -1;
            mAlertIsAttemptNeeded = false;
        }
        int delay = (mAlertAttemptCount+1) * 55*1000;
        return delay;
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
                            chatListener.onMessage(Utils.buildBotMessage(BundleConstants.DELAY_MESSAGES[mAttemptCount], null));
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
    static Handler alertHandler = new Handler();
    Runnable alertRunnable = new Runnable() {

        @Override
        public void run() {
            if (mAlertIsAttemptNeeded) {
//                        KoreEventCenter.post(Utils.buildBotMessage(BundleConstants.DELAY_MESSAGES[mAttemptCount], null));
                if(chatListener != null && isSubscribed && !isAlertAlarmReset){
                    chatListener.onMessage(Utils.buildBotMessage(BundleConstants.SESSION_END_ALERT_MESSAGES[mAlertAttemptCount], null));
                }
                isAlertAlarmReset = false;
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
        mAlertAttemptCount = -1;
        alertHandler.removeCallbacks(alertRunnable);
        mAlertIsAttemptNeeded = false;
    }


    public void resetAlertHandler(){
        mAlertAttemptCount = -1;
        isAlertAlarmReset = true;

    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
