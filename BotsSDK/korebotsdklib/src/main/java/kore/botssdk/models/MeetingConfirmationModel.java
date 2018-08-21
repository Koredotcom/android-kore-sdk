package kore.botssdk.models;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;

public class MeetingConfirmationModel {
    private String title;
    private long date;
    private long slot_start;
    private long slot_end;
    private String where;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public long getSlot_start() {
        return slot_start;
    }

    public void setSlot_start(long slot_start) {
        this.slot_start = slot_start;
    }

    public long getSlot_end() {
        return slot_end;
    }

    public void setSlot_end(long slot_end) {
        this.slot_end = slot_end;
    }

    public ArrayList<UserDetailModel> getAttendees() {
        return attendees;
    }

    public void setAttendees(ArrayList<UserDetailModel> attendees) {
        this.attendees = attendees;
    }

    private ArrayList<UserDetailModel> attendees;

    public String getWhere() {
        return where;
    }

    public void setWhere(String where) {
        this.where = where;
    }

    public class UserDetailModel {
        public String getFirstName() {
            return firstName;
        }

        public void setFirstName(String firstName) {
            this.firstName = firstName;
        }

        public String getLastName() {
            return lastName;
        }

        public void setLastName(String lastName) {
            this.lastName = lastName;
        }

        public String getColor() {
            return color;
        }

        public void setColor(String color) {
            this.color = color;
        }

        private String firstName;
        private String lastName;
        private String color;
        private String initials;

        public String getInitials() {
            if(StringUtils.isEmpty(initials)){
                initials = getFirstChar(firstName) + getFirstChar(lastName);
            }
            return initials.toUpperCase();
        }

        String getFirstChar(String string){
         return   !StringUtils.isEmpty(string) ? string.charAt(0)+"" : "";
        }

        public void setInitials(String initials) {
            this.initials = initials;
        }
    }
}
