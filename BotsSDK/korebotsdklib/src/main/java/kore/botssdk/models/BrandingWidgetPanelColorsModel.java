package kore.botssdk.models;

import kore.botssdk.utils.StringUtils;

class BrandingWidgetPanelColorsModel {
    private String bg_color;
    private String color;
    private String sel_bg_color;
    private String sel_color;

    public BrandingWidgetPanelColorsModel updateWith(BrandingWidgetPanelColorsModel configModel) {
        bg_color = !StringUtils.isNullOrEmpty(configModel.bg_color) ? configModel.bg_color : bg_color;
        color = !StringUtils.isNullOrEmpty(configModel.color) ? configModel.color : color;
        sel_bg_color = !StringUtils.isNullOrEmpty(configModel.sel_bg_color) ? configModel.sel_bg_color : sel_bg_color;
        sel_color = !StringUtils.isNullOrEmpty(configModel.sel_color) ? configModel.sel_color : sel_color;
        return this;
    }

    public String getColor() {
        return color;
    }

    public String getBg_color() {
        return bg_color;
    }

    public String getSel_bg_color() {
        return sel_bg_color;
    }

    public String getSel_color() {
        return sel_color;
    }
}