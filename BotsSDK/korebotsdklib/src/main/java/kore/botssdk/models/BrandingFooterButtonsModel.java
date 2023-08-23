package kore.botssdk.models;

public class BrandingFooterButtonsModel {
    private BrandingFooterMenuButtonModel menu;
    private BrandingIconModel emoji;
    private BrandingIconModel microphone;
    private BrandingIconModel attachment;

    public BrandingFooterMenuButtonModel getMenu() {
        return menu;
    }

    public BrandingIconModel getAttachment() {
        return attachment;
    }

    public BrandingIconModel getEmoji() {
        return emoji;
    }

    public BrandingIconModel getMicrophone() {
        return microphone;
    }

    public void setAttachment(BrandingIconModel attachment) {
        this.attachment = attachment;
    }

    public void setEmoji(BrandingIconModel emoji) {
        this.emoji = emoji;
    }

    public void setMenu(BrandingFooterMenuButtonModel menu) {
        this.menu = menu;
    }

    public void setMicrophone(BrandingIconModel microphone) {
        this.microphone = microphone;
    }
}
