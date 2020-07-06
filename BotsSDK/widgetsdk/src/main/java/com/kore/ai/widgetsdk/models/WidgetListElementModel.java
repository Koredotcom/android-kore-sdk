package com.kore.ai.widgetsdk.models;

import java.util.ArrayList;

public class WidgetListElementModel {

    public ImageModel getImage() {
        return image;
    }

    public void setImage(ImageModel image) {
        this.image = image;
    }

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

    public HeaderOptionsModel getValue() {
        return value;
    }

    public void setValue(HeaderOptionsModel value) {
        this.value = value;
    }

    public ArrayList<ContentModel> getContent() {
        return content;
    }

    public void setContent(ArrayList<ContentModel> content) {
        this.content = content;
    }

    public Widget.DefaultAction getDefault_action() {
        return default_action;
    }

    public void setDefault_action(Widget.DefaultAction default_action) {
        this.default_action = default_action;
    }

    public ArrayList<Widget.Button> getButtons() {
        return buttons;
    }

    public void setButtons(ArrayList<Widget.Button> buttons) {
        this.buttons = buttons;
    }

    private ImageModel image;
    private String title;
    private String subtitle;
    private HeaderOptionsModel value;

    public Object getButtonsLayout() {
        return buttonsLayout;
    }

    public void setButtonsLayout(Object buttonsLayout) {
        this.buttonsLayout = buttonsLayout;
    }

    private Object buttonsLayout;

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    private String icon;
    private String theme;

    public boolean isHasMore() {
        return hasMore;
    }

    public void setHasMore(boolean hasMore) {
        this.hasMore = hasMore;
    }

    private boolean hasMore;
    private ArrayList<ContentModel> content = null;

    public ArrayList<ContentModel> getDetails() {
        return details;
    }

    public void setDetails(ArrayList<ContentModel> details) {
        this.details = details;
    }

    private ArrayList<ContentModel> details = null;
    private Widget.DefaultAction default_action;
    private ArrayList<Widget.Button> buttons = null;
}
