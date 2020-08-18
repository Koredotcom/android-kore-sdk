package com.kore.ai.widgetsdk.models;

import java.io.Serializable;

public class BotTableListRowItemsModel implements Serializable
{
    private BotTableListValueModel value;
    private BotTableListTitleModel title;
    private BotTableListDefaultActionsModel default_action;
    private String bgcolor;

    public void setValue(BotTableListValueModel value)
    {
        this.value = value;
    }

    public BotTableListValueModel getValue()
    {
        return value;
    }

    public void setTitle(BotTableListTitleModel title)
    {
        this.title = title;
    }

    public BotTableListTitleModel getTitle()
    {
        return title;
    }

    public void setDefault_action(BotTableListDefaultActionsModel default_action) {
        this.default_action = default_action;
    }

    public BotTableListDefaultActionsModel getDefault_action() {
        return default_action;
    }

    public void setBgcolor(String bgcolor) {
        this.bgcolor = bgcolor;
    }

    public String getBgcolor() {
        return bgcolor;
    }
}
