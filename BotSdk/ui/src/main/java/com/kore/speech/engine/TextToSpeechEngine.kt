package com.kore.speech.engine

import android.content.Context
import android.speech.tts.TextToSpeech
import android.speech.tts.Voice
import com.kore.speech.TextToSpeechCallback
import java.util.Locale

interface TextToSpeechEngine {
    fun initTextToSpeech(context: Context)
    fun isSpeaking(): Boolean
    fun say(message: String, callback: TextToSpeechCallback)
    fun stop()
    fun shutdown()
    fun setTextToSpeechQueueMode(mode: Int)
    fun setAudioStream(audioStream: Int)
    fun setOnInitListener(onInitListener: TextToSpeech.OnInitListener)
    fun setPitch(pitch: Float)
    fun setSpeechRate(rate: Float)
    fun setLocale(locale: Locale)
    fun setVoice(voice: Voice)
    fun getSupportedVoices(): List<Any>
    fun getCurrentVoice(): Voice?


}
