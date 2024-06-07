package kore.botssdk.models;

import java.util.ArrayList;

/**
 * Copyright (c) 2014 Kore Inc. All rights reserved.
 */

public class BotResponse extends BaseBotMessage {

    public static final String TEMPLATE_TYPE_BUTTON = "button";
    public static final String TEMPLATE_TYPE_LIST = "list";
    public static final String TEMPLATE_TYPE_PIECHART = "piechart";
    public static final String TEMPLATE_TYPE_LINECHART = "linechart";
    public static final String TEMPLATE_TYPE_BARCHART = "barchart";
    public static final String TEMPLATE_TYPE_TABLE = "table";
    public static final String TEMPLATE_TYPE_MINITABLE = "mini_table";
    public static final String TEMPLATE_TYPE_CAROUSEL = "carousel";
    public static final String TEMPLATE_TYPE_CAROUSEL_ADV = "carouselAdv";
    public static final String TEMPLATE_TYPE_QUICK_REPLIES = "quick_replies";
    public static final String TEMPLATE_TYPE_FORM_ACTIONS = "form_actions";
    public static final String COMPONENT_TYPE_TEMPLATE = "template";
    public static final String COMPONENT_TYPE_TEXT = "text";
    public static final String COMPONENT_TYPE_ERROR = "error";
    public static final String COMPONENT_TYPE_MESSAGE = "message";
    public static final String COMPONENT_TYPE_IMAGE = "image";
    public static final String COMPONENT_TYPE_LINK = "link";
    public static final String COMPONENT_TYPE_AUDIO = "audio";
    public static final String COMPONENT_TYPE_VIDEO = "video";
    public static final String  TEMPLATE_TYPE_KORA_CAROUSAL = "kora_carousel";
    public static final String  TEMPLATE_TYPE_KORA_ANNOUNCEMENT_CAROUSAL = "announcement_carousel";
    public static final String  TEMPLATE_TYPE_KORA_SEARCH_CAROUSAL = "kora_search_carousel";
    public static final String  TEMPLATE_TYPE_SLOT_PICKER = "pick_slot_template";
    public static final String  TEMPLATE_TYPE_MEETING_CONFIRM = "meeting_confirmation";
    public static final String TEMPLATE_TYPE_AUTO_FORMS = "auto_open_forms";
    public static final String TEMPLATE_TYPE_CAL_EVENTS = "calendar_events";
    public static final String TEMPLATE_TYPE_FILES_LOOKUP = "files_search_carousel";
    public static final String  TEMPLATE_TYPE_ATTENDEE_SLOTS = "attendee_slots_template";
    public static final String VIEW_STAR = "star";
    static final String TEMPLATE_TYPE_PICKER = "picker";
    public static final String TEMPLATE_TYPE_SESSION_END = "session_end";
    public static final String TEMPLATE_TYPE_SHOW_PROGRESS = "show_progress";
    public static final String TEMPLATE_TYPE_WELCOME_CAROUSEL = "kora_welcome_carousel";
    public static final String TEMPLATE_TYPE_CANCEL_EVENT = "cancel_calendar_events";
    public static final String  TEMPLATE_TYPE_TASK_VIEW = "task_list_preview";
    public static final String TEMPLATE_TYPE_CONVERSATION_END = "conversation_end";
    public static final String TEMPLATE_TASK_FULLVIEW = "task_list_full_preview";
    public static final String KA_SWITCH_SKILL = "ka_switch_skill";
    public static final String KA_CONTACT_VIEW = "kora_contact_lookup";
    public static final String WELCOME_SUMMARY_VIEW = "kora_welcome_summary";
    public static final String TEMPLATE_TYPE_HIDDEN_DIALOG = "hidden_dialog";
    public static final String KORA_SUMMARY_HELP_VIEW = "kora_summary_help";
    public static final String NARRATOR_TEXT = "narrator";
    public static final String TEMPLATE_TYPE_MULTI_SELECT = "multi_select";
    public static final String BAR_CHART_DIRECTION_VERTICAL = "vertical";

    //Added new
    public static final String TABLE_VIEW_RESPONSIVE = "responsive";
    public static final String TEMPLATE_TYPE_FORM = "form_template";
    public static final String TEMPLATE_TYPE_LIST_VIEW  = "listView";
    public static final String TEMPLATE_TYPE_DATE = "dateTemplate";
    public static final String TEMPLATE_TYPE_DATE_RANGE = "daterange";
    public static final String TEMPLATE_TYPE_TABLE_LIST = "tableList";
    public static final String TEMPLATE_TYPE_WELCOME_QUICK_REPLIES = "quick_replies_welcome";
    public static final String TEMPLATE_TYPE_NOTIFICATIONS = "Notification";
    public static final String TEMPLATE_TYPE_FEEDBACK = "feedbackTemplate";
    public static final String TEMPLATE_TYPE_LIST_WIDGET = "List_widget";
    public static final String TEMPLATE_TYPE_LIST_WIDGET_2 = "listWidget";
    public static final String TEMPLATE_TYPE_UNIVERSAL_SEARCH = "kora_universal_search";
    public static final String WELCOME_SUMMARY_VIEW_NOTIFICAION = "welcome_summary_view_notification";
    public static final String TEMPLATE_DROPDOWN = "dropdown_template";
    public static final String TEMPLATE_BANKING_FEEDBACK = "bankingFeedbackTemplate";
    public static final String CONTACT_CARD_TEMPLATE = "contactCardTemplate";
    public static final String CUSTOM_TABLE_TEMPLATE = "custom_table";
    public static final String ADVANCED_LIST_TEMPLATE = "advancedListTemplate";
    public static final String TEMPLATE_TYPE_RESULTS_LIST = "search";
    public static final String TEMPLATE_PDF_DOWNLOAD = "pdfdownload";
    public static final String TEMPLATE_BUTTON_LINK = "buttonLinkTemplate";
    public static final String TEMPLATE_BENEFICIARY = "beneficiaryTemplate";
    public static final String CARD_TEMPLATE = "cardTemplate";

    //widgets
    public static final String TEMPLATE_TYPE_CAL_EVENTS_WIDGET = "calendar_events_widget";
    public static final String TAKE_NOTES = "open_form";
    public static final String MESSAGE_TYPE_OUTGOING = "outgoing";
    public static final String US_MEETING_NOTES_TYPE="MeetingNotes";
    public static final String US_EMAIL_TYPE="Email";
    public static final String US_KNOWLEDGE_TYPE="Article";
    public static final String US_FILES_TYPE="Files";
    public static final String US_KNOWLEDGE_COLLECTION_TYPE="KnowledgeCollection";

    //Theme Properties
    public static final String THEME_NAME = "THEME_NAME";
    public static final String BUBBLE_LEFT_BG_COLOR = "BUBBLE_LEFT_BG_COLOR";
    public static final String BUBBLE_RIGHT_BG_COLOR = "BUBBLE_RIGHT_BG_COLOR";
    public static final String BUBBLE_LEFT_TEXT_COLOR = "BUBBLE_LEFT_TEXT_COLOR";
    public static final String BUBBLE_RIGHT_TEXT_COLOR = "BUBBLE_RIGHT_TEXT_COLOR";
    public static final String BUTTON_ACTIVE_BG_COLOR = "BUTTON_ACTIVE_BG_COLOR";
    public static final String BUTTON_ACTIVE_TXT_COLOR = "BUTTON_ACTIVE_TXT_COLOR";
    public static final String BUTTON_INACTIVE_BG_COLOR = "BUTTON_INACTIVE_BG_COLOR";
    public static final String BUTTON_INACTIVE_TXT_COLOR = "BUTTON_INACTIVE_TXT_COLOR";
    public static final String WIDGET_BG_COLOR = "WIDGET_BG_COLOR";
    public static final String WIDGET_TXT_COLOR = "WIDGET_TXT_COLOR";
    public static final String WIDGET_BORDER_COLOR = "WIDGET_BORDER_COLOR";
    public static final String BUTTON_BORDER_COLOR = "BUTTON_BORDER_COLOR";
    public static final String WIDGET_FOOTER_HINT_COLOR = "WIDGET_FOOTER_HINT_COLOR";
    public static final String WIDGET_DIVIDER_COLOR = "WIDGET_DIVIDER_COLOR";
    public static final String APPLY_THEME_NAME = "APPLY_THEME_NAME";
    public static final String THEME_NAME_1 = "THEME_NAME_1";
    public static final String THEME_NAME_2 = "THEME_NAME_2";

    private String type;
    private BotInfoModel botInfo;
    private ArrayList<BotResponseMessage> message;
    private String icon;
    private String timestamp;
    private String key;

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

    /**
     * returns null if there are no messages
     * @return
     */
    public BotResponseMessage getTempMessage() {
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
