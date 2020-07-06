package com.kore.ai.widgetsdk.events;

/**
 * Created by Ramachandra Pradeep on 05-Mar-19.
 */

public class CancelEvent {
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getJson() {
        return json;
    }

    public void setJson(String json) {
        this.json = json;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    private String message;
    private String json;
    private long time;

    public boolean isScrollUpNeeded() {
        return isScrollUpNeeded;
    }

    public void setScrollUpNeeded(boolean scrollUpNeeded) {
        isScrollUpNeeded = scrollUpNeeded;
    }

    private boolean isScrollUpNeeded;

    public CancelEvent(String message, String json, long time, boolean isScrollUpNeeded){
        this.message = message;
        this.json = json;
        this.time = time;
        this.isScrollUpNeeded = isScrollUpNeeded;
    }

}
