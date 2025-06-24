package kore.botssdk.models;

import java.io.Serializable;

import kore.botssdk.utils.StringUtils;

public class BrandingStarterBoxModel implements Serializable {
    private Boolean show;
    private BrandingIconModel icon;
    private String title;
    private String sub_text;
    private BrandingTitleModel start_conv_button;
    private BrandingTitleModel start_conv_text;
    private BrandingQuickStartButtons quick_start_buttons;
    private BrandingStaticLinksModel links;

    public BrandingStarterBoxModel updateWith(BrandingStarterBoxModel configModel) {
        show = configModel.show != null ? configModel.show : show;
        title = !StringUtils.isNullOrEmpty(configModel.title) ? configModel.title : title;
        sub_text = !StringUtils.isNullOrEmpty(configModel.sub_text) ? configModel.sub_text : sub_text;
        icon = icon != null && configModel.icon != null ? icon.updateWith(configModel.icon) : icon;
        start_conv_button = start_conv_button != null && configModel.start_conv_button != null ? start_conv_button.updateWith(configModel.start_conv_button) : start_conv_button;
        start_conv_text = start_conv_text != null && configModel.start_conv_text != null ? start_conv_text.updateWith(configModel.start_conv_text) : start_conv_text;
        quick_start_buttons = quick_start_buttons != null && configModel.quick_start_buttons != null ? quick_start_buttons.updateWith(configModel.quick_start_buttons) : quick_start_buttons;
        links = links != null && configModel.links != null ? links.updateWith(configModel.links) : links;
        return this;
    }

    public BrandingIconModel getIcon() {
        return icon;
    }

    public String getTitle() {
        return title;
    }

    public BrandingQuickStartButtons getQuick_start_buttons() {
        return quick_start_buttons;
    }

    public BrandingStaticLinksModel getLinks() {
        return links;
    }

    public BrandingTitleModel getStart_conv_button() {
        return start_conv_button;
    }

    public BrandingTitleModel getStart_conv_text() {
        return start_conv_text;
    }

    public String getSub_text() {
        return sub_text;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setShow(boolean show) {
        this.show = show;
    }

    public void setIcon(BrandingIconModel icon) {
        this.icon = icon;
    }

    public void setLinks(BrandingStaticLinksModel links) {
        this.links = links;
    }

    public void setQuick_start_buttons(BrandingQuickStartButtons quick_start_buttons) {
        this.quick_start_buttons = quick_start_buttons;
    }

    public void setStart_conv_button(BrandingTitleModel start_conv_button) {
        this.start_conv_button = start_conv_button;
    }

    public void setStart_conv_text(BrandingTitleModel start_conv_text) {
        this.start_conv_text = start_conv_text;
    }

    public void setSub_text(String sub_text) {
        this.sub_text = sub_text;
    }

    public boolean isShow() {
        return show != null ? show : false;
    }
}
