package kore.botssdk.models;

import java.io.Serializable;

@SuppressWarnings("UnKnownNullness")
public class BrandingFooterButtonsModel implements Serializable {
    private BrandingFooterMenuButtonModel menu;
    private BrandingIconModel emoji;
    private BrandingIconModel microphone;
    private BrandingIconModel attachment;
    private BrandingIconModel speaker;

    public BrandingFooterButtonsModel updateWith(BrandingFooterButtonsModel configModel) {
        menu = menu != null && configModel.menu != null ? menu.updateWith(configModel.menu) : menu;
        emoji = emoji != null && configModel.emoji != null ? emoji.updateWith(configModel.emoji) : emoji;
        microphone = microphone != null && configModel.microphone != null ? microphone.updateWith(configModel.microphone) : microphone;
        attachment = attachment != null && configModel.attachment != null ? attachment.updateWith(configModel.attachment) : attachment;
        speaker = speaker != null && configModel.speaker != null ? speaker.updateWith(configModel.speaker) : speaker;
        return this;
    }

    public BrandingFooterMenuButtonModel getMenu() {
        return menu;
    }

    public BrandingIconModel getAttachment() {
        return attachment;
    }

    public BrandingIconModel getSpeaker() {
        return speaker;
    }

    public BrandingIconModel getEmoji() {
        return emoji;
    }

    public BrandingIconModel getMicrophone() {
        return microphone;
    }
}
