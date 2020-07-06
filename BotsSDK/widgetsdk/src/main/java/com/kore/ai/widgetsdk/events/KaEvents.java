package com.kore.ai.widgetsdk.events;

/**
 * Created by Shiva Krishna on 3/9/2018.
 */

public class KaEvents {
    public static class InformationSharedEvent{

    }
    public static class ImageFetchEvent{

    }
    public static class ReminderEvent{
        public  String reminderInfo;
        public ReminderEvent(String reminderInfo){
            this.reminderInfo = reminderInfo;
        }
    }
    public static class NotificationsUpdateEvent{

    }

    public static class RefreshKnowledgeList {
    }
}
