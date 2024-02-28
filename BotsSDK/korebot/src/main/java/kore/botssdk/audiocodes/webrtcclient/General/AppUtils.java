package kore.botssdk.audiocodes.webrtcclient.General;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.media.AudioManager;
import android.media.MediaPlayer;

import com.audiocodes.mv.webrtcsdk.sip.enums.Transport;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import kore.botssdk.R;
import kore.botssdk.application.BotApplication;

public class AppUtils {

    private static final String TAG="AppUtils";

    private static MediaPlayer mediaPlayer = null;
    private static boolean isPlayingAudioFile;

    public static boolean getStringBoolean(int stringId){
        try {
            String value = BotApplication.getGlobalContext().getString(stringId);
            if ("1".equals(value)) {
                return true;
            }
        } catch (Exception e) {
            Log.w(TAG, "oops: "+e);
        }
        return false;
    }

    public static Transport getTransport(String name)
    {
        Transport transport;
        try {
            transport = Transport.valueOf(name);
        } catch (Exception e) {
            transport= Transport.valueOf(BotApplication.getGlobalContext().getString(R.string.sip_account_transport_default));//  Transport.TCP;
        }
        return transport;
    }

//    public static void closeKeyboard(Activity activity)
//    {
//        // Check if no view has focus:
//        View view = activity.getCurrentFocus();
//        if (view != null) {
//            InputMethodManager imm = (InputMethodManager)activity.getSystemService(Context.INPUT_METHOD_SERVICE);
//            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
//        }
//    }

    public static void copyFileFromRawToData(Context context, String fileName, int id) {
        File file = context.getFileStreamPath(fileName);
        if (file.exists()) {
            return;
        }
        Resources r = context.getResources();
        InputStream is = r.openRawResource(id);

        FileOutputStream os;
        try {
            String absolutePath = file.getAbsolutePath();
            os = new FileOutputStream(absolutePath);
        } catch (IOException e) {
            Log.e(TAG, "cannot open output stream file " + e);
            return;
        }
        try {
            for (; ; ) {
                int c = is.read();
                if (c == -1) {
                    break;
                }
                os.write(c);
            }
        } catch (IOException e) {
            Log.e(TAG, "error during writing file " + e);
        } finally {
            try {
                is.close();
            } catch (Throwable ignored) {
            }
            try {
                os.close();
            } catch (Throwable ignored) {
            }
        }
    }


    public static void startRingingMP(String fullPath, boolean isLooping, boolean useSpeaker,
                                      MediaPlayer.OnCompletionListener onCompletionListener) {
        Context context = BotApplication.getGlobalContext();
        Log.i(TAG, "Start ringing (3) using media player...");
        int stramType = AudioManager.STREAM_VOICE_CALL;
        int newVolume = Prefs.getCallVolume();
        if (useSpeaker){
            stramType = AudioManager.STREAM_RING;
            newVolume = Prefs.getRingVolume();
        }
        try {
            AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
            //newVolume = audioManager.getStreamMaxVolume(stramType);
            newVolume = Prefs.getCallVolume();
            if (useSpeaker){
                newVolume = audioManager.getStreamVolume(stramType);
            }
            audioManager.setStreamVolume(stramType, newVolume, AudioManager.FLAG_SHOW_UI);
            audioManager.setSpeakerphoneOn(useSpeaker);

            mediaPlayer = new MediaPlayer();
            mediaPlayer.reset();
            mediaPlayer.setAudioStreamType(stramType);

            if(!fullPath.startsWith("/")){
                fullPath = context.getFileStreamPath(fullPath).getAbsolutePath();
            }
            FileInputStream fis = new FileInputStream(fullPath);
            mediaPlayer.setDataSource(fis.getFD());
            mediaPlayer.setLooping(isLooping);
            mediaPlayer.prepare();
            Log.d(TAG, "Starting to play ringtone using media player");
            mediaPlayer.start();
            isPlayingAudioFile = true;
            if(onCompletionListener==null)
            {
                onCompletionListener = new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mediaPlayer) {
                        isPlayingAudioFile = false;
                    }
                };
            }
            mediaPlayer.setOnCompletionListener(onCompletionListener);
        }
        catch (Throwable e) {
            Log.e(TAG, "media player error " + e);
            if(mediaPlayer != null){
                mediaPlayer.release();
            }
            mediaPlayer = null;
        }
    }

    public static synchronized void stopRingingMP() {
        try {
            if (mediaPlayer != null) {
                if (mediaPlayer.isPlaying()) {
                    Log.d("Ring", "Stopping media player");
                    mediaPlayer.stop();
                    isPlayingAudioFile = false;
                    setSpeaker(false);
                }

                Log.d("Ring", "Releasing media player");
                mediaPlayer.release();
                mediaPlayer = null;
            }
        } catch (IllegalStateException var1) {
            var1.printStackTrace();
        }

    }

    public static void setSpeaker(boolean useSpeaker) {
        try {

            int stramType = AudioManager.STREAM_VOICE_CALL;
            int newVolume = Prefs.getCallVolume();
            if (useSpeaker){
                stramType = AudioManager.STREAM_RING;
                newVolume = Prefs.getRingVolume();
            }
            Context context = BotApplication.getGlobalContext();
            AudioManager audioManager = (AudioManager)context.getSystemService(Context.AUDIO_SERVICE);
            //int maxVolume = audioManager.getStreamMaxVolume(stramType);
            //audioManager.setStreamVolume(stramType, newVolume, 0);
            audioManager.setSpeakerphoneOn(useSpeaker);
        } catch (Exception var4) {
            var4.printStackTrace();
        }

    }


    public static void saveVolumeSettings(boolean prevVol) {

        if (prevVol) {
            //save old volume
            AudioManager audioManager = (AudioManager) BotApplication.getGlobalContext().getSystemService(Context.AUDIO_SERVICE);
            Prefs.setPrevCallVolume(audioManager.getStreamVolume(AudioManager.STREAM_VOICE_CALL));
            Prefs.setPrevRingVolume(audioManager.getStreamVolume(AudioManager.STREAM_RING));
        }
        else  {
            //save call volume
            AudioManager audioManager = (AudioManager) BotApplication.getGlobalContext().getSystemService(Context.AUDIO_SERVICE);
            Prefs.setCallVolume(audioManager.getStreamVolume(AudioManager.STREAM_VOICE_CALL));
            Prefs.setRingVolume(audioManager.getStreamVolume(AudioManager.STREAM_RING));
        }

    }


    public static void setLastCallVolumeSettings(){
        AudioManager audioManager = (AudioManager) BotApplication.getGlobalContext().getSystemService(Context.AUDIO_SERVICE);
       // audioManager.setStreamVolume(AudioManager.STREAM_RING, Prefs.getRingVolume(), 0);
        audioManager.setStreamVolume(AudioManager.STREAM_VOICE_CALL, Prefs.getCallVolume(), 0);
    }

    public static void restorePrevVolumeSettings(){
        AudioManager audioManager = (AudioManager) BotApplication.getGlobalContext().getSystemService(Context.AUDIO_SERVICE);
       // audioManager.setStreamVolume(AudioManager.STREAM_RING, Prefs.getPrevRingVolume(), 0);
        audioManager.setStreamVolume(AudioManager.STREAM_VOICE_CALL, Prefs.getPrevCallVolume(), 0);
    }

    public static boolean isPlayingAudioFile() {
        return isPlayingAudioFile;
    }

    public static int checkOrientation(Activity activity)
    {
        boolean isTablet = activity.getResources().getBoolean(R.bool.isTablet);
        Log.d(TAG, "isTablet: "+isTablet );
        int orientation = activity.getResources().getConfiguration().orientation;
        if (isTablet) {
            Log.d(TAG, "set orientation: SENSOR");
            activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
            orientation = activity.getResources().getConfiguration().orientation;
            Log.d(TAG, "get new orientation: "+orientation);
        } else if(orientation != Configuration.ORIENTATION_PORTRAIT) {
            Log.d(TAG, "set orientation: PORTRAIT");
            activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            return 1;
        }
        return orientation;
    }

}
