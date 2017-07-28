package kore.botssdk.models;

/**
 * Created by Ramachandra Pradeep on 12/16/2016.
 */
public class QuickReplyTemplate {

    private String content_type;
    private String title;
    private String payload;
    private String image_url;

    public String getContent_type() {
        return content_type;
    }

    public String getTitle() {
        return title;
    }

    public String getPayload() {
        return payload;
    }

    public String getImage_url() {
        return image_url;
    }
}
