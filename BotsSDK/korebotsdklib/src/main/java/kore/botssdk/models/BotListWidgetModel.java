package kore.botssdk.models;

import java.io.Serializable;
import java.util.ArrayList;

public class BotListWidgetModel implements Serializable {
    String title;
    String image_url;
    String subtitle;
    BotTableListValueModel value;
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

    public void setValue(BotTableListValueModel value)
    {
        this.value = value;
    }

    public BotTableListValueModel getValue()
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