package kore.botssdk.models;

import com.google.gson.annotations.SerializedName;

public class DescriptionStylesModel
{
    @SerializedName("font-size")
    private String fontSize;
    private String color;
    private String border;
    private String background;

    public String getBackground() {
        return background;
    }
    @SerializedName("border-width")
    private String borderWidth;

    @SerializedName("border-radius")
    private String borderRadius;

    public void setBorder(String border) {
        this.border = border;
    }

    public String getBorder() {
        return border;
    }

    public void setBorderRadius(String borderRadius) {
        this.borderRadius = borderRadius;
    }

    public String getBorderRadius() {
        return borderRadius;
    }

    public void setBorderWidth(String borderWidth) {
        this.borderWidth = borderWidth;
    }

    public String getBorderWidth() {
        return borderWidth;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getFontSize() {
        return fontSize;
    }

    public void setFontSize(String fontSize) {
        this.fontSize = fontSize;
    }
}
