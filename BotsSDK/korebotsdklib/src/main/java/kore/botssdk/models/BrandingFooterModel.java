package kore.botssdk.models;

public class BrandingFooterModel {
    private String bg_color;
    private String layout;
    private String style;
    private BrandingFooterComposeBarModel compose_bar;
    private BrandingFooterButtonsModel buttons;

    public String getBg_color() {
        return bg_color;
    }

    public String getStyle() {
        return style;
    }

    public BrandingFooterButtonsModel getButtons() {
        return buttons;
    }

    public BrandingFooterComposeBarModel getCompose_bar() {
        return compose_bar;
    }

    public String getLayout() {
        return layout;
    }

    public void setBg_color(String bg_color) {
        this.bg_color = bg_color;
    }

    public void setStyle(String style) {
        this.style = style;
    }

    public void setButtons(BrandingFooterButtonsModel buttons) {
        this.buttons = buttons;
    }

    public void setLayout(String layout) {
        this.layout = layout;
    }

    public void setCompose_bar(BrandingFooterComposeBarModel compose_bar) {
        this.compose_bar = compose_bar;
    }
}
