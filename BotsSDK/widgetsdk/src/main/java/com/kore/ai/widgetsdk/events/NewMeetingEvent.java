package com.kore.ai.widgetsdk.events;

public class NewMeetingEvent {
    public String getmId() {
        return mId;
    }

    public void setmId(String mId) {
        this.mId = mId;
    }

    public boolean isFromPresence() {
        return isFromPresence;
    }

    public void setFromPresence(boolean fromPresence) {
        isFromPresence = fromPresence;
    }

    private String mId;
    private boolean isFromPresence;

    public NewMeetingEvent(String mId, boolean isFromPresence){
        this.mId = mId;
        this.isFromPresence = isFromPresence;
    }
}
