package kore.botssdk.models;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Pradeep Mahato on 21/7/17.
 * Copyright (c) 2014 Kore Inc. All rights reserved.
 */
public class BotListModel implements Serializable {
    String title;
    String image_url;
    String subtitle;
    String value;
    String color;
    BotListDefaultModel default_action;
    ArrayList<BotListElementButton> buttons;

    public String getTitle() {
        return title;
    }

    public String getImage_url() {
        return image_url;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public BotListDefaultModel getDefault_action() {
        return default_action;
    }

    public ArrayList<BotListElementButton> getButtons() {
        return buttons;
    }

    public void setValue(String value)
    {
        this.value = value;
    }

    public String getValue()
    {
        return value;
    }

    public void setColor(String color)
    {
        this.color = color;
    }

    public String getColor()
    {
        return color;
    }
}
