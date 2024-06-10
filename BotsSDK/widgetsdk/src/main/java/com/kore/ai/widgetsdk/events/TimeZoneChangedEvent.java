package com.kore.ai.widgetsdk.events;

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
