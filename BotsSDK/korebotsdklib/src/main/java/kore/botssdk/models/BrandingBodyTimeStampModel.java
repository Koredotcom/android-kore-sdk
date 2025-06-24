package kore.botssdk.models;

import java.io.Serializable;

import kore.botssdk.utils.StringUtils;

public class BrandingBodyTimeStampModel implements Serializable {
    private Boolean show;
    private String show_type;
    private String position;
    private String separator;
    private String color;

    public BrandingBodyTimeStampModel updateWith(BrandingBodyTimeStampModel configModel) {
        show = configModel.show != null ? configModel.show : show;
        show_type = !StringUtils.isNullOrEmpty(configModel.show_type) ? configModel.show_type : show_type;
        position = !StringUtils.isNullOrEmpty(configModel.position) ? configModel.position : position;
        separator = !StringUtils.isNullOrEmpty(configModel.separator) ? configModel.separator : separator;
        color = !StringUtils.isNullOrEmpty(configModel.color) ? configModel.color : color;
        return this;
    }

    public boolean isShow() {
        return show != null ? show : false;
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
