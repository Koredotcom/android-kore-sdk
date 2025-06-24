package kore.botssdk.models;

import android.annotation.SuppressLint;

import java.io.Serializable;

import kore.botssdk.utils.StringUtils;

@SuppressLint("UnknownNullness")
public class BrandingGeneralModel implements Serializable {
    private String bot_icon;
    private String size;
    private String themeType;
    private BrandingColorsModel colors;

    public BrandingGeneralModel updateWith(BrandingGeneralModel configModel) {
        bot_icon = !StringUtils.isNullOrEmpty(configModel.bot_icon) ? configModel.bot_icon : bot_icon;
        size = !StringUtils.isNullOrEmpty(configModel.size) ? configModel.size : size;
        themeType = !StringUtils.isNullOrEmpty(configModel.themeType) ? configModel.themeType : themeType;
        colors = colors != null && configModel.colors != null ? colors.updateWith(configModel.colors) : colors;
        return this;
    }

    public BrandingColorsModel getColors() {
        return colors;
    }

    public String getBot_icon() {
        return bot_icon;
    }

    public String getSize() {
        return size;
    }

    public String getThemeType() {
        return themeType;
    }

    public void setBot_icon(String bot_icon) {
        this.bot_icon = bot_icon;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public void setThemeType(String themeType) {
        this.themeType = themeType;
    }
}
