package kore.botssdk.speechtotext;

/**
 * Created by Ramachandra Pradeep on 1/23/2017.
 */
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.SystemClock;

import kore.korebotsdklib.R;

// TODO: add a method that calls back when audio is finished
public class AudioCue {

    private static final int DELAY_AFTER_START_BEEP = 200;

    private final Context mContext;
    private final int mStartSound;
    private final int mStopSound;
    private final int mErrorSound;
    private SoundPlayCompletionListener soundPlayCompletionListener;

    public AudioCue(Context context, SoundPlayCompletionListener soundPlayCompletionListener) {
        mContext = context;
        mStartSound = R.raw.explore_begin;
        mStopSound = R.raw.explore_end;
        mErrorSound = R.raw.error;
        this.soundPlayCompletionListener = soundPlayCompletionListener;
    }

    public AudioCue(Context context, int startSound, int stopSound, int errorSound) {
        mContext = context;
        mStartSound = startSound;
        mStopSound = stopSound;
        mErrorSound = errorSound;
    }

    public void playStartSoundAndSleep() {
        if (playSound(mStartSound)) {
            SystemClock.sleep(DELAY_AFTER_START_BEEP);
        }
    }


    public void playStopSound() {
        playSound(mStopSound);
    }


    public void playErrorSound() {
        playSound(mErrorSound);
    }


    private boolean playSound(final int sound) {
        MediaPlayer mp = MediaPlayer.create(mContext, sound);
        // create can return null, e.g. on Android Wear
        if (mp == null) {
            return false;
        }
        mp.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                if(sound == mStartSound)
                    soundPlayCompletionListener.onAudioCompleted(SoundPlayCompletionListener.Type.START);
                mp.release();
            }
        });
        mp.start();
        return true;
    }

}