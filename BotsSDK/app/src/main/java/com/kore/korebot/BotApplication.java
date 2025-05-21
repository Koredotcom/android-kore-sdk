package com.kore.korebot;

import android.app.Application;
import android.content.Intent;
import android.util.Log;

import com.google.android.gms.security.ProviderInstaller;

public class BotApplication extends Application implements ProviderInstaller.ProviderInstallListener {

    @Override
    public void onCreate() {
        super.onCreate();
        try {
            ProviderInstaller.installIfNeededAsync(this, this);
        } catch (Exception ex) {
            Log.e("BotApplication", "installIfNeededAsync() Error " + ex);
        }
    }

    @Override
    public void onProviderInstalled() {
        Log.d("BotApplication", "Installer is Available or installed");
    }

    /**
     * This method is called if updating fails. The error code indicates
     * whether the error is recoverable.
     */
    @Override
    public void onProviderInstallFailed(int errorCode, Intent recoveryIntent) {
        Log.e("BotApplication", "onProviderInstallFailed");
    }
}
