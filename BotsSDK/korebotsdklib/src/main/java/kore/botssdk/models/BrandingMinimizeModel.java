package kore.botssdk.models;

import java.io.Serializable;

public class BrandingMinimizeModel implements Serializable {
    private String icon;
    private String theme;

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
