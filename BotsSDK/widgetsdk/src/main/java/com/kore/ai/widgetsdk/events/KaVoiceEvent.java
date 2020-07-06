package com.kore.ai.widgetsdk.events;

public class KaVoiceEvent {

    public String getVoiceData() {
        return voiceData;
    }

    public void setVoiceData(String voiceData) {
        this.voiceData = voiceData;
    }

    private String voiceData;

    public KaVoiceEvent(String voiceData){
        this.voiceData = voiceData;
    }
}
