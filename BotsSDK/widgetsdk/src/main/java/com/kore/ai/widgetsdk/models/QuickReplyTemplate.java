package com.kore.ai.widgetsdk.models;

/**
 * Created by Ramachandra Pradeep on 12/16/2016.
 */
public class QuickReplyTemplate {

    private String content_type;
    private String title;

    public void setContent_type(String content_type) {
        this.content_type = content_type;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

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
