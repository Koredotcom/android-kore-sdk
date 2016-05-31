package kore.botssdk.utils;

import android.content.Context;
import android.content.pm.PackageManager;
import android.util.DisplayMetrics;

/**
 * Created by Pradeep Mahato on 30-May-16.
 */
public class Utils {

    public static String getVersion(Context context) {
        try {
            return context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return "";
        }
    }

    public static String getBuildVersion(Context context) {
        //return "v " + getServerInfo(context) + "_" + getVersion(context);
        return "v" + getVersion(context);
    }

    public static float convertDpToPixel(Context context, float dp) {

        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        if(metrics.densityDpi<=DisplayMetrics.DENSITY_HIGH){
            dp=1.4f;
        }
        float px = dp * (metrics.densityDpi / 160f);
        return px;
    }

}
