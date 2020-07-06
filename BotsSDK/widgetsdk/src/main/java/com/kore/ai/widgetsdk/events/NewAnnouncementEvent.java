package com.kore.ai.widgetsdk.events;

/**
 * Created by Ramachandra Pradeep on 22-Apr-19.
 */

public class NewAnnouncementEvent {
    public String getkId() {
        return kId;
    }

    public void setkId(String kId) {
        this.kId = kId;
    }

    private String kId;

    public boolean isFromPresence() {
        return isFromPresence;
    }

    public void setFromPresence(boolean fromPresence) {
        isFromPresence = fromPresence;
    }

    private boolean isFromPresence;

    public NewAnnouncementEvent(String kId, boolean isFromPresence){
        this.kId = kId;
        this.isFromPresence = isFromPresence;
    }
}
