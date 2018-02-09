package kore.botssdk.models;

import java.util.HashMap;

/**
 * Created by Pradeep Mahato on 13/7/17.
 * Copyright (c) 2014 Kore Inc. All rights reserved.
 */
public class BotCaourselButtonModel {
    String type;
    String title;
    String payload;
    String url;

    public HashMap<String, String> getRedirectUrl() {
        return redirectUrl;
    }

    public void setRedirectUrl(HashMap<String, String> redirectUrl) {
        this.redirectUrl = redirectUrl;
    }

    HashMap<String,String> redirectUrl;

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
