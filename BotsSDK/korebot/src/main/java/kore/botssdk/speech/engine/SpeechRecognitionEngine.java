package kore.botssdk.speech.engine;

import android.content.Context;
import android.speech.RecognitionListener;

import java.util.Locale;

import kore.botssdk.speech.GoogleVoiceTypingDisabledException;
import kore.botssdk.speech.SpeechDelegate;
import kore.botssdk.speech.SpeechRecognitionNotAvailable;
import kore.botssdk.speech.ui.SpeechProgressView;

public interface SpeechRecognitionEngine extends RecognitionListener {

    void init(Context context);

    void clear();

    String getPartialResultsAsString();

    void initSpeechRecognizer(Context context);

    void startListening(SpeechProgressView progressView, SpeechDelegate delegate) throws SpeechRecognitionNotAvailable, GoogleVoiceTypingDisabledException;

    void stopListening();

    void returnPartialResultsAndRecreateSpeechRecognizer();

    void setPartialResults(boolean getPartialResults);

    void shutdown();

    boolean isListening();

    Locale getLocale();

    void setLocale(Locale locale);

    void setPreferOffline(boolean preferOffline);

    void setTransitionMinimumDelay(long milliseconds);

    void setStopListeningAfterInactivity(long milliseconds);

    void setCallingPackage(String callingPackage);

    void unregisterDelegate();
}
