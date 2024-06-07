package kore.botssdk.models;

import android.annotation.SuppressLint;

@SuppressLint("UnknownNullness")
public class BotActiveThemeModel
{
    String _id;
    String streamId;
    boolean activeTheme;
    String createdBy;
    String createdOn;
    boolean defaultTheme;
    String lastModifiedBy;
    String lastModifiedOn;
    String refId;
    String state;
    String themeName;
    BrandingBotMessagesModel botMessage;
    BrandingBotMessagesModel userMessage;
    BrandingButtonsModel buttons;
    BrandingGeneralAttributesModel generalAttributes;
    BrandingWidgetBodyModel widgetBody;
    BrandingWidgetBodyModel widgetFooter;
    BrandingWidgetBodyModel widgetHeader;

    public BrandingWidgetBodyModel getWidgetFooter() {
        return widgetFooter;
    }

    public BrandingWidgetBodyModel getWidgetHeader() {
        return widgetHeader;
    }

    public BrandingWidgetBodyModel getWidgetBody() {
        return widgetBody;
    }

    public BrandingBotMessagesModel getUserMessage() {
        return userMessage;
    }

    public BrandingGeneralAttributesModel getGeneralAttributes() {
        return generalAttributes;
    }

    public BrandingButtonsModel getButtons() {
        return buttons;
    }

    public BrandingBotMessagesModel getBotMessage() {
        return botMessage;
    }

    public String get_id() {
        return _id;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public String getCreatedOn() {
        return createdOn;
    }

    public String getLastModifiedBy() {
        return lastModifiedBy;
    }

    public String getLastModifiedOn() {
        return lastModifiedOn;
    }

    public String getRefId() {
        return refId;
    }

    public String getState() {
        return state;
    }

    public String getStreamId() {
        return streamId;
    }

    public String getThemeName() {
        return themeName;
    }

    public boolean isActiveTheme() {
        return activeTheme;
    }

    public boolean isDefaultTheme() {
        return defaultTheme;
    }
}
