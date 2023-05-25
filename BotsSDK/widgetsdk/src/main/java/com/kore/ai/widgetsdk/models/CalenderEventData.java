package com.kore.ai.widgetsdk.models;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Ramachandra Pradeep on 01-Apr-19.
 */

public class CalenderEventData implements Serializable, Cloneable {

    private List<CalEventsTemplateModel.Attendee> attendees = null;
    private CalEventsTemplateModel.Duration duration;

    public List<CalEventsTemplateModel.Attendee> getAttendees() {
        return attendees;
    }
    private String description;
    public String getDescription() {

        return description != null ? description : "";
    }

    public void setDescription(String description) {
        this.description = description;
    }
    public void setAttendees(List<CalEventsTemplateModel.Attendee> attendees) {
        this.attendees = attendees;
    }

    public CalEventsTemplateModel.Duration getDuration() {
        return duration;
    }

    public final void setDuration(CalEventsTemplateModel.Duration duration) {
        this.duration = duration;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getHtmlLink() {
        return htmlLink;
    }

    public void setHtmlLink(String htmlLink) {
        this.htmlLink = htmlLink;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public boolean isAllDay() {
        return isAllDay;
    }

    public void setAllDay(boolean allDay) {
        isAllDay = allDay;
    }

    private String color;
    private String htmlLink;
    private String eventId;
    private String reqTextToDisp;
    private String reqTextToDispForDetails;

    private boolean isAllDay;
    public MeetJoin getMeetJoin() {
        return meetJoin;
    }

    public void setMeetJoin(MeetJoin meetJoin) {
        this.meetJoin = meetJoin;
    }

    private MeetJoin meetJoin;

    public String getReqTextToDisp() {
        return reqTextToDisp;
    }

    public void setReqTextToDisp(String reqTextToDisp) {
        this.reqTextToDisp = reqTextToDisp;
    }

    @Override
    protected CalenderEventData clone() throws CloneNotSupportedException {
        return (CalenderEventData)super.clone();
    }

    public String getReqTextToDispForDetails() {
        return reqTextToDispForDetails;
    }

    public void setReqTextToDispForDetails(String reqTextToDispForDetails) {
        this.reqTextToDispForDetails = reqTextToDispForDetails;
    }
}
