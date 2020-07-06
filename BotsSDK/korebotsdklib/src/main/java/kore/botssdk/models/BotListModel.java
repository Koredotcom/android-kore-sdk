package kore.botssdk.models;

import java.util.ArrayList;

/**
 * Created by Pradeep Mahato on 21/7/17.
 * Copyright (c) 2014 Kore Inc. All rights reserved.
 */
public class BotListModel {
    String title;
    String image_url;
    String subtitle;
    String value;
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

    public void setvalue()
    {
        this.value = value;
    }

    public String getValue()
    {
        return value;
    }
}
