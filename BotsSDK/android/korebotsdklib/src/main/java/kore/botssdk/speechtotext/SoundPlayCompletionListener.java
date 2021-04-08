package kore.botssdk.speechtotext;

/**
 * Created by Ramachandra Pradeep on 1/24/2017.
 */
public interface SoundPlayCompletionListener {
    enum Type {
        START,
        // error occurred, reconstruction needed
        ERROR,
        // recorder stopped
        STOPPED
    }

    void onAudioCompleted(Type soundType);
}
