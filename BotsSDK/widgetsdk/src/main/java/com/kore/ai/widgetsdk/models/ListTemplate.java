package com.kore.ai.widgetsdk.models;

import java.util.ArrayList;

/**
 * Created by Anil Kumar on 12/20/2016.
 */
public class ListTemplate {

    private String title;
    private String image_url;
    private String subtitle;

    private ListTemplateButton default_action;
    private ArrayList<ListTemplateButton> buttons;

    public ListTemplate(){}

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public ArrayList<ListTemplateButton> getButtons() {
        return buttons;
    }

    public void setButtons(ArrayList<ListTemplateButton> buttons) {
        this.buttons = buttons;
    }

    public ListTemplateButton getDefault_action() {
        return default_action;
    }

    public void setDefault_action(ListTemplateButton default_action) {
        this.default_action = default_action;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }
}
