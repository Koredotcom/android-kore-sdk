package kore.botssdk.models;

import java.util.List;

/**
 * Created by Ramachandra Pradeep on 02-Aug-18.
 */

public class CalEventsTemplateModel {

    private Duration duration;
    private String title;
    private String where;
    private String color;
    private String htmlLink;
    private String eventId;
    boolean showDate;
    private List<Attendee> attendees = null;

    public List<Action> getActions() {
        return actions;
    }

    public boolean isShowDate() {
        return showDate;
    }

    public void setActions(List<Action> actions) {
        this.actions = actions;
    }

    private List<Action> actions = null;
    public Duration getDuration() {
        return duration;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getWhere() {
        return where;
    }

    public void setWhere(String where) {
        this.where = where;
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
    public List<Attendee> getAttendees() {
        return attendees;
    }

    public void setAttendees(List<Attendee> attendees) {
        this.attendees = attendees;
    }

    public void setShowDate(boolean flag) {

        showDate=flag;
    }

    public class Duration{

        private double start;
        private double end;

        public double getStart() {
            return start;
        }

        public void setStart(double start) {
            this.start = start;
        }

        public double getEnd() {
            return end;
        }

        public void setEnd(double end) {
            this.end = end;
        }
    }

    public class Attendee {


        private String email;

        private String name;

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

    }
    public class Action {
        private String type;
        private String title;
        private String utterance;

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getUtterance() {
            return utterance;
        }

        public void setUtterance(String utterance) {
            this.utterance = utterance;
        }
    }
}
