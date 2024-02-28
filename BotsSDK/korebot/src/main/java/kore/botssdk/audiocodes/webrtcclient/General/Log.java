package kore.botssdk.audiocodes.webrtcclient.General;


public class Log {


    private static String APP_PREFIX_TAG="WEBRTC";

    public static enum LogLevel
    {
        HIDDEN(1),
        VERBOSE(2),
        DEBUG(3),
        INFO(4),
        WARN(5),
        ERROR(6),
        NONE(100);

        private final int value;

        LogLevel(int value)
        {
            this.value = value;
        }

        public int getValue()
        {
            return value;
        }

        public static LogLevel findValue(int value)
        {
            for (LogLevel v : values())
            {
                if (v.value == value)
                    return v;
            }
            return LogLevel.DEBUG;
        }
    }

    //private static int logLevel = 3;
    private static LogLevel logLevel = LogLevel.DEBUG;
//    public static final int HIDDEN = 1;
//    public static final int VERBOSE = 2;
//    public static final int DEBUG = 3;
//    public static final int INFO = 4;
//    public static final int WARN = 5;
//    public static final int ERROR = 6;
//    public static final int NONE = 100;

    public static void setLogLevel(LogLevel tempLogLevel) {
        logLevel=tempLogLevel;
    }

//    public static void setLogLevel(int tempLogLevel) {
//        logLevel=tempLogLevel;
//    }

    public static int h(String tag, String msg)
    {
        if(logLevel.value > LogLevel.HIDDEN.value) {
            return 0;
        }
        tag=editTag(tag);
        return android.util.Log.println(android.util.Log.VERBOSE, tag, msg);//android.util.Log.v(tag, msg);
    }
    public static int v(String tag, String msg)
    {
        if(logLevel.value > LogLevel.VERBOSE.value) {
            return 0;
        }
        tag=editTag(tag);
        return android.util.Log.println(android.util.Log.VERBOSE, tag, msg);//android.util.Log.v(tag, msg);
    }
    public static int d(String tag, String msg)
    {
        if(logLevel.value > LogLevel.DEBUG.value) {
            return 0;
        }
        tag=editTag(tag);

        return android.util.Log.println(android.util.Log.DEBUG,tag, msg);//android.util.Log.v(tag, msg);
    }
    public static int i(String tag, String msg)
    {
        if(logLevel.value > LogLevel.INFO.value) {
            return 0;
        }
        tag=editTag(tag);
        return android.util.Log.println(android.util.Log.INFO, tag, msg);//android.util.Log.v(tag, msg);
    }
    public static int w(String tag, String msg)
    {
        if(logLevel.value > LogLevel.WARN.value) {
            return 0;
        }
        tag=editTag(tag);
        return android.util.Log.println(android.util.Log.WARN, tag, msg);//android.util.Log.v(tag, msg);
    }
    public static int e(String tag, String msg)
    {
        if(logLevel.value > LogLevel.ERROR.value) {
            return 0;
        }
        tag=editTag(tag);
        return android.util.Log.println(android.util.Log.ERROR, tag, msg);//android.util.Log.v(tag, msg);
    }

    public static String editTag(String tag)
    {
        if(tag!=null)
        {
            tag=APP_PREFIX_TAG+tag;
        }
        return tag;
    }
//    public static int printLog(String tag, String msg)
//    {
//        return android.util.Log.v(tag, msg);
//    }
}
