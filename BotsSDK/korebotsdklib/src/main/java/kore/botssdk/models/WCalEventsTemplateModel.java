package kore.botssdk.models;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Ramachandra Pradeep on 01-Apr-19.
 */

public class WCalEventsTemplateModel extends BaseCalenderTemplateModel implements Serializable, Cloneable {
    public String getTemplate_type() {
        return template_type;
    }

    public void setTemplate_type(String template_type) {
        this.template_type = template_type;
    }

    public String getTitle() {
        return title;
    }
    boolean isActive;
    String meetingId;

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public String getMeetingId() {
        return meetingId;
    }

    public void setMeetingId(String meetingId) {
        this.meetingId = meetingId;
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
        return location != null ? location : "";
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

    public boolean isOnGoing() {
        return isOnGoing;
    }

    public void setOnGoing(boolean onGoing) {
        isOnGoing = onGoing;
    }

    private boolean isOnGoing;


    public class Action implements Serializable {
        private String type;
        private String title;
        private String utterance;
        private String custom_type;

        private boolean activityTrack;

        private AnalyticsModel activityInfo = null;

        public boolean isActivityTrack() {
            return activityTrack;
        }

        public void setActivityTrack(boolean activityTrack) {
            this.activityTrack = activityTrack;
        }

        public AnalyticsModel getActivityInfo() {
            return activityInfo;
        }

        public void setActivityInfo(AnalyticsModel activityInfo) {
            this.activityInfo = activityInfo;
        }

        public String getCustom_type() {
            return custom_type != null ? custom_type : "";
        }

        public void setCustom_type(String custom_type) {
            this.custom_type = custom_type;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getDial() {
            return dial != null ? dial : "";
        }

        public void setDial(String dial) {
            this.dial = dial;
        }

        private String url;
        private String dial;

        public String getType() {
            return type != null ? type : "";
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

    /*
    "activityInfo": {
            "entity": "Meeting",
            "action": "DAILIN",
            "entityId": "701urkb31bj80cuapnr4vpdsro",
            "from": "widget"
          }
     */

    public static class AnalyticsModel implements Serializable{
        private String entity;
        private String entityId;
        private String action;
        private String from;

        public String getEntity() {
            return entity;
        }

        public void setEntity(String entity) {
            this.entity = entity;
        }

        public String getEntityId() {
            return entityId;
        }

        public void setEntityId(String entityId) {
            this.entityId = entityId;
        }

        public String getAction() {
            return action;
        }

        public void setAction(String action) {
            this.action = action;
        }

        public String getFrom() {
            return from;
        }

        public void setFrom(String from) {
            this.from = from;
        }
    }


    @Override
    public WCalEventsTemplateModel clone() throws CloneNotSupportedException {
            WCalEventsTemplateModel obj = (WCalEventsTemplateModel)super.clone();
            CalenderEventData calEv = obj.getData().clone();
            calEv.setDuration(calEv.getDuration().clone());
            obj.setData(calEv);

            return obj;
    }
}
