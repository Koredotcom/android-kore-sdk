/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.annotation.TargetApi
 *  android.content.Context
 *  android.media.AudioDeviceInfo
 *  android.media.AudioFormat
 *  android.media.AudioFormat$Builder
 *  android.media.AudioManager
 *  android.media.AudioRecord
 *  android.media.AudioRecord$Builder
 *  android.media.AudioRecordingConfiguration
 *  android.os.Build$VERSION
 *  android.os.Process
 *  androidx.annotation.Nullable
 *  androidx.annotation.RequiresApi
 */
package org.webrtc.audio;

import android.annotation.TargetApi;
import android.content.Context;
import android.media.AudioDeviceInfo;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioRecord;
import android.media.AudioRecordingConfiguration;
import android.os.Build;
import android.os.Process;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import org.webrtc.CalledByNative;
import org.webrtc.Logging;
import org.webrtc.ThreadUtils;
import org.webrtc.audio.JavaAudioDeviceModule;
import org.webrtc.audio.WebRtcAudioEffects;
import org.webrtc.audio.WebRtcAudioUtils;

class WebRtcAudioRecord {
    private static final String TAG = "WebRtcAudioRecordExternal";
    private static final int CALLBACK_BUFFER_SIZE_MS = 10;
    private static final int BUFFERS_PER_SECOND = 100;
    private static final int BUFFER_SIZE_FACTOR = 2;
    private static final long AUDIO_RECORD_THREAD_JOIN_TIMEOUT_MS = 2000L;
    public static final int DEFAULT_AUDIO_SOURCE = 7;
    public static final int DEFAULT_AUDIO_FORMAT = 2;
    private static final int AUDIO_RECORD_START = 0;
    private static final int AUDIO_RECORD_STOP = 1;
    private static final int CHECK_REC_STATUS_DELAY_MS = 100;
    private final Context context;
    private final AudioManager audioManager;
    private final int audioSource;
    private final int audioFormat;
    private long nativeAudioRecord;
    private final WebRtcAudioEffects effects = new WebRtcAudioEffects();
    @Nullable
    private ByteBuffer byteBuffer;
    @Nullable
    private AudioRecord audioRecord;
    @Nullable
    private AudioRecordThread audioThread;
    @Nullable
    private AudioDeviceInfo preferredDevice;
    private final ScheduledExecutorService executor;
    @Nullable
    private ScheduledFuture<String> future;
    private volatile boolean microphoneMute;
    private final AtomicReference<Boolean> audioSourceMatchesRecordingSessionRef = new AtomicReference();
    private byte[] emptyBytes;
    @Nullable
    private final JavaAudioDeviceModule.AudioRecordErrorCallback errorCallback;
    @Nullable
    private final JavaAudioDeviceModule.AudioRecordStateCallback stateCallback;
    @Nullable
    private final JavaAudioDeviceModule.SamplesReadyCallback audioSamplesReadyCallback;
    private final boolean isAcousticEchoCancelerSupported;
    private final boolean isNoiseSuppressorSupported;
    private static final AtomicInteger nextSchedulerId = new AtomicInteger(0);

    @CalledByNative
    WebRtcAudioRecord(Context context, AudioManager audioManager) {
        this(context, WebRtcAudioRecord.newDefaultScheduler(), audioManager, 7, 2, null, null, null, WebRtcAudioEffects.isAcousticEchoCancelerSupported(), WebRtcAudioEffects.isNoiseSuppressorSupported());
    }

    public WebRtcAudioRecord(Context context, ScheduledExecutorService scheduler, AudioManager audioManager, int audioSource, int audioFormat, @Nullable JavaAudioDeviceModule.AudioRecordErrorCallback errorCallback, @Nullable JavaAudioDeviceModule.AudioRecordStateCallback stateCallback, @Nullable JavaAudioDeviceModule.SamplesReadyCallback audioSamplesReadyCallback, boolean isAcousticEchoCancelerSupported, boolean isNoiseSuppressorSupported) {
        if (isAcousticEchoCancelerSupported && !WebRtcAudioEffects.isAcousticEchoCancelerSupported()) {
            throw new IllegalArgumentException("HW AEC not supported");
        }
        if (isNoiseSuppressorSupported && !WebRtcAudioEffects.isNoiseSuppressorSupported()) {
            throw new IllegalArgumentException("HW NS not supported");
        }
        this.context = context;
        this.executor = scheduler;
        this.audioManager = audioManager;
        this.audioSource = audioSource;
        this.audioFormat = audioFormat;
        this.errorCallback = errorCallback;
        this.stateCallback = stateCallback;
        this.audioSamplesReadyCallback = audioSamplesReadyCallback;
        this.isAcousticEchoCancelerSupported = isAcousticEchoCancelerSupported;
        this.isNoiseSuppressorSupported = isNoiseSuppressorSupported;
        Logging.d(TAG, "ctor" + WebRtcAudioUtils.getThreadInfo());
    }

    @CalledByNative
    public void setNativeAudioRecord(long nativeAudioRecord) {
        this.nativeAudioRecord = nativeAudioRecord;
    }

    @CalledByNative
    boolean isAcousticEchoCancelerSupported() {
        return this.isAcousticEchoCancelerSupported;
    }

    @CalledByNative
    boolean isNoiseSuppressorSupported() {
        return this.isNoiseSuppressorSupported;
    }

    @CalledByNative
    boolean isAudioConfigVerified() {
        return this.audioSourceMatchesRecordingSessionRef.get() != null;
    }

    @CalledByNative
    boolean isAudioSourceMatchingRecordingSession() {
        Boolean audioSourceMatchesRecordingSession = this.audioSourceMatchesRecordingSessionRef.get();
        if (audioSourceMatchesRecordingSession == null) {
            Logging.w(TAG, "Audio configuration has not yet been verified");
            return false;
        }
        return audioSourceMatchesRecordingSession;
    }

    @CalledByNative
    private boolean enableBuiltInAEC(boolean enable) {
        Logging.d(TAG, "enableBuiltInAEC(" + enable + ")");
        return this.effects.setAEC(enable);
    }

    @CalledByNative
    private boolean enableBuiltInNS(boolean enable) {
        Logging.d(TAG, "enableBuiltInNS(" + enable + ")");
        return this.effects.setNS(enable);
    }

    @CalledByNative
    private int initRecording(int sampleRate, int channels) {
        Logging.d(TAG, "initRecording(sampleRate=" + sampleRate + ", channels=" + channels + ")");
        if (this.audioRecord != null) {
            this.reportWebRtcAudioRecordInitError("InitRecording called twice without StopRecording.");
            return -1;
        }
        int bytesPerFrame = channels * WebRtcAudioRecord.getBytesPerSample(this.audioFormat);
        int framesPerBuffer = sampleRate / 100;
        this.byteBuffer = ByteBuffer.allocateDirect(bytesPerFrame * framesPerBuffer);
        if (!this.byteBuffer.hasArray()) {
            this.reportWebRtcAudioRecordInitError("ByteBuffer does not have backing array.");
            return -1;
        }
        Logging.d(TAG, "byteBuffer.capacity: " + this.byteBuffer.capacity());
        this.emptyBytes = new byte[this.byteBuffer.capacity()];
        this.nativeCacheDirectBufferAddress(this.nativeAudioRecord, this.byteBuffer);
        int channelConfig = this.channelCountToConfiguration(channels);
        int minBufferSize = AudioRecord.getMinBufferSize((int)sampleRate, (int)channelConfig, (int)this.audioFormat);
        if (minBufferSize == -1 || minBufferSize == -2) {
            this.reportWebRtcAudioRecordInitError("AudioRecord.getMinBufferSize failed: " + minBufferSize);
            return -1;
        }
        Logging.d(TAG, "AudioRecord.getMinBufferSize: " + minBufferSize);
        int bufferSizeInBytes = Math.max(2 * minBufferSize, this.byteBuffer.capacity());
        Logging.d(TAG, "bufferSizeInBytes: " + bufferSizeInBytes);
        try {
            if (Build.VERSION.SDK_INT >= 23) {
                this.audioRecord = WebRtcAudioRecord.createAudioRecordOnMOrHigher(this.audioSource, sampleRate, channelConfig, this.audioFormat, bufferSizeInBytes);
                this.audioSourceMatchesRecordingSessionRef.set(null);
                if (this.preferredDevice != null) {
                    this.setPreferredDevice(this.preferredDevice);
                }
            } else {
                this.audioRecord = WebRtcAudioRecord.createAudioRecordOnLowerThanM(this.audioSource, sampleRate, channelConfig, this.audioFormat, bufferSizeInBytes);
                this.audioSourceMatchesRecordingSessionRef.set(null);
            }
        }
        catch (IllegalArgumentException | UnsupportedOperationException e) {
            this.reportWebRtcAudioRecordInitError(e.getMessage());
            this.releaseAudioResources();
            return -1;
        }
        if (this.audioRecord == null || this.audioRecord.getState() != 1) {
            this.reportWebRtcAudioRecordInitError("Creation or initialization of audio recorder failed.");
            this.releaseAudioResources();
            return -1;
        }
        this.effects.enable(this.audioRecord.getAudioSessionId());
        this.logMainParameters();
        this.logMainParametersExtended();
        int numActiveRecordingSessions = this.logRecordingConfigurations(this.audioRecord, false);
        if (numActiveRecordingSessions != 0) {
            Logging.w(TAG, "Potential microphone conflict. Active sessions: " + numActiveRecordingSessions);
        }
        return framesPerBuffer;
    }

    @RequiresApi(value=23)
    @TargetApi(value=23)
    void setPreferredDevice(@Nullable AudioDeviceInfo preferredDevice) {
        Logging.d(TAG, "setPreferredDevice " + (preferredDevice != null ? Integer.valueOf(preferredDevice.getId()) : null));
        this.preferredDevice = preferredDevice;
        if (this.audioRecord != null && !this.audioRecord.setPreferredDevice(preferredDevice)) {
            Logging.e(TAG, "setPreferredDevice failed");
        }
    }

    @CalledByNative
    private boolean startRecording() {
        Logging.d(TAG, "startRecording");
        WebRtcAudioRecord.assertTrue(this.audioRecord != null);
        WebRtcAudioRecord.assertTrue(this.audioThread == null);
        try {
            this.audioRecord.startRecording();
        }
        catch (IllegalStateException e) {
            this.reportWebRtcAudioRecordStartError(JavaAudioDeviceModule.AudioRecordStartErrorCode.AUDIO_RECORD_START_EXCEPTION, "AudioRecord.startRecording failed: " + e.getMessage());
            return false;
        }
        if (this.audioRecord.getRecordingState() != 3) {
            this.reportWebRtcAudioRecordStartError(JavaAudioDeviceModule.AudioRecordStartErrorCode.AUDIO_RECORD_START_STATE_MISMATCH, "AudioRecord.startRecording failed - incorrect state: " + this.audioRecord.getRecordingState());
            return false;
        }
        this.audioThread = new AudioRecordThread("AudioRecordJavaThread");
        this.audioThread.start();
        this.scheduleLogRecordingConfigurationsTask(this.audioRecord);
        return true;
    }

    @CalledByNative
    private boolean stopRecording() {
        Logging.d(TAG, "stopRecording");
        WebRtcAudioRecord.assertTrue(this.audioThread != null);
        if (this.future != null) {
            if (!this.future.isDone()) {
                this.future.cancel(true);
            }
            this.future = null;
        }
        this.audioThread.stopThread();
        if (!ThreadUtils.joinUninterruptibly(this.audioThread, 2000L)) {
            Logging.e(TAG, "Join of AudioRecordJavaThread timed out");
            WebRtcAudioUtils.logAudioState(TAG, this.context, this.audioManager);
        }
        this.audioThread = null;
        this.effects.release();
        this.releaseAudioResources();
        return true;
    }

    @TargetApi(value=23)
    private static AudioRecord createAudioRecordOnMOrHigher(int audioSource, int sampleRate, int channelConfig, int audioFormat, int bufferSizeInBytes) {
        Logging.d(TAG, "createAudioRecordOnMOrHigher");
        return new AudioRecord.Builder().setAudioSource(audioSource).setAudioFormat(new AudioFormat.Builder().setEncoding(audioFormat).setSampleRate(sampleRate).setChannelMask(channelConfig).build()).setBufferSizeInBytes(bufferSizeInBytes).build();
    }

    private static AudioRecord createAudioRecordOnLowerThanM(int audioSource, int sampleRate, int channelConfig, int audioFormat, int bufferSizeInBytes) {
        Logging.d(TAG, "createAudioRecordOnLowerThanM");
        return new AudioRecord(audioSource, sampleRate, channelConfig, audioFormat, bufferSizeInBytes);
    }

    private void logMainParameters() {
        Logging.d(TAG, "AudioRecord: session ID: " + this.audioRecord.getAudioSessionId() + ", channels: " + this.audioRecord.getChannelCount() + ", sample rate: " + this.audioRecord.getSampleRate());
    }

    @TargetApi(value=23)
    private void logMainParametersExtended() {
        if (Build.VERSION.SDK_INT >= 23) {
            Logging.d(TAG, "AudioRecord: buffer size in frames: " + this.audioRecord.getBufferSizeInFrames());
        }
    }

    @TargetApi(value=24)
    private int logRecordingConfigurations(AudioRecord audioRecord, boolean verifyAudioConfig) {
        if (Build.VERSION.SDK_INT < 24) {
            Logging.w(TAG, "AudioManager#getActiveRecordingConfigurations() requires N or higher");
            return 0;
        }
        if (audioRecord == null) {
            return 0;
        }
        List configs = this.audioManager.getActiveRecordingConfigurations();
        int numActiveRecordingSessions = configs.size();
        Logging.d(TAG, "Number of active recording sessions: " + numActiveRecordingSessions);
        if (numActiveRecordingSessions > 0) {
            WebRtcAudioRecord.logActiveRecordingConfigs(audioRecord.getAudioSessionId(), configs);
            if (verifyAudioConfig) {
                this.audioSourceMatchesRecordingSessionRef.set(WebRtcAudioRecord.verifyAudioConfig(audioRecord.getAudioSource(), audioRecord.getAudioSessionId(), audioRecord.getFormat(), audioRecord.getRoutedDevice(), configs));
            }
        }
        return numActiveRecordingSessions;
    }

    private static void assertTrue(boolean condition) {
        if (!condition) {
            throw new AssertionError((Object)"Expected condition to be true");
        }
    }

    private int channelCountToConfiguration(int channels) {
        return channels == 1 ? 16 : 12;
    }

    private native void nativeCacheDirectBufferAddress(long var1, ByteBuffer var3);

    private native void nativeDataIsRecorded(long var1, int var3);

    public void setMicrophoneMute(boolean mute) {
        Logging.w(TAG, "setMicrophoneMute(" + mute + ")");
        this.microphoneMute = mute;
    }

    private void releaseAudioResources() {
        Logging.d(TAG, "releaseAudioResources");
        if (this.audioRecord != null) {
            this.audioRecord.release();
            this.audioRecord = null;
        }
        this.audioSourceMatchesRecordingSessionRef.set(null);
    }

    private void reportWebRtcAudioRecordInitError(String errorMessage) {
        Logging.e(TAG, "Init recording error: " + errorMessage);
        WebRtcAudioUtils.logAudioState(TAG, this.context, this.audioManager);
        this.logRecordingConfigurations(this.audioRecord, false);
        if (this.errorCallback != null) {
            this.errorCallback.onWebRtcAudioRecordInitError(errorMessage);
        }
    }

    private void reportWebRtcAudioRecordStartError(JavaAudioDeviceModule.AudioRecordStartErrorCode errorCode, String errorMessage) {
        Logging.e(TAG, "Start recording error: " + (Object)((Object)errorCode) + ". " + errorMessage);
        WebRtcAudioUtils.logAudioState(TAG, this.context, this.audioManager);
        this.logRecordingConfigurations(this.audioRecord, false);
        if (this.errorCallback != null) {
            this.errorCallback.onWebRtcAudioRecordStartError(errorCode, errorMessage);
        }
    }

    private void reportWebRtcAudioRecordError(String errorMessage) {
        Logging.e(TAG, "Run-time recording error: " + errorMessage);
        WebRtcAudioUtils.logAudioState(TAG, this.context, this.audioManager);
        if (this.errorCallback != null) {
            this.errorCallback.onWebRtcAudioRecordError(errorMessage);
        }
    }

    private void doAudioRecordStateCallback(int audioState) {
        Logging.d(TAG, "doAudioRecordStateCallback: " + WebRtcAudioRecord.audioStateToString(audioState));
        if (this.stateCallback != null) {
            if (audioState == 0) {
                this.stateCallback.onWebRtcAudioRecordStart();
            } else if (audioState == 1) {
                this.stateCallback.onWebRtcAudioRecordStop();
            } else {
                Logging.e(TAG, "Invalid audio state");
            }
        }
    }

    private static int getBytesPerSample(int audioFormat) {
        switch (audioFormat) {
            case 3: {
                return 1;
            }
            case 1: 
            case 2: 
            case 13: {
                return 2;
            }
            case 4: {
                return 4;
            }
        }
        throw new IllegalArgumentException("Bad audio format " + audioFormat);
    }

    private void scheduleLogRecordingConfigurationsTask(AudioRecord audioRecord) {
        Logging.d(TAG, "scheduleLogRecordingConfigurationsTask");
        if (Build.VERSION.SDK_INT < 24) {
            return;
        }
        Callable<String> callable = () -> {
            if (this.audioRecord == audioRecord) {
                this.logRecordingConfigurations(audioRecord, true);
            } else {
                Logging.d(TAG, "audio record has changed");
            }
            return "Scheduled task is done";
        };
        if (this.future != null && !this.future.isDone()) {
            this.future.cancel(true);
        }
        this.future = this.executor.schedule(callable, 100L, TimeUnit.MILLISECONDS);
    }

    @TargetApi(value=24)
    private static boolean logActiveRecordingConfigs(int session, List<AudioRecordingConfiguration> configs) {
        WebRtcAudioRecord.assertTrue(!configs.isEmpty());
        Iterator<AudioRecordingConfiguration> it = configs.iterator();
        Logging.d(TAG, "AudioRecordingConfigurations: ");
        while (it.hasNext()) {
            AudioRecordingConfiguration config = it.next();
            StringBuilder conf = new StringBuilder();
            int audioSource = config.getClientAudioSource();
            conf.append("  client audio source=").append(WebRtcAudioUtils.audioSourceToString(audioSource)).append(", client session id=").append(config.getClientAudioSessionId()).append(" (").append(session).append(")").append("\n");
            AudioFormat format = config.getFormat();
            conf.append("  Device AudioFormat: ").append("channel count=").append(format.getChannelCount()).append(", channel index mask=").append(format.getChannelIndexMask()).append(", channel mask=").append(WebRtcAudioUtils.channelMaskToString(format.getChannelMask())).append(", encoding=").append(WebRtcAudioUtils.audioEncodingToString(format.getEncoding())).append(", sample rate=").append(format.getSampleRate()).append("\n");
            format = config.getClientFormat();
            conf.append("  Client AudioFormat: ").append("channel count=").append(format.getChannelCount()).append(", channel index mask=").append(format.getChannelIndexMask()).append(", channel mask=").append(WebRtcAudioUtils.channelMaskToString(format.getChannelMask())).append(", encoding=").append(WebRtcAudioUtils.audioEncodingToString(format.getEncoding())).append(", sample rate=").append(format.getSampleRate()).append("\n");
            AudioDeviceInfo device = config.getAudioDevice();
            if (device != null) {
                WebRtcAudioRecord.assertTrue(device.isSource());
                conf.append("  AudioDevice: ").append("type=").append(WebRtcAudioUtils.deviceTypeToString(device.getType())).append(", id=").append(device.getId());
            }
            Logging.d(TAG, conf.toString());
        }
        return true;
    }

    @TargetApi(value=24)
    private static boolean verifyAudioConfig(int source, int session, AudioFormat format, AudioDeviceInfo device, List<AudioRecordingConfiguration> configs) {
        WebRtcAudioRecord.assertTrue(!configs.isEmpty());
        for (AudioRecordingConfiguration config : configs) {
            AudioDeviceInfo configDevice = config.getAudioDevice();
            if (configDevice == null || config.getClientAudioSource() != source || config.getClientAudioSessionId() != session || config.getClientFormat().getEncoding() != format.getEncoding() || config.getClientFormat().getSampleRate() != format.getSampleRate() || config.getClientFormat().getChannelMask() != format.getChannelMask() || config.getClientFormat().getChannelIndexMask() != format.getChannelIndexMask() || config.getFormat().getEncoding() == 0 || config.getFormat().getSampleRate() <= 0 || config.getFormat().getChannelMask() == 0 && config.getFormat().getChannelIndexMask() == 0 || !WebRtcAudioRecord.checkDeviceMatch(configDevice, device)) continue;
            Logging.d(TAG, "verifyAudioConfig: PASS");
            return true;
        }
        Logging.e(TAG, "verifyAudioConfig: FAILED");
        return false;
    }

    @TargetApi(value=24)
    private static boolean checkDeviceMatch(AudioDeviceInfo devA, AudioDeviceInfo devB) {
        return devA.getId() == devB.getId() && devA.getType() == devB.getType();
    }

    private static String audioStateToString(int state) {
        switch (state) {
            case 0: {
                return "START";
            }
            case 1: {
                return "STOP";
            }
        }
        return "INVALID";
    }

    static ScheduledExecutorService newDefaultScheduler() {
        final AtomicInteger nextThreadId = new AtomicInteger(0);
        return Executors.newScheduledThreadPool(0, new ThreadFactory(){

            @Override
            public Thread newThread(Runnable r) {
                Thread thread = Executors.defaultThreadFactory().newThread(r);
                thread.setName(String.format("WebRtcAudioRecordScheduler-%s-%s", nextSchedulerId.getAndIncrement(), nextThreadId.getAndIncrement()));
                return thread;
            }
        });
    }

    private class AudioRecordThread
    extends Thread {
        private volatile boolean keepAlive;

        public AudioRecordThread(String name) {
            super(name);
            this.keepAlive = true;
        }

        @Override
        public void run() {
            Process.setThreadPriority((int)-19);
            Logging.d(WebRtcAudioRecord.TAG, "AudioRecordThread" + WebRtcAudioUtils.getThreadInfo());
            WebRtcAudioRecord.assertTrue(WebRtcAudioRecord.this.audioRecord.getRecordingState() == 3);
            WebRtcAudioRecord.this.doAudioRecordStateCallback(0);
            long lastTime = System.nanoTime();
            while (this.keepAlive) {
                int bytesRead = WebRtcAudioRecord.this.audioRecord.read(WebRtcAudioRecord.this.byteBuffer, WebRtcAudioRecord.this.byteBuffer.capacity());
                if (bytesRead == WebRtcAudioRecord.this.byteBuffer.capacity()) {
                    if (WebRtcAudioRecord.this.microphoneMute) {
                        WebRtcAudioRecord.this.byteBuffer.clear();
                        WebRtcAudioRecord.this.byteBuffer.put(WebRtcAudioRecord.this.emptyBytes);
                    }
                    if (this.keepAlive) {
                        WebRtcAudioRecord.this.nativeDataIsRecorded(WebRtcAudioRecord.this.nativeAudioRecord, bytesRead);
                    }
                    if (WebRtcAudioRecord.this.audioSamplesReadyCallback == null) continue;
                    byte[] data = Arrays.copyOfRange(WebRtcAudioRecord.this.byteBuffer.array(), WebRtcAudioRecord.this.byteBuffer.arrayOffset(), WebRtcAudioRecord.this.byteBuffer.capacity() + WebRtcAudioRecord.this.byteBuffer.arrayOffset());
                    WebRtcAudioRecord.this.audioSamplesReadyCallback.onWebRtcAudioRecordSamplesReady(new JavaAudioDeviceModule.AudioSamples(WebRtcAudioRecord.this.audioRecord.getAudioFormat(), WebRtcAudioRecord.this.audioRecord.getChannelCount(), WebRtcAudioRecord.this.audioRecord.getSampleRate(), data));
                    continue;
                }
                String errorMessage = "AudioRecord.read failed: " + bytesRead;
                Logging.e(WebRtcAudioRecord.TAG, errorMessage);
                if (bytesRead != -3) continue;
                this.keepAlive = false;
                WebRtcAudioRecord.this.reportWebRtcAudioRecordError(errorMessage);
            }
            try {
                if (WebRtcAudioRecord.this.audioRecord != null) {
                    WebRtcAudioRecord.this.audioRecord.stop();
                    WebRtcAudioRecord.this.doAudioRecordStateCallback(1);
                }
            }
            catch (IllegalStateException e) {
                Logging.e(WebRtcAudioRecord.TAG, "AudioRecord.stop failed: " + e.getMessage());
            }
        }

        public void stopThread() {
            Logging.d(WebRtcAudioRecord.TAG, "stopThread");
            this.keepAlive = false;
        }
    }
}

