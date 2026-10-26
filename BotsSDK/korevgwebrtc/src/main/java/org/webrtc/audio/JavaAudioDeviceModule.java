/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.media.AudioAttributes
 *  android.media.AudioDeviceInfo
 *  android.media.AudioManager
 *  android.os.Build$VERSION
 *  androidx.annotation.RequiresApi
 */
package org.webrtc.audio;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.AudioDeviceInfo;
import android.media.AudioManager;
import android.os.Build;
import androidx.annotation.RequiresApi;
import java.nio.ByteBuffer;
import java.util.concurrent.ScheduledExecutorService;
import org.webrtc.JniCommon;
import org.webrtc.Logging;
import org.webrtc.audio.AudioDeviceModule;
import org.webrtc.audio.WebRtcAudioEffects;
import org.webrtc.audio.WebRtcAudioManager;
import org.webrtc.audio.WebRtcAudioRecord;
import org.webrtc.audio.WebRtcAudioTrack;

public class JavaAudioDeviceModule
implements AudioDeviceModule {
    private static final String TAG = "JavaAudioDeviceModule";
    private final Context context;
    private final AudioManager audioManager;
    public final WebRtcAudioRecord audioInput;
    public final WebRtcAudioTrack audioOutput;
    private final int inputSampleRate;
    private final int outputSampleRate;
    private final boolean useStereoInput;
    private final boolean useStereoOutput;
    private final Object nativeLock = new Object();
    private long nativeAudioDeviceModule;

    public static Builder builder(Context context) {
        return new Builder(context);
    }

    public static boolean isBuiltInAcousticEchoCancelerSupported() {
        return WebRtcAudioEffects.isAcousticEchoCancelerSupported();
    }

    public static boolean isBuiltInNoiseSuppressorSupported() {
        return WebRtcAudioEffects.isNoiseSuppressorSupported();
    }

    private JavaAudioDeviceModule(Context context, AudioManager audioManager, WebRtcAudioRecord audioInput, WebRtcAudioTrack audioOutput, int inputSampleRate, int outputSampleRate, boolean useStereoInput, boolean useStereoOutput) {
        this.context = context;
        this.audioManager = audioManager;
        this.audioInput = audioInput;
        this.audioOutput = audioOutput;
        this.inputSampleRate = inputSampleRate;
        this.outputSampleRate = outputSampleRate;
        this.useStereoInput = useStereoInput;
        this.useStereoOutput = useStereoOutput;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public long getNativeAudioDeviceModulePointer() {
        Object object = this.nativeLock;
        synchronized (object) {
            if (this.nativeAudioDeviceModule == 0L) {
                this.nativeAudioDeviceModule = JavaAudioDeviceModule.nativeCreateAudioDeviceModule(this.context, this.audioManager, this.audioInput, this.audioOutput, this.inputSampleRate, this.outputSampleRate, this.useStereoInput, this.useStereoOutput);
            }
            return this.nativeAudioDeviceModule;
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public void release() {
        Object object = this.nativeLock;
        synchronized (object) {
            if (this.nativeAudioDeviceModule != 0L) {
                JniCommon.nativeReleaseRef(this.nativeAudioDeviceModule);
                this.nativeAudioDeviceModule = 0L;
            }
        }
    }

    @Override
    public void setSpeakerMute(boolean mute) {
        Logging.d(TAG, "setSpeakerMute: " + mute);
        this.audioOutput.setSpeakerMute(mute);
    }

    @Override
    public void setMicrophoneMute(boolean mute) {
        Logging.d(TAG, "setMicrophoneMute: " + mute);
        this.audioInput.setMicrophoneMute(mute);
    }

    public void setAudioRecordEnabled(boolean enable) {
        this.audioInput.setUseAudioRecord(enable);
    }

    public void prewarmRecording() {
        this.audioInput.initRecordingIfNeeded();
        this.audioInput.prewarmRecordingIfNeeded();
    }

    public void requestStartRecording() {
        this.audioInput.initRecordingIfNeeded();
        this.audioInput.startRecordingIfNeeded();
    }

    public void requestStopRecording() {
        this.audioInput.initRecordingIfNeeded();
        this.audioInput.stopRecordingIfNeeded();
    }

    @Override
    public boolean setNoiseSuppressorEnabled(boolean enabled) {
        Logging.d(TAG, "setNoiseSuppressorEnabled: " + enabled);
        return this.audioInput.setNoiseSuppressorEnabled(enabled);
    }

    @RequiresApi(value=23)
    public void setPreferredInputDevice(AudioDeviceInfo preferredInputDevice) {
        Logging.d(TAG, "setPreferredInputDevice: " + preferredInputDevice);
        this.audioInput.setPreferredDevice(preferredInputDevice);
    }

    private static native long nativeCreateAudioDeviceModule(Context var0, AudioManager var1, WebRtcAudioRecord var2, WebRtcAudioTrack var3, int var4, int var5, boolean var6, boolean var7);

    public static class Builder {
        private final Context context;
        private ScheduledExecutorService scheduler;
        private final AudioManager audioManager;
        private int inputSampleRate;
        private int outputSampleRate;
        private int audioSource = 7;
        private int audioFormat = 2;
        private AudioTrackErrorCallback audioTrackErrorCallback;
        private AudioRecordErrorCallback audioRecordErrorCallback;
        private SamplesReadyCallback samplesReadyCallback;
        private PlaybackSamplesReadyCallback playbackSamplesReadyCallback;
        private AudioBufferCallback audioBufferCallback;
        private AudioTrackStateCallback audioTrackStateCallback;
        private AudioRecordStateCallback audioRecordStateCallback;
        private boolean useHardwareAcousticEchoCanceler = JavaAudioDeviceModule.isBuiltInAcousticEchoCancelerSupported();
        private boolean useHardwareNoiseSuppressor = JavaAudioDeviceModule.isBuiltInNoiseSuppressorSupported();
        private boolean useStereoInput;
        private boolean useStereoOutput;
        private AudioAttributes audioAttributes;
        private boolean useLowLatency;
        private boolean enableVolumeLogger;

        private Builder(Context context) {
            this.context = context;
            this.audioManager = (AudioManager)context.getSystemService("audio");
            this.inputSampleRate = WebRtcAudioManager.getSampleRate(this.audioManager);
            this.outputSampleRate = WebRtcAudioManager.getSampleRate(this.audioManager);
            this.useLowLatency = false;
            this.enableVolumeLogger = true;
        }

        public Builder setScheduler(ScheduledExecutorService scheduler) {
            this.scheduler = scheduler;
            return this;
        }

        public Builder setSampleRate(int sampleRate) {
            Logging.d(JavaAudioDeviceModule.TAG, "Input/Output sample rate overridden to: " + sampleRate);
            this.inputSampleRate = sampleRate;
            this.outputSampleRate = sampleRate;
            return this;
        }

        public Builder setInputSampleRate(int inputSampleRate) {
            Logging.d(JavaAudioDeviceModule.TAG, "Input sample rate overridden to: " + inputSampleRate);
            this.inputSampleRate = inputSampleRate;
            return this;
        }

        public Builder setOutputSampleRate(int outputSampleRate) {
            Logging.d(JavaAudioDeviceModule.TAG, "Output sample rate overridden to: " + outputSampleRate);
            this.outputSampleRate = outputSampleRate;
            return this;
        }

        public Builder setAudioSource(int audioSource) {
            this.audioSource = audioSource;
            return this;
        }

        public Builder setAudioFormat(int audioFormat) {
            this.audioFormat = audioFormat;
            return this;
        }

        public Builder setAudioTrackErrorCallback(AudioTrackErrorCallback audioTrackErrorCallback) {
            this.audioTrackErrorCallback = audioTrackErrorCallback;
            return this;
        }

        public Builder setAudioRecordErrorCallback(AudioRecordErrorCallback audioRecordErrorCallback) {
            this.audioRecordErrorCallback = audioRecordErrorCallback;
            return this;
        }

        public Builder setSamplesReadyCallback(SamplesReadyCallback samplesReadyCallback) {
            this.samplesReadyCallback = samplesReadyCallback;
            return this;
        }

        public Builder setAudioBufferCallback(AudioBufferCallback audioBufferCallback) {
            this.audioBufferCallback = audioBufferCallback;
            return this;
        }

        public Builder setPlaybackSamplesReadyCallback(PlaybackSamplesReadyCallback playbackSamplesReadyCallback) {
            this.playbackSamplesReadyCallback = playbackSamplesReadyCallback;
            return this;
        }

        public Builder setAudioTrackStateCallback(AudioTrackStateCallback audioTrackStateCallback) {
            this.audioTrackStateCallback = audioTrackStateCallback;
            return this;
        }

        public Builder setAudioRecordStateCallback(AudioRecordStateCallback audioRecordStateCallback) {
            this.audioRecordStateCallback = audioRecordStateCallback;
            return this;
        }

        public Builder setUseHardwareNoiseSuppressor(boolean useHardwareNoiseSuppressor) {
            if (useHardwareNoiseSuppressor && !JavaAudioDeviceModule.isBuiltInNoiseSuppressorSupported()) {
                Logging.e(JavaAudioDeviceModule.TAG, "HW NS not supported");
                useHardwareNoiseSuppressor = false;
            }
            this.useHardwareNoiseSuppressor = useHardwareNoiseSuppressor;
            return this;
        }

        public Builder setUseHardwareAcousticEchoCanceler(boolean useHardwareAcousticEchoCanceler) {
            if (useHardwareAcousticEchoCanceler && !JavaAudioDeviceModule.isBuiltInAcousticEchoCancelerSupported()) {
                Logging.e(JavaAudioDeviceModule.TAG, "HW AEC not supported");
                useHardwareAcousticEchoCanceler = false;
            }
            this.useHardwareAcousticEchoCanceler = useHardwareAcousticEchoCanceler;
            return this;
        }

        public Builder setUseStereoInput(boolean useStereoInput) {
            this.useStereoInput = useStereoInput;
            return this;
        }

        public Builder setUseStereoOutput(boolean useStereoOutput) {
            this.useStereoOutput = useStereoOutput;
            return this;
        }

        public Builder setUseLowLatency(boolean useLowLatency) {
            this.useLowLatency = useLowLatency;
            return this;
        }

        public Builder setAudioAttributes(AudioAttributes audioAttributes) {
            this.audioAttributes = audioAttributes;
            return this;
        }

        public Builder setEnableVolumeLogger(boolean enableVolumeLogger) {
            this.enableVolumeLogger = enableVolumeLogger;
            return this;
        }

        public JavaAudioDeviceModule createAudioDeviceModule() {
            ScheduledExecutorService executor;
            Logging.d(JavaAudioDeviceModule.TAG, "createAudioDeviceModule");
            if (this.useHardwareNoiseSuppressor) {
                Logging.d(JavaAudioDeviceModule.TAG, "HW NS will be used.");
            } else {
                if (JavaAudioDeviceModule.isBuiltInNoiseSuppressorSupported()) {
                    Logging.d(JavaAudioDeviceModule.TAG, "Overriding default behavior; now using WebRTC NS!");
                }
                Logging.d(JavaAudioDeviceModule.TAG, "HW NS will not be used.");
            }
            if (this.useHardwareAcousticEchoCanceler) {
                Logging.d(JavaAudioDeviceModule.TAG, "HW AEC will be used.");
            } else {
                if (JavaAudioDeviceModule.isBuiltInAcousticEchoCancelerSupported()) {
                    Logging.d(JavaAudioDeviceModule.TAG, "Overriding default behavior; now using WebRTC AEC!");
                }
                Logging.d(JavaAudioDeviceModule.TAG, "HW AEC will not be used.");
            }
            int MIN_LOW_LATENCY_SDK_VERSION = 26;
            if (this.useLowLatency && Build.VERSION.SDK_INT >= 26) {
                Logging.d(JavaAudioDeviceModule.TAG, "Low latency mode will be used.");
            }
            if ((executor = this.scheduler) == null) {
                executor = WebRtcAudioRecord.newDefaultScheduler();
            }
            WebRtcAudioRecord audioInput = new WebRtcAudioRecord(this.context, executor, this.audioManager, this.audioSource, this.audioFormat, this.audioRecordErrorCallback, this.audioRecordStateCallback, this.samplesReadyCallback, this.audioBufferCallback, this.useHardwareAcousticEchoCanceler, this.useHardwareNoiseSuppressor, this.inputSampleRate, this.useStereoInput ? 2 : 1);
            WebRtcAudioTrack audioOutput = new WebRtcAudioTrack(this.context, this.audioManager, this.audioAttributes, this.audioTrackErrorCallback, this.audioTrackStateCallback, this.playbackSamplesReadyCallback, this.useLowLatency, this.enableVolumeLogger);
            return new JavaAudioDeviceModule(this.context, this.audioManager, audioInput, audioOutput, this.inputSampleRate, this.outputSampleRate, this.useStereoInput, this.useStereoOutput);
        }
    }

    public static interface AudioBufferCallback {
        public long onBuffer(ByteBuffer var1, int var2, int var3, int var4, int var5, long var6);
    }

    public static interface AudioTrackStateCallback {
        public void onWebRtcAudioTrackStart();

        public void onWebRtcAudioTrackStop();
    }

    public static interface AudioTrackErrorCallback {
        public void onWebRtcAudioTrackInitError(String var1);

        public void onWebRtcAudioTrackStartError(AudioTrackStartErrorCode var1, String var2);

        public void onWebRtcAudioTrackError(String var1);
    }

    public static enum AudioTrackStartErrorCode {
        AUDIO_TRACK_START_EXCEPTION,
        AUDIO_TRACK_START_STATE_MISMATCH;

    }

    public static interface PlaybackSamplesReadyCallback {
        public void onWebRtcAudioTrackSamplesReady(AudioSamples var1);
    }

    public static interface SamplesReadyCallback {
        public void onWebRtcAudioRecordSamplesReady(AudioSamples var1);
    }

    public static class AudioSamples {
        private final int audioFormat;
        private final int channelCount;
        private final int sampleRate;
        private final byte[] data;

        public AudioSamples(int audioFormat, int channelCount, int sampleRate, byte[] data) {
            this.audioFormat = audioFormat;
            this.channelCount = channelCount;
            this.sampleRate = sampleRate;
            this.data = data;
        }

        public int getAudioFormat() {
            return this.audioFormat;
        }

        public int getChannelCount() {
            return this.channelCount;
        }

        public int getSampleRate() {
            return this.sampleRate;
        }

        public byte[] getData() {
            return this.data;
        }
    }

    public static interface AudioRecordStateCallback {
        public void onWebRtcAudioRecordStart();

        public void onWebRtcAudioRecordStop();
    }

    public static interface AudioRecordErrorCallback {
        public void onWebRtcAudioRecordInitError(String var1);

        public void onWebRtcAudioRecordStartError(AudioRecordStartErrorCode var1, String var2);

        public void onWebRtcAudioRecordError(String var1);
    }

    public static enum AudioRecordStartErrorCode {
        AUDIO_RECORD_START_EXCEPTION,
        AUDIO_RECORD_START_STATE_MISMATCH;

    }
}

