package com.kore.ai.widgetsdk.models;

import java.util.ArrayList;

/**
 * Created by Shiva Krishna on 6/15/2018.
 */

public class MeetingSlotModel {
    public long getDay() {
        return day;
    }

    public ArrayList<Slot> getSlots() {
        return slots;
    }

    private long day;

    public void setSlots(ArrayList<Slot> slots) {
        this.slots = slots;
    }

    private ArrayList<Slot> slots;

    public class Slot{
        public long getStart() {
            return start;
        }

        public long getEnd() {
            return end;
        }

        public int getType() {
            return type;
        }

        private long start;

        public void setStart(long start) {
            this.start = start;
        }

        public void setEnd(long end) {
            this.end = end;
        }

        public void setType(int type) {
            this.type = type;
        }

        private long end;
        private int type;

        @Override
        public boolean equals(Object o) {
            return o instanceof Slot && ((Slot) o).getStart() == getStart() && ((Slot) o).getEnd() == getEnd();
        }
    }
}


