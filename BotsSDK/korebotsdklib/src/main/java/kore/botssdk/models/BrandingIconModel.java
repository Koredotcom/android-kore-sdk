package kore.botssdk.models;

import java.io.Serializable;

import kore.botssdk.utils.StringUtils;

public class BrandingIconModel implements Serializable {
    private String icon_url;
    private String size;
    private String shape;
    private Boolean show;
    private String color;
    private String icon;
    private String type;
    private BrandingQuickStartButtonActionModel action;

    public BrandingIconModel updateWith(BrandingIconModel configModel) {
        icon_url = !StringUtils.isNullOrEmpty(configModel.icon_url) ? configModel.icon_url : icon_url;
        size = !StringUtils.isNullOrEmpty(configModel.size) ? configModel.size : size;
        shape = !StringUtils.isNullOrEmpty(configModel.shape) ? configModel.shape : shape;
        show = configModel.show != null ? configModel.show : show;
        color = !StringUtils.isNullOrEmpty(configModel.color) ? configModel.color : color;
        icon = !StringUtils.isNullOrEmpty(configModel.icon) ? configModel.icon : icon;
        type = !StringUtils.isNullOrEmpty(configModel.type) ? configModel.type : type;
        action = configModel.action != null && action != null ? action.updateWith(configModel.action) : action;
        return this;
    }

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
        return show != null ? show : false;
    }

    public void setShow(boolean show) {
        this.show = show;
    }

    public String getSize() {
        return size;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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
