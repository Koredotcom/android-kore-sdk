package kore.botssdk.models;

public class BrandingIconModel {
    private String icon_url;
    private String size;
    private String shape;
    private boolean show;
    private String color;
    private String icon;
    private BrandingQuickStartButtonActionModel action;

    public void setAction(BrandingQuickStartButtonActionModel action) {
        this.action = action;
    }

    public BrandingQuickStartButtonActionModel getAction() {
        return action;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getIcon() {
        return icon;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public boolean isShow() {
        return show;
    }

    public void setShow(boolean show) {
        this.show = show;
    }

    public String getSize() {
        return size;
    }

    public String getIcon_url() {
        return icon_url;
    }

    public String getShape() {
        return shape;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public void setIcon_url(String icon_url) {
        this.icon_url = icon_url;
    }

    public void setShape(String shape) {
        this.shape = shape;
    }
}
