package com.kore.common.speech

import android.os.Build
import android.util.Log
import com.kore.botclient.BuildConfig
import com.kore.common.speech.Logger.LoggerDelegate
import com.kore.common.utils.LogUtils

/**
 * Default logger delegate implementation which logs in LogCat with [Log].
 * Log tag is set to **UploadService** for all the logs.
 * @author gotev (Aleksandar Gotev)
 */
class DefaultLoggerDelegate : LoggerDelegate {
    override fun error(tag: String, message: String) {
        LogUtils.e(TAG, "$tag - $message")
    }

    override fun error(tag: String, message: String, exception: Throwable) {
        Log.e(TAG, "$tag - $message", exception)
    }

    override fun debug(tag: String, message: String) {
        if (BuildConfig.DEBUG)
            Log.d(TAG, "$tag - $message")
    }

    override fun info(tag: String, message: String) {
        if (BuildConfig.DEBUG)
            Log.i(TAG, "$tag - $message")
    }

    companion object {
        private val TAG = Speech::class.java.simpleName
    }
}
