package kore.botssdk.models;

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

    public ValueModel getValue() {
        return value;
    }

    public void setValue(ValueModel value) {
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

    public ArrayList<BotButtonModel> getButtons() {
        return buttons;
    }

    public void setButtons(ArrayList<BotButtonModel> buttons) {
        this.buttons = buttons;
    }

    private ImageModel image;
    private String title;
    private String subtitle;
    private ValueModel value;
    private ArrayList<ContentModel> content = null;

    public ArrayList<ContentModel> getDetails() {
        return details;
    }

    public void setDetails(ArrayList<ContentModel> details) {
        this.details = details;
    }

    private ArrayList<ContentModel> details = null;
    private Widget.DefaultAction default_action;
    private ArrayList<BotButtonModel> buttons = null;
}
