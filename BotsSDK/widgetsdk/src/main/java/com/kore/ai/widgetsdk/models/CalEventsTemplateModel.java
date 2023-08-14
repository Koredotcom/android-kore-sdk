package com.kore.ai.widgetsdk.models;

import android.text.TextUtils;

import androidx.annotation.NonNull;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Ramachandra Pradeep on 02-Aug-18.
 */

public class CalEventsTemplateModel extends BaseCalenderTemplateModel implements Cloneable {

    private Duration duration;
    private String title;
    private String where;
    private String description;
    String mId;
    private boolean isAllDay;
    private String reqTextToDisplay;
    private String reqTextToDisplayForDetails;

    public Widget.DefaultAction getDefaultAction() {
        return default_action;
    }

    public void setDefault_action(Widget.DefaultAction default_action) {
        this.default_action = default_action;
    }

    private Widget.DefaultAction default_action;

    public String getmId() {
        return mId;
    }

    public void setmId(String mId) {
        this.mId = mId;
    }

    public MeetJoin getMeetJoin() {
        return meetJoin;
    }

    public void setMeetJoin(MeetJoin meetJoin) {
        this.meetJoin = meetJoin;
    }

    private MeetJoin meetJoin;


    public String getDescription() {

        return description != null ? description : "";
    }

    public void setDescription(String description) {
        this.description = description;
    }

    private String color;
    private String htmlLink;
    private String eventId;
    private String meetingNoteId;
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

    public final void setDuration(Duration duration) {
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

        showDate = flag;
    }

    public String getMeetingNoteId() {
        return meetingNoteId;
    }

    public void setMeetingNoteId(String meetingNoteId) {
        this.meetingNoteId = meetingNoteId;
    }

    public boolean isAllDay() {
        return isAllDay;
    }

    public void setAllDay(boolean allDay) {
        isAllDay = allDay;
    }

    public String getReqTextToDisplay() {
        return reqTextToDisplay;
    }

    public void setReqTextToDisplay(String reqTextToDisplay) {
        this.reqTextToDisplay = reqTextToDisplay;
    }

    public String getReqTextToDisplayForDetails() {
        return reqTextToDisplayForDetails;
    }

    public void setReqTextToDisplayForDetails(String reqTextToDisplayForDetails) {
        this.reqTextToDisplayForDetails = reqTextToDisplayForDetails;
    }

    public static class Duration  implements Serializable,Cloneable{

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

        @Override
        protected Duration clone() throws CloneNotSupportedException {
            return (Duration)super.clone();
        }
    }

    public static class Attendee implements Serializable{

        private boolean optional;
        private boolean resource;
        private boolean self;
        private String emailId;

        public String getEmailId() {
            return emailId;
        }

        public void setEmailId(String emailId) {
            this.emailId = emailId;
        }

        public boolean isSelf() {
            return self;
        }

        public void setSelf(boolean self) {
            this.self = self;
        }

        public String getStatus() {

            return status != null ? status : "";
        }

        public void setStatus(String status) {
            this.status = status;
        }

        private String status;

        public boolean isResource() {
            return resource;
        }

        public void setResource(boolean resource) {
            this.resource = resource;
        }

        public boolean isOrganizer() {
            return organizer;
        }

        public void setOrganizer(boolean organizer) {
            this.organizer = organizer;
        }

        private boolean organizer;


        public boolean isOptional() {
            return optional;
        }

        public void setOptional(boolean optional) {
            this.optional = optional;
        }

        private String email;

        private String name;

        private String id;

        public boolean isCheckState() {
            return checkState;
        }

        public void setCheckState(boolean checkState) {
            this.checkState = checkState;
        }

        boolean checkState;
        public String getEmail() {
            return email!=null?email:emailId;
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

        @NonNull
        @Override
        public String toString() {
            return name!=null&& !TextUtils.isEmpty(name)?name:email;
        }


        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

    }

    public static class Action implements Serializable {
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

    @Override
    public CalEventsTemplateModel clone() throws CloneNotSupportedException {
        CalEventsTemplateModel obj = (CalEventsTemplateModel)super.clone();
        obj.setDuration(obj.getDuration().clone());
        return obj;
    }
}
