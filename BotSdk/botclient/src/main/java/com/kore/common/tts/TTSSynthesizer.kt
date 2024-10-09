package com.kore.common.tts

import android.content.Context
import android.speech.tts.TextToSpeech
import java.util.Locale

class TTSSynthesizer(context: Context) {
    private var textToSpeech: TextToSpeech? = null
    private var ttsEnabled = false
    private var language: String? = null

    init {
        initNative(context)
    }

    private fun initNative(context: Context) {
        textToSpeech = TextToSpeech(context) { status: Int ->
            if (status != TextToSpeech.ERROR) textToSpeech!!.language = Locale.US
        }
    }

    fun speak(textualMessage: String) {
        textToSpeech?.speak(textualMessage, TextToSpeech.QUEUE_FLUSH, null, null)
    }

    fun stopTextToSpeech() {
        textToSpeech?.stop()
    }

    fun isTtsEnabled(): Boolean {
        return ttsEnabled
    }

    fun setTtsEnabled(ttsEnabled: Boolean) {
        this.ttsEnabled = ttsEnabled
        if (!ttsEnabled) {
            stopTextToSpeech()
        }
    }
}