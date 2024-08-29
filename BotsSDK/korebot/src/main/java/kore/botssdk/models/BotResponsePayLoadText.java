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
    private BotInfoModel botInfo;
    private ArrayList<BotResponseMessagePayloadText> message;
    private String icon;

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    private String messageId;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setBotInfo(BotInfoModel botInfo) {
        this.botInfo = botInfo;
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

    /*
     * returns null if there are no messages
     */
    public BotResponseMessagePayloadText getTempMessage() {
        return message!=null && message.size() > 0?message.get(0):null;
    }

    public BotInfoModel getBotInfo() {
        return botInfo;
    }

    public String getIcon() {
        return icon;
    }

    @Override
    public boolean isSend() {
        return false;
    }

}
