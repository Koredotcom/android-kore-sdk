package com.kore.ai.widgetsdk.events;

public class KaEvents {
    public static class InformationSharedEvent{

    }
    public static class ImageFetchEvent{

    }
    public static class ReminderEvent{
        public final String reminderInfo;
        public ReminderEvent(String reminderInfo){
            this.reminderInfo = reminderInfo;
        }
    }
    public static class NotificationsUpdateEvent{

    }

    public static class RefreshKnowledgeList {
    }
}
