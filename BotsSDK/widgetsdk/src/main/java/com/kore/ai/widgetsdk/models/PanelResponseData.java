package com.kore.ai.widgetsdk.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class PanelResponseData implements Serializable {

    @SerializedName("panels")
    @Expose
    private List<Panel> panels = null;

    public List<Panel> getPanels() {
        return panels;
    }

    public void setPanels(List<Panel> panels) {
        this.panels = panels;
    }


    public static class Panel implements Serializable,Cloneable {

        @Override
        public Panel clone() throws CloneNotSupportedException {
            Panel panel = (Panel) super.clone();
            panel.setWidgets(panel.getClonedObject());
            return panel;
        }
        private String _id;
        private String skillId;

        public String getSkillId() {
            return skillId!=null?skillId:"";
        }

        public void setSkillId(String skillId) {
            this.skillId = skillId;
        }

        public String get_id() {
            return _id!=null?_id:"";
        }

        public void set_id(String _id) {
            this._id = _id;
        }

        @SerializedName("theme")
        @Expose
        private String theme;

        public String getTheme() {
            if(theme != null)
                return theme;
            else
                return "#e5f5f6";
        }

        public void setTheme(String theme) {
            this.theme = theme;
        }

        private boolean isItemClicked;

        private String type;

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public boolean isItemClicked() {
            return isItemClicked;
        }

        public void setItemClicked(boolean itemClicked) {
            isItemClicked = itemClicked;
        }

        public String getTeamId() {
            return teamId;
        }

        public void setTeamId(String teamId) {
            this.teamId = teamId;
        }

        String teamId;

        @SerializedName("name")
        @Expose
        private String name;
        @SerializedName("icon")
        @Expose
        private String icon;
        @SerializedName("iconId")
        @Expose
        private String iconId;

        @SerializedName("widgets")
        @Expose
        private List<WidgetsModel> widgets = null;

        @SerializedName("trigger")
        @Expose
        private String trigger;

        @SerializedName("callbackURL")
        @Expose
        private String callbackURL;

        public String getCallbackURL() {
            return callbackURL;
        }

        public void setCallbackURL(String trigger) {
            this.callbackURL = trigger;
        }

        public String getTrigger() {
            return trigger;
        }

        public void setTrigger(String trigger) {
            this.trigger = trigger;
        }


        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getIcon() {
            return icon;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }

        public String getIconId() {
            return iconId;
        }

        public void setIconId(String iconId) {
            this.iconId = iconId;
        }

        public List<WidgetsModel> getWidgets() {
            return widgets;
        }

        public List<WidgetsModel> getClonedObject()
        {
            List<WidgetsModel> clone = new ArrayList<>();
            for (WidgetsModel item : widgets) {
                try {
                    clone.add(item.clone());
                } catch (CloneNotSupportedException e) {
                    e.printStackTrace();
                }
            }
            return clone;
        }

        public final void setWidgets(List<WidgetsModel> widgets) {
            this.widgets = widgets;
        }
    }
}