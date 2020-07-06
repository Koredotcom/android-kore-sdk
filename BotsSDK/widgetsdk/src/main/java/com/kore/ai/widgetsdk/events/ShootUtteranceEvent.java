package com.kore.ai.widgetsdk.events;

import java.util.HashMap;

/**
 * Created by Ramachandra Pradeep on 03-Apr-19.
 */

public class ShootUtteranceEvent {
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getPayLoad() {
        return payLoad;
    }

    public void setPayLoad(String payLoad) {
        this.payLoad = payLoad;
    }

    private String message;

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    private String body;
    private String payLoad;

    public boolean isScrollUpNeeded() {
        return isScrollUpNeeded;
    }

    public void setScrollUpNeeded(boolean scrollUpNeeded) {
        isScrollUpNeeded = scrollUpNeeded;
    }

    private boolean isScrollUpNeeded;

    public HashMap<String, String> getDataMap() {
        return dataMap;
    }

    public void setDataMap(HashMap<String, String> dataMap) {
        this.dataMap = dataMap;
    }

    private HashMap<String,String> dataMap;

}