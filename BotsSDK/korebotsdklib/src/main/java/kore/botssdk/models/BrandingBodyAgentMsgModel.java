package kore.botssdk.models;

public class BrandingBodyAgentMsgModel {
    private String bg_color;
    private String color;
    private String separator;
    private BrandingIconModel icon;
    private BrandingTitleModel title;
    private BrandingTitleModel sub_title;

    public void setBg_color(String bg_color) {
        this.bg_color = bg_color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public void setIcon(BrandingIconModel icon) {
        this.icon = icon;
    }

    public void setTitle(BrandingTitleModel title) {
        this.title = title;
    }

    public void setSub_title(BrandingTitleModel sub_title) {
        this.sub_title = sub_title;
    }

    public void setSeparator(String separator) {
        this.separator = separator;
    }

    public String getBg_color() {
        return bg_color;
    }

    public BrandingTitleModel getTitle() {
        return title;
    }

    public BrandingTitleModel getSub_title() {
        return sub_title;
    }

    public BrandingIconModel getIcon() {
        return icon;
    }

    public String getColor() {
        return color;
    }

    public String getSeparator() {
        return separator;
    }
}
