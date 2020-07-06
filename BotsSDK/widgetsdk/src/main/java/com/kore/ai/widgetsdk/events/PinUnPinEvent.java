package com.kore.ai.widgetsdk.events;

public class PinUnPinEvent {
    String panelId;
    public PinUnPinEvent(String panelId) {
        this.panelId=panelId;
    }

    public String getPanelId() {
        return panelId;
    }
}
