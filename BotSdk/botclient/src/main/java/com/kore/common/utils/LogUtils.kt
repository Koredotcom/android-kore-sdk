package com.kore.common.utils

import android.util.Log
import com.kore.botclient.BuildConfig

object LogUtils {
    private var isEnabled = BuildConfig.DEBUG
    fun enableLog(enable: Boolean) {
        isEnabled = enable
    }

    fun i(tag: String, string: String?) {
        if (isEnabled) Log.i(tag, "$string")
    }

    fun e(tag: String, string: String?) {
        if (isEnabled) Log.e(tag, "$string")
    }

    fun d(tag: String, string: String?) {
        if (isEnabled) Log.d(tag, "$string")
    }

    fun v(tag: String, string: String?) {
        if (isEnabled) Log.v(tag, "$string")
    }

    fun w(tag: String, string: String?) {
        if (isEnabled) Log.w(tag, "$string")
    }
}