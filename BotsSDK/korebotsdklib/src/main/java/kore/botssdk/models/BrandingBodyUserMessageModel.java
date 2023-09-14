package kore.botssdk.models;

import java.io.Serializable;

public class BrandingBodyUserMessageModel implements Serializable {
    private String bg_color;
    private String color;

    public void setBg_color(String bg_color) {
        this.bg_color = bg_color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getBg_color() {
        return bg_color;
    }

    public String getColor() {
        return color;
    }
}
