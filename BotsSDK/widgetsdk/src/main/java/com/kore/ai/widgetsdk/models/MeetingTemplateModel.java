package com.kore.ai.widgetsdk.models;

import java.util.ArrayList;

public class MeetingTemplateModel {
    private ArrayList<MeetingSlotModel> working_hours;
    private ArrayList<BotButtonModel> buttons;

    public ArrayList<MeetingSlotModel> getWorking_hours() {
        return working_hours;
    }

    public void setWorking_hours(ArrayList<MeetingSlotModel> working_hours) {
        this.working_hours = working_hours;
    }

    public ArrayList<MeetingSlotModel.Slot> getQuick_slots() {
        return quick_slots;
    }

    public void setQuick_slots(ArrayList<MeetingSlotModel.Slot> quick_slots) {
        this.quick_slots = quick_slots;
    }

    private ArrayList<MeetingSlotModel.Slot> quick_slots;
    private boolean showMore;

    public boolean isShowMore() {
        return showMore;
    }

    public void setShowMore(boolean showMore) {
        this.showMore = showMore;
    }

    public ArrayList<BotButtonModel> getButtons() {
        return buttons;
    }

    public void setButtons(ArrayList<BotButtonModel> buttons) {
        this.buttons = buttons;
    }
}
