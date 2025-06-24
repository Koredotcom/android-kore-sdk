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

    public BotBrandingModel updateWith(BotBrandingModel configModel) {
        general = general != null && configModel.general != null ? general.updateWith(configModel.general) : general;
        chat_bubble = chat_bubble != null && configModel.chat_bubble != null ? chat_bubble.updateWith(configModel.chat_bubble) : chat_bubble;
        welcome_screen = welcome_screen != null && configModel.welcome_screen != null ? welcome_screen.updateWith(configModel.welcome_screen) : welcome_screen;
        header = header != null && configModel.header != null ? header.updateWith(configModel.header) : header;
        footer = footer != null && configModel.footer != null ? footer.updateWith(configModel.footer) : footer;
        body = body != null && configModel.body != null ? body.updateWith(configModel.body) : body;
        widget_panel = widget_panel != null && configModel.widget_panel != null ? widget_panel.updateWith(configModel.widget_panel) : widget_panel;
        override_kore_config = override_kore_config != null && configModel.override_kore_config != null ? override_kore_config.updateWith(configModel.override_kore_config) : override_kore_config;
        return this;
    }

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
