package kore.botssdk.models;

import androidx.annotation.NonNull;

import java.io.Serializable;

public class PromotionsModel implements Serializable {
    private String banner;
    private BrandingQuickStartButtonActionModel action;

    @NonNull
    public String getBanner() {
        return banner;
    }

    public void setBanner(@NonNull String banner) {
        this.banner = banner;
    }

    @NonNull
    public BrandingQuickStartButtonActionModel getAction() {
        return action;
    }

    public void setAction(@NonNull BrandingQuickStartButtonActionModel action) {
        this.action = action;
    }
}
