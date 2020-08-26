package com.kore.ai.widgetsdk.events;

public class EntityEditEvent {
    private String message;
    private String body;
    private String payLoad;
    private String type;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

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

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public boolean isScrollUpNeeded() {
        return isScrollUpNeeded;
    }

    public void setScrollUpNeeded(boolean scrollUpNeeded) {
        isScrollUpNeeded = scrollUpNeeded;
    }

    private boolean isScrollUpNeeded;
}
