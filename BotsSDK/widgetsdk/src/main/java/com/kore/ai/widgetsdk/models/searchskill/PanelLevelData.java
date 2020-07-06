package com.kore.ai.widgetsdk.models.searchskill;

public class PanelLevelData {
    private String name;
    private String _id ;
    private String skillId;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public void setSkillId(String skillId) {
        this.skillId = skillId;
    }

    public String get_id() {
        return _id;
    }

    public String getSkillId() {
        return skillId;
    }
}
