package kore.botssdk.models;

import java.io.Serializable;

@SuppressWarnings("UnKnownNullness")
public class BotBrandingModel implements Serializable {
    private BrandingGeneralModel general;
    private BrandingChatBubbleModel chat_bubble;
    private BrandingWelcomeModel welcome_screen;
    private BrandingHeaderModel header;
    private BrandingFooterModel footer;
    private BrandingBodyModel body;
    private BrandingWidgetPanelColorsModel widget_panel;
    private BrandingOverrideConfigModel override_kore_config;

    public BrandingWidgetPanelColorsModel getWidget_panel() {
        return widget_panel;
    }

    public BrandingOverrideConfigModel getOverride_kore_config() {
        return override_kore_config;
    }

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
