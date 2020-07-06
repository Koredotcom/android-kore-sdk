package com.kore.ai.widgetsdk.models;

/**
 * Created by Ramachandra Pradeep on 12/15/2016.
 */
public class ComponentModel {

    private String type;
    private PayloadOuter payload;

    public ComponentModel(){}

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public PayloadOuter getPayload() {
        return payload;
    }

    public void setPayload(PayloadOuter payload) {
        this.payload = payload;
    }

}