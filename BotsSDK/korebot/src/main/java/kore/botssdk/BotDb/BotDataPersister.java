/*
package kore.botssdk.BotDb;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.octo.android.robospice.request.SpiceRequest;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import kore.botssdk.models.BotRequest;
import kore.botssdk.utils.DateUtils;

public class BotDataPersister extends SpiceRequest<Void> {

    private Context mContext;
    private static Gson gson = new Gson();
    private String payload;
    private boolean isSentMessage;
    private BotRequest sentMsg;
    private String userId;

    public BotDataPersister(Context mContext, String userId, String payload, boolean isSentMessage, BotRequest sentMsg) {
        super(Void.class);
        this.mContext = mContext;
        this.payload = payload;
        this.isSentMessage = isSentMessage;
        this.userId = userId;
        this.sentMsg = sentMsg;
    }

    @Override
    public Void loadDataFromNetwork() throws Exception {
        persistChats();
        return null;
    }


    private void persistChats() {
        Log.d("==========++++========="," The user Id is "+userId+" Hey The Payl load is "+payload);
        BotMessageDBModel botMessageDBModel = null;
        if (gson == null) {
            gson = new Gson();
        }
        try {

//            if (botMessageDBModel.getType().equals("ack")) return;
            //This case is user sent message but from another channel
            if (!isSentMessage) {
                botMessageDBModel = gson.fromJson(payload, BotMessageDBModel.class);
                botMessageDBModel.processMessage();
            } else {
                botMessageDBModel = new BotMessageDBModel();
                List<Object> botObj = new ArrayList<>(1);
                sentMsg.setSend(true);
                botObj.add(sentMsg);
                botMessageDBModel.setMessage(botObj);
                botMessageDBModel.processMessage();
                botMessageDBModel.setMessageNeedToPersist(true);
                botMessageDBModel.setSentMessage(true);
                botMessageDBModel.setType("bot_request");
                botMessageDBModel.setCreatedOn(sentMsg.getCreatedOn());
            }
        }catch (Exception e){
            if(e instanceof JsonSyntaxException){
                try {
                    sentMsg = gson.fromJson(payload, BotRequest.class);
                    sentMsg.setCreatedOn(DateUtils.isoFormatter.format(new Date()));
                    botMessageDBModel = new BotMessageDBModel();
                    List<Object> botObj = new ArrayList<>(1);
                    sentMsg.setSend(true);
                    botObj.add(sentMsg);
                    botMessageDBModel.setMessage(botObj);
                    botMessageDBModel.processMessage();
                    botMessageDBModel.setMessageNeedToPersist(true);
                    botMessageDBModel.setSentMessage(true);
                    botMessageDBModel.setType("bot_request");
                    botMessageDBModel.setCreatedOn(sentMsg.getCreatedOn());
                    isSentMessage = true;
                }catch (Exception e1){
                    e1.printStackTrace();
                }

            }
        }
//        DBHelper dbHelper = new DBHelper(mContext);
//        BotMessageDao botMessageDAO = dbHelper.getBotMessageDao();
        try {
            if (botMessageDBModel != null*/
/* && botMessageDBModel.isMessageNeedToPersist()*//*
 && !botMessageDBModel.getType().equals("ack")) {
                botMessageDBModel.setSentMessage(isSentMessage);
//                botMessageDAO.createOrUpdate(botMessageDBModel);
//                botMessageDAO.refresh(botMessageDBModel);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


//       dbHelper.close();

    }

}
*/
