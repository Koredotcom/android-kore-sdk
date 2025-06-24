package kore.botssdk.models;

import java.io.Serializable;

import kore.botssdk.utils.StringUtils;

public class BrandingQuickStartButtonActionModel implements Serializable {
    private String type;
    private String value;
    private String icon;
    private String title;

    public BrandingQuickStartButtonActionModel updateWith(BrandingQuickStartButtonActionModel configModel) {
        type = !StringUtils.isNullOrEmpty(configModel.type) ? configModel.type : type;
        value = !StringUtils.isNullOrEmpty(configModel.value) ? configModel.value : value;
        icon = !StringUtils.isNullOrEmpty(configModel.icon) ? configModel.icon : icon;
        title = !StringUtils.isNullOrEmpty(configModel.title) ? configModel.title : title;
        return this;
    }

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
