package kore.botssdk.bot;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.TimeZone;
import java.util.UUID;

import dagger.hilt.android.EntryPointAccessors;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import kore.botssdk.delegate.LoginTokenDelegate;
import kore.botssdk.delegate.LoginTokenDelegateEntryPoint;
import kore.botssdk.models.BotInfoModel;
import kore.botssdk.models.BotSocketOptions;
import kore.botssdk.net.RestResponse;
import kore.botssdk.utils.Utils;
import kore.botssdk.websocket.SocketConnectionListener;
import kore.botssdk.websocket.SocketWrapper;

/**
 * Created by Ramachandra Pradeep on 6/13/2016.
 * Copyright (c) 2014 Kore Inc. All rights reserved.
 */

/**
 * Gateway for clients to interact with Bots.
 */
public class BotClient {

    public static final String BOT_TOKEN = "botToken";
    public static final String HR_ACCESS_TOKEN = "hrAccessToken";
    public static final String ITSM_ACCESS_TOKEN = "itsmAccessToken";
    private Context mContext;

    public RestResponse.BotCustomData getCustomData() {
        return customData;
    }

    public void setCustomData(RestResponse.BotCustomData customData) {
        this.customData = customData;
    }

    private RestResponse.BotCustomData customData;

    public BotInfoModel getBotInfoModel() {
        return botInfoModel;
    }

    public void setBotInfoModel(BotInfoModel botInfoModel) {
        this.botInfoModel = botInfoModel;
    }

    private BotInfoModel botInfoModel;

    private LoginTokenDelegate delegate = null;

    private BotClient() {
    }

    /**
     * @param mContext
     */
    public BotClient(Context mContext) {
        this.customData = new RestResponse.BotCustomData();
        this.mContext = mContext.getApplicationContext();
        inject();
    }

    public BotClient(Context mContext, RestResponse.BotCustomData customData) {
        this.mContext = mContext;
        this.customData = customData;
        inject();
    }

    private void inject() {
        LoginTokenDelegateEntryPoint entryPoint = EntryPointAccessors.fromApplication(
                mContext.getApplicationContext(),
                LoginTokenDelegateEntryPoint.class
        );
        delegate = entryPoint.getLoginTokenDelegate();
    }

    public void connectAsAnonymousUserForKora(String userAccessToken, String jwtToken, String chatBotName, String taskBotId, SocketConnectionListener socketConnectionListener,
                                              String url, String botUserId, String auth) {
//        String uuid = UUID.randomUUID().toString();//"e56dd516-5491-45b2-9ff7-ffcb7d8f2461";
        botInfoModel = new BotInfoModel(chatBotName, taskBotId, customData);
        SocketWrapper.getInstance(mContext).ConnectAnonymousForKora(userAccessToken, jwtToken, botInfoModel, socketConnectionListener, url, botUserId, auth);
    }

    /**
     * Connection for anonymous user
     *
     * @param socketConnectionListener
     */
    public void connectAsAnonymousUser(String jwtToken, String chatBotName, String taskBotId, SocketConnectionListener socketConnectionListener) {

        String uuid = UUID.randomUUID().toString();//"e56dd516-5491-45b2-9ff7-ffcb7d8f2461";
        botInfoModel = new BotInfoModel(chatBotName, taskBotId, customData);
        SocketWrapper.getInstance(mContext).connectAnonymous(jwtToken, botInfoModel, socketConnectionListener, null);
    }


    public void shouldAttemptToReconnect(boolean value) {
        SocketWrapper.getInstance(mContext).shouldAttemptToReconnect(value);
    }

    /**
     * Connection for anonymous user
     *
     * @param socketConnectionListener
     */
    public void connectAsAnonymousUserWithOptions(String jwtToken, String chatBotName,
                                                  String taskBotId, SocketConnectionListener socketConnectionListener, BotSocketOptions options) {

        String uuid = UUID.randomUUID().toString();//"e56dd516-5491-45b2-9ff7-ffcb7d8f2461";
        botInfoModel = new BotInfoModel(chatBotName, taskBotId, customData);
        SocketWrapper.getInstance(mContext).connectAnonymous(jwtToken, botInfoModel, socketConnectionListener, options);
    }


    public String generateJWT(String email, String secret, String clientId, boolean isAnonymousUser) {
        long curTime = System.currentTimeMillis();
        long expTime = curTime + 86400000;
//        hsh.put("clientSecret",clientSecret);

        return Jwts.builder().claim("iss", clientId).claim("iat", curTime).claim("exp", expTime)
                .claim("aud", "https://idproxy.kore.com/authorize").claim("sub", email).claim("isAnonymous", isAnonymousUser).
                signWith(SignatureAlgorithm.HS256, secret.getBytes()).compact();
    }

    public String generateJWTForAPI(String email, String secret, String clientId, boolean isAnonymousUser) {
        long curTime = System.currentTimeMillis();
        long expTime = curTime + 86400000;

        return Jwts.builder()
                .setHeaderParam("typ", "JWT")
                .claim("iat", curTime)
                .claim("exp", expTime)
                .claim("aud", "https://idproxy.kore.com/authorize")
                .claim("iss", clientId)
                .claim("sub", email)
                .claim("isAnonymous", isAnonymousUser)
                .claim("userIdentity", email)
                .claim("appId", clientId)
                .signWith(SignatureAlgorithm.HS256, secret.getBytes())
                .compact();
    }

    public String getAccessToken() {
        return SocketWrapper.getInstance(mContext).getAccessToken();
    }

    public String getUserId() {
        return SocketWrapper.getInstance(mContext).getBotUserId();
    }

    /**
     * [MANDATORY] Invoke this method to disconnect the previously connected socket connection.
     */
    public void disconnect() {
        SocketWrapper.getInstance(mContext).disConnect();
    }

    /**
     * @return whether socket connection is present
     */
    public boolean isConnected() {
        return SocketWrapper.getInstance(mContext).isConnected();
    }

    private void setCustomData() {
        customData.put(BOT_TOKEN, getAccessToken());
        if (delegate != null) {
            customData.put(HR_ACCESS_TOKEN, delegate.getLoginToken(mContext));
            customData.put(ITSM_ACCESS_TOKEN, delegate.getLoginToken(mContext));
        }
    }

    /**
     * Method to send messages over socket.
     * It uses FIFO pattern to first send if any pending requests are present
     * following current request later onward.
     * <p/>
     * pass 'msg' as NULL on reconnection of the socket to empty the pool
     * by sending messages from the pool.
     *
     * @param msg
     */
    public void sendMessage(String msg) {
        if (msg != null && !msg.isEmpty()) {
            RestResponse.BotPayLoad botPayLoad = new RestResponse.BotPayLoad();
            RestResponse.BotMessage botMessage = new RestResponse.BotMessage(msg);
            setCustomData();
            botMessage.setCustomData(customData);
            botPayLoad.setMessage(botMessage);
            botPayLoad.setBotInfo(botInfoModel);

            RestResponse.Meta meta = new RestResponse.Meta(TimeZone.getDefault().getID(), Locale.getDefault().getISO3Language());
            botPayLoad.setMeta(meta);

            Gson gson = new Gson();
            String jsonPayload = gson.toJson(botPayLoad);

            Log.d("BotClient", "Payload : " + jsonPayload);
            SocketWrapper.getInstance(mContext).sendMessage(jsonPayload);
//            BotRequestPool.getBotRequestStringArrayList().add(jsonPayload);
        }
//        sendQueMessages();
    }

    /**
     * Method to send messages over socket.
     * It uses FIFO pattern to first send if any pending requests are present
     * following current request later onward.
     * <p/>
     * pass 'msg' as NULL on reconnection of the socket to empty the pool
     * by sending messages from the pool.
     *
     * @param msg
     */
    public void sendMessage(String msg, ArrayList<HashMap<String, String>> attachements) {

        if (msg != null && !msg.isEmpty()) {
            RestResponse.BotPayLoad botPayLoad = new RestResponse.BotPayLoad();
            RestResponse.BotMessage botMessage = new RestResponse.BotMessage(msg);

            if (attachements != null && attachements.size() > 0)
                botMessage = new RestResponse.BotMessage(msg, attachements);

            setCustomData();

            botMessage.setCustomData(customData);
            botPayLoad.setMessage(botMessage);
            botPayLoad.setBotInfo(botInfoModel);

            RestResponse.Meta meta = new RestResponse.Meta(TimeZone.getDefault().getID(), Locale.getDefault().getISO3Language());
            botPayLoad.setMeta(meta);

            Gson gson = new Gson();
            String jsonPayload = gson.toJson(botPayLoad);

            Log.d("BotClient", "Payload : " + jsonPayload);
            SocketWrapper.getInstance(mContext).sendMessage(jsonPayload);
        } else if (attachements != null && attachements.size() > 0) {
            RestResponse.BotPayLoad botPayLoad = new RestResponse.BotPayLoad();
            RestResponse.BotMessage botMessage = new RestResponse.BotMessage("", attachements);

            setCustomData();

            botMessage.setCustomData(customData);
            botPayLoad.setMessage(botMessage);
            botPayLoad.setBotInfo(botInfoModel);

            RestResponse.Meta meta = new RestResponse.Meta(TimeZone.getDefault().getID(), Locale.getDefault().getISO3Language());
            botPayLoad.setMeta(meta);

            Gson gson = new Gson();
            String jsonPayload = gson.toJson(botPayLoad);

            Log.d("BotClient", "Payload : " + jsonPayload);
            SocketWrapper.getInstance(mContext).sendMessage(jsonPayload);
        }

    }

    public void updateAuthToken(String accessToken) {
        if (customData != null) {
            customData.put("kmToken", accessToken);
        }
    }

    public void sendFormData(String payLoad, String message) {

        if (payLoad != null && !payLoad.isEmpty()) {
            RestResponse.BotPayLoad botPayLoad = new RestResponse.BotPayLoad();
            RestResponse.BotMessage botMessage = new RestResponse.BotMessage(payLoad);
            setCustomData();
            botMessage.setCustomData(customData);
            botMessage.setParams(Utils.jsonToMap(payLoad));
            botPayLoad.setMessage(botMessage);
            botPayLoad.setBotInfo(botInfoModel);

            RestResponse.Meta meta = new RestResponse.Meta(TimeZone.getDefault().getID(), Locale.getDefault().getISO3Language());
            botPayLoad.setMeta(meta);

            Gson gson = new Gson();
            String jsonPayload = gson.toJson(botPayLoad);

            Log.d("BotClient", "Payload : " + jsonPayload);
            SocketWrapper.getInstance(mContext).sendMessage(jsonPayload);
//            BotRequestPool.getBotRequestStringArrayList().add(jsonPayload);
//            sendQueMessages();
        }

    }

    /*private void sendQueMessages(){
        if (!BotRequestPool.isPoolEmpty()) {
            if (!BotRequestPool.getBotRequestStringArrayList().isEmpty()) {
                ArrayList<String> botRequestStringArrayList = BotRequestPool.getBotRequestStringArrayList();
                int len = botRequestStringArrayList.size();
                for (int i = 0; i < len; i++) {
                    String botRequestPayload = botRequestStringArrayList.get(i);
                    boolean wasSuccessfullySend = SocketWrapper.getInstance(mContext).sendMessage(botRequestPayload);
                    if (wasSuccessfullySend) {
                        BotRequestPool.getBotRequestStringArrayList().remove(botRequestPayload);
                        i--; //reset the parameter
                        len--; //reset the length.
                    } else {
                        break;//Break the loop, as re-connection would be attempted from sendMessage(...)
                    }
                }
            }
        }
    }*/

}
