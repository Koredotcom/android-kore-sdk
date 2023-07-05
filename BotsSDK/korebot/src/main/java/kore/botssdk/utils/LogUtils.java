package kore.botssdk.utils;

public class LogUtils
{
    static boolean LOG = false;

    public static void enableLog(boolean enable) {
        LOG = enable;
    }

    public static void info(String tag, String string) {
        if (LOG) android.util.Log.i(tag, string);
    }
    public static void error(String tag, String string) {
        if (LOG) android.util.Log.e(tag, string);
    }
    public static void debug(String tag, String string) {
        if (LOG) android.util.Log.d(tag, string);
    }
    public static void verbose(String tag, String string) {
        if (LOG) android.util.Log.v(tag, string);
    }
    public static void warn(String tag, String string) {
        if (LOG) android.util.Log.w(tag, string);
    }

    public static boolean isEnabled()
    {
        return LOG;
    }
}
