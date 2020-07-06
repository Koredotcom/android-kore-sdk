package com.kore.ai.widgetsdk.models;

import java.util.ArrayList;

public class HeaderOptionsModel {
    private String type;
    private String text;
    private UrlModel url;
    private Widget.Button button;
    private ArrayList<Widget.Button> menu;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public UrlModel getUrl() {
        return url;
    }

    public void setUrl(UrlModel url) {
        this.url = url;
    }

    public Widget.Button getButton() {
        return button;
    }

    public void setButton(Widget.Button button) {
        this.button = button;
    }

    public ArrayList<Widget.Button> getMenu() {
        return menu;
    }

    public void setMenu(ArrayList<Widget.Button> menu) {
        this.menu = menu;
    }

    public ImageModel getImage() {
        return image;
    }

    public void setImage(ImageModel image) {
        this.image = image;
    }

    private ImageModel image;

}
