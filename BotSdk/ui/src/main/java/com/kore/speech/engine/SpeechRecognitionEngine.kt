package com.kore.speech.engine

import android.content.Context
import android.speech.RecognitionListener
import com.kore.speech.GoogleVoiceTypingDisabledException
import com.kore.speech.SpeechDelegate
import com.kore.speech.SpeechRecognitionNotAvailable
import java.util.Locale

interface SpeechRecognitionEngine : RecognitionListener {
    fun init(context: Context)
    fun clear()
    fun getPartialResultsAsString(): String
    fun initSpeechRecognizer(context: Context)

    @Throws(SpeechRecognitionNotAvailable::class, GoogleVoiceTypingDisabledException::class)
    fun startListening(progressView: com.kore.speech.ui.SpeechProgressView?, delegate: SpeechDelegate)
    fun stopListening()
    fun returnPartialResultsAndRecreateSpeechRecognizer()
    fun setPartialResults(getPartialResults: Boolean)
    fun shutdown()
    fun isListening(): Boolean
    fun getLocale(): Locale
    fun setPreferOffline(preferOffline: Boolean)
    fun setTransitionMinimumDelay(milliseconds: Long)
    fun setStopListeningAfterInactivity(milliseconds: Long)
    fun setCallingPackage(callingPackage: String?)
    fun unregisterDelegate()
    fun setLocale(locale: Locale)
}
