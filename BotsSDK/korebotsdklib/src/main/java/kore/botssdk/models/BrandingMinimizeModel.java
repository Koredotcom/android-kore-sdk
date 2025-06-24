package kore.botssdk.models;

import java.io.Serializable;

import kore.botssdk.utils.StringUtils;

public class BrandingMinimizeModel implements Serializable {
    private String icon;
    private String theme;

    public BrandingMinimizeModel updateWith(BrandingMinimizeModel configModel) {
        icon = !StringUtils.isNullOrEmpty(configModel.icon) ? configModel.icon : icon;
        theme = !StringUtils.isNullOrEmpty(configModel.theme) ? configModel.theme : theme;
        return this;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    public String getIcon() {
        return icon;
    }

    public String getTheme() {
        return theme;
    }
}
