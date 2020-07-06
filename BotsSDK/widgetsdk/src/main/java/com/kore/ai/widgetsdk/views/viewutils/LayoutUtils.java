package com.kore.ai.widgetsdk.views.viewutils;

import android.view.View;

/**
 * Created by Pradeep Mahato on 31-May-16.
 * Copyright (c) 2014 Kore Inc. All rights reserved.
 */
public class LayoutUtils {

    public static void layoutChild(View v, int l, int t) {
        v.layout(l, t, l + v.getMeasuredWidth(), t + v.getMeasuredHeight());
    }

    public static void layoutChildByAligningRightHorizontally(View v, int l, int t, int centeringBoundWidth) {
        if (v.getVisibility() != View.GONE) {
            int startingOffset = l + (centeringBoundWidth - v.getMeasuredWidth());
            layoutChild(v, startingOffset, t);
        }
    }

    public static void layoutChildByCenteringItHorizontally(View v, int l, int t, int centeringBoundWidth) {
        if (v.getVisibility() != View.GONE) {
            int startingOffset = l + (centeringBoundWidth / 2 - v.getMeasuredWidth() / 2);
            layoutChild(v, startingOffset, t);
        }
    }

    public static void layoutChildByCenteringItVertically(View v, int l, int t, int centeringBoundHeight) {
        if (v.getVisibility() != View.GONE) {
            int startingOffset = t + (centeringBoundHeight / 2 - v.getMeasuredHeight() / 2);
            layoutChild(v, l, startingOffset);
        }
    }

    public static void layoutChildByCenteringItInParent(View v, int l, int t, int centeringBoundWidth, int centeringBoundHeight) {
        if (v.getVisibility() != View.GONE) {
            int startingOffsetX = l + (centeringBoundWidth / 2 - v.getMeasuredWidth() / 2);
            int startingOffsetY = t + (centeringBoundHeight / 2 - v.getMeasuredHeight() / 2);
            layoutChild(v, startingOffsetX, startingOffsetY);
        }
    }
}
