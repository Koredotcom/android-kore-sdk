package com.kore.ai.widgetsdk.models;

import java.io.Serializable;

public class BotTableListDefaultActionsModel implements Serializable
{
    private String type;
    private String payload;
    private String title;
    private String utterance;
    private String url;

    public void setPayload(String payload) {
        this.payload = payload;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setUtterance(String utterance) {
        this.utterance = utterance;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPayload() {
        return payload;
    }

    public String getTitle() {
        return title;
    }

    public String getType() {
        return type;
    }

    public String getUtterance() {
        return utterance;
    }

    public String getUrl() {
        return url;
    }
}
