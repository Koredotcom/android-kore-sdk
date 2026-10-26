/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.media.AudioRecord
 *  android.os.Build$VERSION
 *  android.os.Process
 *  androidx.annotation.Nullable
 */
package org.webrtc.voiceengine;

import android.media.AudioRecord;
import android.os.Build;
import android.os.Process;
import androidx.annotation.Nullable;
import java.nio.ByteBuffer;
import java.util.Arrays;
import org.webrtc.Logging;
import org.webrtc.ThreadUtils;
import org.webrtc.voiceengine.WebRtcAudioEffects;
import org.webrtc.voiceengine.WebRtcAudioUtils;

public class WebRtcAudioRecord {
    private static final boolean DEBUG = false;
    private static final String TAG = "WebRtcAudioRecord";
    private static final int BITS_PER_SAMPLE = 16;
    private static final int CALLBACK_BUFFER_SIZE_MS = 10;
    private static final int BUFFERS_PER_SECOND = 100;
    private static final int BUFFER_SIZE_FACTOR = 2;
    private static final long AUDIO_RECORD_THREAD_JOIN_TIMEOUT_MS = 2000L;
    private static final int DEFAULT_AUDIO_SOURCE;
    private static int audioSource;
    private final long nativeAudioRecord;
    @Nullable
    private WebRtcAudioEffects effects;
    private ByteBuffer byteBuffer;
    @Nullable
    private AudioRecord audioRecord;
    @Nullable
    private AudioRecordThread audioThread;
    private static volatile boolean microphoneMute;
    private byte[] emptyBytes;
    @Nullable
    private static WebRtcAudioRecordErrorCallback errorCallback;
    @Nullable
    private static WebRtcAudioRecordSamplesReadyCallback audioSamplesReadyCallback;

    public static void setErrorCallback(WebRtcAudioRecordErrorCallback errorCallback) {
        Logging.d(TAG, "Set error callback");
        WebRtcAudioRecord.errorCallback = errorCallback;
    }

    public static void setOnAudioSamplesReady(WebRtcAudioRecordSamplesReadyCallback callback) {
        audioSamplesReadyCallback = callback;
    }

    WebRtcAudioRecord(long nativeAudioRecord) {
        Logging.d(TAG, "ctor" + WebRtcAudioUtils.getThreadInfo());
        this.nativeAudioRecord = nativeAudioRecord;
        this.effects = WebRtcAudioEffects.create();
    }

    private boolean enableBuiltInAEC(boolean enable) {
        Logging.d(TAG, "enableBuiltInAEC(" + enable + ')');
        if (this.effects == null) {
            Logging.e(TAG, "Built-in AEC is not supported on this platform");
            return false;
        }
        return this.effects.setAEC(enable);
    }

    private boolean enableBuiltInNS(boolean enable) {
        Logging.d(TAG, "enableBuiltInNS(" + enable + ')');
        if (this.effects == null) {
            Logging.e(TAG, "Built-in NS is not supported on this platform");
            return false;
        }
        return this.effects.setNS(enable);
    }

    private int initRecording(int sampleRate, int channels) {
        Logging.d(TAG, "initRecording(sampleRate=" + sampleRate + ", channels=" + channels + ")");
        if (this.audioRecord != null) {
            this.reportWebRtcAudioRecordInitError("InitRecording called twice without StopRecording.");
            return -1;
        }
        int bytesPerFrame = channels * 2;
        int framesPerBuffer = sampleRate / 100;
        this.byteBuffer = ByteBuffer.allocateDirect(bytesPerFrame * framesPerBuffer);
        Logging.d(TAG, "byteBuffer.capacity: " + this.byteBuffer.capacity());
        this.emptyBytes = new byte[this.byteBuffer.capacity()];
        this.nativeCacheDirectBufferAddress(this.byteBuffer, this.nativeAudioRecord);
        int channelConfig = this.channelCountToConfiguration(channels);
        int minBufferSize = AudioRecord.getMinBufferSize((int)sampleRate, (int)channelConfig, (int)2);
        if (minBufferSize == -1 || minBufferSize == -2) {
            this.reportWebRtcAudioRecordInitError("AudioRecord.getMinBufferSize failed: " + minBufferSize);
            return -1;
        }
        Logging.d(TAG, "AudioRecord.getMinBufferSize: " + minBufferSize);
        int bufferSizeInBytes = Math.max(2 * minBufferSize, this.byteBuffer.capacity());
        Logging.d(TAG, "bufferSizeInBytes: " + bufferSizeInBytes);
        try {
            this.audioRecord = new AudioRecord(audioSource, sampleRate, channelConfig, 2, bufferSizeInBytes);
        }
        catch (IllegalArgumentException e) {
            this.reportWebRtcAudioRecordInitError("AudioRecord ctor error: " + e.getMessage());
            this.releaseAudioResources();
            return -1;
        }
        if (this.audioRecord == null || this.audioRecord.getState() != 1) {
            this.reportWebRtcAudioRecordInitError("Failed to create a new AudioRecord instance");
            this.releaseAudioResources();
            return -1;
        }
        if (this.effects != null) {
            this.effects.enable(this.audioRecord.getAudioSessionId());
        }
        this.logMainParameters();
        this.logMainParametersExtended();
        return framesPerBuffer;
    }

    private boolean startRecording() {
        Logging.d(TAG, "startRecording");
        WebRtcAudioRecord.assertTrue(this.audioRecord != null);
        WebRtcAudioRecord.assertTrue(this.audioThread == null);
        try {
            this.audioRecord.startRecording();
        }
        catch (IllegalStateException e) {
            this.reportWebRtcAudioRecordStartError(AudioRecordStartErrorCode.AUDIO_RECORD_START_EXCEPTION, "AudioRecord.startRecording failed: " + e.getMessage());
            return false;
        }
        if (this.audioRecord.getRecordingState() != 3) {
            this.reportWebRtcAudioRecordStartError(AudioRecordStartErrorCode.AUDIO_RECORD_START_STATE_MISMATCH, "AudioRecord.startRecording failed - incorrect state :" + this.audioRecord.getRecordingState());
            return false;
        }
        this.audioThread = new AudioRecordThread("AudioRecordJavaThread");
        this.audioThread.start();
        return true;
    }

    private boolean stopRecording() {
        Logging.d(TAG, "stopRecording");
        WebRtcAudioRecord.assertTrue(this.audioThread != null);
        this.audioThread.stopThread();
        if (!ThreadUtils.joinUninterruptibly(this.audioThread, 2000L)) {
            Logging.e(TAG, "Join of AudioRecordJavaThread timed out");
            WebRtcAudioUtils.logAudioState(TAG);
        }
        this.audioThread = null;
        if (this.effects != null) {
            this.effects.release();
        }
        this.releaseAudioResources();
        return true;
    }

    private void logMainParameters() {
        Logging.d(TAG, "AudioRecord: session ID: " + this.audioRecord.getAudioSessionId() + ", channels: " + this.audioRecord.getChannelCount() + ", sample rate: " + this.audioRecord.getSampleRate());
    }

    private void logMainParametersExtended() {
        if (Build.VERSION.SDK_INT >= 23) {
            Logging.d(TAG, "AudioRecord: buffer size in frames: " + this.audioRecord.getBufferSizeInFrames());
        }
    }

    private static void assertTrue(boolean condition) {
        if (!condition) {
            throw new AssertionError((Object)"Expected condition to be true");
        }
    }

    private int channelCountToConfiguration(int channels) {
        return channels == 1 ? 16 : 12;
    }

    private native void nativeCacheDirectBufferAddress(ByteBuffer var1, long var2);

    private native void nativeDataIsRecorded(int var1, long var2);

    public static synchronized void setAudioSource(int source) {
        Logging.w(TAG, "Audio source is changed from: " + audioSource + " to " + source);
        audioSource = source;
    }

    private static int getDefaultAudioSource() {
        return 7;
    }

    public static void setMicrophoneMute(boolean mute) {
        Logging.w(TAG, "setMicrophoneMute(" + mute + ")");
        microphoneMute = mute;
    }

    private void releaseAudioResources() {
        Logging.d(TAG, "releaseAudioResources");
        if (this.audioRecord != null) {
            this.audioRecord.release();
            this.audioRecord = null;
        }
    }

    private void reportWebRtcAudioRecordInitError(String errorMessage) {
        Logging.e(TAG, "Init recording error: " + errorMessage);
        WebRtcAudioUtils.logAudioState(TAG);
        if (errorCallback != null) {
            errorCallback.onWebRtcAudioRecordInitError(errorMessage);
        }
    }

    private void reportWebRtcAudioRecordStartError(AudioRecordStartErrorCode errorCode, String errorMessage) {
        Logging.e(TAG, "Start recording error: " + (Object)((Object)errorCode) + ". " + errorMessage);
        WebRtcAudioUtils.logAudioState(TAG);
        if (errorCallback != null) {
            errorCallback.onWebRtcAudioRecordStartError(errorCode, errorMessage);
        }
    }

    private void reportWebRtcAudioRecordError(String errorMessage) {
        Logging.e(TAG, "Run-time recording error: " + errorMessage);
        WebRtcAudioUtils.logAudioState(TAG);
        if (errorCallback != null) {
            errorCallback.onWebRtcAudioRecordError(errorMessage);
        }
    }

    static {
        audioSource = DEFAULT_AUDIO_SOURCE = WebRtcAudioRecord.getDefaultAudioSource();
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
            long lastTime = System.nanoTime();
            while (this.keepAlive) {
                int bytesRead = WebRtcAudioRecord.this.audioRecord.read(WebRtcAudioRecord.this.byteBuffer, WebRtcAudioRecord.this.byteBuffer.capacity());
                if (bytesRead == WebRtcAudioRecord.this.byteBuffer.capacity()) {
                    if (microphoneMute) {
                        WebRtcAudioRecord.this.byteBuffer.clear();
                        WebRtcAudioRecord.this.byteBuffer.put(WebRtcAudioRecord.this.emptyBytes);
                    }
                    if (this.keepAlive) {
                        WebRtcAudioRecord.this.nativeDataIsRecorded(bytesRead, WebRtcAudioRecord.this.nativeAudioRecord);
                    }
                    if (audioSamplesReadyCallback == null) continue;
                    byte[] data = Arrays.copyOf(WebRtcAudioRecord.this.byteBuffer.array(), WebRtcAudioRecord.this.byteBuffer.capacity());
                    audioSamplesReadyCallback.onWebRtcAudioRecordSamplesReady(new AudioSamples(WebRtcAudioRecord.this.audioRecord, data));
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

    public static interface WebRtcAudioRecordSamplesReadyCallback {
        public void onWebRtcAudioRecordSamplesReady(AudioSamples var1);
    }

    public static class AudioSamples {
        private final int audioFormat;
        private final int channelCount;
        private final int sampleRate;
        private final byte[] data;

        private AudioSamples(AudioRecord audioRecord, byte[] data) {
            this.audioFormat = audioRecord.getAudioFormat();
            this.channelCount = audioRecord.getChannelCount();
            this.sampleRate = audioRecord.getSampleRate();
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

    public static interface WebRtcAudioRecordErrorCallback {
        public void onWebRtcAudioRecordInitError(String var1);

        public void onWebRtcAudioRecordStartError(AudioRecordStartErrorCode var1, String var2);

        public void onWebRtcAudioRecordError(String var1);
    }

    public static enum AudioRecordStartErrorCode {
        AUDIO_RECORD_START_EXCEPTION,
        AUDIO_RECORD_START_STATE_MISMATCH;

    }
}

