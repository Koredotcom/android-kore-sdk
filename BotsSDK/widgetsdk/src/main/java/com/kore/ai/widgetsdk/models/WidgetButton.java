package com.kore.ai.widgetsdk.models;

/**
 * Created by Ramachandra Pradeep on 21-Mar-19.
 */

public class WidgetButton {

    private String template_type;
    private String title;
    private String action_type;

    public boolean isHasMore() {
        return hasMore;
    }

    public void setHasMore(boolean hasMore) {
        this.hasMore = hasMore;
    }

    public String getApi() {
        return api;
    }

    public void setApi(String api) {
        this.api = api;
    }

    private boolean hasMore;
    private String api;

    public String getTemplateType() {
        return template_type;
    }

    public void setTemplateType(String templateType) {
        this.template_type = templateType;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getActionType() {
        return action_type;
    }

    public void setActionType(String actionType) {
        this.action_type = actionType;
    }

}
