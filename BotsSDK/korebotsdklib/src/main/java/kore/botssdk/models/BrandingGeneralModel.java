package kore.botssdk.models;

import java.io.Serializable;

public class BrandingGeneralModel implements Serializable {
    private String bot_icon;
    private String size;
    private String themeType;

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
