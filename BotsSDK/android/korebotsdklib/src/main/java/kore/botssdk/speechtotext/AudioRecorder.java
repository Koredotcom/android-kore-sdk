package kore.botssdk.speechtotext;

/**
 * Created by Ramachandra Pradeep on 1/23/2017.
 */
import android.media.MediaRecorder;

public interface AudioRecorder {
    int DEFAULT_AUDIO_SOURCE = MediaRecorder.AudioSource.DEFAULT;
    int DEFAULT_SAMPLE_RATE = 16000;
    short RESOLUTION_IN_BYTES = 2;
    // Number of channels (MONO = 1, STEREO = 2)
    short CHANNELS = 1;

    String getWsArgs();

    State getState();

    byte[] consumeRecordingAndTruncate();

    byte[] consumeRecording();

    void start();

    float getRmsdb();

    void release();

    boolean isPausing();

    enum State {
        // recorder is ready, but not yet recording
        READY,

        // recorder recording
        RECORDING,

        // error occurred, reconstruction needed
        ERROR,

        // recorder stopped
        STOPPED
    }
}
