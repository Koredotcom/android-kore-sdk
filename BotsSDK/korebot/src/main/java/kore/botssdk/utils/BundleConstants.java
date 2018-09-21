package kore.botssdk.utils;

/**
 * Created by Pradeep Mahato on 14/7/17.
 * Copyright (c) 2014 Kore Inc. All rights reserved.
 */
public class BundleConstants {

    public static String CAROUSEL_ITEM_IMAGE_URL;
    public static String CAROUSEL_ITEM_TITLE;
    public static String CAROUSEL_ITEM_SUBTITLE;
    public static String CAROUSEL_ITEM_DEFAULT_ACTION_TYPE;
    public static String CAROUSEL_ITEM_DEFAULT_ACTION_URL;
    public static String CAROUSEL_ITEM_BUTTON;

    public static String CAROUSEL_ITEM = "carousel_item";
    public static String CAROUSEL_ITEM_POSITION = "carousel_item_position";

    public static String BUTTON_TYPE_POSTBACK = "postback";
    public static String BUTTON_TYPE_TEXT = "text";
    public static String BUTTON_TYPE_POSTBACK_DISP_PAYLOAD = "postback_disp_payload";
    public static String BUTTON_TYPE_WEB_URL = "web_url";
    public static String BUTTON_TYPE_USER_INTENT = "USERINTENT";
    public static String FORM_TYPE_OPEN_FORM="open_form";
    public static String FORM_TYPE_SHOW_FORM="show_form";
    public final static String SHOW_PIC = "showPic";
    public static final int TYPING_STATUS_TIME = 1500; //in millisecond
    public static final String SLOTS_LIST = "slots_list";
    public static final String QUICK_SLOTS = "quick_slots";
    public static final String DELAY_MESSAGES[] = { "I’m working on it…", "Still, working on it…",
            "This is taking a bit longer. Please wait…", "Things are slower than usual today. Please wait…",
            "I’m sorry. The server response seems really slow today. Please continue to wait...",
            "I’m sorry. I haven’t gotten a response from the server yet. Please continue to wait...",
            "I apologize. The server is has not responded yet. Please continue to wait…"};
    public static final String SESSION_END_ALERT_MESSAGES[] = {"Your session will expire very soon as you are inactive more than 100 sec...",
    "Your session is expiring now...","Your session is going to expire soon as you are inactive for last one min... "};

}
