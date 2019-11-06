package kore.botssdk.utils;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

/**
 * Created by Pradeep Mahato on 30-May-16.
 * Copyright (c) 2014 Kore Inc. All rights reserved.
 */
public class Utility {
    public static boolean singleItemInList=false;

    public static boolean isIsSingleItemInList()
    {
        return singleItemInList;
    }

    public static float convertDpToPixel(Context context, float dp) {

        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        if (metrics.densityDpi <= DisplayMetrics.DENSITY_HIGH) {
            dp = 1.4f;
        }
        float px = dp * (metrics.densityDpi / 160f);
        return px;
    }

    public static void showVirtualKeyboard(Activity activity, View view) {
        showHideVirtualKeyboard(activity, view, true);
    }

    public static void hideVirtualKeyboard(Activity activity) {
        showHideVirtualKeyboard(activity, null, false);
    }

    private static void showHideVirtualKeyboard(Activity activity, View view, boolean show) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);

        if (show) {
            imm.showSoftInput(view, InputMethodManager.RESULT_UNCHANGED_SHOWN);

        } else {

            View focusView = activity.getCurrentFocus();
            if (focusView == null) {
                return;
            }

            imm.hideSoftInputFromWindow(focusView.getWindowToken(), InputMethodManager.RESULT_UNCHANGED_SHOWN);
        }
    }

    public static boolean checkIsSkillKora() {
        if(Constants.SKILL_SELECTION.equalsIgnoreCase(Constants.SKILL_HOME)||TextUtils.isEmpty(Constants.SKILL_SELECTION))
            return true;
            return false;
    }
}
