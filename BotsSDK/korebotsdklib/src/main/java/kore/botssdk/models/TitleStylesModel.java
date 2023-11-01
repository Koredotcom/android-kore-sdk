package kore.botssdk.models;

import com.google.gson.annotations.SerializedName;

public class TitleStylesModel
{
    @SerializedName("padding-top")
    private String paddingTop;
    @SerializedName("font-size")
    private String fontSize;
    private String color;
    private String border;

    public String getColor() {
        return color;
    }

    public String getBorder() {
        return border;
    }

    public String getFontSize() {
        return fontSize;
    }

    public String getPaddingTop() {
        return paddingTop;
    }
}
