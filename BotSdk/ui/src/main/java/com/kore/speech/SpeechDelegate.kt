package com.kore.speech

/**
 * Speech delegate interface. It contains the methods to receive speech events.
 *
 * @author Aleksandar Gotev
 */
interface SpeechDelegate {
    /**
     * Invoked when the speech recognition is started.
     */
    fun onStartOfSpeech()

    /**
     * The sound level in the audio stream has changed.
     * There is no guarantee that this method will be called.
     * @param value the new RMS dB value
     */
    fun onSpeechRmsChanged(value: Float)

    /**
     * Invoked when there are partial speech results.
     * @param results list of strings. This is ensured to be non null and non empty.
     */
    fun onSpeechPartialResults(results: List<String?>?)

    /**
     * Invoked when there is a speech result
     * @param result string resulting from speech recognition.
     * This is ensured to be non null.
     */
    fun onSpeechResult(result: String?)
}
