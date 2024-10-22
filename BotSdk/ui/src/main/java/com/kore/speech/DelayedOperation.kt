package com.kore.speech

import android.content.Context
import android.os.Handler
import java.util.Timer
import java.util.TimerTask

/**
 * @author Aleksandar Gotev
 */
class DelayedOperation(context: Context?, tag: String, delayInMilliseconds: Long) {
    interface Operation {
        fun onDelayedOperation()
        fun shouldExecuteDelayedOperation(): Boolean
    }

    private val mDelay: Long
    private var mOperation: Operation? = null
    private var mTimer: Timer? = null
    private var started = false
    private val mContext: Context
    private val mTag: String

    init {
        requireNotNull(context) { "Context is null" }
        require(delayInMilliseconds > 0) { "The delay in milliseconds must be > 0" }
        mContext = context
        mTag = tag
        mDelay = delayInMilliseconds
        Logger.debug(LOG_TAG, "created delayed operation with tag: $mTag")
    }

    fun start(operation: Operation?) {
        requireNotNull(operation) { "The operation must be defined!" }
        Logger.debug(LOG_TAG, "starting delayed operation with tag: $mTag")
        mOperation = operation
        cancel()
        started = true
        resetTimer()
    }

    fun resetTimer() {
        if (!started) return
        if (mTimer != null) mTimer!!.cancel()
        Logger.debug(LOG_TAG, "resetting delayed operation with tag: $mTag")
        mTimer = Timer()
        mTimer!!.schedule(object : TimerTask() {
            override fun run() {
                if (mOperation!!.shouldExecuteDelayedOperation()) {
                    Logger.debug(LOG_TAG, "executing delayed operation with tag: $mTag")
                    Handler(mContext.mainLooper).post { mOperation!!.onDelayedOperation() }
                }
                cancel()
            }
        }, mDelay)
    }

    fun cancel() {
        if (mTimer != null) {
            Logger.debug(LOG_TAG, "cancelled delayed operation with tag: $mTag")
            mTimer!!.cancel()
            mTimer = null
        }
        started = false
    }

    companion object {
        private val LOG_TAG = DelayedOperation::class.java.simpleName
    }
}
