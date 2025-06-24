package kore.botssdk.models;

import java.io.Serializable;

import kore.botssdk.utils.StringUtils;

public class BrandingBodyBackgroundModel implements Serializable {
    private String type;
    private String color;
    private String img;

    public BrandingBodyBackgroundModel updateWith(BrandingBodyBackgroundModel configModel) {
        type = !StringUtils.isNullOrEmpty(configModel.type) ? configModel.type : type;
        color = !StringUtils.isNullOrEmpty(configModel.color) ? configModel.color : color;
        img = !StringUtils.isNullOrEmpty(configModel.img) ? configModel.img : img;
        return this;
    }

    public String getType() {
        return type;
    }

    public String getColor() {
        return color;
    }

    public String getImg() {
        return img;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public void setImg(String img) {
        this.img = img;
    }
}
