package kore.botssdk.models;

public class BotBrandingModel {
    private BrandingGeneralModel general;
    private BrandingChatBubbleModel chat_bubble;
    private BrandingWelcomeModel welcome_screen;
    private BrandingHeaderModel header;
    private BrandingFooterModel footer;
    private BrandingBodyModel body;

    public BrandingBodyModel getBody() {
        return body;
    }

    public void setBody(BrandingBodyModel body) {
        this.body = body;
    }

    public void setChat_bubble(BrandingChatBubbleModel chat_bubble) {
        this.chat_bubble = chat_bubble;
    }

    public void setFooter(BrandingFooterModel footer) {
        this.footer = footer;
    }

    public void setGeneral(BrandingGeneralModel general) {
        this.general = general;
    }

    public void setHeader(BrandingHeaderModel header) {
        this.header = header;
    }

    public void setWelcome_screen(BrandingWelcomeModel welcome_screen) {
        this.welcome_screen = welcome_screen;
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
