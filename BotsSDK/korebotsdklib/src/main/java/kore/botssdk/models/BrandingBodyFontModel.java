package kore.botssdk.models;

public class BrandingBodyFontModel {
    private String family;
    private String size;
    private String style;

    public void setStyle(String style) {
        this.style = style;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public void setFamily(String family) {
        this.family = family;
    }

    public String getStyle() {
        return style;
    }

    public String getSize() {
        return size;
    }

    public String getFamily() {
        return family;
    }
}
