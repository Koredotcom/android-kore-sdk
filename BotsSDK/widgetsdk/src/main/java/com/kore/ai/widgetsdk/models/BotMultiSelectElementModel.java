package com.kore.ai.widgetsdk.models;

public class BotMultiSelectElementModel implements MultiSelectBase {

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    private String title;
    private String value;

}
