package com.kore.common.speech.engine

import android.content.Context
import android.speech.RecognitionListener
import com.kore.common.speech.GoogleVoiceTypingDisabledException
import com.kore.common.speech.SpeechDelegate
import com.kore.common.speech.SpeechRecognitionNotAvailable
import com.kore.common.speech.ui.SpeechProgressView
import java.util.Locale

interface SpeechRecognitionEngine : RecognitionListener {
    fun init(context: Context)
    fun clear()
    fun getPartialResultsAsString(): String
    fun initSpeechRecognizer(context: Context)

    @Throws(SpeechRecognitionNotAvailable::class, GoogleVoiceTypingDisabledException::class)
    fun startListening(progressView: com.kore.common.speech.ui.SpeechProgressView?, delegate: SpeechDelegate)
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
