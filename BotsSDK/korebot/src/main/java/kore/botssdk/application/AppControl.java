package kore.botssdk.application;

import android.content.Context;

import kore.botssdk.view.viewUtils.DimensionUtil;

/**
 * Created by Pradeep Mahato on 31-May-16.
 * Copyright (c) 2014 Kore Inc. All rights reserved.
 */
public class AppControl {
    private static AppControl singleton;
    private final DimensionUtil dimensionUtil;

    public AppControl(Context context) {
        singleton = this;

        dimensionUtil = new DimensionUtil(context);
    }

    public static AppControl getInstance() {
        return singleton;
    }
    public static AppControl getInstance(Context mContext) {
        if(singleton == null){
            singleton = new AppControl(mContext);
        }
        return singleton;
    }

    public DimensionUtil getDimensionUtil() {
        return dimensionUtil;
    }
}
