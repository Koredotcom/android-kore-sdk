package com.kore.common.speech

import android.content.Context
import android.os.Handler
import android.speech.tts.UtteranceProgressListener
import java.lang.ref.WeakReference

/**
 * @author Kristiyan Petrov (kristiyan@igenius.net)
 */
class TtsProgressListener(
    context: Context,
    private val mTtsCallbacks: MutableMap<String, TextToSpeechCallback>
) : UtteranceProgressListener() {
    private val contextWeakReference: WeakReference<Context>

    init {
        contextWeakReference = WeakReference(context)
    }

    override fun onStart(utteranceId: String) {
        val callback = mTtsCallbacks[utteranceId]
        val context = contextWeakReference.get()
        if (callback != null && context != null) {
            Handler(context.mainLooper).post { callback.onStart() }
        }
    }

    override fun onDone(utteranceId: String) {
        val callback = mTtsCallbacks[utteranceId]
        val context = contextWeakReference.get()
        if (callback != null && context != null) {
            Handler(context.mainLooper).post {
                callback.onCompleted()
                mTtsCallbacks.remove(utteranceId)
            }
        }
    }

    override fun onError(utteranceId: String) {
        val callback = mTtsCallbacks[utteranceId]
        val context = contextWeakReference.get()
        if (callback != null && context != null) {
            Handler(context.mainLooper).post {
                callback.onError()
                mTtsCallbacks.remove(utteranceId)
            }
        }
    }
}
