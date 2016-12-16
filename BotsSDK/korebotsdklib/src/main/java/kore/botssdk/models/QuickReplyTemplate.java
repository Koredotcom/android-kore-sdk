package kore.botssdk.models;

/**
 * Created by Ramachandra Pradeep on 12/16/2016.
 */
public class QuickReplyTemplate {

    private String content_type;
    private String title;
    private String payload;

    public QuickReplyTemplate(){}

    public void setContent_type(String content_type){
        this.content_type = content_type;
    }

    public String getContent_type() {
        return content_type;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }

    public String getPayload() {
        return payload;
    }
}
