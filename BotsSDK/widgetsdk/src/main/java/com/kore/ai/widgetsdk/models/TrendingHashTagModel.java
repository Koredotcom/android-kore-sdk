package com.kore.ai.widgetsdk.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TrendingHashTagModel {
    public DefaultAction getDefaultAction() {
        return defaultAction;
    }

    public void setDefaultAction(DefaultAction defaultAction) {
        this.defaultAction = defaultAction;
    }

    @SerializedName("default_action")
    @Expose
    private DefaultAction defaultAction;
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    @SerializedName("title_right")
    @Expose
    private String title_right;

    public String getTitle_right() {
        return title_right;
    }

    public void setTitle_right(String title_right) {
        this.title_right = title_right;
    }

    @SerializedName("title")
    @Expose
    private String name;


    private int count;

    public TrendingHashTagModel(){

    }


    public class DefaultAction {

        @SerializedName("type")
        @Expose
        private String type;
        @SerializedName("utterance")
        @Expose
        private String utterance;

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getUtterance() {
            return utterance;
        }

        public void setUtterance(String utterance) {
            this.utterance = utterance;
        }

    }
}
