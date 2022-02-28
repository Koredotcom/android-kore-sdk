package com.kore.findlysdk.models;

import java.util.HashMap;

public class LinkedBotNLModel
{
    private String childBotName;
    private String intent;
    private boolean isRefresh;

    public LinkedBotNLModel(String childBotName, String intent, boolean isRefresh) {
        this.childBotName = childBotName;
        this.intent = intent;
        this.isRefresh = isRefresh;
    }

    public void setChildBotName(String childBotName) {
        this.childBotName = childBotName;
    }

    public void setIntent(String intent) {
        this.intent = intent;
    }

    public void setRefresh(boolean refresh) {
        isRefresh = refresh;
    }

    public String getChildBotName() {
        return childBotName;
    }

    public String getIntent() {
        return intent;
    }

    public boolean isRefresh() {
        return isRefresh;
    }
}
