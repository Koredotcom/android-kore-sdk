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

    public static final SimpleDateFormat isoFormatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.ENGLISH);
    public static final SimpleDateFormat isoFormatterHelp = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.ENGLISH);

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

    /**
     * Get the package version
     *
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
        return !isNullOrEmpty(url) && Patterns.WEB_URL.matcher(url).matches();
    }

    public static boolean isWebURL(CharSequence url) {
        return !isNullOrEmpty(url) && Patterns.WEB_URL.matcher(url).matches();
    }

    public static boolean isPhoneNo(String text) {
        return !isNullOrEmpty(text) && Patterns.PHONE.matcher(text).matches();
    }

    public static BotResponse buildBotMessage(String msg, String streamId, String botName) {

        isoFormatterHelp.setTimeZone(TimeZone.getTimeZone("UTC"));

        return buildBotMessage(msg, streamId, botName, isoFormatterHelp.format(new Date()));

    }

    public static BotResponse buildBotMessage(String msg, String streamId, String botName, String createdOn) {
        return buildBotMessage(msg, streamId, botName, createdOn, null);
    }

    public static BotResponse buildBotMessage(String msg, String streamId, String botName, String createdOn, String msgId) {
        BotResponse botResponse = new BotResponse();

        botResponse.setType("bot_response");
        botResponse.setFrom("bot");

        if (msgId != null) {
            botResponse.setMessageId(msgId);
        }
        botResponse.setCreatedOn(createdOn);

        BotInfoModel bInfo = new BotInfoModel(botName, streamId, null);
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





    public static final String inPayload = "{\"template_type\":\"kora_summary_help\",\"elements\":[{\"title\":\"How can I help you?\",\"body\":[{\"type\":\"postback\",\"title\":\"Schedule a meeting\",\"payload\":\"Schedule a meeting\"},{\"type\":\"postback\",\"title\":\"Set a reminder\",\"payload\":\"Set a reminder\"},{\"type\":\"postback\",\"title\":\"Create task\",\"payload\":\"Create task\"},{\"type\":\"postback\",\"title\":\"Help me prepare for COVID-19 crisis\",\"payload\":\"Ask covid what is covid-19\"}]}],\"isNewVolley\":true}";

    public static String buildHelpMessage(String payload, PayloadInner.Skill skill, KoraSummaryHelpModel element) {
        BotResponse botResponse = new BotResponse();

        botResponse.setType("bot_response");
        botResponse.setFrom("bot");

        botResponse.setMessageId("");
        isoFormatterHelp.setTimeZone(TimeZone.getTimeZone("UTC"));
        botResponse.setCreatedOn(isoFormatterHelp.format(new Date()));

        BotInfoModel bInfo = new BotInfoModel("", "", null);
        botResponse.setBotInfo(bInfo);

        BotResponseMessage botResponseMessage = new BotResponseMessage();
        botResponseMessage.setType(BotResponse.COMPONENT_TYPE_TEXT);

        ComponentModel cModel = new ComponentModel();
        cModel.setType(BotResponse.COMPONENT_TYPE_TEMPLATE);

        PayloadOuter pOuter = new PayloadOuter();
        pOuter.setType(BotResponse.COMPONENT_TYPE_TEMPLATE);
        PayloadInner payloadInner = new Gson().fromJson(payload, PayloadInner.class);
        if(element != null) {
            ArrayList list = new ArrayList(1);
            list.add(element);
            payloadInner.setElements(list);
        }
        if(skill!=null)
        {
            payloadInner.setSkill(skill);
        }
        pOuter.setPayload(payloadInner);

        cModel.setPayload(pOuter);
        botResponseMessage.setComponent(cModel);
        ArrayList<BotResponseMessage> message = new ArrayList<>(1);
        message.add(botResponseMessage);
        botResponse.setMessage(message);


        return new Gson().toJson(botResponse);
    }

    public static BotResponse buildBotMessage(PayloadOuter pOuter, String streamId, String botName, String createdOn, String msgId) {
        BotResponse botResponse = new BotResponse();

        botResponse.setType("bot_response");
        botResponse.setFrom("bot");

        if (msgId != null) {
            botResponse.setMessageId(msgId);
        }
        botResponse.setCreatedOn(createdOn);

        BotInfoModel bInfo = new BotInfoModel(botName, streamId, null);
        botResponse.setBotInfo(bInfo);

        BotResponseMessage botResponseMessage = new BotResponseMessage();
        botResponseMessage.setType(BotResponse.COMPONENT_TYPE_TEXT);

        ComponentModel cModel = new ComponentModel();
        cModel.setType(BotResponse.COMPONENT_TYPE_TEMPLATE);

        cModel.setPayload(pOuter);
        botResponseMessage.setComponent(cModel);
        ArrayList<BotResponseMessage> message = new ArrayList<>(1);
        message.add(botResponseMessage);
        botResponse.setMessage(message);


        return botResponse;
    }

    /*public static BotResponse buildBotMessageForConversationEnd(long time, String botName, String streamId) {
        BotResponse botResponse = new BotResponse();

        botResponse.setType("bot_response");
        botResponse.setFrom("bot");

        botResponse.setCreatedOn(BaseBotMessage.isoFormatter.format(time).toString());

        BotInfoModel bInfo = new BotInfoModel(botName, streamId, null);
        botResponse.setBotInfo(bInfo);

        BotResponseMessage botResponseMessage = new BotResponseMessage();
        botResponseMessage.setType(BotResponse.COMPONENT_TYPE_TEXT);

        ComponentModel cModel = new ComponentModel();
        cModel.setType(BotResponse.COMPONENT_TYPE_TEMPLATE);

        PayloadOuter pOuter = new PayloadOuter();
        pOuter.setType(BotResponse.COMPONENT_TYPE_TEMPLATE);

        PayloadInner payloadInner = new PayloadInner();
        payloadInner.setTemplate_type(BotResponse.TEMPLATE_TYPE_CONVERSATION_END);
        payloadInner.setText("This conversation has been ended!");
        payloadInner.setShowComposeBar(true);
        pOuter.setPayload(payloadInner);
        cModel.setPayload(pOuter);
        botResponseMessage.setComponent(cModel);
        ArrayList<BotResponseMessage> message = new ArrayList<>(1);
        message.add(botResponseMessage);
        botResponse.setMessage(message);


        return botResponse;
    }*/

    public static BotResponse getLastReceivedMsg(ArrayList<BaseBotMessage> list) {
        if (list != null && list.size() > 0) {
            int index = list.size() - 1;
            for (; index >= 0; index--)
                if (!list.get(index).isSend()) {
                    return (BotResponse) list.get(index);
                }
        }
        return null;
    }

    public static boolean shouldShowHelp(BotResponse msg) {
        boolean shouldShowHelp = true;
        if (msg != null) {
            ComponentModel components = msg.getMessage().get(0).getComponent();
            try {
                PayloadOuter outer = components.getPayload();
                if (outer != null) {
                    PayloadInner payloadInner = outer.getPayload();
                    if (payloadInner != null && !StringUtils.isNullOrEmpty(payloadInner.getTemplate_type())) {
                        if (payloadInner.getTemplate_type().equals(BotResponse.TEMPLATE_TYPE_HIDDEN_DIALOG)) {
                            shouldShowHelp = true;
                        } else {
                            shouldShowHelp = isGreaterThanFifteenMins(msg.getCreatedOn());
                        }
                    } else {
                        shouldShowHelp = isGreaterThanFifteenMins(msg.getCreatedOn());
                    }
                } else {
                    shouldShowHelp = isGreaterThanFifteenMins(msg.getCreatedOn());
                }
            } catch (com.google.gson.JsonSyntaxException ex) {
                ex.printStackTrace();
                shouldShowHelp = isGreaterThanFifteenMins(msg.getCreatedOn());
            }
        }
        return shouldShowHelp;
    }

    private static boolean isGreaterThanFifteenMins(String time) {
        long timeStampMillis = 0;
        try {
            timeStampMillis = DateUtils.isoFormatter.parse(time).getTime() + TimeZone.getDefault().getRawOffset();
            if ((System.currentTimeMillis() - timeStampMillis) > (1000 * 60 * 15)) {
                return true;
            } else
                return false;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return true;
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

    public static long getTimeInMillis(String timeStamp) throws ParseException {
        if (timeStamp == null || timeStamp.isEmpty()) return System.currentTimeMillis();
        return isoFormatter.parse(timeStamp).getTime();

    }

    public static String ah(String accessToken) {
        return "bearer " + accessToken;
    }

    public static int getMaxLinesOfText(String text, float width, int currentTextSize) {
        Rect bounds = new Rect();
        Paint paint = new Paint();
        paint.setTextSize(currentTextSize);
        paint.getTextBounds(text, 0, text.length(), bounds);
        return (int) Math.ceil((float) bounds.width() / width);
    }

    public static HashMap<String, Object> jsonToMap(String jsonString) {
        HashMap<String, Object> map = null;
        try {
            map = new Gson().fromJson(jsonString, new TypeToken<HashMap<String, Object>>() {
            }.getType());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }

}
