package kore.botssdk.models;

public class BotBeneficiaryModel
{
    private String title;
    private String icon;
    private String description;

    public void setDescription(String description) {
        this.description = description;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public String getIcon() {
        return icon;
    }

    public String getTitle() {
        return title;
    }
}
