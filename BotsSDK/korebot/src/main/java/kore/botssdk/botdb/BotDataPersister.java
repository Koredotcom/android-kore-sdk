package kore.botssdk.botdb;

import android.content.Context;

import com.google.gson.Gson;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import kore.botssdk.models.BotRequest;

public class BotDataPersister /*extends SpiceRequest<Void>*/ {

    private final Context mContext;
    private static final Gson gson = new Gson();
    private final String payload;
    private final boolean isSentMessage;
    private final BotRequest sentMsg;
    private final String userId;

    public BotDataPersister(Context mContext, String userId, String payload, boolean isSentMessage, BotRequest sentMsg) {
//        super(Void.class);
        this.mContext = mContext;
        this.payload = payload;
        this.isSentMessage = isSentMessage;
        this.userId = userId;
        this.sentMsg = sentMsg;
    }

    public Observable<Boolean> loadDataFromNetwork()  {
        return Observable.create(new ObservableOnSubscribe() {
            @Override
            public void subscribe(ObservableEmitter emitter) throws Exception {
                try{
                    persistChats();
                    emitter.onNext(true);
                    emitter.onComplete();
                }catch(Exception e){
                    emitter.onError(e);
                }
            }
        });
    }


    private void persistChats() {
        /*Log.d("==========++++========="," The user Id is "+userId+" Hey The Payl load is "+payload);
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
        DBHelper dbHelper = new DBHelper(mContext);
        BotMessageDao botMessageDAO = dbHelper.getBotMessageDao();
        try {
            if (botMessageDBModel != null*//* && botMessageDBModel.isMessageNeedToPersist()*//* && !botMessageDBModel.getType().equals("ack")) {
                botMessageDBModel.setSentMessage(isSentMessage);
                botMessageDAO.createOrUpdate(botMessageDBModel);
                botMessageDAO.refresh(botMessageDBModel);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


       dbHelper.close();
*/
    }

}
