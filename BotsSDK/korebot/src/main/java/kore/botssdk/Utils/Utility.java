package kore.botssdk.utils;

import android.content.Context;
import android.content.pm.PackageManager;
import android.util.DisplayMetrics;

/**
 * Created by Pradeep Mahato on 30-May-16.
 * Copyright (c) 2014 Kore Inc. All rights reserved.
 */
public class Utility {

    public static float convertDpToPixel(Context context, float dp) {

        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        if(metrics.densityDpi<=DisplayMetrics.DENSITY_HIGH){
            dp=1.4f;
        }
        float px = dp * (metrics.densityDpi / 160f);
        return px;
    }

}
