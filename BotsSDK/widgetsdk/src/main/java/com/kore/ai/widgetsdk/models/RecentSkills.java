package com.kore.ai.widgetsdk.models;

import java.util.ArrayList;

public class RecentSkills  {

    private ArrayList<PayloadInner.Skill> skills = null;

    public ArrayList<PayloadInner.Skill> getSkills() {
        return skills;
    }

    public void setSkills(ArrayList<PayloadInner.Skill> skills) {
        this.skills = skills;
    }
}
