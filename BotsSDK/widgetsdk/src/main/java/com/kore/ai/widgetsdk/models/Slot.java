package com.kore.ai.widgetsdk.models;

import java.util.List;

/**
 * Created by Ramachandra Pradeep on 27-Aug-18.
 */

public class Slot {
    private long end;
    private long start;
    private long weight;
    private List<String> al = null;

    public long getEnd() {
        return end;
    }

    public void setEnd(int end) {
        this.end = end;
    }

    public long getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public long getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public List<String> getAl() {
        return al;
    }

    public void setAl(List<String> al) {
        this.al = al;
    }
}
