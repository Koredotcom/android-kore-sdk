package com.kore.ai.widgetsdk.events;

public class PinUnPinEvent {
    final String panelId;
    public PinUnPinEvent(String panelId) {
        this.panelId=panelId;
    }

    public String getPanelId() {
        return panelId;
    }
}
