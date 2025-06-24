package kore.botssdk.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import kore.botssdk.utils.StringUtils;

public class BrandingFooterComposeBarModel implements Serializable {
    private String bg_color;
    @SerializedName("outline-color")
    private String outline_color;
    private String placeholder;
    private String inline_color;

    public BrandingFooterComposeBarModel updateWith(BrandingFooterComposeBarModel configModel) {
        bg_color = !StringUtils.isNullOrEmpty(configModel.bg_color) ? configModel.bg_color : bg_color;
        outline_color = !StringUtils.isNullOrEmpty(configModel.outline_color) ? configModel.outline_color : outline_color;
        placeholder = !StringUtils.isNullOrEmpty(configModel.placeholder) ? configModel.placeholder : placeholder;
        inline_color = !StringUtils.isNullOrEmpty(configModel.inline_color) ? configModel.inline_color : inline_color;
        return this;
    }

    public void setBg_color(String bg_color) {
        this.bg_color = bg_color;
    }

    public void setOutline_color(String outline_color) {
        this.outline_color = outline_color;
    }

    public void setPlaceholder(String placeholder) {
        this.placeholder = placeholder;
    }

    public String getBg_color() {
        return bg_color;
    }

    public String getOutline_color() {
        return outline_color;
    }

    public String getPlaceholder() {
        return placeholder;
    }

    public void setInline_color(String inline_color) {
        this.inline_color = inline_color;
    }

    public String getInline_color() {
        return inline_color;
    }
}
