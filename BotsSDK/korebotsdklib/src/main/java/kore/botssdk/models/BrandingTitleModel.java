package kore.botssdk.models;

import java.io.Serializable;

import kore.botssdk.utils.StringUtils;

public class BrandingTitleModel implements Serializable {
    String name;
    String color;
    String type;
    String img;

    public BrandingTitleModel updateWith(BrandingTitleModel configModel) {
        name = !StringUtils.isNullOrEmpty(configModel.name) ? configModel.name : name;
        color = !StringUtils.isNullOrEmpty(configModel.color) ? configModel.color : color;
        type = !StringUtils.isNullOrEmpty(configModel.type) ? configModel.type : type;
        img = !StringUtils.isNullOrEmpty(configModel.img) ? configModel.img : img;
        return this;
    }

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
