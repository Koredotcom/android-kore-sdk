package kore.botssdk.models;

import android.annotation.SuppressLint;
@SuppressLint("UnknownNullness")
public class BotActiveThemeModel
{
    String _id;
    String streamId;
    boolean activeTheme;
    String createdBy;
    String createdOn;
    boolean defaultTheme;
    String lastModifiedBy;
    String lastModifiedOn;
    String refId;
    String state;
    String themeName;
    BotBrandingModel v3;

    public BotBrandingModel getV3() {
        return v3;
    }

    public void setV3(BotBrandingModel v3) {
        this.v3 = v3;
    }

    public String get_id() {
        return _id;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public String getCreatedOn() {
        return createdOn;
    }

    public String getLastModifiedBy() {
        return lastModifiedBy;
    }

    public String getLastModifiedOn() {
        return lastModifiedOn;
    }

    public String getRefId() {
        return refId;
    }

    public String getState() {
        return state;
    }

    public String getStreamId() {
        return streamId;
    }

    public String getThemeName() {
        return themeName;
    }

    public boolean isActiveTheme() {
        return activeTheme;
    }

    public boolean isDefaultTheme() {
        return defaultTheme;
    }
}
