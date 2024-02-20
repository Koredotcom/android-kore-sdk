package kore.botssdk.view.viewUtils;

import android.content.Context;
import android.content.res.Resources;

import kore.botssdk.utils.Utility;

/**
 * Created by Pradeep Mahato on 31-May-16.
 * Copyright (c) 2014 Kore Inc. All rights reserved.
 */
public class DimensionUtil {

    final Context context;
    public static float dp1;
    public float screenHeight = 0;
    public float screenWidth = 0;
    public float density;

    public DimensionUtil(Context context) {
        this.context = context;
        init();
    }

    private void init() {
        dp1 = Utility.convertDpToPixel(context, 1);
        screenHeight = Resources.getSystem().getDisplayMetrics().heightPixels;
        screenWidth = Resources.getSystem().getDisplayMetrics().widthPixels;
        density = Resources.getSystem().getDisplayMetrics().density;
    }

}
