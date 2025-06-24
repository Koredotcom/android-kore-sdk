package kore.botssdk.models;

import java.io.Serializable;

import kore.botssdk.utils.StringUtils;

public class BrandingChatBubbleModel implements Serializable {
    private String style;
    private BrandingIconModel icon;
    private BrandingMinimizeModel minimise;
    private String sound;
    private String alignment;
    private String animation;
    private String expand_animation;
    private String primary_color;
    private String secondary_color;

    public BrandingChatBubbleModel updateWith(BrandingChatBubbleModel configModel){
        style = !StringUtils.isNullOrEmpty(configModel.style) ? configModel.style : style;
        icon = configModel.icon != null && icon != null ? icon.updateWith(configModel.icon) : icon;
        minimise = configModel.minimise != null && minimise != null ? minimise.updateWith(configModel.minimise) : minimise;
        sound = !StringUtils.isNullOrEmpty(configModel.sound) ? configModel.sound : sound;
        alignment = !StringUtils.isNullOrEmpty(configModel.alignment) ? configModel.alignment : alignment;
        animation = !StringUtils.isNullOrEmpty(configModel.animation) ? configModel.animation : animation;
        expand_animation = !StringUtils.isNullOrEmpty(configModel.expand_animation) ? configModel.expand_animation : expand_animation;
        primary_color = !StringUtils.isNullOrEmpty(configModel.primary_color) ? configModel.primary_color : primary_color;
        secondary_color = !StringUtils.isNullOrEmpty(configModel.secondary_color) ? configModel.secondary_color : secondary_color;
        return this;
    }

    public BrandingIconModel getIcon() {
        return icon;
    }

    public BrandingMinimizeModel getMinimise() {
        return minimise;
    }

    public String getAlignment() {
        return alignment;
    }

    public String getAnimation() {
        return animation;
    }

    public String getExpand_animation() {
        return expand_animation;
    }

    public String getPrimary_color() {
        return primary_color;
    }

    public String getSecondary_color() {
        return secondary_color;
    }

    public String getSound() {
        return sound;
    }

    public String getStyle() {
        return style;
    }

    public void setIcon(BrandingIconModel icon) {
        this.icon = icon;
    }

    public void setAlignment(String alignment) {
        this.alignment = alignment;
    }

    public void setAnimation(String animation) {
        this.animation = animation;
    }

    public void setExpand_animation(String expand_animation) {
        this.expand_animation = expand_animation;
    }

    public void setMinimise(BrandingMinimizeModel minimise) {
        this.minimise = minimise;
    }

    public void setPrimary_color(String primary_color) {
        this.primary_color = primary_color;
    }

    public void setSecondary_color(String secondary_color) {
        this.secondary_color = secondary_color;
    }

    public void setSound(String sound) {
        this.sound = sound;
    }

    public void setStyle(String style) {
        this.style = style;
    }
}
