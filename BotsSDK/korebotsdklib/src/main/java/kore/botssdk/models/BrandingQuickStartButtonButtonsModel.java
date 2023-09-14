package kore.botssdk.models;

import androidx.annotation.NonNull;

import java.io.Serializable;

public class BrandingQuickStartButtonButtonsModel implements Serializable {
    private String title;
    private String description;

    @NonNull
    public String getDescription() {
        return description;
    }

    public void setDescription(@NonNull String description) {
        this.description = description;
    }

    private BrandingQuickStartButtonActionModel action;

    @NonNull
    public String getTitle() {
        return title;
    }

    @NonNull
    public BrandingQuickStartButtonActionModel getAction() {
        return action;
    }

    public void setTitle(@NonNull String title) {
        this.title = title;
    }

    public void setAction(@NonNull BrandingQuickStartButtonActionModel action) {
        this.action = action;
    }
}
