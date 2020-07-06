package com.kore.ai.widgetsdk.models;

/**
 * Created by Anil Kumar on 12/20/2016.
 */
public class BotCustomListModel {
    private String title;
    private String subtitle;
    private String image_url;
    private String btn_url;
    private String btn_type;
    private String btn_title;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public String getImageUrl() {
        return image_url;
    }

    public void setImageUrl(String link) {
        this.image_url = link;
    }

    public String getBtn_url() {
        return btn_url;
    }

    public void setBtn_url(String btn_url) {
        this.btn_url = btn_url;
    }

    public String getBtn_type() {
        return btn_type;
    }

    public void setBtn_type(String btn_type) {
        this.btn_type = btn_type;
    }

    public void setBtn_title(String btn_title) {
        this.btn_title = btn_title;
    }

    public String getBtn_title() {
        return btn_title;
    }

}