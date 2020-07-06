package com.kore.ai.widgetsdk.models;

import java.util.ArrayList;

public class WidgetTaskTemplateResponse {
    private ArrayList<WTaskTemplateModel> taskData;

    public void setTaskData(ArrayList<WTaskTemplateModel> taskData) {
        this.taskData = taskData;
    }

    public void setButtons(ArrayList<BotButtonModel> buttons) {
        this.buttons = buttons;
    }

    public void setShowButton(boolean showButton) {
        this.showButton = showButton;
    }

    private ArrayList<BotButtonModel> buttons;
    private boolean showButton;

    public ArrayList<WTaskTemplateModel> getTaskData() {
        return taskData;
    }

    public ArrayList<BotButtonModel> getButtons() {
        return buttons;
    }

    public boolean isShowButton() {
        return showButton;
    }
    private boolean showMore;
    private String url;

    public boolean isShowMore() {
        return showMore;
    }

    public void setShowMore(boolean showMore) {
        this.showMore = showMore;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}

