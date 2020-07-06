package com.kore.ai.widgetsdk.room.models;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

/**
 * Created by Ramachandra Pradeep on 26-Oct-18.
 */

@Entity(tableName = "bot_db_message")
public class BotDBMessage {
    public static final String TABLE_NAME = "bot_db_message";
    public static final String IS_DIRTY = "is_dirty";
    public static final String IS_RESOLVED = "is_resolved";
    public static final String CREATED_ON = "created_on";
    public static final String LAST_MODIFIED = "last_modified";
    public static final String RESOLVED_ON = "resolved_on";
    public static final String IS_SENT_MESSAGE = "isSentMessage";
    public static final String TYPE = "type";
    public static final String FROM = "from";
    public static final String MESSAGE = "message_str";
    public static final String MESSAGE_ID = "messageId";
    public static final String BOT_INFO = "botInfo";
    public static final String CLIENT_INFO = "client";
    public static final String META = "meta";
    public static final String BOT_ICON = "icon";
    public static final String RESOURCE_ID = "resourceid";
    public static final String TRACE_ID = "traceId";
    public static final String IS_MESSAGE_NEED_TO_PERSIST = "isMessageNeedToPersist";

    private static Gson gson = new Gson();

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    @NonNull private int id;

    @ColumnInfo(name = IS_DIRTY)
    private boolean isDirty;

    @ColumnInfo(name = IS_RESOLVED)
    private boolean isResolved;

    @ColumnInfo(name = CREATED_ON)
    private String createdOn;

    @NonNull
    public int getId() {
        return id;
    }

    public void setId(@NonNull int id) {
        this.id = id;
    }

    public boolean isDirty() {
        return isDirty;
    }

    public void setDirty(boolean dirty) {
        isDirty = dirty;
    }

    public boolean isResolved() {
        return isResolved;
    }

    public void setResolved(boolean resolved) {
        isResolved = resolved;
    }

    public String getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(String createdOn) {
        this.createdOn = createdOn;
    }

    public String getLastModifiedOn() {
        return lastModifiedOn;
    }

    public void setLastModifiedOn(String lastModifiedOn) {
        this.lastModifiedOn = lastModifiedOn;
    }

    public String getResolvedOn() {
        return resolvedOn;
    }

    public void setResolvedOn(String resolvedOn) {
        this.resolvedOn = resolvedOn;
    }

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

    public String getMessage_str() {
        return message_str;
    }

    public void setMessage_str(String message_str) {
        this.message_str = message_str;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getClient() {
        return client;
    }

    public void setClient(String client) {
        this.client = client;
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

    @ColumnInfo(name = LAST_MODIFIED)
    private String lastModifiedOn;

    @ColumnInfo(name = RESOLVED_ON)
    private String resolvedOn;

    @ColumnInfo(name = IS_SENT_MESSAGE)
    private boolean isSentMessage;

    @ColumnInfo(name = TYPE)
    private String type;

    @ColumnInfo(name = FROM)
    private String from;
    @Ignore
    private List<Object> message;

    @ColumnInfo(name = MESSAGE)
    private String message_str;

    @ColumnInfo(name = MESSAGE_ID)
    private String messageId;

    //    @DatabaseField(columnName = BotMessageColumns.BOT_INFO, dataType = DataType.SERIALIZABLE)
//    HashMap<String, Object> botInfo;

    @ColumnInfo(name = CLIENT_INFO)
    private String client;

    //    @DatabaseField(columnName = BotMessageColumns.META, dataType = DataType.SERIALIZABLE)
//    HashMap<String, Object> meta;

    @ColumnInfo(name = BOT_ICON)
    private String icon;

    @ColumnInfo(name = RESOURCE_ID)
    private String resourceid;

    @ColumnInfo(name = TRACE_ID)
    private String traceId;


    @ColumnInfo(name = IS_MESSAGE_NEED_TO_PERSIST)
    private boolean isMessageNeedToPersist;

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
