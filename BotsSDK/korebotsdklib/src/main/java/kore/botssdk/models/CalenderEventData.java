package kore.botssdk.models;

import java.util.List;

/**
 * Created by Ramachandra Pradeep on 01-Apr-19.
 */

public class CalenderEventData {

    private List<CalEventsTemplateModel.Attendee> attendees = null;
    private CalEventsTemplateModel.Duration duration;

    public List<CalEventsTemplateModel.Attendee> getAttendees() {
        return attendees;
    }

    public void setAttendees(List<CalEventsTemplateModel.Attendee> attendees) {
        this.attendees = attendees;
    }

    public CalEventsTemplateModel.Duration getDuration() {
        return duration;
    }

    public void setDuration(CalEventsTemplateModel.Duration duration) {
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

    private boolean isAllDay;


}
