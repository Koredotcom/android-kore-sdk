package kore.botssdk.speech;

import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.SpeechRecognizer;

public class KoreSpeech {
    private static final String LOG_TAG = KoreSpeech.class.getSimpleName();

    private static KoreSpeech instance = null;

    private SpeechRecognizer mSpeechRecognizer;
    private SpeechListener mDelegate;


    private final RecognitionListener mListener = new RecognitionListener() {
    @Override
    public void onReadyForSpeech(Bundle params) {

    }

    @Override
    public void onBeginningOfSpeech() {

    }

    @Override
    public void onRmsChanged(float rmsdB) {

    }

    @Override
    public void onBufferReceived(byte[] buffer) {

    }

    @Override
    public void onEndOfSpeech() {

    }

    @Override
    public void onError(int error) {

    }

    @Override
    public void onResults(Bundle results) {

    }

    @Override
    public void onPartialResults(Bundle partialResults) {

    }

    @Override
    public void onEvent(int eventType, Bundle params) {

    }
};

}
