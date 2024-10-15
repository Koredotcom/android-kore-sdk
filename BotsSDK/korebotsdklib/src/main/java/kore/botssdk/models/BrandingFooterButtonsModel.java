package kore.botssdk.models;

import java.io.Serializable;

@SuppressWarnings("UnKnownNullness")
public class BrandingFooterButtonsModel implements Serializable {
    private BrandingFooterMenuButtonModel menu;
    private BrandingIconModel emoji;
    private BrandingIconModel microphone;
    private BrandingIconModel attachment;
    private BrandingIconModel speaker;

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
