package kore.botssdk.application;

import android.content.Context;

import kore.botssdk.view.viewUtils.DimensionUtil;

/**
 * Created by Pradeep Mahato on 31-May-16.
 */
public class AppControl {

    Context context;
    static AppControl singleton;
    DimensionUtil dimensionUtil;

    public AppControl(Context context) {
        this.context = context;
        singleton = this;

        dimensionUtil = new DimensionUtil(context);
    }

    public static AppControl getInstance() {
        return singleton;
    }

    public DimensionUtil getDimensionUtil() {
        return dimensionUtil;
    }
}
