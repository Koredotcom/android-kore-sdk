package kore.botssdk.models;

public class BotMessageAckModel
{
    private String clientMessageId;
    private String type = "ack";
    private String replyto;
    private String status = "delivered";
    private String key;
    private String id;

    public void setClientMessageId(String clientMessageId) {
        this.clientMessageId = clientMessageId;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public void setReplyto(String replyto) {
        this.replyto = replyto;
    }

}
