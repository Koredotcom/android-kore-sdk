package com.kore.common.speech.engine

import android.content.Context
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import android.speech.tts.Voice
import com.kore.common.speech.Logger
import com.kore.common.speech.TextToSpeechCallback
import com.kore.common.speech.TtsProgressListener
import java.util.Locale
import java.util.UUID

class BaseTextToSpeechEngine : TextToSpeechEngine {
    private var mTextToSpeech: TextToSpeech? = null
    private var mTttsInitListener: TextToSpeech.OnInitListener? = null
    private var mTtsProgressListener: UtteranceProgressListener? = null
    private var mTtsRate = 1.0f
    private var mTtsPitch = 1.0f
    private var mLocale = Locale.getDefault()
    private var voice: Voice? = null
    private var mTtsQueueMode: Int = TextToSpeech.QUEUE_FLUSH
    private var mAudioStream: Int = TextToSpeech.Engine.DEFAULT_STREAM
    private val mTtsCallbacks: MutableMap<String, TextToSpeechCallback> =
        HashMap<String, TextToSpeechCallback>()

    override fun initTextToSpeech(context: Context) {
        if (mTextToSpeech != null) {
            return
        }
        mTtsProgressListener = TtsProgressListener(context, mTtsCallbacks)
        mTextToSpeech = TextToSpeech(context.applicationContext, mTttsInitListener)
        mTextToSpeech!!.setOnUtteranceProgressListener(mTtsProgressListener)
        mTextToSpeech!!.setLanguage(mLocale)
        mTextToSpeech!!.setPitch(mTtsPitch)
        mTextToSpeech!!.setSpeechRate(mTtsRate)
        if (voice == null) {
            voice = mTextToSpeech!!.defaultVoice
        }
        mTextToSpeech!!.setVoice(voice)
    }

    override fun isSpeaking(): Boolean {
        return mTextToSpeech!!.isSpeaking
    }

    override fun setOnInitListener(onInitListener: TextToSpeech.OnInitListener) {
        mTttsInitListener = onInitListener
    }

    override fun setLocale(locale: Locale) {
        mLocale = locale
        mTextToSpeech?.setLanguage(locale)
    }

    override fun say(message: String, callback: TextToSpeechCallback) {
        val utteranceId = UUID.randomUUID().toString()
        mTtsCallbacks[utteranceId] = callback
        val params = Bundle()
        params.putString(TextToSpeech.Engine.KEY_PARAM_STREAM, mAudioStream.toString())
        params.putString(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, utteranceId)
        mTextToSpeech?.speak(message, mTtsQueueMode, params, utteranceId)
    }

    override fun shutdown() {
        if (mTextToSpeech != null) {
            try {
                mTtsCallbacks.clear()
                mTextToSpeech!!.stop()
                mTextToSpeech!!.shutdown()
            } catch (exc: Exception) {
                Logger.error(javaClass.simpleName, "Warning while de-initing text to speech", exc)
            }
        }
    }

    override fun setTextToSpeechQueueMode(mode: Int) {
        mTtsQueueMode = mode
    }

    override fun setAudioStream(audioStream: Int) {
        mAudioStream = audioStream
    }

    override fun stop() {
        mTextToSpeech?.stop()
    }

    override fun setPitch(pitch: Float) {
        mTtsPitch = pitch
        mTextToSpeech?.setPitch(pitch)
    }

    override fun setSpeechRate(rate: Float) {
        mTtsRate = rate
        mTextToSpeech?.setSpeechRate(rate)
    }

    override fun setVoice(voice: Voice) {
        this.voice = voice
        mTextToSpeech?.setVoice(voice)
    }

    override fun getSupportedVoices(): List<Voice> {
        if (mTextToSpeech != null) {
            val voices: Set<Voice> = mTextToSpeech!!.voices
            val voicesList: ArrayList<Voice> = ArrayList(voices.size)
            voicesList.addAll(voices)
            return voicesList
        }
        return ArrayList(1)
    }

    override fun getCurrentVoice(): Voice? {
        return mTextToSpeech?.voice
    }
}
