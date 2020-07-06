package com.kore.ai.widgetsdk.models;

/**
 * Created by Ramachandra Pradeep on 11-Mar-19.
 */

public class MultiAction {
    private String type;
    private String title;
    private String utterance;

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

    public String getUtterance() {
        return utterance;
    }

    public void setUtterance(String utterance) {
        this.utterance = utterance;
    }
}
