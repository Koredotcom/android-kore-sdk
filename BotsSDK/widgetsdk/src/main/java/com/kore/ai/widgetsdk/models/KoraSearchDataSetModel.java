package com.kore.ai.widgetsdk.models;

/**
 * Created by Shiva Krishna on 2/8/2018.
 */

public class KoraSearchDataSetModel{
    String type;
    public enum ViewType{
        EMAIL_VIEW,KNOWLEDGE_VIEW,SHOW_MORE_VIEW
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public ViewType getViewType() {
        return viewType;
    }

    public void setViewType(ViewType viewType) {
        this.viewType = viewType;
    }

    public Object getPayload() {
        return payload;
    }

    public void setPayload(Object payload) {
        this.payload = payload;
    }

    ViewType viewType;
    Object payload;
}