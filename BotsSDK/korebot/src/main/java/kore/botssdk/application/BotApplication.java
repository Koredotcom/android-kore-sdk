package kore.botssdk.application;

import android.app.Application;
import android.content.IntentFilter;
import android.net.ConnectivityManager;

import com.google.firebase.FirebaseApp;

import kore.botssdk.fcm.FCMWrapper;
import kore.botssdk.listener.NetworkStateReceiver;

/**
 * Created by Pradeep Mahato on 31-May-16.
 * Copyright (c) 2014 Kore Inc. All rights reserved.
 */
public class BotApplication extends Application {

    AppControl appControl;
    @Override
    public void onCreate() {
        super.onCreate();
        appControl = new AppControl(getApplicationContext());
        FirebaseApp.initializeApp(getApplicationContext());
        FCMWrapper.getInstance().init();
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(new NetworkStateReceiver(), filter);
    }

    public static boolean isActivityVisible() {
        return activityVisible;
    }

    public static void activityResumed() {
        activityVisible = true;
    }

    public static void activityPaused() {
        activityVisible = false;
    }

    private static boolean activityVisible;
}
