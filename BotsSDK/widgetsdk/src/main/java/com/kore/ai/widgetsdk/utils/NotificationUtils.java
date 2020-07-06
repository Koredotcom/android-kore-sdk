package com.kore.ai.widgetsdk.utils;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by Ramachandra Pradeep on 30-Oct-18.
 */

public class NotificationUtils {
    public final static String GROUP_KEY_NOTIFICATIONS = "group_key_notifications";

    public static final String SHORT_TYPE_APP_CONTROL_CHANGED = "er";
    public static final String SHORT_TYPE_KORA_REMINDER     = "kr";
    public static final String SHORT_TYPE_KORA_QUESTION     = "kq";
    public static final String SHORT_TYPE_KORA_COMMENT     = "kc";
    public static final String SHORT_TYPE_KORA_KNOWLEDGE     = "kk";
    public static final String SHORT_TYPE_KORA_TEAM    = "tmm";
    public static final String SHORT_TYPE_KNOWLEDGE_PENDING    = "kp";
    public static final String SHORT_TYPE_KORA_MEETING_REMINDER = "kmr";
    public static final String SHORT_TYPE_KORA_MEETING_ORGANISER = "kmo";
    public static final String SHORT_TYPE_CALENDAR_EVENT_UPDATE = "kceu";
    public static final String SHORT_TYPE_KORA_SYSTEM_ALERT = "kse";
    public static final String SHORT_TYPE_SUBDOMAIN_SCHEDULE_MEETING = "kme";
    public static final String SHORT_TYPE_ANNOUNCEMENT = "kaa";
    public static final String SHORT_TYPE_VOICE_DATA = "klt";
    public static final String SHORT_TYPE_TASK = "kt";
    public static final String SHORT_TYPE_KORA_MEETING_NOTES = "kmn";
    public static final String SHORT_TYPE_ANNOUNCEMENT_COMMENT = "kac";
    public static final String SHORT_TYPE_KORA_LOGOUT = "kfl";
    public static final String SHORT_TYPE_KORA_UPDATE = "ku";
    public static final String SHORT_TYPE_KORA_ONBOARD_UPDATE = "kou";
    public static final String SHORT_TYPE_KORE_MESSENGER_UPDATE = "kman";
    public static final String SHORT_TYPE_BADGE_READ = "ke";

    public static final String SHORT_TYPE_KEYS = "kes";

    private final static String LOG_TAG = "NotificationUtils";



    private static final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.ENGLISH);;

    static {
        format.setTimeZone(TimeZone.getTimeZone("UTC"));
    }

    public static ArrayList<String> getAnnouncementList() {
        return announcementList;
    }

    private static ArrayList<String> announcementList = new ArrayList<>();
    private static boolean isFromProfile = false;

    public static void addAnnouncementToList(String announcementId){
        synchronized (announcementList){
            announcementList.add(announcementId);
        }
    }

    public static void clearAnnouncementList(){
        synchronized (announcementList){
            announcementList.clear();
        }
    }

    public static boolean isFromProfile(){
        return isFromProfile;
    }
    public static void setIsFromProfile(boolean isFromProfilel){
        isFromProfile = isFromProfilel;
    }
}
