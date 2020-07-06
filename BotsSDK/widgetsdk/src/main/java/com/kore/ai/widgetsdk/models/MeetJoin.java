package com.kore.ai.widgetsdk.models;

import java.io.Serializable;

public class MeetJoin implements Serializable {
    String meetingUrl;
    String dialIn;

    public String getMeetingUrl() {
        return meetingUrl;
    }

    public void setMeetingUrl(String meetingUrl) {
        this.meetingUrl = meetingUrl;
    }

    public String getDialIn() {
        return dialIn;
    }

    public void setDialIn(String dialIn) {
        this.dialIn = dialIn;
    }
}
