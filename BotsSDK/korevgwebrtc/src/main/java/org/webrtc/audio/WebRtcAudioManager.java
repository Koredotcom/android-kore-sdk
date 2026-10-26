/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.media.AudioManager
 *  android.media.AudioRecord
 *  android.media.AudioTrack
 *  android.os.Build$VERSION
 */
package org.webrtc.audio;

import android.content.Context;
import android.media.AudioManager;
import android.media.AudioRecord;
import android.media.AudioTrack;
import android.os.Build;
import org.webrtc.CalledByNative;
import org.webrtc.Logging;
import org.webrtc.audio.WebRtcAudioUtils;

class WebRtcAudioManager {
    private static final String TAG = "WebRtcAudioManagerExternal";
    private static final int DEFAULT_SAMPLE_RATE_HZ = 16000;
    private static final int BITS_PER_SAMPLE = 16;
    private static final int DEFAULT_FRAME_PER_BUFFER = 256;

    WebRtcAudioManager() {
    }

    @CalledByNative
    static AudioManager getAudioManager(Context context) {
        return (AudioManager)context.getSystemService("audio");
    }

    @CalledByNative
    static int getOutputBufferSize(Context context, AudioManager audioManager, int sampleRate, int numberOfOutputChannels) {
        return WebRtcAudioManager.isLowLatencyOutputSupported(context) ? WebRtcAudioManager.getLowLatencyFramesPerBuffer(audioManager) : WebRtcAudioManager.getMinOutputFrameSize(sampleRate, numberOfOutputChannels);
    }

    @CalledByNative
    static int getInputBufferSize(Context context, AudioManager audioManager, int sampleRate, int numberOfInputChannels) {
        return WebRtcAudioManager.isLowLatencyInputSupported(context) ? WebRtcAudioManager.getLowLatencyFramesPerBuffer(audioManager) : WebRtcAudioManager.getMinInputFrameSize(sampleRate, numberOfInputChannels);
    }

    private static boolean isLowLatencyOutputSupported(Context context) {
        return context.getPackageManager().hasSystemFeature("android.hardware.audio.low_latency");
    }

    private static boolean isLowLatencyInputSupported(Context context) {
        return Build.VERSION.SDK_INT >= 21 && WebRtcAudioManager.isLowLatencyOutputSupported(context);
    }

    @CalledByNative
    static int getSampleRate(AudioManager audioManager) {
        if (WebRtcAudioUtils.runningOnEmulator()) {
            Logging.d(TAG, "Running emulator, overriding sample rate to 8 kHz.");
            return 8000;
        }
        int sampleRateHz = WebRtcAudioManager.getSampleRateForApiLevel(audioManager);
        Logging.d(TAG, "Sample rate is set to " + sampleRateHz + " Hz");
        return sampleRateHz;
    }

    private static int getSampleRateForApiLevel(AudioManager audioManager) {
        if (Build.VERSION.SDK_INT < 17) {
            return 16000;
        }
        String sampleRateString = audioManager.getProperty("android.media.property.OUTPUT_SAMPLE_RATE");
        return sampleRateString == null ? 16000 : Integer.parseInt(sampleRateString);
    }

    private static int getLowLatencyFramesPerBuffer(AudioManager audioManager) {
        if (Build.VERSION.SDK_INT < 17) {
            return 256;
        }
        String framesPerBuffer = audioManager.getProperty("android.media.property.OUTPUT_FRAMES_PER_BUFFER");
        return framesPerBuffer == null ? 256 : Integer.parseInt(framesPerBuffer);
    }

    private static int getMinOutputFrameSize(int sampleRateInHz, int numChannels) {
        int bytesPerFrame = numChannels * 2;
        int channelConfig = numChannels == 1 ? 4 : 12;
        return AudioTrack.getMinBufferSize((int)sampleRateInHz, (int)channelConfig, (int)2) / bytesPerFrame;
    }

    private static int getMinInputFrameSize(int sampleRateInHz, int numChannels) {
        int bytesPerFrame = numChannels * 2;
        int channelConfig = numChannels == 1 ? 16 : 12;
        return AudioRecord.getMinBufferSize((int)sampleRateInHz, (int)channelConfig, (int)2) / bytesPerFrame;
    }
}

