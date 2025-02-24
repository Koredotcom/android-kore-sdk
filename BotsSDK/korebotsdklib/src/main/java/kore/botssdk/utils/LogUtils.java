package kore.botssdk.utils;

import androidx.annotation.NonNull;

import java.util.logging.Level;
import java.util.logging.Logger;

public class LogUtils {
    static boolean LOG = true;
    private static final Logger logger = Logger.getLogger(LogUtils.class.getName());
    public static void enableLog(boolean enable) {
        LOG = enable;
    }

    public static void i(@NonNull String tag,@NonNull String string) {
        if (LOG) android.util.Log.i(tag, string);
    }

    public static void e(@NonNull String tag,@NonNull String string) {
        if (LOG) android.util.Log.e(tag, string);
    }

    public static void d(@NonNull String tag, @NonNull String string) {
        if (LOG) android.util.Log.d(tag, string);
    }

    public static void v(@NonNull String tag, @NonNull String string) {
        if (LOG) android.util.Log.v(tag, string);
    }

    public static void w(@NonNull String tag, @NonNull String string) {
        if (LOG) android.util.Log.w(tag, string);
    }

    public static boolean isEnabled() {
        return LOG;
    }

    public static void stackTrace(@NonNull Exception e) {
        if (LOG) logger.log(Level.SEVERE, "An error occurred", e);
    }
}
