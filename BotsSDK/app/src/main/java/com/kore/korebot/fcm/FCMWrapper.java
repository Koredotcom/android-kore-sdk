package com.kore.korebot.fcm;

import static kore.botssdk.utils.BundleConstants.GROUP_KEY_NOTIFICATIONS;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import kore.botssdk.R;
import kore.botssdk.activity.BotChatActivity;
import kore.botssdk.models.BotResponse;
import kore.botssdk.net.SDKConfiguration;
import kore.botssdk.utils.BundleUtils;

@SuppressWarnings("UnKnownNullness")
public class FCMWrapper extends FirebaseMessagingService {
    public static final String TAG = "FCMWrapper";
    private SharedPreferences sharedPreferences;

    @Override
    public void onNewToken(@NonNull String token) {
        sharedPreferences = getSharedPreferences(BotResponse.THEME_NAME, MODE_PRIVATE);
        if (!token.equals("")) {
            sharedPreferences.edit().putString("FCMToken", token).apply();
        }

        super.onNewToken(token);
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        RemoteMessage.Notification notif = remoteMessage.getNotification();
        if (notif != null) {
            String title = notif.getTitle();
            String text = notif.getBody();
            Log.e(TAG, "message " + title);

            postGDAuthRequiredNotification(title, text);
        }
    }

    public void init() {
        try {
            Log.d(TAG, "init FCM");
            FirebaseMessaging.getInstance().setAutoInitEnabled(true);
            sharedPreferences = getSharedPreferences(BotResponse.THEME_NAME, MODE_PRIVATE);
        } catch (Throwable th) {
            Log.e(TAG, "init FCM failed", th);
        }
    }

    public void postGDAuthRequiredNotification(String title, String pushMessage) {
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder nBuilder;
        int importance = NotificationManager.IMPORTANCE_DEFAULT;
        NotificationChannel notificationChannel = new NotificationChannel("Kore_Push_Service", "Kore_Android", importance);
        mNotificationManager.createNotificationChannel(notificationChannel);
        nBuilder = new NotificationCompat.Builder(this, notificationChannel.getId());

        nBuilder.setContentTitle(title).setSmallIcon(R.drawable.ic_launcher).setColor(Color.parseColor("#009dab")).setContentText(pushMessage).setGroup(GROUP_KEY_NOTIFICATIONS).setGroupSummary(true).setAutoCancel(true).setPriority(NotificationCompat.PRIORITY_HIGH);
        if (alarmSound != null) {
            nBuilder.setSound(alarmSound);
        }

        Intent intent = new Intent(getApplicationContext(), BotChatActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(BundleUtils.PICK_TYPE, "Notification");
        bundle.putString(BundleUtils.BOT_NAME_INITIALS, String.valueOf(SDKConfiguration.Client.bot_name.charAt(0)));
        intent.putExtras(bundle);
        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, PendingIntent.FLAG_MUTABLE);
        nBuilder.setContentIntent(pendingIntent);

        Notification notification = nBuilder.build();
        mNotificationManager.notify("YUIYUYIU", 237891, notification);
    }
}
