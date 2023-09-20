package kore.botssdk.models;

import androidx.annotation.NonNull;

import java.io.Serializable;

public class BotBrandingModel implements Serializable {
    private BrandingGeneralModel general;
    private BrandingChatBubbleModel chat_bubble;
    private BrandingWelcomeModel welcome_screen;
    private BrandingHeaderModel header;
    private BrandingFooterModel footer;
    private BrandingBodyModel body;

    public BrandingBodyModel getBody() {
        return body;
    }

    public BrandingFooterModel getFooter() {
        return footer;
    }

    public BrandingHeaderModel getHeader() {
        return header;
    }

    public BrandingWelcomeModel getWelcome_screen() {
        return welcome_screen;
    }

    public BrandingGeneralModel getGeneral() {
        return general;
    }
    public BrandingChatBubbleModel getChat_bubble() {
        return chat_bubble;
    }
}
