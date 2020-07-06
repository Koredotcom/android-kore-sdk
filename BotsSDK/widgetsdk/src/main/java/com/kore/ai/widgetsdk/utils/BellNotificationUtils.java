package com.kore.ai.widgetsdk.utils;

import com.kore.ai.widgetsdk.events.KaEvents;
import com.kore.ai.widgetsdk.events.KoreEventCenter;
import com.kore.ai.widgetsdk.net.KaRestBuilder;
import com.kore.ai.widgetsdk.net.KaRestResponse;
import com.kore.ai.widgetsdk.room.models.AuthData;
import com.kore.ai.widgetsdk.room.models.UserData;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BellNotificationUtils {
    public static KaRestResponse.Notifications getNotifications() {
        return notifications;
    }
    private static UserData userData;
    private static AuthData authData;
    public static void setNotifications(KaRestResponse.Notifications notifications) {
        BellNotificationUtils.notifications = notifications;
        KoreEventCenter.post(notifications.getnStats());
    }

    private static KaRestResponse.Notifications notifications;

    public static void resetUpdates(ArrayList<KaRestResponse.Updates> updates) {
        if (notifications != null) {
            notifications.setUpdates(updates);
        }
    }

    public static void resetActions(ArrayList<KaRestResponse.Actions> actions) {
        if (notifications != null&&actions!=null) {
            notifications.setActions(actions);
        }
    }

    public static void addUpdates(ArrayList<KaRestResponse.Updates> updates) {
        if (notifications != null) {
            if (notifications.getUpdates() != null) {
                notifications.getUpdates().addAll(updates);
            } else {
                notifications.setUpdates(updates);
            }
        }
    }

    public static void addActions(ArrayList<KaRestResponse.Actions> actions) {
        if (notifications != null &&actions!=null) {
            if (notifications.getActions() != null) {
                notifications.getActions().addAll(actions);
            } else {
                notifications.setActions(actions);
            }
        }
    }

    public static void setStats(KaRestResponse.Stats stats) {
        if (notifications != null) {
            notifications.setnStats(stats);
            KoreEventCenter.post(notifications.getnStats());
        }
    }

    public static ArrayList<KaRestResponse.Updates> getUpdates(){
        if (notifications != null) {
            return notifications.getUpdates();
        }
        return null;
    }



    public static ArrayList<KaRestResponse.Actions> getActions(){
        if (notifications != null) {
            return notifications.getActions();
        }
        return null;
    }
    public static KaRestResponse.Stats getStats(){
        if(notifications != null){
            return notifications.getnStats();
        }
        return null;
    }

    public static void getActionsAndUpdates(String userId,String accessToken) {
        Call<KaRestResponse.Notifications> notifications = KaRestBuilder.getKaRestAPI().getNotifications(userId, Utils.ah(accessToken));
        notifications.enqueue(new Callback<KaRestResponse.Notifications>() {
            @Override
            public void onResponse(Call<KaRestResponse.Notifications> call, Response<KaRestResponse.Notifications> response) {
                if (response.isSuccessful()) {

                    BellNotificationUtils.setNotifications(response.body());
                }else{
                    KoreEventCenter.post(new KaEvents.NotificationsUpdateEvent());
                }
            }

            @Override
            public void onFailure(Call<KaRestResponse.Notifications> call, Throwable t) {
                KoreEventCenter.post(new KaEvents.NotificationsUpdateEvent());
            }
        });
    }


    public static boolean isUnread(KaRestResponse.Stats stats){

        return stats != null && (stats.getActions_count() > 0 || stats.getUpdates_unseen() > 0);
    }
}
