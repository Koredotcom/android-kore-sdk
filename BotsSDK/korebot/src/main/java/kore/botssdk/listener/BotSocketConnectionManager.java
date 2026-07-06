package kore.botssdk.listener;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.gson.Gson;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Objects;
import java.util.TimeZone;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import kore.botssdk.bot.BotClient;
import kore.botssdk.botdb.BotDataPersister;
import kore.botssdk.event.KoreEventCenter;
import kore.botssdk.events.AuthTokenUpdateEvent;
import kore.botssdk.events.NetworkEvents;
import kore.botssdk.events.SocketDataTransferModel;
import kore.botssdk.io.crossbar.autobahn.websocket.interfaces.IWebSocketConnectionHandler;
import kore.botssdk.models.BotInfoModel;
import kore.botssdk.models.BotRequest;
import kore.botssdk.models.JWTTokenResponse;
import kore.botssdk.models.UserNameModel;
import kore.botssdk.net.BotJWTRestBuilder;
import kore.botssdk.net.RestAPIHelper;
import kore.botssdk.net.RestBuilder;
import kore.botssdk.net.RestResponse;
import kore.botssdk.net.SDKConfiguration;
import kore.botssdk.utils.BundleConstants;
import kore.botssdk.utils.DateUtils;
import kore.botssdk.utils.LogUtils;
import kore.botssdk.utils.NetworkUtility;
import kore.botssdk.utils.StringUtils;
import kore.botssdk.utils.TTSSynthesizer;
import kore.botssdk.utils.Utils;
import kore.botssdk.websocket.SocketWrapper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@SuppressLint("UnknownNullness")
public class BotSocketConnectionManager extends BaseSocketConnectionManager {
    BotClient botClient;
    private TTSSynthesizer ttsSynthesizer;
    static BotSocketConnectionManager botSocketConnectionManager;
    private String accessToken;
    boolean isReconnect = false;
    RestResponse.BotCustomData customData;

    public void setChatListener(SocketChatListener chatListener) {
        this.chatListener = chatListener;
    }

    SocketChatListener chatListener;
    private RestResponse.BotCustomData botCustomData;
    private final String LOG_TAG = getClass().getSimpleName();
    private boolean isWithAuth;

    public CONNECTION_STATE getConnection_state() {
        return connection_state;
    }

    CONNECTION_STATE connection_state = CONNECTION_STATE.DISCONNECTED;
    String botName, streamId;
    private String userId;

    private BotSocketConnectionManager() {
        KoreEventCenter.register(this);
    }

    public static BotSocketConnectionManager getInstance() {
        if (botSocketConnectionManager == null) {
            botSocketConnectionManager = new BotSocketConnectionManager();
        }
        return botSocketConnectionManager;
    }


    @Override
    public void onOpen(boolean isReconnection) {
        connection_state = CONNECTION_STATE.CONNECTED;
        if (chatListener != null) {
            chatListener.onConnectionStateChanged(connection_state, isReconnection);
        }
    }


    @Override
    public void onClose(int code, String reason) {
        if (code == IWebSocketConnectionHandler.CLOSE_CANNOT_CONNECT) {
            connection_state = CONNECTION_STATE.DISCONNECTED;
        } else {
            connection_state = CONNECTION_STATE.CONNECTED_BUT_DISCONNECTED;
        }

        if (chatListener != null) {
            chatListener.onConnectionStateChanged(connection_state, false);
        }

        if (SDKConfiguration.Server.getBotStatusListener() != null) {
            if (code == IWebSocketConnectionHandler.CLOSE_CANNOT_CONNECT) {
                SDKConfiguration.Server.getBotStatusListener().onBotConnectionFail(
                        "BotConnectionFail",
                        "The bot was unable to connect. Please check the internet connection and try again."
                );
            } else if (code == IWebSocketConnectionHandler.CLOSE_CONNECTION_LOST) {
                SDKConfiguration.Server.getBotStatusListener().onBotDisconnected(
                        "BotConnectionLost",
                        "The bot was disconnected due to a network connectivity issue. Please check the internet connection and try reconnecting."
                );
            }
        }
    }

    @Override
    public void onTextMessage(String payload) {
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

    public static void killInstanceToReconnect() {
        if (botSocketConnectionManager != null) {
            botSocketConnectionManager.stopDelayMsgTimer();
            botSocketConnectionManager.stopAlertMsgTimer();
            botSocketConnectionManager.shutDownConnectionToReconnect();
        }
    }

    @Override
    public void refreshJwtToken() {
        if (isWithAuth) {
            makeJwtCallWithToken(true);
        } else if (!StringUtils.isNullOrEmpty(SDKConfiguration.JWTServer.getJwt_token())) {
            makeJwtGrantCall(SDKConfiguration.JWTServer.getJwt_token(), true);
        } else {
            makeStsJwtCallWithConfig(true);
        }
    }

    @Override
    public void onReconnectStopped(String reason) {
        if(chatListener != null)
            chatListener.onConnectionStateChanged(CONNECTION_STATE.RECONNECTION_STOPPED, false);
    }

    @Override
    public void onStartCompleted(boolean isReconnect) {
        if(chatListener != null)
            chatListener.onStartCompleted(isReconnect);
    }

    private void makeStsJwtCallWithConfig(final boolean isRefresh) {
        Call<JWTTokenResponse> getBankingConfigService = BotJWTRestBuilder.getBotJWTRestAPI().getJWTToken(getRequestObject());
        getBankingConfigService.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<JWTTokenResponse> call, @NonNull Response<JWTTokenResponse> response) {

                if (response.isSuccessful()) {
                    JWTTokenResponse jwtTokenResponse = response.body();

                    if (jwtTokenResponse != null) {
                        try {
                            String jwt = jwtTokenResponse.getJwt();
                            botName = SDKConfiguration.Client.bot_name;
                            streamId = SDKConfiguration.Client.bot_id;
                            if (!isRefresh) {
                                botClient.connectAsAnonymousUser(jwt, botName, streamId, botSocketConnectionManager, isReconnect);
                            } else {
                                KoreEventCenter.post(jwt);
                            }
                        } catch (Exception e) {
                            LogUtils.e("Error at MakeStsJwtCall", e+"");
                            Toast.makeText(mContext, "Something went wrong in fetching JWT", Toast.LENGTH_SHORT).show();
                            connection_state = isRefresh ? CONNECTION_STATE.CONNECTED_BUT_DISCONNECTED : CONNECTION_STATE.DISCONNECTED;
                            if (chatListener != null) chatListener.onConnectionStateChanged(connection_state, false);
                        }
                    }
                } else {
                    connection_state = isRefresh ? CONNECTION_STATE.CONNECTED_BUT_DISCONNECTED : CONNECTION_STATE.DISCONNECTED;
                }
            }

            @Override
            public void onFailure(@NonNull Call<JWTTokenResponse> call, @NonNull Throwable t) {
                LogUtils.d("token refresh", Objects.requireNonNull(t.getMessage()));
                connection_state = isRefresh ? CONNECTION_STATE.CONNECTED_BUT_DISCONNECTED : CONNECTION_STATE.DISCONNECTED;
            }
        });

    }

    private void makeJwtGrantCall(String jwtToken, boolean isRefresh) {
        try {
            botName = SDKConfiguration.Client.bot_name;
            streamId = SDKConfiguration.Client.bot_id;
            if (!isRefresh) {
                botClient.connectAsAnonymousUser(jwtToken, botName, streamId, botSocketConnectionManager, isReconnect);
            } else {
                KoreEventCenter.post(jwtToken);
            }
        } catch (Exception e) {
            LogUtils.e("Error at makeJwtGrantCall", e+"");

            if(SDKConfiguration.Server.getBotStatusListener() != null)
                SDKConfiguration.Server.getBotStatusListener().onBotConnectionFail("BotNotConnected", "Error at makeJwtGrantCall"+e);

            connection_state = isRefresh ? CONNECTION_STATE.CONNECTED_BUT_DISCONNECTED : CONNECTION_STATE.DISCONNECTED;
            if (chatListener != null) chatListener.onConnectionStateChanged(connection_state, false);
        }
    }

    private HashMap<String, Object> getRequestObject() {

        HashMap<String, Object> hsh = new HashMap<>();

        hsh.put("clientId", SDKConfiguration.Client.client_id);
        hsh.put("clientSecret", SDKConfiguration.Client.client_secret);
        hsh.put("identity", SDKConfiguration.Client.identity);
        hsh.put("aud", "https://idproxy.kore.com/authorize");
        hsh.put("isAnonymous", false);

        return hsh;
    }

    public void persistBotMessage(String payload, boolean isSentMessage, BotRequest sentMsg) {

        new BotDataPersister(mContext, userId, payload, isSentMessage, sentMsg).loadDataFromNetwork().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {
            }

            @Override
            public void onNext(@NonNull Boolean isSuccess) {
                LogUtils.d(LOG_TAG, "Persistence success");
            }

            @Override
            public void onError(@NonNull Throwable e) {
                LogUtils.d(LOG_TAG, "Persistence fail");
            }

            @Override
            public void onComplete() {
            }
        });
    }

    private void makeJwtCallWithToken(final boolean isRefresh) {
        Call<JWTTokenResponse> jwtTokenCall = RestBuilder.getRestAPI().getJWTToken(Utils.accessTokenHeader(accessToken), new HashMap<>());
        RestAPIHelper.enqueueWithRetry(jwtTokenCall, new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<JWTTokenResponse> call, @NonNull Response<JWTTokenResponse> response) {
                if (response.isSuccessful()) {
                    jwtKeyResponse = response.body();
                    if (jwtKeyResponse != null) {
                        botName = jwtKeyResponse.getBotName();
                        streamId = jwtKeyResponse.getStreamId();
                        if (isRefresh) {
                            KoreEventCenter.post(jwtKeyResponse.getJwt());
                        }
                    } else {
                        connection_state = isRefresh ? CONNECTION_STATE.CONNECTED_BUT_DISCONNECTED : CONNECTION_STATE.DISCONNECTED;
                    }

                } else {
                    connection_state = isRefresh ? CONNECTION_STATE.CONNECTED_BUT_DISCONNECTED : CONNECTION_STATE.DISCONNECTED;
                }
            }

            @Override
            public void onFailure(@NonNull Call<JWTTokenResponse> call, @NonNull Throwable t) {
                LogUtils.d("token refresh", Objects.requireNonNull(t.getMessage()));
                connection_state = isRefresh ? CONNECTION_STATE.CONNECTED_BUT_DISCONNECTED : CONNECTION_STATE.DISCONNECTED;
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
        if (connection_state == null || connection_state == CONNECTION_STATE.DISCONNECTED) {
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
            initiateConnection();
        }
    }

    @Override
    public void startAndInitiateConnectionWithConfig(Context mContext, RestResponse.BotCustomData botCustomData1) {
        this.botCustomData = botCustomData1;
        if (connection_state == null || connection_state == CONNECTION_STATE.DISCONNECTED) {
            this.mContext = mContext;
            connection_state = CONNECTION_STATE.CONNECTING;
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
            initiateConnection();
        }
    }

    @Override
    public void startAndInitiateConnectionWithReconnect(Context mContext, RestResponse.BotCustomData botCustomData, boolean isReconnect) {
        this.botCustomData = botCustomData;
        this.isReconnect = isReconnect;
        if (connection_state == null || connection_state == CONNECTION_STATE.DISCONNECTED) {
            this.mContext = mContext;
            connection_state = CONNECTION_STATE.CONNECTING;
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
            initiateConnection();
        }
    }

    @Override
    public void startAndInitiateConnection(Context mContext, String userId, String accessToken, UserNameModel userNameModel, String orgId) {

    }


    private void initiateConnection() {
        if (!NetworkUtility.isNetworkConnectionAvailable(mContext)) {
            connection_state = CONNECTION_STATE.DISCONNECTED;
            if (chatListener != null) {
                chatListener.onConnectionStateChanged(connection_state, false);
            }
            return;
        }

        if (isWithAuth) {
            makeJwtCallWithToken(false);
        } else if (!StringUtils.isNullOrEmpty(SDKConfiguration.JWTServer.getJwt_token())) {
            makeJwtGrantCall(SDKConfiguration.JWTServer.getJwt_token(), SDKConfiguration.Client.isWebHook);
        } else {
            makeStsJwtCallWithConfig(SDKConfiguration.Client.isWebHook);
        }
    }

    public String getAccessToken() {
        return SocketWrapper.getInstance(mContext).getAccessToken();
    }

    public void sendMessage(String message) {
        stopTextToSpeech();
        customData = new RestResponse.BotCustomData();

        //Update the bot content list with the send message
        RestResponse.BotPayLoad botPayLoad = new RestResponse.BotPayLoad();
        RestResponse.BotMessage botMessage = new RestResponse.BotMessage(message, "");

        if(SDKConfiguration.OverrideKoreConfig.update_custom_data_to_user_message)
            customData.putAll(SDKConfiguration.Server.customData);

        customData.put("botToken", getAccessToken());
        botMessage.setCustomData(customData);
        botPayLoad.setMessage(botMessage);
        BotInfoModel botInfo = new BotInfoModel(botName, streamId, null);
        botPayLoad.setBotInfo(botInfo);

        RestResponse.Meta meta = new RestResponse.Meta(TimeZone.getDefault().getID(), Locale.getDefault().getISO3Language());
        botPayLoad.setMeta(meta);

        Gson gson = new Gson();
        String jsonPayload = gson.toJson(botPayLoad);

        botClient.sendMessage(jsonPayload);

        BotRequest botRequest = gson.fromJson(jsonPayload, BotRequest.class);
        botRequest.setCreatedOn(DateUtils.isoFormatter.format(new Date()));

        long timeMillis = botPayLoad.getClientMessageId();
        botRequest.setCreatedInMillis(timeMillis);
        botRequest.setFormattedDate(DateUtils.formattedSentDateV6(mContext, timeMillis));
        botRequest.setTimeStamp(botRequest.prepareLocaleTimeStamp(mContext, timeMillis));

        persistBotMessage(null, true, botRequest);
        if (chatListener != null) {
            chatListener.onMessage(new SocketDataTransferModel(EVENT_TYPE.TYPE_MESSAGE_UPDATE, message, botRequest, false));
        }
    }

    public void sendAttachmentMessage(String message, ArrayList<HashMap<String, String>> attachments) {
        stopTextToSpeech();
        final RestResponse.BotPayLoad botPayLoad = getBotPayLoad(message, attachments, botName, streamId);

        Gson gson = new Gson();
        String jsonPayload = gson.toJson(botPayLoad);

        botClient.sendMessage(jsonPayload);

        BotRequest botRequest = gson.fromJson(jsonPayload, BotRequest.class);
        botRequest.setCreatedOn(DateUtils.isoFormatter.format(new Date()));
        long timeMillis = botPayLoad.getClientMessageId();
        botRequest.setCreatedInMillis(timeMillis);
        botRequest.setFormattedDate(DateUtils.formattedSentDateV6(mContext, timeMillis));
        botRequest.setTimeStamp(botRequest.prepareLocaleTimeStamp(mContext, timeMillis));

        persistBotMessage(null, true, botRequest);
        if (chatListener != null) {
            chatListener.onMessage(new SocketDataTransferModel(EVENT_TYPE.TYPE_MESSAGE_UPDATE, message, botRequest, false));
        }

    }

    private RestResponse.BotPayLoad getBotPayLoad(String message, ArrayList<HashMap<String, String>> attachments, String botName, String streamId) {
        RestResponse.BotMessage botMessage = null;
        RestResponse.BotPayLoad botPayLoad = new RestResponse.BotPayLoad();

        customData = new RestResponse.BotCustomData();

        if(SDKConfiguration.OverrideKoreConfig.update_custom_data_to_user_message)
            customData.putAll(SDKConfiguration.Server.customData);

        customData.put("botToken", getAccessToken());

        if (message != null) {
            if (attachments != null && !attachments.isEmpty()) {
                botMessage = new RestResponse.BotMessage(message, attachments);
                botMessage.setCustomData(customData);
            }
            else {
                botMessage = new RestResponse.BotMessage(message, "");
                botMessage.setCustomData(customData);
            }
        } else if (attachments != null && !attachments.isEmpty()) {
            botMessage = new RestResponse.BotMessage("", attachments);
        }

        //Update the bot content list with the send message
        botPayLoad.setMessage(botMessage);
        BotInfoModel botInfo = new BotInfoModel(botName, streamId, null);
        botPayLoad.setBotInfo(botInfo);

        RestResponse.Meta meta = new RestResponse.Meta(TimeZone.getDefault().getID(), Locale.getDefault().getISO3Language());
        botPayLoad.setMeta(meta);

        return botPayLoad;
    }

    public void sendPayload(String message, String payLoad) {
        stopTextToSpeech();
        RestResponse.BotPayLoad botPayLoad = new RestResponse.BotPayLoad();
        customData = new RestResponse.BotCustomData();

        if(SDKConfiguration.OverrideKoreConfig.update_custom_data_to_user_message)
            customData.putAll(SDKConfiguration.Server.customData);

        //Update the bot content list with the send message
        RestResponse.BotMessage botMessage = new RestResponse.BotMessage(payLoad, message);
        botPayLoad.setMessage(botMessage);
        customData.put("botToken", getAccessToken());
        botMessage.setCustomData(customData);
        BotInfoModel botInfo = new BotInfoModel(botName, streamId, null);
        botPayLoad.setBotInfo(botInfo);

        //Adding the metadata for bot request
        RestResponse.Meta meta = new RestResponse.Meta(TimeZone.getDefault().getID(), Locale.getDefault().getISO3Language());
        botPayLoad.setMeta(meta);

        Gson gson = new Gson();
        String jsonPayload = gson.toJson(botPayLoad);

        botClient.sendMessage(jsonPayload);

        BotRequest botRequest = gson.fromJson(jsonPayload, BotRequest.class);

        if(message != null && !message.isEmpty())
            botRequest.getMessage().setBody(message);

        botRequest.setCreatedOn(DateUtils.isoFormatter.format(new Date()));
        long timeMillis = botPayLoad.getClientMessageId();
        botRequest.setCreatedInMillis(timeMillis);
        botRequest.setFormattedDate(DateUtils.formattedSentDateV6(mContext, timeMillis));
        botRequest.setTimeStamp(botRequest.prepareLocaleTimeStamp(mContext, timeMillis));
        persistBotMessage(null, true, botRequest);
        if (chatListener != null) {
            chatListener.onMessage(new SocketDataTransferModel(EVENT_TYPE.TYPE_MESSAGE_UPDATE, message, botRequest, false));
        }
    }

    @Override
    public void shutDownConnection() {
        if (botClient != null) botClient.disconnect();
        if (ttsSynthesizer != null) {
            ttsSynthesizer.stopTextToSpeech();
        }
        KoreEventCenter.unregister(this);
        botSocketConnectionManager = null;
    }
    public void shutDownConnectionToReconnect() {
        botSocketConnectionManager = null;
        if (botClient != null) botClient.disconnectToReconnect();
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
            if (ttsSynthesizer != null) ttsSynthesizer.stopTextToSpeech();
        } catch (IllegalArgumentException | NullPointerException exception) {
            LogUtils.e("Error at stopTextToSpeech", ""+exception);
        }
    }

    public boolean isTTSEnabled() {
        return ttsSynthesizer != null && ttsSynthesizer.isTtsEnabled();
    }

    public void startSpeak(String text) {
        if (text != null && !text.isEmpty() && isSubscribed && ttsSynthesizer != null) {
            ttsSynthesizer.speak(text.replaceAll("<.*?>", ""), botClient.getAccessToken());
        }
    }

    public void onEvent(NetworkEvents.NetworkConnectivityEvent event) {
        if (event.getNetworkInfo() != null && event.getNetworkInfo().isConnected()) {
            if (botClient != null && botClient.isConnected()) return;
            checkConnectionAndRetry(mContext);
        }
    }

    public void onEvent(AuthTokenUpdateEvent ev) {
        if (ev.getAccessToken() != null) {
            accessToken = ev.getAccessToken();
            checkConnectionAndRetry(mContext);
        }
    }

    public void checkConnectionAndRetry(Context mContext) {
        ///here going to refresh jwt token from chat activity and it should not
        if (botClient == null) {
            this.mContext = mContext;
            botClient = new BotClient(mContext, botCustomData);
            refreshJwtToken();
            return;
        }
        if (connection_state == CONNECTION_STATE.DISCONNECTED) initiateConnection();
        else if (connection_state == CONNECTION_STATE.CONNECTED_BUT_DISCONNECTED) {
            refreshJwtToken();
        }
    }

    /**
     * initial reconnection count
     */
    int mAttemptCount = -1;
    int mAlertAttemptCount = 0;
    boolean mIsAttemptNeeded = true;
    boolean mAlertIsAttemptNeeded = true;

    /**
     * The reconnection attempt delay(incremental delay)
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


    void postDelayMessage() {
        int mDelay = getDelay();
        try {
            final Handler _handler = new Handler();
            Runnable r = () -> {
                if (mIsAttemptNeeded) {
                    if (chatListener != null) {
                        chatListener.onMessage(Utils.buildBotMessage(BundleConstants.DELAY_MESSAGES[mAttemptCount], streamId, botName));
                    }
                    postDelayMessage();
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
                if (chatListener != null) {
                    try {
                        chatListener.onMessage(Utils.buildBotMessage(BundleConstants.SESSION_END_ALERT_MESSAGES[mAlertAttemptCount - 1], streamId, botName));
                    } catch (ArrayIndexOutOfBoundsException e) {
                        LogUtils.e("Error at alertRunnable", ""+e);
                    }
                }
                postAlertDelayMessage();
            }

        }
    };

    void postAlertDelayMessage() {
        int mDelay = alertDelay();
        try {
            alertHandler.postDelayed(alertRunnable, mDelay);
        } catch (Exception e) {
            LogUtils.d("KoraSocketConnection", ":: The Exception is " + e);
        }
    }

    public void startDelayMsgTimer() {
        mIsAttemptNeeded = true;
        postDelayMessage();
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
}
