package kore.botssdk.models;

import java.io.Serializable;

import kore.botssdk.utils.StringUtils;

public class BrandingBodyUserMessageModel implements Serializable {
    private String bg_color;
    private String color;

    public BrandingBodyUserMessageModel updateWith(BrandingBodyUserMessageModel configModel) {
        bg_color = !StringUtils.isNullOrEmpty(configModel.bg_color) ? configModel.bg_color : bg_color;
        color = !StringUtils.isNullOrEmpty(configModel.color) ? configModel.color : color;
        return this;
    }

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
