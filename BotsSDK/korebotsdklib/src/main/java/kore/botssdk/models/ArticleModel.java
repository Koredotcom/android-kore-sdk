package kore.botssdk.models;

public class ArticleModel {
    private String icon;
    private String title;
    private String description;
    private String createdOn;
    private String updatedOn;
    private ArticleButtonModel button;

    public String getIcon() {
        return icon;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getCreatedOn() {
        return createdOn;
    }

    public String getUpdatedOn() {
        return updatedOn;
    }

    public ArticleButtonModel getButton() {
        return button;
    }
}
