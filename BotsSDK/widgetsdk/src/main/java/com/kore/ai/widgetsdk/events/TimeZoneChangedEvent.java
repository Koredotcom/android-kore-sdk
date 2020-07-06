package com.kore.ai.widgetsdk.events;

/**
 * Created by Ramachandra Pradeep on 14-Feb-19.
 */

public class TimeZoneChangedEvent {
    public boolean isChanged() {
        return isChanged;
    }

    public void setChanged(boolean changed) {
        isChanged = changed;
    }

    private boolean isChanged = true;
    public TimeZoneChangedEvent(boolean isChanged){
        this.isChanged = isChanged;
    }
}
