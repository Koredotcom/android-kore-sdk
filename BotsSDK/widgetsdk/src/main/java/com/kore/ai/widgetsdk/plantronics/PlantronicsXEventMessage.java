
package com.kore.ai.widgetsdk.plantronics;

import java.util.HashMap;
import java.util.Map;

public class PlantronicsXEventMessage {
    //Plantronics Events
    public static final String USER_AGENT_EVENT = "USER-AGENT";
    public static final String SENSOR_STATUS_EVENT = "SENSORSTATUS";
    public static final String A2DP_EVENT = "A2DP";
    public static final String AUDIO_EVENT = "AUDIO";
    public static final String VOCALYST_EVENT = "VOCALYST";
    public static final String LANG_EVENT = "LANG";
    public static final String BATTERY_EVENT = "BATTERY";
    public static final String CONNECTED_EVENT = "CONNECTED";
    public static final String BUTTON_EVENT = "BUTTON";
    public static final String DON_EVENT = "DON";
    public static final String DOFF_EVENT = "DOFF";
    public static String HEADSET_CONNECTED_EVENT = "HEADSET_CONNECTED";
    public static String HEADSET_DISCONNECTED_EVENT = "HEADSET_DISCONNECTED";
    public static String CALL_STATUS_CHANGED_EVENT = "CALL_STATUS_CHANGED_EVENT";
    //holds properties that are transmitted as part of the XEvent
    private final Map<String, Object> messageProperties = new HashMap<String, Object>();
    private final String eventType;


/**
     * Message must have an event type in order to create
     *
     * @param eventType
     */

    public PlantronicsXEventMessage(String eventType) {
        this.eventType = eventType;
    }

    public String getEventType() {
        return eventType;
    }

    public void addProperty(String key, Object value) {
        messageProperties.put(key, value);
    }

    public Map<String, Object> getProperties() {
        return messageProperties;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Plantronics Event " + eventType + "");
        if (!messageProperties.isEmpty()) {
            sb.append("Event Properties:");
            for (String key : messageProperties.keySet()) {
                sb.append("&#8226;");
                sb.append(key);
                sb.append(":");
                sb.append(messageProperties.get(key));
                sb.append("");
            }
        }
        return sb.toString();
    }

}

