package kore.botssdk.audiocodes.oauth;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import kore.botssdk.application.BotApplication;
import kore.botssdk.audiocodes.webrtcclient.General.Log;

public class OAuthReceiver extends BroadcastReceiver {


    public enum Type {
        REFRESH_TOKEN, ACCESS_TOKEN
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(OAuthManager.TAG, "onReceive alarm");
        OAuthIntentService.enqueueWork(context, intent);
    }

    protected static void setRefreshTokenAlarm(long interval){
        setAlarm(BotApplication.getGlobalContext(), interval, Type.REFRESH_TOKEN);
    }

    protected static void setAccessTokenAlarm(long interval){
        setAlarm(BotApplication.getGlobalContext(), interval, Type.ACCESS_TOKEN);
    }

    private static void setAlarm(Context context, long interval, Type type) {
        Log.i(OAuthManager.TAG, "set Alarm. interval: " + interval + " seconds. Type " + type);
        interval = interval * 1000;
        AlarmManager am = (AlarmManager) context
                .getSystemService(Context.ALARM_SERVICE);
        Intent alarmIntent = new Intent(context, OAuthReceiver.class);
        alarmIntent.setData(Uri.parse(type.toString()));
        long startingTime = System.currentTimeMillis();
        startingTime = startingTime + interval;
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0,
                alarmIntent, PendingIntent.FLAG_CANCEL_CURRENT | PendingIntent.FLAG_IMMUTABLE);
        am.setExact(AlarmManager.RTC_WAKEUP, startingTime, pendingIntent);
    }
}
