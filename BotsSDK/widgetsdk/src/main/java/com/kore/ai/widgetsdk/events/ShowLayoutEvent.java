package com.kore.ai.widgetsdk.events;

public class ShowLayoutEvent {
    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    private int type;
    public ShowLayoutEvent(int type){
        this.type = type;
    }
}
