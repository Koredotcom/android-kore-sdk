package com.kore.ai.widgetsdk.models;

public class UniversalSearchSkillModel {
    private String title;
    private String desc;
    private String cOn;
    private String lMod;
    private String rating;

    public String getcOn() {
        return cOn;
    }

    public void setcOn(String cOn) {
        this.cOn = cOn;
    }

    public String getlMod() {
        return lMod;
    }

    public void setlMod(String lMod) {
        this.lMod = lMod;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }


    private Widget.DefaultAction defaultAction;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public Widget.DefaultAction getDefault_action() {
        return defaultAction;
    }

    public void setDefault_action(Widget.DefaultAction defaultAction) {
        this.defaultAction = defaultAction;
    }


}
