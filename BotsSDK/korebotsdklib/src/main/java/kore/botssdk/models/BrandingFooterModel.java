package kore.botssdk.models;

import java.io.Serializable;

import kore.botssdk.utils.StringUtils;

public class BrandingFooterModel implements Serializable {
    private String bg_color;
    private String layout;
    private String style;
    private String icons_color;
    private BrandingFooterComposeBarModel compose_bar;
    private BrandingFooterButtonsModel buttons;

    public BrandingFooterModel updateWith(BrandingFooterModel configModel) {
        bg_color = !StringUtils.isNullOrEmpty(configModel.bg_color) ? configModel.bg_color : bg_color;
        layout = !StringUtils.isNullOrEmpty(configModel.layout) ? configModel.layout : layout;
        style = !StringUtils.isNullOrEmpty(configModel.style) ? configModel.style : style;
        icons_color = !StringUtils.isNullOrEmpty(configModel.icons_color) ? configModel.icons_color : icons_color;
        compose_bar = compose_bar != null && configModel.compose_bar != null ? compose_bar.updateWith(configModel.compose_bar) : compose_bar;
        buttons = buttons != null && configModel.buttons != null ? buttons.updateWith(configModel.buttons) : buttons;
        return this;
    }

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
