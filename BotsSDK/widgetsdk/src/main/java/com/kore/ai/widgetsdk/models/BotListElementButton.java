package com.kore.ai.widgetsdk.models;

/**
 * Created by Pradeep Mahato on 21/7/17.
 * Copyright (c) 2014 Kore Inc. All rights reserved.
 */
public class BotListElementButton {
    String title;
    String type;
    String url;
    String webview_height_ratio;
    String fallback_url;
    String payload;

    public String getTitle() {
        return title;
    }

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
}
