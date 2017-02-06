package kore.botssdk.speechtotext;

/**
 * Created by Ramachandra Pradeep on 1/23/2017.
 */
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.media.audiofx.AcousticEchoCanceler;
import android.media.audiofx.AutomaticGainControl;
import android.media.audiofx.NoiseSuppressor;
import android.os.Build;
import android.util.Log;

/**
 * The following takes effect only on Jelly Bean and higher.
 *
 * @author Kaarel Kaljurand
 */
public class SpeechRecord extends AudioRecord {

    public SpeechRecord(int sampleRateInHz, int bufferSizeInBytes)
            throws IllegalArgumentException {

        this(
                MediaRecorder.AudioSource.VOICE_RECOGNITION,
                sampleRateInHz,
                AudioFormat.CHANNEL_IN_MONO,
                AudioFormat.ENCODING_PCM_16BIT,
                bufferSizeInBytes,
                false,
                false,
                false
        );
    }


    public SpeechRecord(int sampleRateInHz, int bufferSizeInBytes, boolean noise, boolean gain, boolean echo)
            throws IllegalArgumentException {

        this(
                MediaRecorder.AudioSource.VOICE_RECOGNITION,
                sampleRateInHz,
                AudioFormat.CHANNEL_IN_MONO,
                AudioFormat.ENCODING_PCM_16BIT,
                bufferSizeInBytes,
                noise,
                gain,
                echo
        );
    }


    // This is a copy of the AudioRecord constructor
    public SpeechRecord(int audioSource, int sampleRateInHz, int channelConfig, int audioFormat, int bufferSizeInBytes)
            throws IllegalArgumentException {

        this(audioSource, sampleRateInHz, channelConfig, audioFormat, bufferSizeInBytes, false, false, false);
    }


    public SpeechRecord(int audioSource, int sampleRateInHz, int channelConfig, int audioFormat, int bufferSizeInBytes,
                        boolean noise, boolean gain, boolean echo)
            throws IllegalArgumentException {

        super(audioSource, sampleRateInHz, channelConfig, audioFormat, bufferSizeInBytes);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            Log.i("SppechRecord","Trying to enhance audio because running on SDK " + Build.VERSION.SDK_INT);

            int audioSessionId = getAudioSessionId();

            if (noise) {
                if (NoiseSuppressor.create(audioSessionId) == null) {
                    Log.i("SppechRecord","NoiseSuppressor: failed");
                } else {
                    Log.i("SppechRecord","NoiseSuppressor: ON");
                }
            } else {
                Log.i("SppechRecord","NoiseSuppressor: OFF");
            }

            if (gain) {
                if (AutomaticGainControl.create(audioSessionId) == null) {
                    Log.i("SppechRecord","AutomaticGainControl: failed");
                } else {
                    Log.i("SppechRecord","AutomaticGainControl: ON");
                }
            } else {
                Log.i("SppechRecord","AutomaticGainControl: OFF");
            }

            if (echo) {
                if (AcousticEchoCanceler.create(audioSessionId) == null) {
                    Log.i("SppechRecord","AcousticEchoCanceler: failed");
                } else {
                    Log.i("SppechRecord","AcousticEchoCanceler: ON");
                }
            } else {
                Log.i("SppechRecord","AcousticEchoCanceler: OFF");
            }
        }
    }


    public static boolean isNoiseSuppressorAvailable() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            return NoiseSuppressor.isAvailable();
        }
        return false;
    }
}