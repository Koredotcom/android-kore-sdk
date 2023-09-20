package kore.botssdk.models;

import java.io.Serializable;

public class BrandingTitleModel implements Serializable {
    String name;
    String color;
    String type;
    String img;

    public String getType() {
        return type;
    }

    public String getImg() {
        return img;
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setColor(String color) {
        this.color = color;
    }
}
