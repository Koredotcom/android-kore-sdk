package com.kore.ai.widgetsdk.models;

import java.util.ArrayList;
public class BotListModel {
    String title;
    String image_url;
    String subtitle;
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
}
