package kore.botssdk.utils;

/**
 * Copyright (c) 2014 Kore Inc. All rights reserved.
 */
public class BundleConstants {
    public static final String REQ_TEXT_TO_DISPLAY = "reqTextTodisplay";

    public static final String KNOWLEDGE_ID = "knowledgeId";
    public static final String SHOW_SHARE ="show_share" ;
    public static final String FAQ = "faq";
    public static final String DEFAULT = "default";
    public static final String OPTIONS = "options";
    public static final String TABLE = "table";
    public static final String RIGHT = "right";
    public static final String LEFT = "left";
    public static final String BOLD = "bold";
    public static final String FULL_WIDTH = "fullwidth";
    public static final String CAROUSEL_ITEM = "carousel_item";
    public static final String CAROUSEL_ITEM_POSITION = "carousel_item_position";
    public static final String RESOURCE_ID = "resource_id";
    public static final String BUTTON_TYPE_POSTBACK = "postback";
    public static final String BUTTON_TYPE_TEXT = "text";
    public static final String BUTTON_TYPE_POSTBACK_DISP_PAYLOAD = "postback_disp_payload";
    public static final String BUTTON_TYPE_WEB_URL = "web_url";
    public static final String BUTTON_TYPE_URL = "url";
    public static final String BUTTON_TYPE_CONFIRM = "Confirm";
    public static final String BUTTON_TYPE_HELP_RESOLVE = "help_resource";
    public static final String BUTTON_TYPE_USER_INTENT = "USERINTENT";
    public static String FORM_TYPE_OPEN_FORM="open_form";
    public static String FORM_TYPE_SHOW_FORM="show_form";
    public final static String SHOW_PIC = "showPic";
    public static final String ICON = "icon";
    public static final String BUTTON = "button";
    public static final String DROP_DOWN = "dropdown";
    public static final String DROP_DOWN_ICON = "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAAwAAAAHCAYAAAA8sqwkAAAACXBIWXMAAAsTAAALEwEAmpwYAAAAAXNSR0IArs4c6QAAAARnQU1BAACxjwv8YQUAAABdSURBVHgBjctNDYAwDIbhNkUAoKAZCOCIHBwhASzgCAfDQelhh2Xrfr5Tkz4vgDF2y8VuPa0fWRgEDz33cZ748/4pBhEOwy2NqIztiOo4j7CN407uQTGDyNsVqP0BaHUk0IS2sYcAAAAASUVORK5CYII=";
    public static final int TYPING_STATUS_TIME = 1000; //in millisecond
    public static final String SLOTS_LIST = "slots_list";
    public static final String QUICK_SLOTS = "quick_slots";
    public static final String[] DELAY_MESSAGES = { "I’m working on it…", "Still, working on it…",
            "This is taking a bit longer. Please wait…", "Things are slower than usual today. Please wait…",
            "I’m sorry. The server response seems really slow today. Please continue to wait...",
            "I’m sorry. I haven’t gotten a response from the server yet. Please continue to wait...",
            "I apologize. The server is has not responded yet. Please continue to wait…"};
    public static final String[] SESSION_END_ALERT_MESSAGES = {"We have noticed that chat is inactive for around a minute. The chat will be closed soon",
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


    public static final String PAYLOAD = "payload";
    public static final String MESSAGE = "message";
    public static final String PICK_TYPE = "pickType";
    public static final String FILE_CONTEXT = "fileContext";


    public static final String CHOOSE_TYPE_GALLERY = "choose";
    public static String CHOOSE_TYPE_IMAGE_VIDEO= "choose_image_video";
    public static final String CHOOSE_TYPE_VIDEO_GALLERY = "choose_video";
    public static final String CHOOSE_TYPE_FILE = "choose_file";
    public static final String CHOOSE_TYPE_CAMERA = "camera";
    public static final String FOR_MESSAGE = "message";
    public static String FOR_PROFILE = "profile";
    public static final String MEDIA_TYPE_AUDIO = "audio";
    public static final String MEDIA_TYPE_VIDEO = "video";
    public static final String MEDIA_TYPE_IMAGE = "image";
    public static final String MEDIA_TYPE_NONE = "text";
    public static final String MEDIA_TYPE_DOCUMENT = "docs";

    public static final String DOWNLOAD_URL = "downloadUrl";
    public static final String TARGET_PATH = "targetPath";
    public static final String FILE_NAME = "fileName";
    public static final String FILE_ID = "fileId";
    public static final String MEDIA_TYPE = "mediaType";
    public static final String REQUEST_HEADERS = "requestHeaders";
    public static final String DOWNLOAD_STATUS = "downloadStatus";
    public static final String DOWNLOAD_STATUS_SUCCESS = "downloadStatusSuccess";
    public static final String DOWNLOAD_STATUS_FAILURE = "downloadStatusFailure";
    public static final String PIN_CERTIFICATE = "pinCertificate";
    public static final String INTENT_ACTION_MEDIA_DOWNLOADED = "com.kore.media.downloaded";

    public static final String DOWNLOADED_MEDIA_FILE_PATH = "mediaDownloadedFilePath";
    public static final String DOWNLOADED_MEDIA_TYPE = "downloadedMediaType";
    public static final String DOWNLOADED_MEDIA_COMPONENT_ID = "downloadedMediaComponentId";
    public static final String KEY_FOOTER_MEDIA_FILE_PATH = "KeyFooterMediaFilePath";
    public static final String KEY_IMMEDIATE_PLAYBACK = "KeyImmediatePlayback";
    public static final String KEY_SEEK_POINTER = "KeySeekPointer";
    public static final String KEY_PLAYBACK_SEEK_POSITION = "KeyPlaybackSeekPosition";

    public static final String ORIENTATION = "orientation";

    public static final int ONBOARDING_COMPLETE_CODE = 12522;
    public static final int REQ_CODE_COLOR = 673;
    public static final int NON_MEMBER_OR_MEMBER_PERMISSIONS = 757;
    public static final int REQ_ADMIN_PERMISSIONS=896;
    public static final int TEAM_LAUNCHING=887;
    public static final int SKILL_STORE_LAUNCH = 122;
    public static final int REQ_CODE_PROFILE_REFRESH = 674;

    /**
     * meeting related bundles
     */
    public static final String SELECTED_DATE_POSITION = "selected_date_position";
    public static final String SELECTED_TIME_POSITION = "selected_time_position";

    public static final String SELECTED_CUSTOM_DATE_FROM = "selected_custom_date_from";
    public static final String SELECTED_CUSTOM_DATE_TO = "selected_custom_date_to";

    public static final String SELECTED_CUSTOM_TIME_FROM = "selected_custom_time_from";
    public static final String SELECTED_CUSTOM_TIME_TO = "selected_custom_time_to";

    public static final String SELECTED_DURATION = "selected_duration";
    public static final String SELECTED_DURATION_POSITION = "selected_duration_position";
    public static final String IS_FOR_ALL = "is_for_all";



    public static final String SELECTED_COLOR = "selected_color";
    public static final String FROM_OVERLAY = "from_overlay";

    /**
     * teams bundle
     */

    public static final String CAN_SHARE = "can_share";
    public static final String APPROVAL_MODE = "approval_mode";
    public static final String IS_MEMBER_MODE = "is_member_mode";
    public static final String CAN_MANAGE_MEMBERS = "can_manage_members";
    public static final String CAN_CHANGE_SETTINGS = "can_change_settings";
    public static final String CAN_APPROVE = "can_approve";
    public static final String TEAM_ID = "team_id";
    public static final String PENDING_COUNT = "pending_count";
    public static final String TEAM_NAME = "team_name";
    public static final String OWNER = "owner";
    public static final String IS_CREATE_MODE = "is_create_mode";
    public static final String TEAM_DATA = "team_data";
    public static final String IS_PENDING = "is_pending";
    public static final String IS_APPROVED = "is_approved";
    public static final String IS_SHARED = "is_shared";


    //Meeting requests
    public static final String IS_POST_DATA_TO_BOT_THROUGH_PUSH = "post_data_to_bot";
    public static final String IS_ONBOARD_TRY_NOW_BUTTON_CLICKED = "onboard_try_now_post_data";
    //    public static final String TRY_NOW_UTTERANCE = "try_now_utterance";
//    public static final String DATA_TO_POST = "data_to_post";
    public static final String MEETING_HOST_TYPE = "meeting_host_type";
    public static final String MEETING_ID = "meeting_id";
    public static final String HOST_NAME = "host_name";
    public static final String MEETING_TITLE = "meeting_title";
    public static final String START_TIME = "startTime";
    public static final String SHOULD_SHOW_PERMISSIONS = "should_show_permissions";
    public static final String END_TIME = "endTime";
    public static final String USER_AVAILABLE = "user_available";
    public static final String MEETING_TYPE_INFO = "meeting_type_info";
    public static final String MAKE_IT_DEFAULT = "make_it_default";
    public static final String ID = "id";
    public static final String NAME_ID_MAP = "name_id_map";
    public static final String CATEGORY = "shortcut_category";
    public static final String IS_FROM_SHORTCUTS = "is_from_shortcuts";

    public static final int CAPTURE_IMAGE_BUNDLED_PREMISSION_REQUEST = 1234;
    public static final int CAPTURE_IMAGE_CHOOSE_FILES_BUNDLED_PREMISSION_REQUEST = 1434;
    public static final int CAPTURE_IMAGE_CHOOSE_FILES_RECORD_BUNDLED_PREMISSION_REQUEST = 3453;
}
