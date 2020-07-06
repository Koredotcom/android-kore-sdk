package com.kore.ai.widgetsdk.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MeetingAttendingRequestModel {

    public String eventId;
    @SerializedName("user")
    @Expose
    private UserRequestData user;

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public UserRequestData getUser() {
        return user;
    }

    public void setUser(UserRequestData user) {
        this.user = user;
    }


}
