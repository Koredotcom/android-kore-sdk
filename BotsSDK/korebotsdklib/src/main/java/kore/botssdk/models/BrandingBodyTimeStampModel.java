package kore.botssdk.models;

public class BrandingBodyTimeStampModel {
    private boolean show;
    private String show_type;
    private String position;
    private String separator;
    private String color;

    public boolean isShow() {
        return show;
    }

    public String getSeparator() {
        return separator;
    }

    public void setSeparator(String separator) {
        this.separator = separator;
    }

    public String getColor() {
        return color;
    }

    public String getPosition() {
        return position;
    }

    public String getShow_type() {
        return show_type;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public void setShow(boolean show) {
        this.show = show;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public void setShow_type(String show_type) {
        this.show_type = show_type;
    }
}
