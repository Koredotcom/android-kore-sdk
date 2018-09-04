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

import kore.botssdk.autobahn.WebSocket;
import kore.botssdk.bot.BotClient;
import kore.botssdk.event.KoreEventCenter;
import kore.botssdk.events.AuthTokenUpdateEvent;
import kore.botssdk.events.NetworkEvents;
import kore.botssdk.models.BotInfoModel;
import kore.botssdk.models.BotRequest;
import kore.botssdk.models.JWTTokenResponse;
import kore.botssdk.net.KaJwtRequest;
import kore.botssdk.net.RestResponse;
import kore.botssdk.utils.BundleConstants;
import kore.botssdk.utils.DateUtils;
import kore.botssdk.utils.NetworkUtility;
import kore.botssdk.utils.TTSSynthesizer;
import kore.botssdk.utils.Utils;

/**
 * Created by Ramachandra Pradeep on 03-Jan-18.
 */

public class BotSocketConnectionManager extends BaseSocketConnectionManager {

    private BotClient botClient;
    private TTSSynthesizer ttsSynthesizer;
    private static BotSocketConnectionManager botSocketConnectionManager;
    private String accessToken;


    private final String LOG_TAG = getClass().getSimpleName();
    private Gson gson = new Gson();

    public CONNECTION_STATE getConnection_state() {
        return connection_state;
    }

    public void setConnection_state(CONNECTION_STATE connection_state) {
        this.connection_state = connection_state;
    }

    private CONNECTION_STATE connection_state;
    private String botAccessToken,botUserId;
    private String botName, streamId;

    private String userId;

    private BotSocketConnectionManager(){
        KoreEventCenter.register(this);
    }

    public static BotSocketConnectionManager getInstance(){
        if(botSocketConnectionManager == null){
            synchronized (BotSocketConnectionManager.class){
                if(botSocketConnectionManager == null)
                    botSocketConnectionManager = new BotSocketConnectionManager();
            }
        }
        return botSocketConnectionManager;
    }


    @Override
    public void onOpen() {
        connection_state = CONNECTION_STATE.CONNECTED;
        KoreEventCenter.post(connection_state);
        botAccessToken = botClient.getAccessToken();
        botUserId = botClient.getUserId();
        botClient.sendMessage(null, null, null);
        socketUpdateListener.onConnectionUpdated();

    }



    @Override
    public void onClose(WebSocket.WebSocketConnectionObserver.WebSocketCloseNotification code, String reason) {
        connection_state = CONNECTION_STATE.CONNECTED_BUT_DISCONNECTED;
        KoreEventCenter.post(connection_state);
    }
    @Override
    public void onTextMessage(String payload) {
        socketUpdateListener.onMessageUpdate(payload,false,null);
    }

    public static void killInstance(){
        if(botSocketConnectionManager != null){
            botSocketConnectionManager.stopDelayMsgTimer();
            botSocketConnectionManager.shutDownConnection();
        }
    }

    @Override
    public void refreshJwtToken() {
        KaJwtRequest request = new KaJwtRequest(accessToken,true);
        botsSpiceManager.execute(request, new RequestListener<JWTTokenResponse>() {
            @Override
            public void onRequestFailure(SpiceException e) {
                if (e instanceof NoNetworkException){
//                    Toast.makeText(mContext, "No Network", Toast.LENGTH_SHORT).show();
                }else {
                    Log.d("token refresh", e.getMessage());
                }
                connection_state = CONNECTION_STATE.CONNECTED_BUT_DISCONNECTED;
            }

            @Override
            public void onRequestSuccess(JWTTokenResponse jwt) {
                jwtKeyResponse = jwt;
                botName = jwtKeyResponse.getBotName();
                streamId = jwtKeyResponse.getStreamId();
                /*botClient.connectAsAnonymousUserForKora(accessToken, jwtKeyResponse.getJwt(),jwtKeyResponse.getClientId(),
                        jwtKeyResponse.getBotName(),jwtKeyResponse.getStreamId(), botSocketConnectionManager);*/
                KoreEventCenter.post(jwt.getJwt());
            }
        });
    }

    @Override
    public void onRawTextMessage(byte[] payload) {}
    @Override
    public void onBinaryMessage(byte[] payload) {}

    @Override
    public void startAndInitiateConnection(Context mContext, String userId, String accessToken,SocketUpdateListener socketUpdateListener) {
        if(connection_state == null || connection_state == CONNECTION_STATE.DISCONNECTED) {
            this.mContext = mContext;
            this.userId = userId;
            this.accessToken = accessToken;
            connection_state = CONNECTION_STATE.CONNECTING;
            KoreEventCenter.post(connection_state);
            RestResponse.BotCustomData botCustomData = new RestResponse.BotCustomData();
            botCustomData.put("kmUId", userId);
            botCustomData.put("kmToken", accessToken);
            botClient = new BotClient(mContext, botCustomData);
            ttsSynthesizer = new TTSSynthesizer(mContext);
            this.socketUpdateListener = socketUpdateListener;
            if (!botsSpiceManager.isStarted())
                botsSpiceManager.start(this.mContext);
            initiateConnection();
        }
    }
    private void initiateConnection() {
        if(!NetworkUtility.isNetworkConnectionAvailable(mContext)){
//            Toast.makeText(mContext, "No Network", Toast.LENGTH_SHORT).show();
            connection_state = CONNECTION_STATE.DISCONNECTED;
            KoreEventCenter.post(connection_state);
            return;
        }
        KaJwtRequest request = new KaJwtRequest(accessToken,true);
        botsSpiceManager.execute(request, new RequestListener<JWTTokenResponse>() {
            @Override
            public void onRequestFailure(SpiceException e) {
                if (e instanceof NoNetworkException){
//                    Toast.makeText(mContext, "No Network", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(mContext, "Something went wrong", Toast.LENGTH_SHORT).show();
                }
                connection_state = CONNECTION_STATE.DISCONNECTED;
            }

            @Override
            public void onRequestSuccess(JWTTokenResponse jwt) {
                jwtKeyResponse = jwt;
                botName = jwtKeyResponse.getBotName();
                streamId = jwtKeyResponse.getStreamId();
                botClient.connectAsAnonymousUserForKora(accessToken, jwtKeyResponse.getJwt(),jwtKeyResponse.getClientId(),
                        jwtKeyResponse.getBotName(),jwtKeyResponse.getStreamId(), botSocketConnectionManager);
            }
        });
    }

    public String getStreamId() {
        return streamId;
    }

    public void setStreamId(String streamId) {
        this.streamId = streamId;
    }

    public void sendInitMessage(String initialMessage){
        botClient.sendMessage(initialMessage, botName, streamId);
    }

    public void sendMessage(String message, String payLoad){
        stopTextToSpeech();
        if(payLoad!=null)
            botClient.sendMessage(payLoad, botName, streamId);
        else
            botClient.sendMessage(message, botName, streamId);

        //Update the bot content list with the send message
        RestResponse.BotMessage botMessage = new RestResponse.BotMessage(message);
        RestResponse.BotPayLoad botPayLoad = new RestResponse.BotPayLoad();
        botPayLoad.setMessage(botMessage);
        BotInfoModel botInfo = new BotInfoModel(botName, streamId);
        botPayLoad.setBotInfo(botInfo);
        Gson gson = new Gson();
        String jsonPayload = gson.toJson(botPayLoad);

        BotRequest botRequest = gson.fromJson(jsonPayload, BotRequest.class);
        botRequest.setCreatedOn(DateUtils.isoFormatter.format(new Date()));
        socketUpdateListener.onMessageUpdate(message,true,botRequest);

    }
    public void sendPayload(String message, String payLoad){
        stopTextToSpeech();
        if(payLoad!=null)
            botClient.sendFormData(payLoad, botName, streamId,message);
        else
            botClient.sendMessage(message, botName, streamId);


        //Update the bot content list with the send message
        RestResponse.BotMessage botMessage = new RestResponse.BotMessage(message);
        RestResponse.BotPayLoad botPayLoad = new RestResponse.BotPayLoad();
        botPayLoad.setMessage(botMessage);
        BotInfoModel botInfo = new BotInfoModel(botName, streamId);
        botPayLoad.setBotInfo(botInfo);
        Gson gson = new Gson();
        String jsonPayload = gson.toJson(botPayLoad);

        BotRequest botRequest = gson.fromJson(jsonPayload, BotRequest.class);
        botRequest.setCreatedOn(DateUtils.isoFormatter.format(new Date()));
        socketUpdateListener.onMessageUpdate(message,true,botRequest);

    }

    public void sendPayloadWithParams(String message, String body, String payLoad){
        stopTextToSpeech();
        botClient.sendFormData(payLoad, botName, streamId,body);

        //Update the bot content list with the send message
        RestResponse.BotMessage botMessage = new RestResponse.BotMessage(message);
        RestResponse.BotPayLoad botPayLoad = new RestResponse.BotPayLoad();
        botPayLoad.setMessage(botMessage);
        BotInfoModel botInfo = new BotInfoModel(botName, streamId);
        botPayLoad.setBotInfo(botInfo);
        Gson gson = new Gson();
        String jsonPayload = gson.toJson(botPayLoad);

        BotRequest botRequest = gson.fromJson(jsonPayload, BotRequest.class);
        botRequest.setCreatedOn(DateUtils.isoFormatter.format(new Date()));
        socketUpdateListener.onMessageUpdate(message,true,botRequest);

    }

    @Override
    public void shutDownConnection() {
        if(botsSpiceManager.isStarted())
            botsSpiceManager.shouldStop();
        botSocketConnectionManager = null;
        if(botClient != null)
            botClient.disconnect();
        if(ttsSynthesizer != null){
            ttsSynthesizer.stopTextToSpeech();

        }
    }

    @Override
    public void subscribe() {
        isSubscribed = true;
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
    private void stopTextToSpeech(){
        try {
            ttsSynthesizer.stopTextToSpeech();
        }catch (IllegalArgumentException | NullPointerException exception){exception.printStackTrace();}
    }
    public void setTtsEnabled(boolean ttsEnabled){
        ttsSynthesizer.setTtsEnabled(ttsEnabled);
    }
    public boolean isTTSEnabled() {
        return ttsSynthesizer != null && ttsSynthesizer.isTtsEnabled();
    }
    public void startSpeak(String text){
        if(text != null && !text.isEmpty() && isSubscribed) {
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

    public void onEvent(NetworkEvents.NetworkConnectivityEvent event){
        if(event.getNetworkInfo() != null && event.getNetworkInfo().isConnected()){
            checkConnectionAndRetry(mContext);
        }
    }

    public void onEvent(AuthTokenUpdateEvent ev){
        if(ev.getAccessToken() != null) {
            accessToken = ev.getAccessToken();
            checkConnectionAndRetry(mContext);
        }
    }

    public void checkConnectionAndRetry(Context mContext){
        if(botClient == null){
            this.mContext = mContext;
            RestResponse.BotCustomData botCustomData = new RestResponse.BotCustomData();
            botCustomData.put("kmUId", userId);
            botCustomData.put("kmToken", accessToken);
            botClient = new BotClient(mContext, botCustomData);
            refreshJwtToken();
            return;
        }
        if(connection_state == CONNECTION_STATE.DISCONNECTED)
            initiateConnection();
        else if(connection_state == CONNECTION_STATE.CONNECTED_BUT_DISCONNECTED){
            refreshJwtToken();
        }
    }


    /**
     * initial reconnection count
     */
    private int mAttemptCount = -1;

    private boolean mIsAttemptNeeded = true;
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
        if(delay < 3000) delay = 5000;
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
                        KoreEventCenter.post(Utils.buildBotMessage(BundleConstants.DELAY_MESSAGES[mAttemptCount],null));
                        postDelayMessage();
                    }

                }
            };
            _handler.postDelayed(r, mDelay);
        } catch (Exception e) {
            Log.d("KoraSocketConnection", ":: The Exception is " + e.toString());
        }
    }

    public void startDelayMsgTimer(){
        mIsAttemptNeeded = true;
        postDelayMessage();
    }


    public void stopDelayMsgTimer(){
        mAttemptCount = -1;
        mIsAttemptNeeded = false;
    }





    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
