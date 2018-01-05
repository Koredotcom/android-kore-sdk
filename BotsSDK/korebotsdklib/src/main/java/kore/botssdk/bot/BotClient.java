package kore.botssdk.bot;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Locale;
import java.util.TimeZone;
import java.util.UUID;

import kore.botssdk.models.BotInfoModel;
import kore.botssdk.net.BotRequestPool;
import kore.botssdk.net.RestResponse;
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
    private Context mContext;

    private BotClient() {
    }

    /**
     * @param mContext
     */
    public BotClient(Context mContext) {
        this.mContext = mContext.getApplicationContext();
    }

    /**
     * Connection for anonymous user
     *
     * @param clientId
     * @param socketConnectionListener
     */
    public void connectAsAnonymousUser(String jwtToken, String clientId, String chatBotName, String taskBotId, SocketConnectionListener socketConnectionListener) {
        String uuid = UUID.randomUUID().toString();//"e56dd516-5491-45b2-9ff7-ffcb7d8f2461";
        SocketWrapper.getInstance(mContext).connectAnonymous(jwtToken, clientId, chatBotName, taskBotId, uuid, socketConnectionListener);

    }

    public  String getAccessToken(){
        return SocketWrapper.getInstance(mContext).getAccessToken();
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
     *
     * @param msg
     */
    public void sendMessage(String msg, String chatBotName, String taskBotId) {

        if (msg != null && !msg.isEmpty()) {



            RestResponse.BotPayLoad botPayLoad = new RestResponse.BotPayLoad();

            RestResponse.BotMessage botMessage = new RestResponse.BotMessage(msg);
            botMessage.setAuthorization("bearer " + getAccessToken());
            botPayLoad.setMessage(botMessage);

            BotInfoModel botInfo = new BotInfoModel(chatBotName,taskBotId);
            botPayLoad.setBotInfo(botInfo);

            RestResponse.Meta meta = new RestResponse.Meta(TimeZone.getDefault().getID(), Locale.getDefault().getISO3Language());
            botPayLoad.setMeta(meta);

            Gson gson = new Gson();
            String jsonPayload = gson.toJson(botPayLoad);

            Log.d("BotClient", "Payload : " + jsonPayload);
            BotRequestPool.getBotRequestStringArrayList().add(jsonPayload);
        }

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
    }

}
