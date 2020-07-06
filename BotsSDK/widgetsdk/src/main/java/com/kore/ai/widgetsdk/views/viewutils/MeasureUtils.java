package com.kore.ai.widgetsdk.views.viewutils;

import android.view.View;

/**
 * Created by Pradeep Mahato on 31-May-16.
 * Copyright (c) 2014 Kore Inc. All rights reserved.
 */
public class MeasureUtils {

    public static void measure(View view) {
        int wrapSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        measure(view, wrapSpec, wrapSpec);
    }

    public static void measure(View view, int widthMeasureSpec, int heightMeasureSpec) {
        if (view != null && (view.getVisibility() == View.VISIBLE || view.getVisibility() == View.INVISIBLE)) {
            view.measure(widthMeasureSpec, heightMeasureSpec);
        } else {
            int zeroSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.EXACTLY);
            view.measure(zeroSpec, zeroSpec);
        }
    }
}
