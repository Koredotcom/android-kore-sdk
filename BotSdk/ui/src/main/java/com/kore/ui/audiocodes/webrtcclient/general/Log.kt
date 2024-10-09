package com.kore.ui.audiocodes.webrtcclient.general

import android.util.Log

object Log {
    private const val APP_PREFIX_TAG = "WEBRTC"

    //private static int logLevel = 3;
    private var logLevel = LogLevel.DEBUG

    //    public static final int HIDDEN = 1;
    //    public static final int VERBOSE = 2;
    //    public static final int DEBUG = 3;
    //    public static final int INFO = 4;
    //    public static final int WARN = 5;
    //    public static final int ERROR = 6;
    //    public static final int NONE = 100;
    fun setLogLevel(tempLogLevel: LogLevel) {
        logLevel = tempLogLevel
    }

    //    public static void setLogLevel(int tempLogLevel) {
    //        logLevel=tempLogLevel;
    //    }
    fun h(tag1: String?, msg: String?): Int {
        var tag = tag1
        if (logLevel.value > LogLevel.HIDDEN.value) {
            return 0
        }
        tag = editTag(tag)
        return Log.println(Log.VERBOSE, tag, msg!!) //android.util.Log.v(tag, msg);
    }

    @JvmStatic
    fun v(tag1: String?, msg: String?): Int {
        var tag = tag1
        if (logLevel.value > LogLevel.VERBOSE.value) {
            return 0
        }
        tag = editTag(tag)
        return Log.println(Log.VERBOSE, tag, msg!!) //android.util.Log.v(tag, msg);
    }

    @JvmStatic
    fun d(tag1: String?, msg: String?): Int {
        var tag = tag1
        if (logLevel.value > LogLevel.DEBUG.value) {
            return 0
        }
        tag = editTag(tag)
        return Log.println(Log.DEBUG, tag, msg!!) //android.util.Log.v(tag, msg);
    }

    @JvmStatic
    fun i(tag1: String?, msg: String?): Int {
        var tag = tag1
        if (logLevel.value > LogLevel.INFO.value) {
            return 0
        }
        tag = editTag(tag)
        return Log.println(Log.INFO, tag, msg!!) //android.util.Log.v(tag, msg);
    }

    @JvmStatic
    fun w(tag1: String?, msg: String?): Int {
        var tag = tag1
        if (logLevel.value > LogLevel.WARN.value) {
            return 0
        }
        tag = editTag(tag)
        return Log.println(Log.WARN, tag, msg!!) //android.util.Log.v(tag, msg);
    }

    @JvmStatic
    fun e(tag1: String?, msg: String?): Int {
        var tag = tag1
        if (logLevel.value > LogLevel.ERROR.value) {
            return 0
        }
        tag = editTag(tag)
        return Log.println(Log.ERROR, tag, msg!!) //android.util.Log.v(tag, msg);
    }

    fun editTag(tag1: String?): String? {
        var tag = tag1
        if (tag != null) {
            tag = APP_PREFIX_TAG + tag
        }
        return tag
    } //    public static int printLog(String tag, String msg)

    //    {
    //        return android.util.Log.v(tag, msg);
    //    }
    enum class LogLevel(val value: Int) {
        HIDDEN(1), VERBOSE(2), DEBUG(3), INFO(4), WARN(5), ERROR(6), NONE(100);

        companion object {
            fun findValue(value: Int): LogLevel {
                for (v in values()) {
                    if (v.value == value) return v
                }
                return DEBUG
            }
        }
    }
}
