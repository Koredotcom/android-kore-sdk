package com.kore.ai.widgetsdk.events;

public class RefreshMeetingEvent {
    public int getwId() {
        return wId;
    }

    public void setwId(int wId) {
        this.wId = wId;
    }

    private int wId;

    public RefreshMeetingEvent(int wId){
        this.wId = wId;
    }
}
