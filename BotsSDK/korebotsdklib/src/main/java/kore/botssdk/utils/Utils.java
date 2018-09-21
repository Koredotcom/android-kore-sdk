package kore.botssdk.utils;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.TimeZone;

import kore.botssdk.models.BaseBotMessage;
import kore.botssdk.models.BotInfoModel;
import kore.botssdk.models.BotResponse;
import kore.botssdk.models.BotResponseMessage;
import kore.botssdk.models.ComponentModel;
import kore.botssdk.models.PayloadInner;
import kore.botssdk.models.PayloadOuter;

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

    /**
     * Get the package version
     * @param context
     * @return
     */
    public static String getBuildVersion(Context context) {
        return "v" + getVersion(context);
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

    public static boolean isWebURL(String url) {
        return !isNullOrEmpty(url) && android.util.Patterns.WEB_URL.matcher(url).matches();
    }
    public static boolean isWebURL(CharSequence url) {
        return !isNullOrEmpty(url) && android.util.Patterns.WEB_URL.matcher(url).matches();
    }


    public static BotResponse buildBotMessage(String msg, String streamId,String botName){
        BotResponse botResponse = new BotResponse();

        botResponse.setType("bot_response");
        botResponse.setFrom("bot");


        Calendar calendar = Calendar.getInstance();
        long date = System.currentTimeMillis();
        int offset = TimeZone.getDefault().getOffset(date);
        calendar.setTimeInMillis(date - offset);
        botResponse.setCreatedOn(BaseBotMessage.isoFormatter.format(calendar.getTime()).toString());

        BotInfoModel bInfo = new BotInfoModel(botName,streamId,null);
        botResponse.setBotInfo(bInfo);

        BotResponseMessage botResponseMessage = new BotResponseMessage();
        botResponseMessage.setType(BotResponse.COMPONENT_TYPE_TEXT);

        ComponentModel cModel = new ComponentModel();
        cModel.setType(BotResponse.COMPONENT_TYPE_TEXT);

        PayloadOuter pOuter = new PayloadOuter();
        pOuter.setText(msg);

        cModel.setPayload(pOuter);
        botResponseMessage.setComponent(cModel);
        ArrayList<BotResponseMessage> message = new ArrayList<>(1);
        message.add(botResponseMessage);
        botResponse.setMessage(message);


        return botResponse;
    }



    /**
     * Pass media length in seconds, and get in below format
     * 00:00:00 (hrs:mins:sec)
     */
    public static String getMediaLength(long _seconds) {
        StringBuilder buffer = new StringBuilder();
        long _minutes = _seconds / 60;
        long _hours = _minutes / 60;

        long hours = _hours % 24;
        long minutes = _minutes % 60;
        long seconds = _seconds % 60;

        if (hours > 0) {
            buffer.append(String.format("%02d", hours));
            buffer.append(":");

        } else {
            buffer.append("00:");
        }

        if (minutes > 0) {
            buffer.append(String.format("%02d", minutes));
            buffer.append(":");

        } else {
            buffer.append("00:");
        }

        if (seconds > 0) {
            buffer.append(String.format("%02d", seconds));
        } else {
            buffer.append("00");
        }

        return buffer.toString();
    }
    public static void showHideVirtualKeyboard(Activity activity, View view, boolean show) {
        if (activity == null) {
            return;
        }

        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);

        if (show) { //show keyboard
            if (view == null) {
                return;
            }

            imm.showSoftInput(view, InputMethodManager.RESULT_UNCHANGED_SHOWN);

        } else {  // hide keyboard

            View focusView = activity.getCurrentFocus();
            if (focusView == null) {
                return;
            }

            imm.hideSoftInputFromWindow(focusView.getWindowToken(), InputMethodManager.RESULT_UNCHANGED_SHOWN);
        }
    }


    public static void toggleVirtualKeyboard(Activity activity, int showFlags, int hideFlags) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(showFlags, hideFlags);
    }
}
