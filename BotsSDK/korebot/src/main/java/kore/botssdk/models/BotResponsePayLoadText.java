package kore.botssdk.models;

import java.util.ArrayList;

@SuppressWarnings("UnKnownNullness")
public class BotResponsePayLoadText extends BaseBotMessage {
    public static final String BAR_CHART_DIRECTION_VERTICAL = "vertical";

    //Theme Properties
    public static final String THEME_NAME = "THEME_NAME";
    public static final String BUTTON_ACTIVE_BG_COLOR = "BUTTON_ACTIVE_BG_COLOR";
    public static final String BUTTON_ACTIVE_TXT_COLOR = "BUTTON_ACTIVE_TXT_COLOR";

    private String type;
    private ArrayList<BotResponseMessagePayloadText> message;
    private String icon;

    public String getMessageId() {
        return messageId;
    }

    private String messageId;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setMessage(ArrayList<BotResponseMessagePayloadText> message) {
        this.message = message;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public ArrayList<BotResponseMessagePayloadText> getMessage() {
        return message;
    }


    public String getIcon() {
        return icon;
    }

    @Override
    public boolean isSend() {
        return false;
    }

}
