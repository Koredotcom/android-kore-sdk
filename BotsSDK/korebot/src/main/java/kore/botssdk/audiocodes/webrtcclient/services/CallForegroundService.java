package kore.botssdk.audiocodes.webrtcclient.services;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

import kore.botssdk.application.BotApplication;
import kore.botssdk.audiocodes.webrtcclient.General.Log;
import kore.botssdk.audiocodes.webrtcclient.General.NotificationUtils;

public class CallForegroundService extends Service {
    private static final String TAG = "CallForeService";

    private static final String START_FOREGROUND = "START_FOREGROUND";
    private static final String STOP_FOREGROUND = "STOP_FOREGROUND";

    @Override
    public void onCreate() {
        Log.d(TAG, "onCreate");
        super.onCreate();
    }

    @SuppressLint("ForegroundServiceType")
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) {
            Log.d(TAG, "Received intent: " + intent.getAction());
            String action = intent.getAction();
            if (START_FOREGROUND.equals(action)) {
                Log.d(TAG, "Received Start Foreground Intent ");
                Notification notification = NotificationUtils.addServiceNotification();
                startForeground(NotificationUtils.NOTIFICATION_SERVICE_ID, notification);

            } else if (STOP_FOREGROUND.equals(action)) {
                Log.d(TAG, "Received Stop Foreground Intent");
                stopForeground(true);
                stopSelf();
            }
        }
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy");
    }

    @Override
    public IBinder onBind(Intent intent) {
        // Used only in case of bound services.
        return null;
    }

    public static void startService(){
        Log.d(TAG, "startService foreground ");
        Context context = BotApplication.getGlobalContext();
        Intent startIntent = new Intent(context, CallForegroundService.class);
        startIntent.setAction(CallForegroundService.START_FOREGROUND);
        context.startService(startIntent);
    }

    public static void stopService(){
        Log.d(TAG, "stopService foreground ");
        Context context = BotApplication.getGlobalContext();
        Intent stopIntent = new Intent(context, CallForegroundService.class);
        stopIntent.setAction(CallForegroundService.STOP_FOREGROUND);
        context.stopService(stopIntent);
        NotificationUtils.removeServiceNotification();
    }

}