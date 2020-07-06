package com.kore.ai.widgetsdk.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PinWidget {



    @SerializedName("pname")
    @Expose
    private String panelName;

    public String getPanelName() {
        return panelName;
    }

    public void setPanelName(String panelName) {
        this.panelName = panelName;
    }

    @SerializedName("id")
    @Expose
    private String widgetId;
    @SerializedName("panelId")
    @Expose
    private String panelId;
    @SerializedName("name")
    @Expose
    private String widgetTitle;

    public String getWidgetTitle() {
        return widgetTitle;
    }

    public void setWidgetTitle(String widgetTitle) {
        this.widgetTitle = widgetTitle;
    }

    public String getWidgetId() {
        return widgetId;
    }

    public void setWidgetId(String widgetId) {
        this.widgetId = widgetId;
    }

    @SerializedName("skillId")
    @Expose
    private String skillId;



    public String getPanelId() {
        return panelId;
    }

    public void setPanelId(String panelId) {
        this.panelId = panelId;
    }



    public String getSkillId() {
        return skillId;
    }

    public void setSkillId(String skillId) {
        this.skillId = skillId;
    }
}