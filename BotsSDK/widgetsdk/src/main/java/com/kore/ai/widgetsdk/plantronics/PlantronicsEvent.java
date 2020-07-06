package com.kore.ai.widgetsdk.plantronics;

public class PlantronicsEvent {
    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    private String eventType;

    public PlantronicsEvent(String eventType){
        this.eventType = eventType;
    }
}
