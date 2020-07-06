package com.kore.ai.widgetsdk.models;

import java.util.ArrayList;

public class AttendeeSlotTemplateModel {
    private ArrayList<MeetingSlotModel.Slot> popularSlots;

    public ArrayList<MeetingSlotModel.Slot> getPopularSlots() {
        return popularSlots;
    }

    private ArrayList<MeetingSlotModel.Slot> selectedSots;

    public void setPopularSlots(ArrayList<MeetingSlotModel.Slot> popularSlots) {
        this.popularSlots = popularSlots;
    }

    public ArrayList<MeetingSlotModel.Slot> getOtherSlots() {
        return otherSlots;
    }

    public void setOtherSlots(ArrayList<MeetingSlotModel.Slot> otherSlots) {
        this.otherSlots = otherSlots;
    }

    private ArrayList<MeetingSlotModel.Slot> otherSlots;

    public ArrayList<MeetingSlotModel.Slot> getSelectedSots() {
        return selectedSots;
    }

    public void setSelectedSots(ArrayList<MeetingSlotModel.Slot> selectedSots) {
        this.selectedSots = selectedSots;
    }
    private MeetingConfirmationModel meetingDetails;

    public MeetingConfirmationModel getMeetingDetails() {
        return meetingDetails;
    }

    public void setMeetingDetails(MeetingConfirmationModel meetingDetails) {
        this.meetingDetails = meetingDetails;
    }
}
