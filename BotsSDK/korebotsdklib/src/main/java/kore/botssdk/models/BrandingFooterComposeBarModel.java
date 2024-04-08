package kore.botssdk.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class BrandingFooterComposeBarModel implements Serializable {
    private String bg_color;
    @SerializedName("outline-color")
    private String outline_color;
    private String placeholder;
    private String inline_color;

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
