package kore.botssdk.models;

public class BrandingStaticLinkLinksModel {
    private String title;
    private String description;
    private BrandingQuickStartButtonActionModel action;

    public BrandingQuickStartButtonActionModel getAction() {
        return action;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public void setAction(BrandingQuickStartButtonActionModel action) {
        this.action = action;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
