package com.kore.ai.widgetsdk.utils;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Shiva Krishna on 11/17/2017.
 */

public class AppUtils {

    public static final String URL_REGEX = "(http|ftp|https)://|([\\w_-]+(?:(?:\\.[\\w_-]+)+))([\\w.,@?^=%&:/~+#-]*[\\w@?^=%&/~+#-])?";//"^((https?|ftp)://|(www|ftp)\\.)?[a-z0-9-]+(\\.[a-z0-9-]+)+([/?].*)?$";

    public static float convertDpToPixel(Context context, float dp) {

        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        if (metrics.densityDpi <= DisplayMetrics.DENSITY_HIGH) {
            dp = 1.4f;
        }
        return dp * (metrics.densityDpi / 160f);
    }

    public static Map getMapObject(Object params)
    {
        Gson gson = new GsonBuilder().create();
        String json = gson.toJson(params);// obj is your object
        Map<String,Object> result = new Gson().fromJson(json, Map.class);

        return result;
    }

    public static void showHideVirtualKeyboard(Activity activity, View view, boolean show) {
        if (activity == null) {
            return;
        }

        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        View focusView = view == null ? activity.getCurrentFocus() : view;
        if (focusView == null) {
            return;
        }
        imm.hideSoftInputFromWindow(focusView.getWindowToken(), InputMethodManager.RESULT_UNCHANGED_SHOWN);
    }

    public static void toggleVirtualKeyboard(Activity activity, int showFlags, int hideFlags) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(showFlags, hideFlags);
    }

    public static int getActionBarHeight(AppCompatActivity activity) {
        int actionBarHeight = activity.getSupportActionBar().getHeight();

        if (actionBarHeight != 0) {
            return actionBarHeight;
        }

        final TypedValue tv = new TypedValue();
        if (activity.getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true))
            actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data, activity.getResources().getDisplayMetrics());

        if (actionBarHeight == 0) {
            actionBarHeight = 50;
        }

        return actionBarHeight;
    }

    public static boolean hasUrlInDetails(String input){
        Pattern p = Pattern.compile(URL_REGEX);
        Matcher m = p.matcher(input);//replace with string to compare
        if(m.find()) {
            return true;
        }
        return false;
    }

    private static Activity mContexts;

}