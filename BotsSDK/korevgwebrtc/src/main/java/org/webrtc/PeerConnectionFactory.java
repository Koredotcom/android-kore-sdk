/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.os.Process
 *  androidx.annotation.Nullable
 */
package org.webrtc;

import android.content.Context;
import android.os.Process;
import androidx.annotation.Nullable;
import java.util.List;
import org.webrtc.AudioDecoderFactoryFactory;
import org.webrtc.AudioEncoderFactoryFactory;
import org.webrtc.AudioProcessingFactory;
import org.webrtc.AudioSource;
import org.webrtc.AudioTrack;
import org.webrtc.BuiltinAudioDecoderFactoryFactory;
import org.webrtc.BuiltinAudioEncoderFactoryFactory;
import org.webrtc.CalledByNative;
import org.webrtc.ContextUtils;
import org.webrtc.FecControllerFactoryFactoryInterface;
import org.webrtc.JNILogging;
import org.webrtc.Loggable;
import org.webrtc.Logging;
import org.webrtc.MediaConstraints;
import org.webrtc.MediaStream;
import org.webrtc.NativeLibrary;
import org.webrtc.NativeLibraryLoader;
import org.webrtc.NetEqFactoryFactory;
import org.webrtc.NetworkControllerFactoryFactory;
import org.webrtc.NetworkStatePredictorFactoryFactory;
import org.webrtc.PeerConnection;
import org.webrtc.PeerConnectionDependencies;
import org.webrtc.SSLCertificateVerifier;
import org.webrtc.VideoDecoderFactory;
import org.webrtc.VideoEncoderFactory;
import org.webrtc.VideoSource;
import org.webrtc.VideoTrack;
import org.webrtc.audio.AudioDeviceModule;
import org.webrtc.audio.JavaAudioDeviceModule;

public class PeerConnectionFactory {
    public static final String TRIAL_ENABLED = "Enabled";
    @Deprecated
    public static final String VIDEO_FRAME_EMIT_TRIAL = "VideoFrameEmit";
    private static final String TAG = "PeerConnectionFactory";
    private static final String VIDEO_CAPTURER_THREAD_NAME = "VideoCapturerThread";
    private static volatile boolean internalTracerInitialized;
    @Nullable
    private static ThreadInfo staticNetworkThread;
    @Nullable
    private static ThreadInfo staticWorkerThread;
    @Nullable
    private static ThreadInfo staticSignalingThread;
    private long nativeFactory;
    @Nullable
    private volatile ThreadInfo networkThread;
    @Nullable
    private volatile ThreadInfo workerThread;
    @Nullable
    private volatile ThreadInfo signalingThread;

    public static Builder builder() {
        return new Builder();
    }

    public static void initialize(InitializationOptions options) {
        ContextUtils.initialize(options.applicationContext);
        NativeLibrary.initialize(options.nativeLibraryLoader, options.nativeLibraryName);
        PeerConnectionFactory.nativeInitializeAndroidGlobals();
        PeerConnectionFactory.nativeInitializeFieldTrials(options.fieldTrials);
        if (options.enableInternalTracer && !internalTracerInitialized) {
            PeerConnectionFactory.initializeInternalTracer();
        }
        if (options.loggable != null) {
            Logging.injectLoggable(options.loggable, options.loggableSeverity);
            PeerConnectionFactory.nativeInjectLoggable(new JNILogging(options.loggable), options.loggableSeverity.ordinal());
        } else {
            Logging.d(TAG, "PeerConnectionFactory was initialized without an injected Loggable. Any existing Loggable will be deleted.");
            Logging.deleteInjectedLoggable();
            PeerConnectionFactory.nativeDeleteLoggable();
        }
    }

    private static void checkInitializeHasBeenCalled() {
        if (!NativeLibrary.isLoaded() || ContextUtils.getApplicationContext() == null) {
            throw new IllegalStateException("PeerConnectionFactory.initialize was not called before creating a PeerConnectionFactory.");
        }
    }

    private static void initializeInternalTracer() {
        internalTracerInitialized = true;
        PeerConnectionFactory.nativeInitializeInternalTracer();
    }

    public static void shutdownInternalTracer() {
        internalTracerInitialized = false;
        PeerConnectionFactory.nativeShutdownInternalTracer();
    }

    @Deprecated
    public static void initializeFieldTrials(String fieldTrialsInitString) {
        PeerConnectionFactory.nativeInitializeFieldTrials(fieldTrialsInitString);
    }

    public static String fieldTrialsFindFullName(String name) {
        return NativeLibrary.isLoaded() ? PeerConnectionFactory.nativeFindFieldTrialsFullName(name) : "";
    }

    public static boolean startInternalTracingCapture(String tracingFilename) {
        return PeerConnectionFactory.nativeStartInternalTracingCapture(tracingFilename);
    }

    public static void stopInternalTracingCapture() {
        PeerConnectionFactory.nativeStopInternalTracingCapture();
    }

    @CalledByNative
    PeerConnectionFactory(long nativeFactory) {
        PeerConnectionFactory.checkInitializeHasBeenCalled();
        if (nativeFactory == 0L) {
            throw new RuntimeException("Failed to initialize PeerConnectionFactory!");
        }
        this.nativeFactory = nativeFactory;
    }

    @Nullable
    PeerConnection createPeerConnectionInternal(PeerConnection.RTCConfiguration rtcConfig, MediaConstraints constraints, PeerConnection.Observer observer, SSLCertificateVerifier sslCertificateVerifier) {
        this.checkPeerConnectionFactoryExists();
        long nativeObserver = PeerConnection.createNativePeerConnectionObserver(observer);
        if (nativeObserver == 0L) {
            return null;
        }
        long nativePeerConnection = PeerConnectionFactory.nativeCreatePeerConnection(this.nativeFactory, rtcConfig, constraints, nativeObserver, sslCertificateVerifier);
        if (nativePeerConnection == 0L) {
            return null;
        }
        return new PeerConnection(nativePeerConnection);
    }

    @Deprecated
    @Nullable
    public PeerConnection createPeerConnection(PeerConnection.RTCConfiguration rtcConfig, MediaConstraints constraints, PeerConnection.Observer observer) {
        return this.createPeerConnectionInternal(rtcConfig, constraints, observer, null);
    }

    @Deprecated
    @Nullable
    public PeerConnection createPeerConnection(List<PeerConnection.IceServer> iceServers, MediaConstraints constraints, PeerConnection.Observer observer) {
        PeerConnection.RTCConfiguration rtcConfig = new PeerConnection.RTCConfiguration(iceServers);
        return this.createPeerConnection(rtcConfig, constraints, observer);
    }

    @Nullable
    public PeerConnection createPeerConnection(List<PeerConnection.IceServer> iceServers, PeerConnection.Observer observer) {
        PeerConnection.RTCConfiguration rtcConfig = new PeerConnection.RTCConfiguration(iceServers);
        return this.createPeerConnection(rtcConfig, observer);
    }

    @Nullable
    public PeerConnection createPeerConnection(PeerConnection.RTCConfiguration rtcConfig, PeerConnection.Observer observer) {
        return this.createPeerConnection(rtcConfig, null, observer);
    }

    @Nullable
    public PeerConnection createPeerConnection(PeerConnection.RTCConfiguration rtcConfig, PeerConnectionDependencies dependencies) {
        return this.createPeerConnectionInternal(rtcConfig, null, dependencies.getObserver(), dependencies.getSSLCertificateVerifier());
    }

    public MediaStream createLocalMediaStream(String label) {
        this.checkPeerConnectionFactoryExists();
        return new MediaStream(PeerConnectionFactory.nativeCreateLocalMediaStream(this.nativeFactory, label));
    }

    public VideoSource createVideoSource(boolean isScreencast, boolean alignTimestamps) {
        this.checkPeerConnectionFactoryExists();
        return new VideoSource(PeerConnectionFactory.nativeCreateVideoSource(this.nativeFactory, isScreencast, alignTimestamps));
    }

    public VideoSource createVideoSource(boolean isScreencast) {
        return this.createVideoSource(isScreencast, true);
    }

    public VideoTrack createVideoTrack(String id, VideoSource source) {
        this.checkPeerConnectionFactoryExists();
        return new VideoTrack(PeerConnectionFactory.nativeCreateVideoTrack(this.nativeFactory, id, source.getNativeVideoTrackSource()));
    }

    public AudioSource createAudioSource(MediaConstraints constraints) {
        this.checkPeerConnectionFactoryExists();
        return new AudioSource(PeerConnectionFactory.nativeCreateAudioSource(this.nativeFactory, constraints));
    }

    public AudioTrack createAudioTrack(String id, AudioSource source) {
        this.checkPeerConnectionFactoryExists();
        return new AudioTrack(PeerConnectionFactory.nativeCreateAudioTrack(this.nativeFactory, id, source.getNativeAudioSource()));
    }

    public boolean startAecDump(int file_descriptor, int filesize_limit_bytes) {
        this.checkPeerConnectionFactoryExists();
        return PeerConnectionFactory.nativeStartAecDump(this.nativeFactory, file_descriptor, filesize_limit_bytes);
    }

    public void stopAecDump() {
        this.checkPeerConnectionFactoryExists();
        PeerConnectionFactory.nativeStopAecDump(this.nativeFactory);
    }

    public void dispose() {
        this.checkPeerConnectionFactoryExists();
        PeerConnectionFactory.nativeFreeFactory(this.nativeFactory);
        this.networkThread = null;
        this.workerThread = null;
        this.signalingThread = null;
        this.nativeFactory = 0L;
    }

    public long getNativePeerConnectionFactory() {
        this.checkPeerConnectionFactoryExists();
        return PeerConnectionFactory.nativeGetNativePeerConnectionFactory(this.nativeFactory);
    }

    public long getNativeOwnedFactoryAndThreads() {
        this.checkPeerConnectionFactoryExists();
        return this.nativeFactory;
    }

    private void checkPeerConnectionFactoryExists() {
        if (this.nativeFactory == 0L) {
            throw new IllegalStateException("PeerConnectionFactory has been disposed.");
        }
    }

    private static void printStackTrace(@Nullable ThreadInfo threadInfo, boolean printNativeStackTrace) {
        if (threadInfo == null) {
            return;
        }
        String threadName = threadInfo.thread.getName();
        StackTraceElement[] stackTraces = threadInfo.thread.getStackTrace();
        if (stackTraces.length > 0) {
            Logging.w(TAG, threadName + " stacktrace:");
            for (StackTraceElement stackTrace : stackTraces) {
                Logging.w(TAG, stackTrace.toString());
            }
        }
        if (printNativeStackTrace) {
            Logging.w(TAG, "*** *** *** *** *** *** *** *** *** *** *** *** *** *** *** ***");
            Logging.w(TAG, "pid: " + Process.myPid() + ", tid: " + threadInfo.tid + ", name: " + threadName + "  >>> WebRTC <<<");
            PeerConnectionFactory.nativePrintStackTrace(threadInfo.tid);
        }
    }

    @Deprecated
    public static void printStackTraces() {
        PeerConnectionFactory.printStackTrace(staticNetworkThread, false);
        PeerConnectionFactory.printStackTrace(staticWorkerThread, false);
        PeerConnectionFactory.printStackTrace(staticSignalingThread, false);
    }

    public void printInternalStackTraces(boolean printNativeStackTraces) {
        PeerConnectionFactory.printStackTrace(this.signalingThread, printNativeStackTraces);
        PeerConnectionFactory.printStackTrace(this.workerThread, printNativeStackTraces);
        PeerConnectionFactory.printStackTrace(this.networkThread, printNativeStackTraces);
        if (printNativeStackTraces) {
            PeerConnectionFactory.nativePrintStackTracesOfRegisteredThreads();
        }
    }

    @CalledByNative
    private void onNetworkThreadReady() {
        staticNetworkThread = this.networkThread = ThreadInfo.getCurrent();
        Logging.d(TAG, "onNetworkThreadReady");
    }

    @CalledByNative
    private void onWorkerThreadReady() {
        staticWorkerThread = this.workerThread = ThreadInfo.getCurrent();
        Logging.d(TAG, "onWorkerThreadReady");
    }

    @CalledByNative
    private void onSignalingThreadReady() {
        staticSignalingThread = this.signalingThread = ThreadInfo.getCurrent();
        Logging.d(TAG, "onSignalingThreadReady");
    }

    private static native void nativeInitializeAndroidGlobals();

    private static native void nativeInitializeFieldTrials(String var0);

    private static native String nativeFindFieldTrialsFullName(String var0);

    private static native void nativeInitializeInternalTracer();

    private static native void nativeShutdownInternalTracer();

    private static native boolean nativeStartInternalTracingCapture(String var0);

    private static native void nativeStopInternalTracingCapture();

    private static native PeerConnectionFactory nativeCreatePeerConnectionFactory(Context var0, Options var1, long var2, long var4, long var6, VideoEncoderFactory var8, VideoDecoderFactory var9, long var10, long var12, long var14, long var16, long var18);

    private static native long nativeCreatePeerConnection(long var0, PeerConnection.RTCConfiguration var2, MediaConstraints var3, long var4, SSLCertificateVerifier var6);

    private static native long nativeCreateLocalMediaStream(long var0, String var2);

    private static native long nativeCreateVideoSource(long var0, boolean var2, boolean var3);

    private static native long nativeCreateVideoTrack(long var0, String var2, long var3);

    private static native long nativeCreateAudioSource(long var0, MediaConstraints var2);

    private static native long nativeCreateAudioTrack(long var0, String var2, long var3);

    private static native boolean nativeStartAecDump(long var0, int var2, int var3);

    private static native void nativeStopAecDump(long var0);

    private static native void nativeFreeFactory(long var0);

    private static native long nativeGetNativePeerConnectionFactory(long var0);

    private static native void nativeInjectLoggable(JNILogging var0, int var1);

    private static native void nativeDeleteLoggable();

    private static native void nativePrintStackTrace(int var0);

    private static native void nativePrintStackTracesOfRegisteredThreads();

    public static class Builder {
        @Nullable
        private Options options;
        @Nullable
        private AudioDeviceModule audioDeviceModule;
        private AudioEncoderFactoryFactory audioEncoderFactoryFactory = new BuiltinAudioEncoderFactoryFactory();
        private AudioDecoderFactoryFactory audioDecoderFactoryFactory = new BuiltinAudioDecoderFactoryFactory();
        @Nullable
        private VideoEncoderFactory videoEncoderFactory;
        @Nullable
        private VideoDecoderFactory videoDecoderFactory;
        @Nullable
        private AudioProcessingFactory audioProcessingFactory;
        @Nullable
        private FecControllerFactoryFactoryInterface fecControllerFactoryFactory;
        @Nullable
        private NetworkControllerFactoryFactory networkControllerFactoryFactory;
        @Nullable
        private NetworkStatePredictorFactoryFactory networkStatePredictorFactoryFactory;
        @Nullable
        private NetEqFactoryFactory neteqFactoryFactory;

        private Builder() {
        }

        public Builder setOptions(Options options) {
            this.options = options;
            return this;
        }

        public Builder setAudioDeviceModule(AudioDeviceModule audioDeviceModule) {
            this.audioDeviceModule = audioDeviceModule;
            return this;
        }

        public Builder setAudioEncoderFactoryFactory(AudioEncoderFactoryFactory audioEncoderFactoryFactory) {
            if (audioEncoderFactoryFactory == null) {
                throw new IllegalArgumentException("PeerConnectionFactory.Builder does not accept a null AudioEncoderFactoryFactory.");
            }
            this.audioEncoderFactoryFactory = audioEncoderFactoryFactory;
            return this;
        }

        public Builder setAudioDecoderFactoryFactory(AudioDecoderFactoryFactory audioDecoderFactoryFactory) {
            if (audioDecoderFactoryFactory == null) {
                throw new IllegalArgumentException("PeerConnectionFactory.Builder does not accept a null AudioDecoderFactoryFactory.");
            }
            this.audioDecoderFactoryFactory = audioDecoderFactoryFactory;
            return this;
        }

        public Builder setVideoEncoderFactory(VideoEncoderFactory videoEncoderFactory) {
            this.videoEncoderFactory = videoEncoderFactory;
            return this;
        }

        public Builder setVideoDecoderFactory(VideoDecoderFactory videoDecoderFactory) {
            this.videoDecoderFactory = videoDecoderFactory;
            return this;
        }

        public Builder setAudioProcessingFactory(AudioProcessingFactory audioProcessingFactory) {
            if (audioProcessingFactory == null) {
                throw new NullPointerException("PeerConnectionFactory builder does not accept a null AudioProcessingFactory.");
            }
            this.audioProcessingFactory = audioProcessingFactory;
            return this;
        }

        public Builder setFecControllerFactoryFactoryInterface(FecControllerFactoryFactoryInterface fecControllerFactoryFactory) {
            this.fecControllerFactoryFactory = fecControllerFactoryFactory;
            return this;
        }

        public Builder setNetworkControllerFactoryFactory(NetworkControllerFactoryFactory networkControllerFactoryFactory) {
            this.networkControllerFactoryFactory = networkControllerFactoryFactory;
            return this;
        }

        public Builder setNetworkStatePredictorFactoryFactory(NetworkStatePredictorFactoryFactory networkStatePredictorFactoryFactory) {
            this.networkStatePredictorFactoryFactory = networkStatePredictorFactoryFactory;
            return this;
        }

        public Builder setNetEqFactoryFactory(NetEqFactoryFactory neteqFactoryFactory) {
            this.neteqFactoryFactory = neteqFactoryFactory;
            return this;
        }

        public PeerConnectionFactory createPeerConnectionFactory() {
            PeerConnectionFactory.checkInitializeHasBeenCalled();
            if (this.audioDeviceModule == null) {
                this.audioDeviceModule = JavaAudioDeviceModule.builder(ContextUtils.getApplicationContext()).createAudioDeviceModule();
            }
            return PeerConnectionFactory.nativeCreatePeerConnectionFactory(ContextUtils.getApplicationContext(), this.options, this.audioDeviceModule.getNativeAudioDeviceModulePointer(), this.audioEncoderFactoryFactory.createNativeAudioEncoderFactory(), this.audioDecoderFactoryFactory.createNativeAudioDecoderFactory(), this.videoEncoderFactory, this.videoDecoderFactory, this.audioProcessingFactory == null ? 0L : this.audioProcessingFactory.createNative(), this.fecControllerFactoryFactory == null ? 0L : this.fecControllerFactoryFactory.createNative(), this.networkControllerFactoryFactory == null ? 0L : this.networkControllerFactoryFactory.createNativeNetworkControllerFactory(), this.networkStatePredictorFactoryFactory == null ? 0L : this.networkStatePredictorFactoryFactory.createNativeNetworkStatePredictorFactory(), this.neteqFactoryFactory == null ? 0L : this.neteqFactoryFactory.createNativeNetEqFactory());
        }
    }

    public static class Options {
        static final int ADAPTER_TYPE_UNKNOWN = 0;
        static final int ADAPTER_TYPE_ETHERNET = 1;
        static final int ADAPTER_TYPE_WIFI = 2;
        static final int ADAPTER_TYPE_CELLULAR = 4;
        static final int ADAPTER_TYPE_VPN = 8;
        static final int ADAPTER_TYPE_LOOPBACK = 16;
        static final int ADAPTER_TYPE_ANY = 32;
        public int networkIgnoreMask;
        public boolean disableEncryption;
        public boolean disableNetworkMonitor;

        @CalledByNative(value="Options")
        int getNetworkIgnoreMask() {
            return this.networkIgnoreMask;
        }

        @CalledByNative(value="Options")
        boolean getDisableEncryption() {
            return this.disableEncryption;
        }

        @CalledByNative(value="Options")
        boolean getDisableNetworkMonitor() {
            return this.disableNetworkMonitor;
        }
    }

    public static class InitializationOptions {
        final Context applicationContext;
        final String fieldTrials;
        final boolean enableInternalTracer;
        final NativeLibraryLoader nativeLibraryLoader;
        final String nativeLibraryName;
        @Nullable
        Loggable loggable;
        @Nullable
        Logging.Severity loggableSeverity;

        private InitializationOptions(Context applicationContext, String fieldTrials, boolean enableInternalTracer, NativeLibraryLoader nativeLibraryLoader, String nativeLibraryName, @Nullable Loggable loggable, @Nullable Logging.Severity loggableSeverity) {
            this.applicationContext = applicationContext;
            this.fieldTrials = fieldTrials;
            this.enableInternalTracer = enableInternalTracer;
            this.nativeLibraryLoader = nativeLibraryLoader;
            this.nativeLibraryName = nativeLibraryName;
            this.loggable = loggable;
            this.loggableSeverity = loggableSeverity;
        }

        public static Builder builder(Context applicationContext) {
            return new Builder(applicationContext);
        }

        public static class Builder {
            private final Context applicationContext;
            private String fieldTrials = "";
            private boolean enableInternalTracer;
            private NativeLibraryLoader nativeLibraryLoader = new NativeLibrary.DefaultLoader();
            private String nativeLibraryName = "jingle_peerconnection_so";
            @Nullable
            private Loggable loggable;
            @Nullable
            private Logging.Severity loggableSeverity;

            Builder(Context applicationContext) {
                this.applicationContext = applicationContext;
            }

            public Builder setFieldTrials(String fieldTrials) {
                this.fieldTrials = fieldTrials;
                return this;
            }

            public Builder setEnableInternalTracer(boolean enableInternalTracer) {
                this.enableInternalTracer = enableInternalTracer;
                return this;
            }

            public Builder setNativeLibraryLoader(NativeLibraryLoader nativeLibraryLoader) {
                this.nativeLibraryLoader = nativeLibraryLoader;
                return this;
            }

            public Builder setNativeLibraryName(String nativeLibraryName) {
                this.nativeLibraryName = nativeLibraryName;
                return this;
            }

            public Builder setInjectableLogger(Loggable loggable, Logging.Severity severity) {
                this.loggable = loggable;
                this.loggableSeverity = severity;
                return this;
            }

            public InitializationOptions createInitializationOptions() {
                return new InitializationOptions(this.applicationContext, this.fieldTrials, this.enableInternalTracer, this.nativeLibraryLoader, this.nativeLibraryName, this.loggable, this.loggableSeverity);
            }
        }
    }

    private static class ThreadInfo {
        final Thread thread;
        final int tid;

        public static ThreadInfo getCurrent() {
            return new ThreadInfo(Thread.currentThread(), Process.myTid());
        }

        private ThreadInfo(Thread thread, int tid) {
            this.thread = thread;
            this.tid = tid;
        }
    }
}

