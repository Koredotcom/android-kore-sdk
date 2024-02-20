package kore.botssdk.models;

public class BrandingHeaderModel {
    private String bg_color;
    private String size;
    private String style;
    private BrandingIconModel icon;
    private BrandingTitleModel title;
    private BrandingTitleModel sub_title;
    private BrandingHeaderButtonsModel buttons;

    public void setIcon(BrandingIconModel icon) {
        this.icon = icon;
    }

    public void setTitle(BrandingTitleModel title) {
        this.title = title;
    }

    public void setButtons(BrandingHeaderButtonsModel buttons) {
        this.buttons = buttons;
    }

    public void setStyle(String style) {
        this.style = style;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public void setBg_color(String bg_color) {
        this.bg_color = bg_color;
    }

    public void setSub_title(BrandingTitleModel sub_title) {
        this.sub_title = sub_title;
    }

    public BrandingIconModel getIcon() {
        return icon;
    }

    public String getStyle() {
        return style;
    }

    public String getSize() {
        return size;
    }

    public BrandingHeaderButtonsModel getButtons() {
        return buttons;
    }

    public BrandingTitleModel getSub_title() {
        return sub_title;
    }

    public BrandingTitleModel getTitle() {
        return title;
    }

    public String getBg_color() {
        return bg_color;
    }
}
