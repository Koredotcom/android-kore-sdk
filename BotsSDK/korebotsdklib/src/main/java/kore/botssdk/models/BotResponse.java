package kore.botssdk.models;

import java.util.ArrayList;

/**
 * Created by Pradeep Mahato on 03-Jun-16.
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
    public static final String  TEMPLATE_TYPE_KORA_CAROUSAL = "kora_carousel";
    public static final String  TEMPLATE_TYPE_KORA_SEARCH_CAROUSAL = "kora_search_carousel";
    public static final String  TEMPLATE_TYPE_SLOT_PICKER = "pick_slot_template";
    public static final String  TEMPLATE_TYPE_MEETING_CONFIRM = "meeting_confirmation";
    public static final String TEMPLATE_TYPE_AUTO_FORMS = "auto_open_forms";
    public static final String TEMPLATE_TYPE_CAL_EVENTS = "calendar_events";
    public static final String TEMPLATE_TYPE_FILES_LOOKUP = "files_search_carousel";
    public static final String  TEMPLATE_TYPE_ATTENDEE_SLOTS = "attendee_slots_template";
    public static final String TEMPLATE_TYPE_PICKER = "picker";
    public static final String TEMPLATE_TYPE_SESSION_END = "session_end";
    public static final String TEMPLATE_TYPE_SHOW_PROGRESS = "show_progress";
    public static final String TEMPLATE_TYPE_AGENT_TRANSFER = "agent_transfer_mode";


    private String type;
    private BotInfoModel botInfo;
    private ArrayList<BotResponseMessage> message;
    private String icon;

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
