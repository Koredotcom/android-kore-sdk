package kore.botssdk.models;

public class UniversalSearchSkillModel {
    private String title;
    private String desc;

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
        return default_action;
    }

    public void setDefault_action(Widget.DefaultAction default_action) {
        this.default_action = default_action;
    }

    private Widget.DefaultAction default_action;

}
