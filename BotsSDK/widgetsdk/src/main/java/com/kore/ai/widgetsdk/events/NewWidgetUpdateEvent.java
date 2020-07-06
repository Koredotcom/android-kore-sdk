package com.kore.ai.widgetsdk.events;

public class NewWidgetUpdateEvent {
    public int getwId() {
        return wId;
    }

    public void setwId(int wId) {
        this.wId = wId;
    }

    private int wId;

    public NewWidgetUpdateEvent(int wId){
        this.wId = wId;
    }
}
