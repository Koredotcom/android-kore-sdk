package kore.botssdk.application;

import android.app.Application;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import androidx.multidex.MultiDex;

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
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(new NetworkStateReceiver(), filter);
    }/*
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }*/
}
