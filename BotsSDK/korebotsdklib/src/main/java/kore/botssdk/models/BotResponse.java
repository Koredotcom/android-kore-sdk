package kore.botssdk.models;

import java.util.ArrayList;
import java.util.Map;

/**
 * Copyright (c) 2014 Kore Inc. All rights reserved.
 */
@SuppressWarnings("UnKnownNullness")
public class BotResponse extends BaseBotMessage {
    public static final String BUBBLE_STYLE = "BUBBLE_STYLE";
    public static final String TEMPLATE_TYPE_BUTTON = "button";
    public static final String TEMPLATE_TYPE_LIST = "list";
    public static final String TEMPLATE_TYPE_PIE_CHART = "piechart";
    public static final String TEMPLATE_TYPE_LINE_CHART = "linechart";
    public static final String TEMPLATE_TYPE_BARCHART = "barchart";
    public static final String TEMPLATE_TYPE_TABLE = "table";
    public static final String TEMPLATE_TYPE_MINI_TABLE = "mini_table";
    public static final String TEMPLATE_TYPE_CAROUSEL = "carousel";
    public static final String TEMPLATE_TYPE_CAROUSEL_ADV = "carouselAdv";
    public static final String TEMPLATE_TYPE_QUICK_REPLIES = "quick_replies";
    public static final String TEMPLATE_TYPE_CLOCK = "clockTemplate";
    public static final String TEMPLATE_TYPE_RADIO_OPTIONS = "radioOptionTemplate";
    public static final String COMPONENT_TYPE_TEMPLATE = "template";
    public static final String COMPONENT_TYPE_TEXT = "text";
    public static final String COMPONENT_TYPE_ERROR = "error";
    public static final String COMPONENT_TYPE_MESSAGE = "message";
    public static final String COMPONENT_TYPE_IMAGE = "image";
    public static final String COMPONENT_TYPE_AUDIO = "audio";
    public static final String COMPONENT_TYPE_VIDEO = "video";
    public static final String VIEW_STAR = "star";
    public static final String VIEW_NPS = "NPS";
    public static final String VIEW_CSAT = "CSAT";
    public static final String VIEW_THUMBS_UP_DOWN = "ThumbsUpDown";
    static final String TEMPLATE_TYPE_PICKER = "picker";
    public static final String TEMPLATE_TYPE_WELCOME_CAROUSEL = "kora_welcome_carousel";
    public static final String NARRATOR_TEXT = "narrator";
    public static final String TEMPLATE_TYPE_MULTI_SELECT = "multi_select";

    //Added new
    public static final String CAROUSEL_STACKED = "stacked";
    public static final String TEMPLATE_TYPE_FORM = "form_template";
    public static final String TEMPLATE_TYPE_LIST_VIEW = "listView";
    public static final String TEMPLATE_TYPE_DATE = "dateTemplate";
    public static final String TEMPLATE_TYPE_DATE_RANGE = "daterange";
    public static final String TEMPLATE_TYPE_TABLE_LIST = "tableList";
    public static final String TEMPLATE_TYPE_WELCOME_QUICK_REPLIES = "quick_replies_welcome";
    public static final String TEMPLATE_TYPE_NOTIFICATIONS = "Notification";
    public static final String TEMPLATE_TYPE_FEEDBACK = "feedbackTemplate";
    public static final String TEMPLATE_TYPE_LIST_WIDGET = "List_widget";
    public static final String TEMPLATE_TYPE_LIST_WIDGET_2 = "listWidget";
    public static final String TEMPLATE_TYPE_UNIVERSAL_SEARCH = "kora_universal_search";
    public static final String TEMPLATE_DROPDOWN = "dropdown_template";
    public static final String TEMPLATE_BANKING_FEEDBACK = "bankingFeedbackTemplate";
    public static final String CONTACT_CARD_TEMPLATE = "contactCardTemplate";
    public static final String CUSTOM_TABLE_TEMPLATE = "custom_table";
    public static final String ADVANCED_LIST_TEMPLATE = "advancedListTemplate";
    public static final String TEMPLATE_TYPE_RESULTS_LIST = "search";
    public static final String TEMPLATE_PDF_DOWNLOAD = "pdfdownload";
    public static final String TEMPLATE_BENEFICIARY = "beneficiaryTemplate";
    public static final String CARD_TEMPLATE = "cardTemplate";
    public static final String ADVANCED_MULTI_SELECT_TEMPLATE = "advanced_multi_select";
    public static final String TEMPLATE_TYPE_ARTICLE = "articleTemplate";
    public static final String TEMPLATE_TYPE_OTP_VALIDATION = "otpValidationTemplate";
    public static final String TEMPLATE_TYPE_RESET_PIN = "resetPinTemplate";
    public static final String TEMPLATE_TYPE_ANSWER = "answerTemplate";

    public static final String AGENT_INFO_KEY = "agentInfo";
    public static final String EVENT = "EVENT";
    public static final String HISTORY_COUNT = "HISTORY_COUNT";
    public static final String LIVE_AGENT = "live_agent";
    public static final String SELECTED_TIME = "selected_time";
    public static final String SELECTED_FEEDBACK = "selectedFeedback";
    public static final String SELECTED_ITEM = "selected_item";
    public static final String STACKED = "stacked";
    public static final String TABLE_VIEW_RESPONSIVE = "responsive";
    public static final String IS_VIEW_MORE = "is_view_more";
    public static String DATE_FORMAT = "dd/MM/yyyy";
    public static int TIME_FORMAT = 24;

    //widgets
    public static final String TEMPLATE_TYPE_CAL_EVENTS_WIDGET = "calendar_events_widget";
    public static final String TAKE_NOTES = "open_form";
    public static final String MESSAGE_TYPE_OUTGOING = "outgoing";

    //Theme Properties
    public static final String THEME_NAME = "THEME_NAME";
    public static final String BUBBLE_LEFT_BG_COLOR = "BUBBLE_LEFT_BG_COLOR";
    public static final String BUBBLE_RIGHT_BG_COLOR = "BUBBLE_RIGHT_BG_COLOR";
    public static final String BUBBLE_LEFT_TEXT_COLOR = "BUBBLE_LEFT_TEXT_COLOR";
    public static final String BUBBLE_RIGHT_TEXT_COLOR = "BUBBLE_RIGHT_TEXT_COLOR";
    public static final String BUTTON_ACTIVE_BG_COLOR = "BUTTON_ACTIVE_BG_COLOR";
    public static final String TIME_STAMP_TXT_COLOR = "TIME_STAMP_TXT_COLOR";

    public static final String BUTTON_ACTIVE_TXT_COLOR = "BUTTON_ACTIVE_TXT_COLOR";
    public static final String BUTTON_INACTIVE_BG_COLOR = "BUTTON_INACTIVE_BG_COLOR";
    public static final String BUTTON_INACTIVE_TXT_COLOR = "BUTTON_INACTIVE_TXT_COLOR";
    public static final String WIDGET_BG_COLOR = "WIDGET_BG_COLOR";
    public static final String WIDGET_TXT_COLOR = "WIDGET_TXT_COLOR";
    public static final String WIDGET_BORDER_COLOR = "WIDGET_BORDER_COLOR";
    public static final String ICON_1 = "icon-1";
    public static final String ICON_2 = "icon-2";
    public static final String ICON_3 = "icon-3";
    public static final String ICON_4 = "icon-4";
    public static final String HEADER_SIZE_COMPACT = "compact";
    public static final String HEADER_SIZE_LARGE = "large";
    public static final String PLAIN = "plain";
    public static final String BACKGROUND_INVERTED = "backgroundInverted";
    public static final String STACKED_BUTTONS = "stackedButtons";
    public static final String TEXT_INVERTED = "textInverted";

    private String type;
    private BotInfoModel botInfo;
    private ArrayList<BotResponseMessage> message;
    private String icon;
    private String timestamp;
    private String key;
    private boolean fromAgent = false;
    private Map<String, Object> contentState;

    public void setFromAgent(boolean fromAgent) {
        this.fromAgent = fromAgent;
    }

    public boolean isFromAgent() {
        return fromAgent;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public String getKey() {
        return key;
    }

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

    public void setMessage(ArrayList<BotResponseMessage> message) {
        this.message = message;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public ArrayList<BotResponseMessage> getMessage() {
        return message;
    }

    public String getIcon() {
        return icon;
    }

    @Override
    public boolean isSend() {
        return false;
    }

    public Map<String, Object> getContentState() {
        return contentState;
    }

    public void setContentState(Map<String, Object> contentState) {
        this.contentState = contentState;
    }
}
