/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.util.Log
 */
package com.korevg.mv.webrtcsdk.log;

import com.korevg.mv.webrtcsdk.log.ILog;
import com.korevg.mv.webrtcsdk.log.LogLevel;

public class Log {
    private static String TAG_PREFIX = "AC";
    private static LogLevel logLevel = LogLevel.VERBOSE;
    private static ILog logger = null;

    private Log() {
    }

    public static synchronized ILog getLogger() {
        return logger;
    }

    public static void setLogger(ILog log) {
        logger = log;
    }

    public static void setLogLevel(LogLevel logLevel) {
        Log.logLevel = logLevel;
    }

    public static LogLevel getLogLevel() {
        return logLevel;
    }

    public static int v(String tag, String msg) {
        if (logLevel.getValue() > LogLevel.VERBOSE.getValue()) {
            return 0;
        }
        tag = Log.editTag(tag);
        if (logger != null) {
            return logger.v(tag, msg);
        }
        return android.util.Log.v((String)tag, (String)msg);
    }

    public static int v(String tag, String msg, Throwable e) {
        if (logLevel.getValue() > LogLevel.VERBOSE.getValue()) {
            return 0;
        }
        tag = Log.editTag(tag);
        if (logger != null) {
            return logger.v(tag, msg);
        }
        return android.util.Log.v((String)tag, (String)msg, (Throwable)e);
    }

    public static int d(String tag, String msg) {
        if (logLevel.getValue() > LogLevel.DEBUG.getValue()) {
            return 0;
        }
        tag = Log.editTag(tag);
        if (logger != null) {
            return logger.d(tag, msg);
        }
        return android.util.Log.d((String)tag, (String)msg);
    }

    public static int d(String tag, String msg, Throwable e) {
        if (logLevel.getValue() > LogLevel.DEBUG.getValue()) {
            return 0;
        }
        tag = Log.editTag(tag);
        if (logger != null) {
            return logger.d(tag, msg);
        }
        return android.util.Log.d((String)tag, (String)msg, (Throwable)e);
    }

    public static int i(String tag, String msg) {
        if (logLevel.getValue() > LogLevel.INFO.getValue()) {
            return 0;
        }
        tag = Log.editTag(tag);
        if (logger != null) {
            return logger.i(tag, msg);
        }
        return android.util.Log.i((String)tag, (String)msg);
    }

    public static int i(String tag, String msg, Throwable e) {
        if (logLevel.getValue() > LogLevel.INFO.getValue()) {
            return 0;
        }
        tag = Log.editTag(tag);
        if (logger != null) {
            return logger.i(tag, msg);
        }
        return android.util.Log.i((String)tag, (String)msg, (Throwable)e);
    }

    public static int w(String tag, String msg) {
        if (logLevel.getValue() > LogLevel.WARN.getValue()) {
            return 0;
        }
        tag = Log.editTag(tag);
        if (logger != null) {
            return logger.w(tag, msg);
        }
        return android.util.Log.w((String)tag, (String)msg);
    }

    public static int w(String tag, String msg, Throwable e) {
        if (logLevel.getValue() > LogLevel.WARN.getValue()) {
            return 0;
        }
        tag = Log.editTag(tag);
        if (logger != null) {
            return logger.w(tag, msg);
        }
        return android.util.Log.w((String)tag, (String)msg, (Throwable)e);
    }

    public static int e(String tag, String msg) {
        if (logLevel.getValue() > LogLevel.ERROR.getValue()) {
            return 0;
        }
        tag = Log.editTag(tag);
        if (logger != null) {
            return logger.e(tag, msg);
        }
        return android.util.Log.e((String)tag, (String)msg);
    }

    public static int e(String tag, String msg, Throwable e) {
        if (logLevel.getValue() > LogLevel.ERROR.getValue()) {
            return 0;
        }
        tag = Log.editTag(tag);
        if (logger != null) {
            return logger.e(tag, msg, e);
        }
        return android.util.Log.e((String)tag, (String)msg, (Throwable)e);
    }

    private static String editTag(String tag) {
        if (tag != null) {
            tag = TAG_PREFIX + tag;
        }
        return tag;
    }

    public static String getTagPrefix() {
        return TAG_PREFIX;
    }

    public static void setTagPrefix(String tagPrefix) {
        TAG_PREFIX = tagPrefix;
    }
}

