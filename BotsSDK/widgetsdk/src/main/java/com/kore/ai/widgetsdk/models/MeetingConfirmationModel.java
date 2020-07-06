package com.kore.ai.widgetsdk.models;

import android.view.View;

import com.kore.ai.widgetsdk.utils.DateUtils;
import com.kore.ai.widgetsdk.utils.StringUtils;

import java.text.MessageFormat;
import java.util.ArrayList;

import static com.kore.ai.widgetsdk.utils.DateUtils.getTimeInAmPm;

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
    private ArrayList<MeetingSlotModel.Slot> slots;

    public String getWhere() {
        return where;
    }
    public int getLocVisibility(){
        return (where==null || where.isEmpty())? View.GONE:View.VISIBLE;
    }

    public void setWhere(String where) {
        this.where = where;
    }
    public String getDateAndTimeString(){
        String startTime = getTimeInAmPm(slot_start).toUpperCase();
        String endTime = getTimeInAmPm(slot_end).toUpperCase();
          return MessageFormat.format("{0}, {1} to {2} ", DateUtils.getDate(date), startTime, endTime);

    }

    public ArrayList<MeetingSlotModel.Slot> getSlots() {
        return slots;
    }

    public void setSlots(ArrayList<MeetingSlotModel.Slot> slots) {
        this.slots = slots;
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
//            if(StringUtils.isEmpty(initials)){
//                initials = getFirstChar(firstName) + getFirstChar(lastName);
//            }
            return initials.toUpperCase();
        }

//        String getFirstChar(String string){
//         return   !StringUtils.isEmpty(string) ? string.charAt(0)+"" : "";
//        }

        public void setInitials(String initials) {
            this.initials = initials;
        }
    }

    public String getparticipantsAsString(){
        if(attendees != null && attendees.size() > 0){
            int size = attendees.size();
            if(size == 1){
                return  attendees.get(0).getFirstName();
            }else{
                return attendees.get(0).getFirstName() + " and "+ (size-1) +(size - 1 > 1 ?  " others" : " other");
            }
        }else{
            return "";
        }
    }
}
