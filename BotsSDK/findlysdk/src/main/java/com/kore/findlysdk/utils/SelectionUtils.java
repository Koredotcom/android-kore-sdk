package com.kore.findlysdk.utils;

import com.kore.findlysdk.models.MeetingSlotModel;

import java.util.ArrayList;

public class SelectionUtils {

    private static ArrayList<String> selectedTasks = new ArrayList<>();
    private static ArrayList<MeetingSlotModel.Slot> selectedSlots = new ArrayList<>();

    public static ArrayList<String> getSelectedTasks() {
        return selectedTasks;
    }

    public static void setSelectedTasks(ArrayList<String> selectedTasks) {
        SelectionUtils.selectedTasks = selectedTasks;
    }

    public static void addSelectedTask(ArrayList<String> tasks) {
        SelectionUtils.selectedTasks.addAll(tasks);
    }

    public static void resetSelectionTasks() {
        SelectionUtils.selectedTasks = new ArrayList<>();
    }

    public static ArrayList<MeetingSlotModel.Slot> getSelectedSlots() {
        return selectedSlots;
    }

    public static void setSelectedSlots(ArrayList<MeetingSlotModel.Slot> selectedSlots) {
        SelectionUtils.selectedSlots = selectedSlots;
    }

    public static void addSelectedSlots(ArrayList<MeetingSlotModel.Slot> slots) {
        SelectionUtils.selectedSlots.addAll(slots);
    }

    public static void resetSelectionSlots() {
        SelectionUtils.selectedSlots = new ArrayList<>();
    }
}
