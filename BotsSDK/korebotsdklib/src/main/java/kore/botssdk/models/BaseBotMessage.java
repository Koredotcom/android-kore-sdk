package kore.botssdk.models;

/**
 * Created by Pradeep Mahato on 03-Jun-16.
 */

public abstract class BaseBotMessage {

    protected String from;

    protected boolean isSend;
    protected String createdOn;

    public abstract boolean isSend();

    public String getFrom() {
        return from;
    }

    public String getCreatedOn() {
        return createdOn;
    }

}
