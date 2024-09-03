package kore.botssdk.viewUtils;

import android.view.View;

/*
 * Copyright (c) 2014 Kore Inc. All rights reserved.
 */
public class LayoutUtils {

    public static void layoutChild(View v, int l, int t) {
        v.layout(l, t, l + v.getMeasuredWidth(), t + v.getMeasuredHeight());
    }

}
