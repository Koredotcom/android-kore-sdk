package kore.botssdk.voicemode.utils;

import android.util.Log;

/**
 * Helper class for logging with debug mode support.
 */
public class LogHelper {

    private static final String TAG_PREFIX = "KoreWebRTC";
    private static boolean debugEnabled = false;

    /**
     * Enable or disable debug logging.
     * @param enabled true to enable debug logging
     */
    public static void setDebugEnabled(boolean enabled) {
        debugEnabled = enabled;
    }

    /**
     * Check if debug is enabled.
     * @return true if debug logging is enabled
     */
    public static boolean isDebugEnabled() {
        return debugEnabled;
    }

    /**
     * Log debug message (only if debug is enabled).
     * @param tag Tag for the log
     * @param message Message to log
     */
    public static void d(String tag, String message) {
        if (debugEnabled) {
            Log.d(TAG_PREFIX + ":" + tag, message);
        }
    }

    /**
     * Log info message.
     * @param tag Tag for the log
     * @param message Message to log
     */
    public static void i(String tag, String message) {
        Log.i(TAG_PREFIX + ":" + tag, message);
    }

    /**
     * Log warning message.
     * @param tag Tag for the log
     * @param message Message to log
     */
    public static void w(String tag, String message) {
        Log.w(TAG_PREFIX + ":" + tag, message);
    }

    /**
     * Log error message.
     * @param tag Tag for the log
     * @param message Message to log
     */
    public static void e(String tag, String message) {
        Log.e(TAG_PREFIX + ":" + tag, message);
    }

    /**
     * Log error message with exception.
     * @param tag Tag for the log
     * @param message Message to log
     * @param throwable Exception to log
     */
    public static void e(String tag, String message, Throwable throwable) {
        Log.e(TAG_PREFIX + ":" + tag, message, throwable);
    }
}
