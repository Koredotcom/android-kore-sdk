package com.kore.ai.widgetsdk.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.kora.ai.widgetsdk.R;
import com.kore.ai.widgetsdk.models.CalEventsTemplateModel;

import java.util.List;


/**
 * Created by Pradeep Mahato on 30-May-16.
 * Copyright (c) 2014 Kore Inc. All rights reserved.
 */
public class Utility {

    public static String userId="";



    public static Typeface getTypeFaceObj(Context context) {
        return ResourcesCompat.getFont(context, R.font.icomoon);
    }

    public static Drawable changeColorOfDrawable(Context context, int colorCode) {
        Drawable drawable = ContextCompat.getDrawable(context, R.drawable.round_shape_common);
        try {
            ((GradientDrawable) drawable).setColor(context.getResources().getColor(colorCode));
            return drawable;
        } catch (Exception e) {
            return drawable;
        }

    }
    public static String getFormatedAttendiesFromList(List<CalEventsTemplateModel.Attendee> userDetailModels) {
        String users = "";
        if (userDetailModels != null && userDetailModels.size() > 0) {
            if (userDetailModels.size() == 1) {

                return userDetailModels.get(0).getName() != null ? userDetailModels.get(0).getName() : userDetailModels.get(0).getEmail();
            } else {
                int remaining = userDetailModels.size() - 1;
                if (remaining > 1)
                    return String.format("%1$s and %2$d others",
                            userDetailModels.get(0).getName() != null ? userDetailModels.get(0).getName() : userDetailModels.get(0).getEmail(), remaining);
                else
                    return String.format("%1$s and %2$d other",
                            userDetailModels.get(0).getName() != null ? userDetailModels.get(0).getName() : userDetailModels.get(0).getEmail(), remaining);
            }
        }
        return "";
    }

    public static boolean  isViewMoreVisible(WidgetViewMoreEnum widgetViewMoreEnum)
    {
        if(widgetViewMoreEnum==null)
        {
            return true;
        }
        return widgetViewMoreEnum==WidgetViewMoreEnum.COLLAPSE_VIEW;
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

    public static boolean checkIsSkillInCurrent(String skill) {
        if(Constants.SKILL_SELECTION.equalsIgnoreCase(skill)||TextUtils.isEmpty(Constants.SKILL_SELECTION)) {
            return true;
        }

        return false;
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
