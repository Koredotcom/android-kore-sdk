package kore.botssdk.models;

import kore.botssdk.utils.StringUtils;

public class BrandingColorsModel {
    String primary;
    String secondary;
    String primary_text;
    String secondary_text;
    Boolean useColorPaletteOnly;

    public BrandingColorsModel updateWith(BrandingColorsModel configModel) {
        primary = !StringUtils.isNullOrEmpty(configModel.primary) ? configModel.primary : primary;
        secondary = !StringUtils.isNullOrEmpty(configModel.secondary) ? configModel.secondary : secondary;
        primary_text = !StringUtils.isNullOrEmpty(configModel.primary_text) ? configModel.primary_text : primary_text;
        secondary_text = !StringUtils.isNullOrEmpty(configModel.secondary_text) ? configModel.secondary_text : secondary_text;
        useColorPaletteOnly = configModel.useColorPaletteOnly != null ? configModel.useColorPaletteOnly : useColorPaletteOnly;
        return this;
    }

    public String getPrimary() {
        return primary;
    }

    public String getPrimary_text() {
        return primary_text;
    }

    public String getSecondary() {
        return secondary;
    }

    public String getSecondary_text() {
        return secondary_text;
    }

    public boolean isUseColorPaletteOnly() {
        return useColorPaletteOnly != null ? useColorPaletteOnly : false;
    }
}
