package kore.botssdk.listener;

import static kore.botssdk.listener.BaseSocketConnectionManager.CONNECTION_STATE.DISCONNECTED;

import android.content.Context;
import android.os.Handler;
import android.widget.Toast;

import com.google.gson.Gson;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import kore.botssdk.botdb.BotDataPersister;
import kore.botssdk.bot.BotClient;
import kore.botssdk.event.KoreEventCenter;
import kore.botssdk.events.AuthTokenUpdateEvent;
import kore.botssdk.events.NetworkEvents;
import kore.botssdk.events.SocketDataTransferModel;
import kore.botssdk.models.BotInfoModel;
import kore.botssdk.models.BotRequest;
import kore.botssdk.models.JWTTokenResponse;
import kore.botssdk.models.TokenResponseModel;
import kore.botssdk.models.UserNameModel;
import kore.botssdk.net.RestAPIHelper;
import kore.botssdk.net.RestBuilder;
import kore.botssdk.net.RestResponse;
import kore.botssdk.net.SDKConfiguration;
import kore.botssdk.utils.BundleConstants;
import kore.botssdk.utils.DateUtils;
import kore.botssdk.utils.LogUtils;
import kore.botssdk.utils.NetworkUtility;
import kore.botssdk.utils.TTSSynthesizer;
import kore.botssdk.utils.Utils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Ramachandra Pradeep on 03-Jan-18.
 */

public class BotSocketConnectionManager extends BaseSocketConnectionManager {

    private BotClient botClient;
    private TTSSynthesizer ttsSynthesizer;
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
    private final Gson gson = new Gson();
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
        if (chatListener != null) {
            chatListener.onConnectionStateChanged(connection_state, isReconnection);
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
        if (chatListener != null) {
            chatListener.onConnectionStateChanged(connection_state, false);
        }
    }

    @Override
    public void onTextMessage(String payload) {
//        socketUpdateListener.onMessageUpdate(payload, false, null);
//        KoreEventCenter.post(new SocketDataTransferModel(BaseSocketConnectionManager.EVENT_TYPE.TYPE_TEXT_MESSAGE,payload,null));
        persistBotMessage(payload, false, null);
        if (chatListener != null) {
            chatListener.onMessage(new SocketDataTransferModel(EVENT_TYPE.TYPE_TEXT_MESSAGE, payload, null, false));
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
        }
    }

    private void makeJwtCallWithConfig(final boolean isRefresh) {
        try {
            String jwt = botClient.generateJWT(SDKConfiguration.Client.identity, SDKConfiguration.Client.client_secret, SDKConfiguration.Client.client_id, SDKConfiguration.Server.IS_ANONYMOUS_USER);
            botName = SDKConfiguration.Client.bot_name;
            streamId = SDKConfiguration.Client.bot_id;
            if (!isRefresh) {
                botClient.connectAsAnonymousUser(jwt, botName, streamId, botSocketConnectionManager);
            } else {
                KoreEventCenter.post(jwt);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(mContext, "Something went wrong in fetching JWT", Toast.LENGTH_SHORT).show();
            connection_state = isRefresh ? CONNECTION_STATE.CONNECTED_BUT_DISCONNECTED : DISCONNECTED;
            if (chatListener != null)
                chatListener.onConnectionStateChanged(connection_state, false);
        }
    }

    public void makeJwtCallwithConfig() {
        String jwt = "";
        try {
            jwt = botClient.generateJWTForAPI(SDKConfiguration.Client.webHook_identity, SDKConfiguration.Client.webHook_client_secret, SDKConfiguration.Client.webHook_client_id, SDKConfiguration.Server.IS_ANONYMOUS_USER);
            KoreEventCenter.post(jwt);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(mContext, "Something went wrong in fetching JWT", Toast.LENGTH_SHORT).show();
        }

    }


    public void persistBotMessage(String payload, boolean isSentMessage, BotRequest sentMsg) {

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
                        LogUtils.d(LOG_TAG, "Persistence success");
                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtils.d(LOG_TAG, "Persistence fail");
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private void makeJwtCallWithToken(final boolean isRefresh) {
        Call<JWTTokenResponse> jwtTokenCall = RestBuilder.getRestAPI().getJWTToken(Utils.accessTokenHeader(accessToken), new HashMap<String, Object>());
        RestAPIHelper.enqueueWithRetry(jwtTokenCall, new Callback<JWTTokenResponse>() {
            @Override
            public void onResponse(Call<JWTTokenResponse> call, Response<JWTTokenResponse> response) {
                if (response.isSuccessful()) {
                    jwtKeyResponse = response.body();
                    botName = jwtKeyResponse.getBotName();
                    streamId = jwtKeyResponse.getStreamId();
                    if (!isRefresh) {
//                        botClient.connectAsAnonymousUserForKora(accessToken, jwtKeyResponse.getJwt(), jwtKeyResponse.getClientId(),
//                                jwtKeyResponse.getBotName(), jwtKeyResponse.getStreamId(), botSocketConnectionManager);
                    } else {
                        KoreEventCenter.post(response.body().getJwt());
                    }
                } else {
//                    Log.d("token refresh", t.getMessage());
                    connection_state = isRefresh ? CONNECTION_STATE.CONNECTED_BUT_DISCONNECTED : DISCONNECTED;
                }
            }

            @Override
            public void onFailure(Call<JWTTokenResponse> call, Throwable t) {
//                if (t instanceof NoNetworkException) {
//                    Toast.makeText(mContext, "No Network", Toast.LENGTH_SHORT).show();
//                } else {
                LogUtils.d("token refresh", t.getMessage());
//                }
                connection_state = isRefresh ? CONNECTION_STATE.CONNECTED_BUT_DISCONNECTED : DISCONNECTED;
            }
        });


    }

    private void makeTokenCallForJwt(final boolean isRefresh) {

        if (botCustomData == null) {
            botCustomData = new RestResponse.BotCustomData();
        }
//        botCustomData.put("botId", SDKConfiguration.Client.bot_id);
        botCustomData.put("tenantId", SDKConfiguration.Client.tenant_id);

        Call<TokenResponseModel> jwtTokenCall = RestBuilder.getTokenRestAPI().getTokenJWT(botCustomData);
        RestAPIHelper.enqueueWithRetry(jwtTokenCall, new Callback<TokenResponseModel>() {
            @Override
            public void onResponse(Call<TokenResponseModel> call, Response<TokenResponseModel> response) {
                if (response.isSuccessful()) {
                    tokenResponseModel = response.body();
                    botName = tokenResponseModel.getBotInfo().getName();
                    streamId = tokenResponseModel.getBotInfo().get_id();
                    SDKConfiguration.Server.setServerUrl(tokenResponseModel.getKoreAPIUrl());
                    SDKConfiguration.Server.setKoreBotServerUrl(tokenResponseModel.getKoreAPIUrl());
                    KoreEventCenter.post(tokenResponseModel.getBranding());

                    if (!isRefresh) {
                        botClient.connectAsAnonymousUser(tokenResponseModel.getJwt(), botName, streamId, botSocketConnectionManager);
                    } else {
                        KoreEventCenter.post(tokenResponseModel.getJwt());
                    }
                } else {
//                    Log.d("token refresh", t.getMessage());
                    connection_state = isRefresh ? CONNECTION_STATE.CONNECTED_BUT_DISCONNECTED : DISCONNECTED;
                }
            }

            @Override
            public void onFailure(Call<TokenResponseModel> call, Throwable t) {
//                if (t instanceof NoNetworkException) {
//                    Toast.makeText(mContext, "No Network", Toast.LENGTH_SHORT).show();
//                } else {
                LogUtils.d("token refresh", t.getMessage());
//                }
                connection_state = isRefresh ? CONNECTION_STATE.CONNECTED_BUT_DISCONNECTED : DISCONNECTED;
            }
        });


    }

    @Override
    public void onRawTextMessage(byte[] payload) {
    }

    @Override
    public void onBinaryMessage(byte[] payload) {
    }

    @Override
    public void startAndInitiateConnectionWithAuthToken(Context mContext, String userId, String accessToken, RestResponse.BotCustomData botCustomData) {
        if (connection_state == null || connection_state == DISCONNECTED) {
            this.mContext = mContext;
            this.userId = userId;
            this.accessToken = accessToken;
            connection_state = CONNECTION_STATE.CONNECTING;
            this.isWithAuth = true;
            KoreEventCenter.post(connection_state);
            if (botCustomData == null) {
                botCustomData = new RestResponse.BotCustomData();
            }
            botCustomData.put("kmUId", userId);
            botCustomData.put("kmToken", accessToken);
            botClient = new BotClient(mContext, botCustomData);
            ttsSynthesizer = new TTSSynthesizer(mContext);
//            this.socketUpdateListener = socketUpdateListener;

            initiateConnection();
        }
    }

    @Override
    public void startAndInitiateConnectionWithConfig(Context mContext, RestResponse.BotCustomData botCustomData1) {
        this.botCustomData = botCustomData1;
        if (connection_state == null || connection_state == DISCONNECTED) {
            this.mContext = mContext;
            connection_state = CONNECTION_STATE.CONNECTING;
//            KoreEventCenter.post(connection_state);
            if (chatListener != null) {
                chatListener.onConnectionStateChanged(connection_state, false);
            }
            if (botCustomData == null) {
                botCustomData = new RestResponse.BotCustomData();
            }
            botCustomData.put("kmUId", userId);
            botCustomData.put("kmToken", accessToken);
            this.isWithAuth = false;
            botClient = new BotClient(mContext, botCustomData);
            ttsSynthesizer = new TTSSynthesizer(mContext);
//            this.socketUpdateListener = socketUpdateListener;
            /*if (!botsSpiceManager.isStarted())
                botsSpiceManager.start(this.mContext);*/
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
            if (chatListener != null) {
                chatListener.onConnectionStateChanged(connection_state, false);
            }
            return;
        }

        if (isWithAuth) {
            makeJwtCallWithToken(false);
        } else {
            if (!SDKConfiguration.Client.isWebHook)
                makeJwtCallWithConfig(false);
            else
                makeJwtCallwithConfig();
        }
    }

    public String getStreamId() {
        return streamId;
    }

    public void setStreamId(String streamId) {
        this.streamId = streamId;
    }

    public void sendInitMessage(String initialMessage) {
        if (botClient != null)
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
        persistBotMessage(null, true, botRequest);
        if (chatListener != null) {
            chatListener.onMessage(new SocketDataTransferModel(EVENT_TYPE.TYPE_MESSAGE_UPDATE, message, botRequest, false));
        }

    }

    public void sendAttachmentMessage(String message, ArrayList<HashMap<String, String>> attachments) {
        stopTextToSpeech();
        if (message != null) {
            if (attachments != null && attachments.size() > 0)
                botClient.sendMessage(message, attachments);
            else
                botClient.sendMessage(message);
        } else if (attachments != null && attachments.size() > 0)
            botClient.sendMessage(message, attachments);

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
        persistBotMessage(null, true, botRequest);
        if (chatListener != null) {
            chatListener.onMessage(new SocketDataTransferModel(EVENT_TYPE.TYPE_MESSAGE_UPDATE, message, botRequest, false));
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
        BotInfoModel botInfo = new BotInfoModel(botName, streamId, null);
        botPayLoad.setBotInfo(botInfo);
        Gson gson = new Gson();
        String jsonPayload = gson.toJson(botPayLoad);

        BotRequest botRequest = gson.fromJson(jsonPayload, BotRequest.class);
        botRequest.setCreatedOn(DateUtils.isoFormatter.format(new Date()));
//        socketUpdateListener.onMessageUpdate(message, true, botRequest);
//        KoreEventCenter.post(new SocketDataTransferModel(BaseSocketConnectionManager.EVENT_TYPE.TYPE_MESSAGE_UPDATE ,message,botRequest));
        persistBotMessage(null, true, botRequest);
        if (chatListener != null) {
            chatListener.onMessage(new SocketDataTransferModel(EVENT_TYPE.TYPE_MESSAGE_UPDATE, message, botRequest, false));
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
        persistBotMessage(null, true, botRequest);
        if (chatListener != null) {
            chatListener.onMessage(new SocketDataTransferModel(EVENT_TYPE.TYPE_MESSAGE_UPDATE, message, botRequest, false));
        }

    }

    @Override
    public void shutDownConnection() {
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
        if (chatListener != null) {
            chatListener.onConnectionStateChanged(connection_state, false);
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
            if (ttsSynthesizer != null)
                ttsSynthesizer.stopTextToSpeech();
        } catch (IllegalArgumentException | NullPointerException exception) {
            exception.printStackTrace();
        }
    }

    public void setTtsEnabled(boolean ttsEnabled) {
        if (ttsSynthesizer != null) {
            ttsSynthesizer.setTtsEnabled(ttsEnabled);
        }
    }

    public boolean isTTSEnabled() {
        return ttsSynthesizer != null && ttsSynthesizer.isTtsEnabled();
    }

    public void startSpeak(String text) {
        if (text != null && !text.isEmpty() && isSubscribed) {
            ttsSynthesizer.speak(text.replaceAll("<.*?>", ""), botClient.getAccessToken());
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
            if (botClient != null && botClient.isConnected()) return;
            checkConnectionAndRetry(mContext, false);
        }
    }

    public void onEvent(AuthTokenUpdateEvent ev) {
        if (ev.getAccessToken() != null) {
            accessToken = ev.getAccessToken();
            checkConnectionAndRetry(mContext, false);
        }
    }

    public void checkConnectionAndRetry(Context mContext, boolean isFirstTime) {
        ///here going to refresh jwt token from chat activity and it should not
        if (botClient == null) {
            this.mContext = mContext;
            botClient = new BotClient(mContext, botCustomData);
            refreshJwtToken();
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
        SecureRandom rint = new SecureRandom();
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

        return 55 * 1000;
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
                        if (chatListener != null) {
                            chatListener.onMessage(Utils.buildBotMessage(BundleConstants.DELAY_MESSAGES[mAttemptCount], streamId, botName));
                        }
                        postDelayMessage();
                    }

                }
            };
            _handler.postDelayed(r, mDelay);
        } catch (Exception e) {
            LogUtils.d("KoraSocketConnection", ":: The Exception is " + e);
        }
    }

    private static final Handler alertHandler = new Handler();
    private final Runnable alertRunnable = new Runnable() {

        @Override
        public void run() {
            if (mAlertIsAttemptNeeded) {
//                        KoreEventCenter.post(Utils.buildBotMessage(BundleConstants.DELAY_MESSAGES[mAttemptCount], null));
                if (chatListener != null) {
                    try {
                        chatListener.onMessage(Utils.buildBotMessage(BundleConstants.SESSION_END_ALERT_MESSAGES[mAlertAttemptCount - 1], streamId, botName));
                    } catch (ArrayIndexOutOfBoundsException aiobe) {
                    }
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
            LogUtils.d("KoraSocketConnection", ":: The Exception is " + e);
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


    public void resetAlertHandler() {
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
}
