package com.kore.ai.widgetsdk.models;

import java.io.Serializable;

public class BotTableListElementsItemsModel implements Serializable
{
    private BotTableListValueModel value;
    private String title;
    private BotTableListDefaultActionsModel default_action;
    private String subtitle;
    private ImageModel image;
    private String bgcolor;

    public ImageModel getImage() {
        return image;
    }

    public void setImage(ImageModel image) {
        this.image = image;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public void setValue(BotTableListValueModel value)
    {
        this.value = value;
    }

    public BotTableListValueModel getValue()
    {
        return value;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public String getTitle()
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
