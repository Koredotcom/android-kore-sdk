package kore.botssdk.models;

import java.io.Serializable;

import kore.botssdk.utils.StringUtils;

public class BrandingBodyFontModel implements Serializable {
    private String family;
    private String size;
    private String style;

    public BrandingBodyFontModel updateWith(BrandingBodyFontModel configModel) {
        family = !StringUtils.isNullOrEmpty(configModel.family) ? configModel.family : family;
        size = !StringUtils.isNullOrEmpty(configModel.size) ? configModel.size : size;
        style = !StringUtils.isNullOrEmpty(configModel.style) ? configModel.style : style;
        return this;
    }

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
