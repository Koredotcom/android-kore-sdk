package kore.botssdk.audiocodes.webrtcclient.General;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.AudioAttributes;
import android.media.RingtoneManager;
import android.view.View;
import android.widget.RemoteViews;

import androidx.core.app.NotificationCompat;

import com.audiocodes.mv.webrtcsdk.session.AudioCodesSession;
import com.audiocodes.mv.webrtcsdk.session.CallState;
import com.audiocodes.mv.webrtcsdk.useragent.AudioCodesUA;

import kore.botssdk.R;
import kore.botssdk.application.BotApplication;
import kore.botssdk.audiocodes.webrtcclient.Activities.CallActivity;
import kore.botssdk.audiocodes.webrtcclient.Activities.IncomingCallActivity;
import kore.botssdk.audiocodes.webrtcclient.Activities.SplashActivity;

public class NotificationUtils {
    private static final String TAG = "NotificationUtils";

    private static final int NOTIFICATION_ID = 271;
    public static final int NOTIFICATION_SERVICE_ID = 272;
    public static final int NOTIFICATION_CALL_ID = 273;

    private static String CHANNEL_NAME;
    private static String CHANNEL_ID;
    private static String CHANNEL_ID_CALL;
    private static String CHANNEL_NAME_CALL;

    private static NotificationManager notificationManager;

    public static void createAppNotification() {
        Context context = BotApplication.getGlobalContext();
        String title = context.getString(R.string.app_name);
        String text = context.getString(R.string.notification_text_prefix);
        int image;
        if (ACManager.getInstance().isRegisterState()) {
            text += " " + context.getString(R.string.notification_text_connected);
            image = R.mipmap.indicator_green_l;
        } else {
            text += " " + context.getString(R.string.notification_text_disconnected);
            image = R.mipmap.indicator_red_l;
        }
        Class<?> cls = SplashActivity.class;
        if (ACManager.getInstance().getActiveSession() != null) {
            if (ACManager.getInstance().getActiveSession().getCallState() == CallState.RINGING) {
                cls = IncomingCallActivity.class;
            } else if (ACManager.getInstance().getActiveSession().getCallState() != CallState.NULL) {
                cls = CallActivity.class;
            }
        }
        Log.d(TAG, "use activity: " + cls);

        NotificationUtils.createNotification(title, text, image, cls);
    }

    public static void createNotification(String title, String text, int image, Class<?> cls) {
        getNotificationManager();
        Context context = BotApplication.getGlobalContext();
        NotificationCompat.Builder notificationCompat = (NotificationCompat.Builder) new NotificationCompat.Builder(context, CHANNEL_ID);
        notificationCompat.setContentTitle(title);
        notificationCompat.setContentText(text);
        notificationCompat.setAutoCancel(true);
        notificationCompat.setVisibility(NotificationCompat.VISIBILITY_PUBLIC);
        notificationCompat.setOngoing(true);
        notificationCompat.setAutoCancel(false);
        notificationCompat.setOnlyAlertOnce(false);
        notificationCompat.setPriority(Notification.PRIORITY_HIGH);
        notificationCompat.setSmallIcon(image);
        Intent intent = new Intent(context, cls);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_IMMUTABLE);
        notificationCompat.setContentIntent(pendingIntent);
        Notification notifcation = notificationCompat.build();
        getNotificationManager().notify(NOTIFICATION_ID, notifcation);
    }

    public static Notification addServiceNotification() {
        Context context = BotApplication.getGlobalContext();

        NotificationCompat.Builder notificationCompat = (NotificationCompat.Builder) new NotificationCompat.Builder(context, CHANNEL_ID);
        notificationCompat.setAutoCancel(true);

        notificationCompat.setSmallIcon(R.mipmap.desktop_icon);

        Intent notifyIntent = new Intent(context, CallActivity.class);
        notifyIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        notifyIntent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        PendingIntent contentIntent = PendingIntent.getActivity(context, NOTIFICATION_SERVICE_ID, notifyIntent, PendingIntent.FLAG_CANCEL_CURRENT | PendingIntent.FLAG_IMMUTABLE);
        notificationCompat.setContentIntent(contentIntent);

        notificationCompat.setContentText(context.getString(R.string.notification_text_in_call));

        Notification notifcation = notificationCompat.build();
        getNotificationManager().notify(NOTIFICATION_SERVICE_ID, notifcation);
        return notifcation;
    }

    public static void removeServiceNotification() {
        getNotificationManager().cancel(NOTIFICATION_SERVICE_ID);
    }

    public static void removeAllNotifications() {
        getNotificationManager().cancel(NOTIFICATION_ID);
        removeServiceNotification();
    }

    private static NotificationManager getNotificationManager() {
        if (notificationManager == null) {
            Context context = BotApplication.getGlobalContext();
            String name = context.getString(R.string.app_name);
            CHANNEL_ID = name + "_id";
            CHANNEL_NAME = name + "_name";
            CHANNEL_ID_CALL = name + "_id_call";
            CHANNEL_NAME_CALL = name + "_name_call";
            notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                int importance = NotificationManager.IMPORTANCE_LOW;
                NotificationChannel mChannel = new NotificationChannel(
                        CHANNEL_ID, CHANNEL_NAME, importance);
                notificationManager.createNotificationChannel(mChannel);
                mChannel = new NotificationChannel(
                        CHANNEL_ID_CALL, CHANNEL_NAME_CALL, NotificationManager.IMPORTANCE_HIGH);
                AudioAttributes aa = new AudioAttributes.Builder()
                        .setUsage(AudioAttributes.USAGE_NOTIFICATION_RINGTONE)
                        .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                        .build();
                //mChannel.enableVibration(true);
                mChannel.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE), aa);
                notificationManager.createNotificationChannel(mChannel);
            }
        }
        return notificationManager;
    }

    public static void removeCallNotification() {
        Log.d(TAG, "removeCallNotification");
        getNotificationManager().cancel(NOTIFICATION_CALL_ID);
    }

    public static void createCallNotification(String callerId, int sessionId, boolean video) {
        Log.d(TAG, "pushCallNotification " + sessionId);
        Class<?> c = IncomingCallActivity.class;
        Intent intent = getNotificationIntent(c, sessionId, 5);
        Intent videoIntent = null;
        if (video) {
            videoIntent = getNotificationIntent(c, sessionId, 2);
        }
        pushCallNotificationBase(callerId, intent,
                getNotificationIntent(c, sessionId, 1),
                getNotificationIntent(c, sessionId, 0),
                videoIntent);


        Thread timerThread = new Thread(new Runnable() {

            @Override
            public void run() {
                int ringingTimeOutMiliSec = IncomingCallActivity.INCOMING_CALL_RINGING_TIMEOUT_SEC * 1000;
                Log.d(TAG, "Start ringing timout timer for: " + ringingTimeOutMiliSec);
                try {
                    Thread.sleep(ringingTimeOutMiliSec);

                    AudioCodesSession session = AudioCodesUA.getInstance().getSession(sessionId);
                    if (session != null && session.getCallState() == CallState.RINGING) {
                        session.reject(null);
                    }
                } catch (Exception e) {
                    Log.d(TAG, "startRingingTimer interapted");
                    return;
                }
            }
        });
        timerThread.start();

    }

    private static Intent getNotificationIntent(Class<?> activity, int sessionId, int extra) {
        Intent intent = new Intent(BotApplication.getGlobalContext(), activity);
        intent.putExtra(IncomingCallActivity.INTENT_ANSWER_TAG, extra);
        intent.putExtra(IncomingCallActivity.SESSION_ID, sessionId);
        return intent;
    }

    private static void pushCallNotificationBase(String callerId,
                                                 Intent fullScreenIntent,
                                                 Intent answerIntent,
                                                 Intent declineIntent,
                                                 Intent videoIntent) {
        Context context = BotApplication.getGlobalContext();
        PendingIntent fullScreenPendingIntent = PendingIntent.getActivity(context, 0,
                fullScreenIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        PendingIntent pendingAnswerIntent = PendingIntent.getActivity(context, 1,
                answerIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        PendingIntent pendingDeclineIntent = PendingIntent.getActivity(context, 2,
                declineIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        PendingIntent pendingVideoIntent = null;
        if (videoIntent != null) {
            pendingVideoIntent = PendingIntent.getActivity(context, 3,
                    videoIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        }
        getNotificationManager();
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.notification_view);
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(context, CHANNEL_ID_CALL)
                        .setSmallIcon(R.mipmap.indicator_green)
                        .setPriority(NotificationCompat.PRIORITY_HIGH)
                        .setCategory(NotificationCompat.CATEGORY_CALL)
                        .setFullScreenIntent(fullScreenPendingIntent, true)
                        .setTimeoutAfter(IncomingCallActivity.INCOMING_CALL_RINGING_TIMEOUT_SEC * 1000)
                        .setColorized(true)
                        .setCustomContentView(remoteViews);
        int buttonTextColor = Color.parseColor("#ffffff");
        int titleTextColor = Color.parseColor("#000000");
        remoteViews.setOnClickPendingIntent(R.id.accept, pendingAnswerIntent);
        remoteViews.setTextColor(R.id.accept, buttonTextColor);
        remoteViews.setTextViewText(R.id.accept, context.getResources().getString(R.string.incoming_call_button_answer_text));
        remoteViews.setOnClickPendingIntent(R.id.decline, pendingDeclineIntent);
        remoteViews.setTextColor(R.id.decline, buttonTextColor);
        remoteViews.setTextViewText(R.id.decline, context.getResources().getString(R.string.incoming_call_button_decline_text));
        remoteViews.setTextViewText(R.id.title, context.getString(R.string.incoming_call_textview_call_state));
        remoteViews.setTextColor(R.id.title, titleTextColor);
        remoteViews.setTextViewText(R.id.user, callerId);
        remoteViews.setTextColor(R.id.user, titleTextColor);
        Bitmap defBitmap = BitmapFactory.decodeResource(context.getResources(), R.mipmap.default_contact_list_picture);
        defBitmap = ImageUtils.getCroppedRoundBitmap(defBitmap, 40);
        remoteViews.setImageViewBitmap(R.id.contact_picture_iv, defBitmap);
        if (pendingVideoIntent != null) {
            remoteViews.setOnClickPendingIntent(R.id.accept_video, pendingVideoIntent);
            remoteViews.setViewVisibility(R.id.accept_video, View.VISIBLE);
            remoteViews.setTextViewText(R.id.accept_video, context.getResources().getString(R.string.incoming_call_button_answer_video_notification_text));
            remoteViews.setTextColor(R.id.accept_video, buttonTextColor);
        } else {
            remoteViews.setViewVisibility(R.id.accept_video, View.GONE);
        }
        ;
        Notification incomingCallNotification = notificationBuilder.build();
        getNotificationManager().notify(NOTIFICATION_CALL_ID, incomingCallNotification);
    }
}
