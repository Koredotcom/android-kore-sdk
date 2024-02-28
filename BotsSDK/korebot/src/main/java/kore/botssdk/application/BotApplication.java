package kore.botssdk.application;

import static kore.botssdk.fcm.FCMWrapper.TAG;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.IntentFilter;
import android.net.ConnectivityManager;

import com.google.firebase.FirebaseApp;

import kore.botssdk.audiocodes.webrtcclient.General.Log;
import kore.botssdk.audiocodes.webrtcclient.db.MySQLiteHelper;
import kore.botssdk.fcm.FCMWrapper;
import kore.botssdk.listener.NetworkStateReceiver;

/**
 * Created by Pradeep Mahato on 31-May-16.
 * Copyright (c) 2014 Kore Inc. All rights reserved.
 */
public class BotApplication extends Application {

    AppControl appControl;
    private static Context globalContext;
    private static MySQLiteHelper dataBase;
    private static Activity currentActivity;
    private static Activity previousActivity;

    public static Context getGlobalContext()
    {
        return globalContext;
    }

    public static void initDataBase()
    {
        dataBase = new MySQLiteHelper(globalContext);
    }

    public static MySQLiteHelper getDataBase() {
        return dataBase;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        appControl = new AppControl(getApplicationContext());
        globalContext = this;
//        FirebaseApp.initializeApp(getApplicationContext());
//        FCMWrapper.getInstance().init();
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

    public static Activity getCurrentActivity() {
        return currentActivity;
    }

    public static void setCurrentActivity(Activity activity) {
        Log.d(TAG , "setCurrentActivity: " +activity);
        if(currentActivity!=null && currentActivity != getPreviousActivity() && currentActivity != activity){
            setPriviousActivity(currentActivity);
        }
        currentActivity = activity;
    }

    public static Activity getPreviousActivity() {
        return previousActivity;
    }

    public static void setPriviousActivity(Activity activity) {
        previousActivity = activity;
    }
}
