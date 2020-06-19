package kore.botssdk.models;

import java.util.ArrayList;

public class MeetingFollowUpModel {


    private String meeting_title;
    private String color;
    String text;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getTitle() {
        return meeting_title;
    }

    public void setTitle(String meeting_title) {
        this.meeting_title = meeting_title;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }



    private ArrayList<MeetingSlotModel.Slot> quick_slots;

    public ArrayList<MeetingSlotModel.Slot> getSlots() {
        return quick_slots;
    }

    public void setSlots(ArrayList<MeetingSlotModel.Slot> quick_slots) {
        this.quick_slots = quick_slots;
    }
}
