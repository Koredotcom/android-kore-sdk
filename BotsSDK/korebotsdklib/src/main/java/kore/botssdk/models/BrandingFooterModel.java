package kore.botssdk.models;

import java.io.Serializable;

public class BrandingFooterModel implements Serializable {
    private String bg_color;
    private String layout;
    private String style;
    private String icons_color;
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

    public void setIcons_color(String icons_color) {
        this.icons_color = icons_color;
    }
    public String getIcons_color() {
        return icons_color;
    }
    public void setBg_color(String bg_color) {
        this.bg_color = bg_color;
    }

}
