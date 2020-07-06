package com.kore.ai.widgetsdk.models;

import java.util.HashMap;

/**
 * Created by Pradeep Mahato on 13/7/17.
 * Copyright (c) 2014 Kore Inc. All rights reserved.
 */
public class BotCaourselButtonModel {
    String type;
    String title;
    String payload;
    String action;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    String id;

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


    public void setType(String type) {
        this.type = type;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    String url;

/*    public HashMap<String, String> getRedirectUrl() {
        return redirectUrl;
    }

    public void setRedirectUrl(HashMap<String, String> redirectUrl) {
        this.redirectUrl = redirectUrl;
    }

    HashMap<String,String> redirectUrl;*/

    public String getType() {
        return type;
    }

    public String getTitle() {
        return title;
    }

    public String getPayload() {
        return payload;
    }

    public String getUrl() {
        return url;
    }
}
