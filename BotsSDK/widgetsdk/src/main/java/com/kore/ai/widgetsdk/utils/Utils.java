package com.kore.ai.widgetsdk.utils;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Paint;
import android.graphics.Rect;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.Looper;
import android.util.Patterns;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kore.ai.widgetsdk.models.BaseBotMessage;
import com.kore.ai.widgetsdk.models.BotInfoModel;
import com.kore.ai.widgetsdk.models.BotResponse;
import com.kore.ai.widgetsdk.models.BotResponseMessage;
import com.kore.ai.widgetsdk.models.ComponentModel;
import com.kore.ai.widgetsdk.models.KoraSummaryHelpModel;
import com.kore.ai.widgetsdk.models.PayloadInner;
import com.kore.ai.widgetsdk.models.PayloadOuter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by Pradeep Mahato on 06-Jun-16.
 * Copyright (c) 2014 Kore Inc. All rights reserved.
 */
public class Utils {

    /**
     * Retrieve The version name of this package, as specified by the manifest
     *
     * @param context
     * @return
     */
    public static String getVersion(Context context) {
        try {
            return context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return "";
        }
    }

    public static void showToast(Context context, String message) {
        new Handler(Looper.getMainLooper()).post(() -> {
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
        });
    }

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public static boolean isNullOrEmpty(String string) {
        return string == null || string.isEmpty();
    }

    public static boolean isNullOrEmpty(CharSequence charSequence) {
        return charSequence == null || charSequence.toString().isEmpty();
    }

    public static String ah(String accessToken) {
        return "bearer " + accessToken;
    }
}
