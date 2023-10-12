package kore.botssdk.models;

import java.util.HashMap;

/**
 * Copyright (c) 2014 Kore Inc. All rights reserved.
 */
public class BotListDefaultModel {
    public void setType(String type) {
        this.type = type;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    String type;
    String url;
    String webview_height_ratio;
    String fallback_url;
    String payload;
    String action;
    String title;

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public HashMap<String, Object> getCustomData() {
        return customData;
    }

    public void setCustomData(HashMap<String, Object> customData) {
        this.customData = customData;
    }

    HashMap<String,Object> customData;

    public String getType() {
        return type;
    }

    public String getUrl() {
        return url;
    }


    public String getWebview_height_ratio() {
        return webview_height_ratio;
    }

    public String getFallback_url() {
        return fallback_url;
    }

    public String getPayload() {
        return payload;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }
}
