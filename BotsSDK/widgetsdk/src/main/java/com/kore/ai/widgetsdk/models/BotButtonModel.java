package com.kore.ai.widgetsdk.models;

import java.util.HashMap;

/**
 * Created by Pradeep Mahato on 21/7/17.
 * Copyright (c) 2014 Kore Inc. All rights reserved.
 */
public class BotButtonModel implements MultiSelectBase {
    String type;
    String url;
    String title;
    String payload;

    public boolean isIs_actionable() {
        return is_actionable;
    }

    public void setIs_actionable(boolean is_actionable) {
        this.is_actionable = is_actionable;
    }

    boolean is_actionable;
    public HashMap<String, Object> getCustomData() {
        return customData;
    }

    public void setCustomData(HashMap<String, Object> customData) {
        this.customData = customData;
    }

    HashMap<String,Object> customData;


    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    String action;

    public String getType() {
        return type;
    }

    public String getUrl() {
        return url;
    }

    public String getTitle() {
        return title;
    }
    public void setTitle(String title){
        this.title = title;
    }

    public String getPayload() {
        return payload;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }

    public ImageModel getImage() {
        return image;
    }

    public void setImage(ImageModel image) {
        this.image = image;
    }

    private ImageModel image;
}
