package kore.botssdk.models;

public class BrandingQuickStartButtonActionModel {
    private String type;
    private String value;
    private String icon;
    private String title;

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getIcon() {
        return icon;
    }

    public String getType() {
        return type;
    }

    public String getValue() {
        return value;
    }
}
