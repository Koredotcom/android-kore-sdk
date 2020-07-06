package com.kore.ai.widgetsdk.models;

/**
 * Created by Ramachandra Pradeep on 27-Aug-18.
 */

public class Owner {
    private String color;
    private String emailId;
    private String name;
    private String id;
    private long slot;
    private String lN;
    private String fN;

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public long getSlot() {
        return slot;
    }

    public void setSlot(int slot) {
        this.slot = slot;
    }

    public String getLN() {
        return lN;
    }

    public void setLN(String lN) {
        this.lN = lN;
    }

    public String getFN() {
        return fN;
    }

    public void setFN(String fN) {
        this.fN = fN;
    }

}
