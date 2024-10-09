package com.kore.ui.audiocodes.webrtcclient.general

import com.audiocodes.mv.webrtcsdk.log.ILog

class LogI : ILog {
    override fun v(s: String, s1: String): Int {
        return Log.v(s, s1)
    }

    override fun v(s: String, s1: String, throwable: Throwable): Int {
        return Log.v(s, s1)
    }

    override fun d(s: String, s1: String): Int {
        return Log.d(s, s1)
    }

    override fun d(s: String, s1: String, throwable: Throwable): Int {
        return Log.d(s, s1)
    }

    override fun i(s: String, s1: String): Int {
        return Log.i(s, s1)
    }

    override fun i(s: String, s1: String, throwable: Throwable): Int {
        return Log.i(s, s1)
    }

    override fun w(s: String, s1: String): Int {
        return Log.w(s, s1)
    }

    override fun w(s: String, s1: String, throwable: Throwable): Int {
        return Log.w(s, s1)
    }

    override fun e(s: String, s1: String): Int {
        return Log.e(s, s1)
    }

    override fun e(s: String, s1: String, throwable: Throwable): Int {
        return Log.e(s, s1)
    }
}
