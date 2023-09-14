package kore.botssdk.models;

import java.io.Serializable;

public class BrandingBodyFontModel implements Serializable {
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
