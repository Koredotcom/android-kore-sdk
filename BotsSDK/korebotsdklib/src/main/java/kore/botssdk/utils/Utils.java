package kore.botssdk.utils;

import android.content.Context;
import android.content.pm.PackageManager;

/**
 * Created by Pradeep Mahato on 06-Jun-16.
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
        return "v" + getVersion(context);
    }
}
