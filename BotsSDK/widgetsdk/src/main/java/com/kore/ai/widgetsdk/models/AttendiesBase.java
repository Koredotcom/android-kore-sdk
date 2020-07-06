package com.kore.ai.widgetsdk.models;

import java.io.Serializable;
import java.util.List;

public class AttendiesBase implements Serializable {

    List<CalEventsTemplateModel.Attendee> attendeeList;

    public List<CalEventsTemplateModel.Attendee> getAttendeeList() {
        return attendeeList;
    }

    public void setAttendeeList(List<CalEventsTemplateModel.Attendee> attendeeList) {
        this.attendeeList = attendeeList;
    }
}
