package kore.botssdk.voicemode.audio;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.AudioDeviceInfo;
import android.media.AudioFocusRequest;
import android.media.AudioManager;
import android.os.Build;
import android.util.Log;

/**
 * Manages audio session for WebRTC calls.
 * Handles speaker, microphone, bluetooth, and audio routing.
 * Equivalent to InCallManager in React Native.
 */
public class AudioSessionManager {

    private static final String TAG = "AudioSessionManager";

    private final Context context;
    private final AudioManager audioManager;
    
    private AudioFocusRequest audioFocusRequest;
    private boolean isAudioSessionStarted = false;
    private boolean isSpeakerEnabled = true;
    private int originalAudioMode;
    private boolean originalSpeakerphoneOn;
    private int originalStreamVolume;

    public AudioSessionManager(Context context) {
        this.context = context.getApplicationContext();
        this.audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
    }

    /**
     * Start the audio session for a call.
     * Sets up audio mode, requests focus, and configures routing.
     */
    public void startAudioSession() {
        if (isAudioSessionStarted) {
            return;
        }

        try {
            originalAudioMode = audioManager.getMode();
            originalSpeakerphoneOn = audioManager.isSpeakerphoneOn();
            originalStreamVolume = audioManager.getStreamVolume(AudioManager.STREAM_VOICE_CALL);

            requestAudioFocus();

            audioManager.setMode(AudioManager.MODE_IN_COMMUNICATION);
            
            if (isSpeakerEnabled) {
                audioManager.setSpeakerphoneOn(true);
            }

            isAudioSessionStarted = true;
            Log.d(TAG, "Audio session started");
        } catch (Exception e) {
            Log.e(TAG, "Failed to start audio session: " + e.getMessage());
        }
    }

    /**
     * Stop the audio session.
     * Restores original audio settings.
     */
    public void stopAudioSession() {
        if (!isAudioSessionStarted) {
            return;
        }

        try {
            abandonAudioFocus();
            
            audioManager.setMode(originalAudioMode);
            audioManager.setSpeakerphoneOn(originalSpeakerphoneOn);

            isAudioSessionStarted = false;
            Log.d(TAG, "Audio session stopped");
        } catch (Exception e) {
            Log.e(TAG, "Failed to stop audio session: " + e.getMessage());
        }
    }

    /**
     * Enable or disable speakerphone.
     * @param enabled true to use speaker, false for earpiece
     */
    public void setSpeakerEnabled(boolean enabled) {
        isSpeakerEnabled = enabled;
        try {
            audioManager.setSpeakerphoneOn(enabled);
            Log.d(TAG, "Speaker " + (enabled ? "enabled" : "disabled"));
        } catch (Exception e) {
            Log.e(TAG, "Failed to set speaker: " + e.getMessage());
        }
    }

    /**
     * Check if speaker is enabled.
     * @return true if speaker is on
     */
    public boolean isSpeakerEnabled() {
        return isSpeakerEnabled;
    }

    /**
     * Toggle speaker state.
     * @return new speaker state
     */
    public boolean toggleSpeaker() {
        isSpeakerEnabled = !isSpeakerEnabled;
        setSpeakerEnabled(isSpeakerEnabled);
        return isSpeakerEnabled;
    }

    /**
     * Mute the microphone.
     */
    public void muteMicrophone() {
        try {
            audioManager.setMicrophoneMute(true);
            Log.d(TAG, "Microphone muted");
        } catch (Exception e) {
            Log.e(TAG, "Failed to mute microphone: " + e.getMessage());
        }
    }

    /**
     * Unmute the microphone.
     */
    public void unmuteMicrophone() {
        try {
            audioManager.setMicrophoneMute(false);
            Log.d(TAG, "Microphone unmuted");
        } catch (Exception e) {
            Log.e(TAG, "Failed to unmute microphone: " + e.getMessage());
        }
    }

    /**
     * Check if microphone is muted.
     * @return true if muted
     */
    public boolean isMicrophoneMuted() {
        return audioManager.isMicrophoneMute();
    }

    /**
     * Set the volume for voice call stream.
     * @param volume Volume level (0-15 typically)
     */
    public void setVolume(int volume) {
        try {
            int maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_VOICE_CALL);
            int safeVolume = Math.max(0, Math.min(volume, maxVolume));
            audioManager.setStreamVolume(AudioManager.STREAM_VOICE_CALL, safeVolume, 0);
            Log.d(TAG, "Volume set to: " + safeVolume);
        } catch (Exception e) {
            Log.e(TAG, "Failed to set volume: " + e.getMessage());
        }
    }

    /**
     * Get current volume level.
     * @return current volume
     */
    public int getVolume() {
        return audioManager.getStreamVolume(AudioManager.STREAM_VOICE_CALL);
    }

    /**
     * Get maximum volume level.
     * @return max volume
     */
    public int getMaxVolume() {
        return audioManager.getStreamMaxVolume(AudioManager.STREAM_VOICE_CALL);
    }

    /**
     * Check if wired headset is connected.
     * @return true if connected
     */
    public boolean isWiredHeadsetConnected() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            AudioDeviceInfo[] devices = audioManager.getDevices(AudioManager.GET_DEVICES_OUTPUTS);
            for (AudioDeviceInfo device : devices) {
                int type = device.getType();
                if (type == AudioDeviceInfo.TYPE_WIRED_HEADSET || 
                    type == AudioDeviceInfo.TYPE_WIRED_HEADPHONES) {
                    return true;
                }
            }
        }
        return audioManager.isWiredHeadsetOn();
    }

    /**
     * Check if Bluetooth audio is connected.
     * @return true if connected
     */
    public boolean isBluetoothConnected() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            AudioDeviceInfo[] devices = audioManager.getDevices(AudioManager.GET_DEVICES_OUTPUTS);
            for (AudioDeviceInfo device : devices) {
                int type = device.getType();
                if (type == AudioDeviceInfo.TYPE_BLUETOOTH_SCO || 
                    type == AudioDeviceInfo.TYPE_BLUETOOTH_A2DP) {
                    return true;
                }
            }
        }
        return audioManager.isBluetoothScoOn() || audioManager.isBluetoothA2dpOn();
    }

    /**
     * Start Bluetooth SCO for call audio.
     */
    public void startBluetoothSco() {
        try {
            if (!audioManager.isBluetoothScoOn()) {
                audioManager.startBluetoothSco();
                audioManager.setBluetoothScoOn(true);
                Log.d(TAG, "Bluetooth SCO started");
            }
        } catch (Exception e) {
            Log.e(TAG, "Failed to start Bluetooth SCO: " + e.getMessage());
        }
    }

    /**
     * Stop Bluetooth SCO.
     */
    public void stopBluetoothSco() {
        try {
            if (audioManager.isBluetoothScoOn()) {
                audioManager.setBluetoothScoOn(false);
                audioManager.stopBluetoothSco();
                Log.d(TAG, "Bluetooth SCO stopped");
            }
        } catch (Exception e) {
            Log.e(TAG, "Failed to stop Bluetooth SCO: " + e.getMessage());
        }
    }

    private void requestAudioFocus() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            AudioAttributes audioAttributes = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_VOICE_COMMUNICATION)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SPEECH)
                    .build();

            audioFocusRequest = new AudioFocusRequest.Builder(AudioManager.AUDIOFOCUS_GAIN_TRANSIENT)
                    .setAudioAttributes(audioAttributes)
                    .setAcceptsDelayedFocusGain(true)
                    .setOnAudioFocusChangeListener(focusChange -> {
                        Log.d(TAG, "Audio focus changed: " + focusChange);
                    })
                    .build();

            audioManager.requestAudioFocus(audioFocusRequest);
        } else {
            audioManager.requestAudioFocus(
                    focusChange -> Log.d(TAG, "Audio focus changed: " + focusChange),
                    AudioManager.STREAM_VOICE_CALL,
                    AudioManager.AUDIOFOCUS_GAIN_TRANSIENT
            );
        }
    }

    private void abandonAudioFocus() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (audioFocusRequest != null) {
                audioManager.abandonAudioFocusRequest(audioFocusRequest);
                audioFocusRequest = null;
            }
        } else {
            audioManager.abandonAudioFocus(null);
        }
    }
}
