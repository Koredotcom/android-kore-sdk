package com.kore.common.helper

import android.content.Context
import android.media.AudioManager
import android.os.Handler
import android.os.Looper
import com.kore.botclient.R
import com.kore.common.tts.TTSSynthesizer

class SpeechSynthesizerHelper(context: Context) {
    companion object {
        private lateinit var DEFAULT_TTS: String
    }

    private var audioManager: AudioManager = context.applicationContext?.getSystemService(Context.AUDIO_SERVICE) as AudioManager
    private val ttsSynthesizer: TTSSynthesizer = TTSSynthesizer(context)
    private var listener: SynthesizerListener? = null
    private var selectedTts: String

    init {
        DEFAULT_TTS = context.getString(R.string.default_asr)
        selectedTts = DEFAULT_TTS
    }

    fun setListener(listener: SynthesizerListener) {
        this.listener = listener
    }

    private fun onMicroPhone() {
        Handler(Looper.getMainLooper()).postDelayed({
            audioManager.isMicrophoneMute = false
        }, 500)
    }

    fun startSpeak(msg: String?) {
        if (!msg.isNullOrEmpty()) {
            when (selectedTts) {
                DEFAULT_TTS -> ttsSynthesizer.speak(msg)
                else -> listener?.startOtherSynthesizer(msg)
            }
        }
    }

    fun stopSpeak() {
        when (selectedTts) {
            DEFAULT_TTS -> ttsSynthesizer.stopTextToSpeech()
            else -> listener?.stopOtherSynthesizer()
        }
    }

    fun isTTSEnabled(): Boolean {
        return ttsSynthesizer.isTtsEnabled()
    }

    fun setIsTtsEnabled(enabled: Boolean) {
        ttsSynthesizer.setTtsEnabled(enabled)
    }

    interface SynthesizerListener {
        fun startOtherSynthesizer(message: String)
        fun stopOtherSynthesizer()
    }
}