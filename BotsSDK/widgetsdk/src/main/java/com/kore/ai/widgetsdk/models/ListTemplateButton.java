package com.kore.ai.widgetsdk.models;

/**
 * Created by Anil Kumar on 12/20/2016.
 */
public class ListTemplateButton {


    private String type;
    private String title;
    private String url;
    private boolean messenger_extensions;
    private String webview_height_ratio;
    private String fallback_url;

    public ListTemplateButton(){}

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public boolean isMessenger_extensions() {
        return messenger_extensions;
    }

    public void setMessenger_extensions(boolean messenger_extensions) {
        this.messenger_extensions = messenger_extensions;
    }

    public String getWebview_height_ratio() {
        return webview_height_ratio;
    }

    public void setWebview_height_ratio(String webview_height_ratio) {
        this.webview_height_ratio = webview_height_ratio;
    }

    public String getFallback_url() {
        return fallback_url;
    }

    public void setFallback_url(String fallback_url) {
        this.fallback_url = fallback_url;
    }



}
