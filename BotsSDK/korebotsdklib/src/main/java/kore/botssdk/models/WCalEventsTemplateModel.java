package kore.botssdk.models;

import java.util.List;

/**
 * Created by Ramachandra Pradeep on 01-Apr-19.
 */

public class WCalEventsTemplateModel {
    public String getTemplate_type() {
        return template_type;
    }

    public void setTemplate_type(String template_type) {
        this.template_type = template_type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public List<Action> getActions() {
        return actions;
    }

    public void setActions(List<Action> actions) {
        this.actions = actions;
    }

    public CalenderEventData getData() {
        return data;
    }

    public void setData(CalenderEventData data) {
        this.data = data;
    }

    private String template_type;
    private String title;
    private String id;
    private String location;
    private List<WCalEventsTemplateModel.Action> actions = null;
    private CalenderEventData data;

    public boolean isShowDate() {
        return showDate;
    }

    public void setShowDate(boolean showDate) {
        this.showDate = showDate;
    }

    private boolean showDate;


    public class Action {
        private String type;
        private String title;
        private String utterance;

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getUtterance() {
            return utterance;
        }

        public void setUtterance(String utterance) {
            this.utterance = utterance;
        }
    }
}