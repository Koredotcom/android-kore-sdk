package kore.botssdk.Utils;

import android.content.Context;
import android.content.pm.PackageManager;

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

}
