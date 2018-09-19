package kore.botssdk.BotDb;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Ramachandra Pradeep on 06-Mar-18.
 */

@DatabaseTable(tableName = "BOT_MESSAGE", daoClass = BotMessageDaoImpl.class)
public class BotMessageDBModel extends BotDbModel {

    public interface BotMessageColumns{
        String IS_SENT_MESSAGE = "isSentMessage";
        String TYPE = "type";
        String FROM = "from";
        String MESSAGE = "message_str";
        String MESSAGE_ID = "messageId";
        String BOT_INFO = "botInfo";
        String CLIENT_INFO = "client";
        String META = "meta";
        //        String CREATED_ON = "createdTime";
        String BOT_ICON = "icon";
        String RESOURCE_ID = "resourceid";
        String TRACE_ID = "traceId";
        String IS_MESSAGE_NEED_TO_PERSIST = "isMessageNeedToPersist";

    }

    private static Gson gson = new Gson();

    @DatabaseField(generatedId = true, columnName = "id", dataType = DataType.INTEGER)
    private int id;

    @DatabaseField(columnName = BotMessageColumns.IS_SENT_MESSAGE, dataType = DataType.BOOLEAN)
    private boolean isSentMessage;

    @DatabaseField(columnName = BotMessageColumns.TYPE, dataType = DataType.STRING)
    private String type;

    @DatabaseField(columnName = BotMessageColumns.FROM, dataType = DataType.STRING)
    private String from;

    private List<Object> message;

    @DatabaseField(columnName = BotMessageColumns.MESSAGE, dataType = DataType.STRING)
    private String message_str;

    @DatabaseField(columnName = BotMessageColumns.MESSAGE_ID, dataType = DataType.STRING)
    private String messageId;

    @DatabaseField(columnName = BotMessageColumns.BOT_INFO, dataType = DataType.SERIALIZABLE)
    HashMap<String, Object> botInfo;

    @DatabaseField(columnName = BotMessageColumns.CLIENT_INFO, dataType = DataType.STRING)
    private String client;

    @DatabaseField(columnName = BotMessageColumns.META, dataType = DataType.SERIALIZABLE)
    HashMap<String, Object> meta;

//    @DatabaseField(columnName = BotMessageColumns.CREATED_ON, dataType = DataType.STRING)
//    private String createdOn;

    @DatabaseField(columnName = BotMessageColumns.BOT_ICON, dataType = DataType.STRING)
    private String icon;

    @DatabaseField(columnName = BotMessageColumns.RESOURCE_ID, dataType = DataType.STRING)
    private String resourceid;

    @DatabaseField(columnName = BotMessageColumns.TRACE_ID, dataType = DataType.STRING)
    private String traceId;


    @DatabaseField(columnName = BotMessageColumns.IS_MESSAGE_NEED_TO_PERSIST, dataType = DataType.BOOLEAN)
    private boolean isMessageNeedToPersist;

    public boolean isSentMessage() {
        return isSentMessage;
    }

    public void setSentMessage(boolean sentMessage) {
        isSentMessage = sentMessage;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public List<Object> getMessage() {
        return message;
    }

    public void setMessage(List<Object> message) {
        this.message = message;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public HashMap<String, Object> getBotInfo() {
        return botInfo;
    }

    public void setBotInfo(HashMap<String, Object> botInfo) {
        this.botInfo = botInfo;
    }

    public String getClient() {
        return client;
    }

    public void setClient(String client) {
        this.client = client;
    }

    public HashMap<String, Object> getMeta() {
        return meta;
    }

    public void setMeta(HashMap<String, Object> meta) {
        this.meta = meta;
    }

//    public String getCreatedOn() {
//        return createdOn;
//    }
//
//    public void setCreatedOn(String createdOn) {
//        this.createdOn = createdOn;
//    }

    public String getMessage_str() {
        return message_str;
    }

    public void setMessage_str(String message_str) {
        this.message_str = message_str;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getResourceid() {
        return resourceid;
    }

    public void setResourceid(String resourceid) {
        this.resourceid = resourceid;
    }

    public String getTraceId() {
        return traceId;
    }

    public void setTraceId(String traceId) {
        this.traceId = traceId;
    }

    public boolean isMessageNeedToPersist() {
        return isMessageNeedToPersist;
    }

    public void setMessageNeedToPersist(boolean messageNeedToPersist) {
        isMessageNeedToPersist = messageNeedToPersist;
    }


    public void processMessage() {
        if(gson == null)gson = new Gson();
        if(message != null)
            message_str = gson.toJson(message);
    }

    public void dProcessMesssage(){
        if(gson == null)gson = new Gson();
        Type type = new TypeToken<List<Object>>(){}.getType();
        if(message_str != null)
            message = gson.fromJson(message_str, type);
    }

}
