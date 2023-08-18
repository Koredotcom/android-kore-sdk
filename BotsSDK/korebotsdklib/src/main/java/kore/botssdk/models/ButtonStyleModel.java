package kore.botssdk.models;

import com.google.gson.annotations.SerializedName;

public class ButtonStyleModel
{
    @SerializedName("font-size")
    private String fontSize;

    @SerializedName("font-family")
    private String fontFamily;

    @SerializedName("font-style")
    private String fontStyle;

    @SerializedName("font-weight")
    private String fontWeight;

    private String color;
    private String border;

    @SerializedName("background-color")
    private String backgroundColor;
    private String background;
    @SerializedName("border-width")
    private String borderWidth;

    @SerializedName("border-radius")
    private String borderRadius;

    @SerializedName("border-color")
    private String borderColor;

    public String getColor() {
        return color;
    }

    public String getBackground() {
        return background;
    }

    public String getBackgroundColor() {
        return backgroundColor;
    }

    public String getBorder() {
        return border;
    }

    public String getBorderColor() {
        return borderColor;
    }

    public String getBorderRadius() {
        return borderRadius;
    }

    public String getBorderWidth() {
        return borderWidth;
    }

    public String getFontFamily() {
        return fontFamily;
    }

    public String getFontSize() {
        return fontSize;
    }

    public String getFontStyle() {
        return fontStyle;
    }

    public String getFontWeight() {
        return fontWeight;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public void setBackgroundColor(String backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public void setBorder(String border) {
        this.border = border;
    }

    public void setBorderColor(String borderColor) {
        this.borderColor = borderColor;
    }

    public void setBorderRadius(String borderRadius) {
        this.borderRadius = borderRadius;
    }

    public void setBorderWidth(String borderWidth) {
        this.borderWidth = borderWidth;
    }

    public void setFontFamily(String fontFamily) {
        this.fontFamily = fontFamily;
    }

    public void setFontSize(String fontSize) {
        this.fontSize = fontSize;
    }

    public void setFontStyle(String fontStyle) {
        this.fontStyle = fontStyle;
    }

    public void setFontWeight(String fontWeight) {
        this.fontWeight = fontWeight;
    }
}
