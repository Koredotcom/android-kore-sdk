package com.kore.speech

/**
 * Contains the methods which are called to notify text to speech progress status.
 *
 * @author Aleksandar Gotev
 */
interface TextToSpeechCallback {
    fun onStart()
    fun onCompleted()
    fun onError()
}
