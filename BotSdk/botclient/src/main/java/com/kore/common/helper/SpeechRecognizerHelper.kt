package com.kore.common.helper

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer.RESULTS_RECOGNITION
import com.kore.botclient.R

class SpeechRecognizerHelper(private val context: Context) {
    companion object {
        private lateinit var DEFAULT_ASR: String
    }

    private lateinit var speechRecognizer: android.speech.SpeechRecognizer

    private var isRecordingRunning: Boolean = false

    private var listener: SpeechRecognitionListener? = null

    private var selectedAsr: String

    init {
        initRecognizer(context)
        DEFAULT_ASR = context.getString(R.string.default_asr)
        selectedAsr = DEFAULT_ASR
    }

    fun setSelectedAsr(selectedAsr: String) {
        this.selectedAsr = selectedAsr
    }

    fun isRecordingRunning(): Boolean = isRecordingRunning

    fun setRecordingRunning(isRecordingRunning: Boolean) {
        this.isRecordingRunning = isRecordingRunning
    }

    private fun initRecognizer(context: Context) {
        speechRecognizer = android.speech.SpeechRecognizer.createSpeechRecognizer(context)
        speechRecognizer.setRecognitionListener(object : RecognitionListener {
            override fun onReadyForSpeech(params: Bundle) {
                isRecordingRunning = true
                listener?.onReadyForSpeech(params)
            }

            override fun onBeginningOfSpeech() {
                isRecordingRunning = true
                listener?.onBeginningOfSpeech()
            }

            override fun onRmsChanged(rmsdB: Float) {
                listener?.onRmsChanged(rmsdB)
            }

            override fun onBufferReceived(buffer: ByteArray?) {}

            override fun onResults(results: Bundle) {
                isRecordingRunning = false
                val matches = results.getStringArrayList(RESULTS_RECOGNITION) as ArrayList<String>
                listener?.onResults(matches.first())
            }

            override fun onPartialResults(partialResults: Bundle?) {
                listener?.onPartialResults(partialResults)
            }

            override fun onEvent(eventType: Int, params: Bundle?) {}

            override fun onEndOfSpeech() {
                isRecordingRunning = false
                listener?.onEndOfSpeech()
            }

            override fun onError(error: Int) {
                isRecordingRunning = false
                listener?.onError(error)
            }
        })
    }

    interface SpeechRecognitionListener {
        fun onReadyForSpeech(params: Bundle) {}
        fun onBeginningOfSpeech() {}
        fun onRmsChanged(rmsdB: Float) {}
        fun onResults(message: String?)
        fun onPartialResults(partialResults: Bundle?) {}
        fun onEndOfSpeech() {}
        fun onError(error: Int) {}
        fun startOtherRecognition()
        fun stopOtherRecognition()
        fun cancelOtherRecognition()
    }

    fun startListening() {
        if (isRecordingRunning) return

        when (selectedAsr) {
            DEFAULT_ASR -> {
                val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
                intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, context.packageName)
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
                speechRecognizer.startListening(intent)
            }

            else -> {
                listener?.startOtherRecognition()
            }
        }
    }

    fun stopListening() {
        when (selectedAsr) {
            DEFAULT_ASR -> speechRecognizer.stopListening()
            else -> listener?.stopOtherRecognition()
        }
        isRecordingRunning = false
    }

    fun cancel() {
        when (selectedAsr) {
            DEFAULT_ASR -> speechRecognizer.cancel()
            else -> listener?.cancelOtherRecognition()
        }
        isRecordingRunning = false
    }

    fun setRecognitionListener(listener: SpeechRecognitionListener) {
        this.listener = listener
    }
}