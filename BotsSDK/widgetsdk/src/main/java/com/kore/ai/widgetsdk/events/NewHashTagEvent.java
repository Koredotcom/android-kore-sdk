package com.kore.ai.widgetsdk.events;

public class NewHashTagEvent {
    public String getHashTag() {
        return hashTag;
    }

    public void setHashTag(String hashTag) {
        this.hashTag = hashTag;
    }

    private String hashTag;

    public NewHashTagEvent(String hashTag){
        this.hashTag = hashTag;
    }
}
