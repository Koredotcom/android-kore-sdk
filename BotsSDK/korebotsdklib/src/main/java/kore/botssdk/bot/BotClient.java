package kore.botssdk.bot;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.TimeZone;

import kore.botssdk.models.BotInfoModel;
import kore.botssdk.models.BotMessageAckModel;
import kore.botssdk.net.RestResponse;
import kore.botssdk.net.SDKConfiguration;
import kore.botssdk.utils.LogUtils;
import kore.botssdk.utils.StringUtils;
import kore.botssdk.utils.Utils;
import kore.botssdk.websocket.SocketConnectionListener;
import kore.botssdk.websocket.SocketWrapper;

/*
 * Copyright (c) 2014 Kore Inc. All rights reserved.
 */

/**
 * Gateway for clients to interact with Bots.
 */
@SuppressWarnings("UnKnownNullness")
public class BotClient {
    private final Context mContext;

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

    public BotClient(Context mContext) {
        this.customData = new RestResponse.BotCustomData();
        this.mContext = mContext.getApplicationContext();
    }

    public BotClient(Context mContext, RestResponse.BotCustomData customData) {
        this.mContext = mContext;
        this.customData = customData;

    }

    /**
     * Connection for anonymous user
     */
    public void connectAsAnonymousUser(String jwtToken, String chatBotName, String taskBotId, SocketConnectionListener socketConnectionListener, boolean isReconnect) {

        botInfoModel = new BotInfoModel(chatBotName, taskBotId, customData);
        SocketWrapper.getInstance(mContext).connectAnonymous(jwtToken, botInfoModel, socketConnectionListener, null, isReconnect);
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

    /**
     * Method to send messages over socket.
     * It uses FIFO pattern to first send if any pending requests are present
     * following current request later onward.
     * <p/>
     * pass 'msg' as NULL on reconnection of the socket to empty the pool
     * by sending messages from the pool.
     */
    public void sendMessage(String msg) {

        if (msg != null && !msg.isEmpty()) {

            RestResponse.BotPayLoad botPayLoad = new RestResponse.BotPayLoad();
            RestResponse.BotMessage botMessage = new RestResponse.BotMessage(msg);
            customData.put("botToken", getAccessToken());
            botMessage.setCustomData(customData);
            botPayLoad.setMessage(botMessage);
            botPayLoad.setBotInfo(botInfoModel);

            RestResponse.Meta meta = new RestResponse.Meta(TimeZone.getDefault().getID(), Locale.getDefault().getISO3Language());
            botPayLoad.setMeta(meta);

            Gson gson = new Gson();
            String jsonPayload = gson.toJson(botPayLoad);

            LogUtils.d("BotClient", "Payload : " + jsonPayload);
            SocketWrapper.getInstance(mContext).sendMessage(jsonPayload);
        }
    }

    /**
     * Method to send messages over socket.
     * It uses FIFO pattern to first send if any pending requests are present
     * following current request later onward.
     * <p/>
     * pass 'msg' as NULL on reconnection of the socket to empty the pool
     * by sending messages from the pool.
     */
    public void sendMessage(String msg, ArrayList<HashMap<String, String>> attachments) {

        if (msg != null && !msg.isEmpty()) {
            RestResponse.BotPayLoad botPayLoad = new RestResponse.BotPayLoad();
            RestResponse.BotMessage botMessage = new RestResponse.BotMessage(msg);

            if (attachments != null && !attachments.isEmpty()) botMessage = new RestResponse.BotMessage(msg, attachments);

            customData.put("botToken", getAccessToken());

            botMessage.setCustomData(customData);
            botPayLoad.setMessage(botMessage);
            botPayLoad.setBotInfo(botInfoModel);

            RestResponse.Meta meta = new RestResponse.Meta(TimeZone.getDefault().getID(), Locale.getDefault().getISO3Language());
            botPayLoad.setMeta(meta);

            Gson gson = new Gson();
            String jsonPayload = gson.toJson(botPayLoad);

            LogUtils.d("BotClient", "Payload : " + jsonPayload);
            SocketWrapper.getInstance(mContext).sendMessage(jsonPayload);
        } else if (attachments != null && !attachments.isEmpty()) {
            RestResponse.BotPayLoad botPayLoad = new RestResponse.BotPayLoad();
            RestResponse.BotMessage botMessage = new RestResponse.BotMessage("", attachments);

            customData.put("botToken", getAccessToken());

            botMessage.setCustomData(customData);
            botPayLoad.setMessage(botMessage);
            botPayLoad.setBotInfo(botInfoModel);

            RestResponse.Meta meta = new RestResponse.Meta(TimeZone.getDefault().getID(), Locale.getDefault().getISO3Language());
            botPayLoad.setMeta(meta);

            Gson gson = new Gson();
            String jsonPayload = gson.toJson(botPayLoad);

            LogUtils.d("BotClient", "Payload : " + jsonPayload);
            SocketWrapper.getInstance(mContext).sendMessage(jsonPayload);
        }

    }

    /**
     * Method to send message acknowledgement over socket.
     */
    public void sendMsgAcknowledgement(String timestamp, String key) {
        BotMessageAckModel botMessageAckModel = new BotMessageAckModel();
        botMessageAckModel.setClientMessageId(timestamp);
        botMessageAckModel.setId(timestamp);
        botMessageAckModel.setKey(key);
        botMessageAckModel.setReplyto(timestamp);

        Gson gson = new Gson();
        String jsonPayload = gson.toJson(botMessageAckModel);

        Log.d("BotClient", "Payload : " + jsonPayload);
        SocketWrapper.getInstance(mContext).sendMessage(jsonPayload);
    }

    /**
     * Method to send messages over socket.
     * It uses FIFO pattern to first send if any pending requests are present
     * following current request later onward.
     * <p/>
     * pass 'msg' as NULL on reconnection of the socket to empty the pool
     * by sending messages from the pool.
     */
    public void sendReceipts(String eventName, String msgId)
    {
        RestResponse.BotPayLoad botPayLoad = new RestResponse.BotPayLoad();
        RestResponse.BotMessage botMessage = new RestResponse.BotMessage("", "");
        customData.put("botToken",getAccessToken());
        botMessage.setCustomData(customData);
        botPayLoad.setMessage(botMessage);
        botPayLoad.setEvent(eventName);

        if(!StringUtils.isNullOrEmpty(msgId))
            botPayLoad.setMsgId(msgId);

        botInfoModel = new BotInfoModel(SDKConfiguration.Client.bot_name,SDKConfiguration.Client.bot_id,customData);
        botPayLoad.setBotInfo(botInfoModel);
        botPayLoad.setResourceid("/bot.message");

        RestResponse.Meta meta = new RestResponse.Meta(TimeZone.getDefault().getID(), Locale.getDefault().getISO3Language());
        botPayLoad.setMeta(meta);

        Gson gson = new Gson();
        String jsonPayload = gson.toJson(botPayLoad);

        LogUtils.d("BotClient", "Payload : " + jsonPayload);
        SocketWrapper.getInstance(mContext).sendMessage(jsonPayload);
    }

    public void sendAgentCloseMessage(String msg, String botName, String botId) {
        RestResponse.BotPayLoad botPayLoad = new RestResponse.BotPayLoad();
        RestResponse.BotMessage botMessage = new RestResponse.BotMessage(msg);
        customData.put("botToken",getAccessToken());
        botMessage.setCustomData(customData);
        botPayLoad.setMessage(botMessage);
        botPayLoad.setEvent("close_agent_chat");
        botInfoModel = new BotInfoModel(botName,botId,customData);
        botPayLoad.setBotInfo(botInfoModel);
        botPayLoad.setResourceid("/bot.message");

        RestResponse.Meta meta = new RestResponse.Meta(TimeZone.getDefault().getID(), Locale.getDefault().getISO3Language());
        botPayLoad.setMeta(meta);

        Gson gson = new Gson();
        String jsonPayload = gson.toJson(botPayLoad);

        LogUtils.d("BotClient", "Payload : " + jsonPayload);
        SocketWrapper.getInstance(mContext).sendAgentMessage(jsonPayload);
    }

    public void sendFormData(String payLoad, String message) {

        if (payLoad != null && !payLoad.isEmpty()) {
            RestResponse.BotPayLoad botPayLoad = new RestResponse.BotPayLoad();
            RestResponse.BotMessage botMessage = new RestResponse.BotMessage(payLoad);

            if (customData == null) customData = new RestResponse.BotCustomData();

            customData.put("botToken", getAccessToken());
            botMessage.setCustomData(customData);
            botMessage.setParams(Utils.jsonToMap(payLoad));
            botPayLoad.setMessage(botMessage);
            botPayLoad.setBotInfo(botInfoModel);

            RestResponse.Meta meta = new RestResponse.Meta(TimeZone.getDefault().getID(), Locale.getDefault().getISO3Language());
            botPayLoad.setMeta(meta);

            Gson gson = new Gson();
            String jsonPayload = gson.toJson(botPayLoad);

            LogUtils.d("BotClient", "Payload : " + jsonPayload);
            SocketWrapper.getInstance(mContext).sendMessage(jsonPayload);
        }
    }
}
