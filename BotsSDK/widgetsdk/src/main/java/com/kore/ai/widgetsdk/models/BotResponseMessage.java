package com.kore.ai.widgetsdk.models;

/**
 * Created by Pradeep Mahato on 03-Jun-16.
 * Copyright (c) 2014 Kore Inc. All rights reserved.
 */

public class BotResponseMessage {

    private String type;
    private ComponentModel component;
    private BotResponseMessageComponentInfo cInfo;

    public void setType(String type) {
        this.type = type;
    }

    public ComponentModel getComponent() {
        return component;
    }

    public void setComponent(ComponentModel component) {
        this.component = component;
    }

    public void setcInfo(BotResponseMessageComponentInfo cInfo) {
        this.cInfo = cInfo;
    }



    public String getType() {
        return type;
    }

    public BotResponseMessageComponentInfo getcInfo() {
        return cInfo;
    }
}
