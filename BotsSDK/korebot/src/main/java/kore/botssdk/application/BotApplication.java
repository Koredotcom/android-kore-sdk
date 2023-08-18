package kore.botssdk.application;

import android.app.Application;
import android.content.Context;
import android.content.IntentFilter;
import android.net.ConnectivityManager;

import kore.botssdk.FCM.FCMWrapper;
import kore.botssdk.listener.NetworkStateReceiver;

/**
 * Created by Pradeep Mahato on 31-May-16.
 * Copyright (c) 2014 Kore Inc. All rights reserved.
 */
public class BotApplication extends Application {

    AppControl appControl;
    private static Context globalContext;
    @Override
    public void onCreate() {
        super.onCreate();
        appControl = new AppControl(getApplicationContext());
        globalContext = this;
        FCMWrapper.getInstance().init();
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(new NetworkStateReceiver(), filter);
    }

    public static Context getGlobalContext() {
        return globalContext;
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
