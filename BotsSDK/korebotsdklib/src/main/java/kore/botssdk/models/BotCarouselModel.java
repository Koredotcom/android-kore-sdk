package kore.botssdk.models;

import java.util.ArrayList;

/**
 * Created by Pradeep Mahato on 13/7/17.
 * Copyright (c) 2014 Kore Inc. All rights reserved.
 */
public class BotCarouselModel {

    String title = "";
    String image_url = "";
    String subtitle;
    BotListDefaultModel default_action;
    ArrayList<BotCaourselButtonModel> buttons;

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

    public ArrayList<BotCaourselButtonModel> getButtons() {
        return buttons;
    }
}
