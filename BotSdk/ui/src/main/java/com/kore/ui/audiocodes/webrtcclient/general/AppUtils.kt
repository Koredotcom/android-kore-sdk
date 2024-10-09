package com.kore.ui.audiocodes.webrtcclient.general

import android.app.Activity
import android.content.Context
import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.media.AudioManager
import android.media.MediaPlayer
import android.media.MediaPlayer.OnCompletionListener
import com.audiocodes.mv.webrtcsdk.sip.enums.Transport
import com.kore.common.utils.LogUtils
import com.kore.ui.R
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException

object AppUtils {
    private const val TAG = "AppUtils"
    private var mediaPlayer: MediaPlayer? = null
    var isPlayingAudioFile = false
        private set

    fun getStringBoolean(context: Context, stringId: Int): Boolean {
        try {
            val value = context.getString(stringId)
            if ("1" == value) {
                return true
            }
        } catch (e: Exception) {
            Log.w(TAG, "oops: $e")
        }
        return false
    }

    @JvmStatic
    fun getTransport(context: Context, name: String?): Transport {
        val transport: Transport = try {
            Transport.valueOf(name ?: "")
        } catch (e: Exception) {
            Transport.valueOf(context.getString(R.string.sip_account_transport_default)) //  Transport.TCP;
        }
        return transport
    }

    fun copyFileFromRawToData(context: Context, fileName: String?, id: Int) {
        val file = context.getFileStreamPath(fileName)
        if (file.exists()) {
            return
        }
        val resources = context.resources
        val inputStream = resources.openRawResource(id)
        val os: FileOutputStream = try {
            val absolutePath = file.absolutePath
            FileOutputStream(absolutePath)
        } catch (e: IOException) {
            LogUtils.e(TAG, "cannot open output stream file $e")
            return
        }
        try {
            while (true) {
                val c = inputStream.read()
                if (c == -1) {
                    break
                }
                os.write(c)
            }
        } catch (e: IOException) {
            LogUtils.e(TAG, "error during writing file $e")
        } finally {
            try {
                inputStream.close()
            } catch (ignored: Throwable) {
            }
            try {
                os.close()
            } catch (ignored: Throwable) {
            }
        }
    }

    fun startRingingMP(
        context: Context,
        fullPath: String, isLooping: Boolean, useSpeaker: Boolean,
        onCompletionListener: OnCompletionListener?
    ) {
        var fullPathInner = fullPath
        Log.i(TAG, "Start ringing (3) using media player...")
        var streamType = AudioManager.STREAM_VOICE_CALL
        var newVolume: Int
        if (useSpeaker) {
            streamType = AudioManager.STREAM_RING
        }
        try {
            val audioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
            //newVolume = audioManager.getStreamMaxVolume(stramType);
            newVolume = Prefs.getCallVolume(context)
            if (useSpeaker) {
                newVolume = audioManager.getStreamVolume(streamType)
            }
            audioManager.setStreamVolume(streamType, newVolume, AudioManager.FLAG_SHOW_UI)
            audioManager.isSpeakerphoneOn = useSpeaker
            mediaPlayer = MediaPlayer()
            mediaPlayer?.reset()
            mediaPlayer?.setAudioStreamType(streamType)
            if (!fullPathInner.startsWith("/")) {
                fullPathInner = context.getFileStreamPath(fullPathInner).absolutePath
            }
            val fis = FileInputStream(fullPathInner)
            mediaPlayer?.setDataSource(fis.fd)
            mediaPlayer?.isLooping = isLooping
            mediaPlayer?.prepare()
            Log.d(TAG, "Starting to play ringtone using media player")
            mediaPlayer?.start()
            isPlayingAudioFile = true
            mediaPlayer?.setOnCompletionListener(onCompletionListener)
        } catch (e: Throwable) {
            LogUtils.e(TAG, "media player error $e")
            mediaPlayer?.release()
            mediaPlayer = null
        }
    }

    @Synchronized
    fun stopRingingMP(context: Context) {
        try {
            if (mediaPlayer?.isPlaying == true) {
                Log.d("Ring", "Stopping media player")
                mediaPlayer?.stop()
                isPlayingAudioFile = false
                setSpeaker(context, false)
            }
            Log.d("Ring", "Releasing media player")
            mediaPlayer?.release()
            mediaPlayer = null
        } catch (var1: IllegalStateException) {
            var1.printStackTrace()
        }
    }

    fun setSpeaker(context: Context, useSpeaker: Boolean) {
        try {
            var streamType = AudioManager.STREAM_VOICE_CALL
            var newVolume = Prefs.getCallVolume(context)
            if (useSpeaker) {
                streamType = AudioManager.STREAM_RING
                newVolume = Prefs.getRingVolume(context)
            }
            val audioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
            //int maxVolume = audioManager.getStreamMaxVolume(stramType);
            //audioManager.setStreamVolume(stramType, newVolume, 0);
            audioManager.isSpeakerphoneOn = useSpeaker
        } catch (var4: Exception) {
            var4.printStackTrace()
        }
    }

    fun saveVolumeSettings(context: Context, prevVol: Boolean) {
        if (prevVol) {
            //save old volume
            val audioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
            Prefs.setPrevCallVolume(context, audioManager.getStreamVolume(AudioManager.STREAM_VOICE_CALL))
            Prefs.setPrevRingVolume(context, audioManager.getStreamVolume(AudioManager.STREAM_RING))
        } else {
            //save call volume
            val audioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
            Prefs.setCallVolume(context, audioManager.getStreamVolume(AudioManager.STREAM_VOICE_CALL))
            Prefs.setRingVolume(context, audioManager.getStreamVolume(AudioManager.STREAM_RING))
        }
    }

    fun setLastCallVolumeSettings(context: Context) {
        val audioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
        // audioManager.setStreamVolume(AudioManager.STREAM_RING, Prefs.getRingVolume(), 0);
        audioManager.setStreamVolume(AudioManager.STREAM_VOICE_CALL, Prefs.getCallVolume(context), 0)
    }

    fun restorePrevVolumeSettings(context: Context) {
        val audioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
        // audioManager.setStreamVolume(AudioManager.STREAM_RING, Prefs.getPrevRingVolume(), 0);
        audioManager.setStreamVolume(AudioManager.STREAM_VOICE_CALL, Prefs.getPrevCallVolume(context), 0)
    }

    fun checkOrientation(activity: Activity): Int {
        val isTablet = activity.resources.getBoolean(R.bool.isTablet)
        Log.d(TAG, "isTablet: $isTablet")
        var orientation = activity.resources.configuration.orientation
        if (isTablet) {
            Log.d(TAG, "set orientation: SENSOR")
            activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR
            orientation = activity.resources.configuration.orientation
            Log.d(TAG, "get new orientation: $orientation")
        } else if (orientation != Configuration.ORIENTATION_PORTRAIT) {
            Log.d(TAG, "set orientation: PORTRAIT")
            activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
            return 1
        }
        return orientation
    }
}
