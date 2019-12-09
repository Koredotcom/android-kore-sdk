package kore.botssdk.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by Pradeep Mahato on 30-May-16.
 * Copyright (c) 2014 Kore Inc. All rights reserved.
 */
public class Utility {

    public static boolean singleItemInList=false;
    public static String userId="";

    public static boolean isIsSingleItemInList()
    {
        return singleItemInList;
    }


    public static RecyclerView getRecyclerViewTempForOnboard() {
        return recyclerViewTempForOnboard;
    }

    public static void setRecyclerViewTempForOnboard(RecyclerView recyclerViewTempForOnboard, String id) {
        if(Utility.recyclerViewTempForOnboard==null||(recyclerKey!=null&&recyclerKey.equals(id))) {
            Utility.recyclerViewTempForOnboard = recyclerViewTempForOnboard;
            recyclerKey=id;
       }
    }

    private static RecyclerView recyclerViewTempForOnboard;
    public static RecyclerView rootrecyclerView;
    private static String recyclerKey;

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

    public static int getDarkerColor (int color, float factor) {
        int a = Color.alpha( color );
        int r = Color.red( color );
        int g = Color.green( color );
        int b = Color.blue( color );

        return Color.argb( a,
                Math.max( (int)(r * factor), 0 ),
                Math.max( (int)(g * factor), 0 ),
                Math.max( (int)(b * factor), 0 ) );
    }
}
