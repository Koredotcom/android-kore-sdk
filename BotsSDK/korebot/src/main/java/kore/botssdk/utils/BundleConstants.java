package kore.botssdk.utils;

/**
 * Copyright (c) 2014 Kore Inc. All rights reserved.
 */
public class BundleConstants {
    public static final String REQ_TEXT_TO_DISPLAY = "reqTextTodisplay";

    public static final String KNOWLEDGE_ID = "knowledgeId";
    public static final String TASK_ID = "task_id";
    public static final String IS_FROM_TASK_LIST = "is_from_task_list";
    public static final String SHOW_SHARE ="show_share" ;
    public static String CAROUSEL_ITEM = "carousel_item";
    public static String CAROUSEL_ITEM_POSITION = "carousel_item_position";
    public static String RESOURCE_ID = "resource_id";
    public static String BUTTON_TYPE_POSTBACK = "postback";
    public static String BUTTON_TYPE_TEXT = "text";
    public static String BUTTON_TYPE_POSTBACK_DISP_PAYLOAD = "postback_disp_payload";
    public static String BUTTON_TYPE_WEB_URL = "web_url";
    public static String BUTTON_TYPE_WEB_ONLY_URL = "url";
    public static String BUTTON_TYPE_HELP_RESOLVE = "help_resource";
    public static String BUTTON_TYPE_USER_INTENT = "USERINTENT";
    public static String FORM_TYPE_OPEN_FORM="open_form";
    public static final int TYPING_STATUS_TIME = 1500; //in millisecond
    public static final String SLOTS_LIST = "slots_list";
    public static final String QUICK_SLOTS = "quick_slots";
    public static final String DELAY_MESSAGES[] = { "I’m working on it…", "Still, working on it…",
            "This is taking a bit longer. Please wait…", "Things are slower than usual today. Please wait…",
            "I’m sorry. The server response seems really slow today. Please continue to wait...",
            "I’m sorry. I haven’t gotten a response from the server yet. Please continue to wait...",
            "I apologize. The server is has not responded yet. Please continue to wait…"};
    public static final String SESSION_END_ALERT_MESSAGES[] = {"We have noticed that chat is inactive for around a minute. The chat will be closed soon",
    "The chat is still inactive for around two minutes, it will be closed in about a minute","Your session is going to expire soon as you are inactive for last 3 min... "};
    public static final String TASK_INFO = "task_information";

    public static final String CONTACT_SUGGESTIONS = "contact_suggestion";
    public static final String QUESTION_ASKED = "question_asked";
    public static final String INFORMATION_EDITOR = "INFORMATION_EDITOR";
    public static final String ANNOUNCEMENT_EDITOR = "ANNOUNCEMENT_EDITOR";
    public static final String ARTICLE_OPTIONS = "article_options";
    public static final String MEETING_REQUEST = "MEETING_REQUEST";
    public static final String MEETING_INFO = "MEETING_INFO";
    public static final String INFO_SHARE = "INFO_SHARE";
    public static final String INFO_TEACH = "INFO_TRAIN";
    public static final String INFO_VIEW_DETAILS = "INFO_VIEW_DETAILS";
    public static final String EMAIL_REDIRECT = "EMAIL_REDIRECT";
    public static final String OPEN_DIALER = "OPEN_DIALER";
    public static final String OPEN_EMAIL = "OPEN_EMAIL";
    public static final String VIEW_CONTACT = "VIEW_CONTACT";
    public static final String VIEW_LOCATION = "VIEW_LOCATION";
    public static final String MEETING_CHANGE_SLOTS = "MEETING_CHANGE_SLOTS";
    public static final int TRANSPERANCY_50_PERCENT = 0xE6000000;
    public static final int REQ_CODE_REFRESH_CURRENT_PANEL = 5443;
}
