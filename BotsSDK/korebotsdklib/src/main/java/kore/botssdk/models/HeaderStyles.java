package kore.botssdk.models;

import com.google.gson.annotations.SerializedName;

public class HeaderStyles
{
    @SerializedName("background-color")
    private final String background_color;
    private final String color;
    private final String border;

    @SerializedName("font-weight")
    private String font_weight;

    public String getFont_weight() {
        return font_weight;
    }

    @SerializedName("border-left")
    private final String borderLeft;
    @SerializedName("border-right")
    private final String borderRight;
    @SerializedName("border-top")
    private final String borderTop;
    @SerializedName("border-bottom")
    private final String borderBottom;

    public HeaderStyles(String background_color, String color, String border, String borderLeft, String borderRight, String borderTop, String borderBottom) {
        this.background_color = background_color;
        this.color = color;
        this.border = border;
        this.borderLeft = borderLeft;
        this.borderRight = borderRight;
        this.borderTop = borderTop;
        this.borderBottom = borderBottom;
    }

    public String getBorderLeft() {
        return borderLeft;
    }

    public String getBorderBottom() {
        return borderBottom;
    }

    public String getBorderRight() {
        return borderRight;
    }

    public String getBorderTop() {
        return borderTop;
    }

    public String getBackground_color() {
        return background_color;
    }

    public String getBorder() {
        return border;
    }

    public String getColor() {
        return color;
    }
}
