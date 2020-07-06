
package com.kore.ai.widgetsdk.plantronics;

import java.util.HashMap;
import java.util.Map;

public class PlantronicsXEventMessage {
    //Plantronics Events
    public static String USER_AGENT_EVENT = "USER-AGENT";
    public static String SENSOR_STATUS_EVENT = "SENSORSTATUS";
    public static String A2DP_EVENT = "A2DP";
    public static String AUDIO_EVENT = "AUDIO";
    public static String VOCALYST_EVENT = "VOCALYST";
    public static String LANG_EVENT = "LANG";
    public static String BATTERY_EVENT = "BATTERY";
    public static String CONNECTED_EVENT = "CONNECTED";
    public static String BUTTON_EVENT = "BUTTON";
    public static String DON_EVENT = "DON";
    public static String DOFF_EVENT = "DOFF";
    public static String HEADSET_CONNECTED_EVENT = "HEADSET_CONNECTED";
    public static String HEADSET_DISCONNECTED_EVENT = "HEADSET_DISCONNECTED";
    public static String CALL_STATUS_CHANGED_EVENT = "CALL_STATUS_CHANGED_EVENT";
    //holds properties that are transmitted as part of the XEvent
    private Map<String, Object> messageProperties = new HashMap<String, Object>();
    private String eventType;


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

