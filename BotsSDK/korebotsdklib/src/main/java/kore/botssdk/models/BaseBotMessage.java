package kore.botssdk.models;

/**
 * Created by Pradeep Mahato on 03-Jun-16.
 */

public abstract class BaseBotMessage {

    protected String from;

    protected boolean isSend;
    public abstract boolean isSend();

    public String getFrom() {
        return from;
    }
}
