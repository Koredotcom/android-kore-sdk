package kore.botssdk.models;

import java.io.Serializable;

import kore.botssdk.utils.StringUtils;

public class BrandingHeaderModel implements Serializable {
    private String bg_color;
    private String size;
    private String style;
    private String icons_color;
    private String avatar_bg_color;
    private BrandingIconModel icon;
    private BrandingTitleModel title;
    private BrandingTitleModel sub_title;
    private BrandingHeaderButtonsModel buttons;

    public BrandingHeaderModel updateWith(BrandingHeaderModel configModel) {
        bg_color = !StringUtils.isNullOrEmpty(configModel.bg_color) ? configModel.bg_color : bg_color;
        size = !StringUtils.isNullOrEmpty(configModel.size) ? configModel.size : size;
        style = !StringUtils.isNullOrEmpty(configModel.style) ? configModel.style : style;
        icons_color = !StringUtils.isNullOrEmpty(configModel.icons_color) ? configModel.icons_color : icons_color;
        avatar_bg_color = !StringUtils.isNullOrEmpty(configModel.avatar_bg_color) ? configModel.avatar_bg_color : avatar_bg_color;
        icon = configModel.icon != null && icon != null ? icon.updateWith(configModel.icon) : icon;
        title = configModel.title != null && title != null ? title.updateWith(configModel.title) : title;
        sub_title = configModel.sub_title != null && sub_title != null ? sub_title.updateWith(configModel.sub_title) : sub_title;
        buttons = configModel.buttons != null && buttons != null ? buttons.updateWith(configModel.buttons) : buttons;
        return this;
    }

    public void setAvatar_bg_color(String avatar_bg_color) {
        this.avatar_bg_color = avatar_bg_color;
    }

    public String getAvatar_bg_color() {
        return avatar_bg_color;
    }

    public void setIcon(BrandingIconModel icon) {
        this.icon = icon;
    }

    public void setTitle(BrandingTitleModel title) {
        this.title = title;
    }

    public void setButtons(BrandingHeaderButtonsModel buttons) {
        this.buttons = buttons;
    }

    public void setStyle(String style) {
        this.style = style;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public void setBg_color(String bg_color) {
        this.bg_color = bg_color;
    }

    public void setSub_title(BrandingTitleModel sub_title) {
        this.sub_title = sub_title;
    }

    public BrandingIconModel getIcon() {
        return icon;
    }

    public String getIcons_color() {
        return icons_color;
    }

    public void setIcons_color(String icons_color) {
        this.icons_color = icons_color;
    }

    public String getStyle() {
        return style;
    }

    public String getSize() {
        return size;
    }

    public BrandingHeaderButtonsModel getButtons() {
        return buttons;
    }

    public BrandingTitleModel getSub_title() {
        return sub_title;
    }

    public BrandingTitleModel getTitle() {
        return title;
    }

    public String getBg_color() {
        return bg_color;
    }
}
