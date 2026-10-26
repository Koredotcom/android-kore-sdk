/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  org.webrtc.DefaultVideoDecoderFactory
 *  org.webrtc.DefaultVideoEncoderFactory
 *  org.webrtc.EglBase
 *  org.webrtc.Logging
 *  org.webrtc.Logging$Severity
 *  org.webrtc.PeerConnection$IceServer
 *  org.webrtc.PeerConnection$RTCConfiguration
 *  org.webrtc.PeerConnection$RtcpMuxPolicy
 *  org.webrtc.PeerConnection$SdpSemantics
 *  org.webrtc.PeerConnectionFactory
 *  org.webrtc.PeerConnectionFactory$InitializationOptions
 *  org.webrtc.PeerConnectionFactory$InitializationOptions$Builder
 *  org.webrtc.PeerConnectionFactory$Options
 *  org.webrtc.SoftwareVideoDecoderFactory
 *  org.webrtc.SoftwareVideoEncoderFactory
 *  org.webrtc.VideoDecoderFactory
 *  org.webrtc.VideoEncoderFactory
 *  org.webrtc.audio.AudioDeviceModule
 *  org.webrtc.audio.JavaAudioDeviceModule
 *  org.webrtc.audio.JavaAudioDeviceModule$AudioRecordErrorCallback
 *  org.webrtc.audio.JavaAudioDeviceModule$AudioRecordStartErrorCode
 *  org.webrtc.audio.JavaAudioDeviceModule$AudioTrackErrorCallback
 *  org.webrtc.audio.JavaAudioDeviceModule$AudioTrackStartErrorCode
 */
package com.korevg.mv.webrtcsdk.webrtc;

import android.content.Context;
import com.korevg.mv.webrtcsdk.log.Log;
import com.korevg.mv.webrtcsdk.log.LogLevel;
import com.korevg.mv.webrtcsdk.webrtc.ACWebRTCCall;
import com.korevg.mv.webrtcsdk.webrtc.listeners.WebRTCCallEventListener;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import org.webrtc.DefaultVideoDecoderFactory;
import org.webrtc.DefaultVideoEncoderFactory;
import org.webrtc.EglBase;
import org.webrtc.Logging;
import org.webrtc.PeerConnection;
import org.webrtc.PeerConnectionFactory;
import org.webrtc.SoftwareVideoDecoderFactory;
import org.webrtc.SoftwareVideoEncoderFactory;
import org.webrtc.VideoDecoderFactory;
import org.webrtc.VideoEncoderFactory;
import org.webrtc.audio.AudioDeviceModule;
import org.webrtc.audio.JavaAudioDeviceModule;

public class ACWebRTCManager {
    private static final String TAG = "ACWebRTCManager";
    private List<PeerConnection.IceServer> iceServerList;
    private ACWebRTCCall webRTCCall;
    private static ACWebRTCManager instance;
    private ScheduledExecutorService executor;
    private PeerConnectionFactory factory;
    private PeerConnection.RTCConfiguration rtcConfiguration;
    private EglBase rootEglBase;
    public static Context context;
    private LogLevel logLevel = LogLevel.DEBUG;
    private boolean useOpenSLES = true;
    private boolean disableBuiltInAEC = false;
    private boolean disableBuiltInNS = false;
    private boolean enableH264HighProfile = true;
    private boolean videoCodecHwAcceleration = false;

    private ACWebRTCManager() {
        this.executor = Executors.newSingleThreadScheduledExecutor();
    }

    public static synchronized ACWebRTCManager getInstance() {
        if (instance == null) {
            instance = new ACWebRTCManager();
        }
        return instance;
    }

    public void initWebRTC(final Context context) {
        ACWebRTCManager.context = context;
        this.executor.execute(new Runnable(){

            @Override
            public void run() {
                ACWebRTCManager.this.initWebRTCInternal(context);
            }
        });
    }

    private void initWebRTCInternal(Context context) {
        VideoDecoderFactory decoderFactory;
        VideoEncoderFactory encoderFactory;
        PeerConnectionFactory.Options options = new PeerConnectionFactory.Options();
        PeerConnectionFactory.InitializationOptions.Builder initOptionsBuilder = PeerConnectionFactory.InitializationOptions.builder((Context)context);
        PeerConnectionFactory.InitializationOptions initOptions = initOptionsBuilder.createInitializationOptions();
        PeerConnectionFactory.initialize((PeerConnectionFactory.InitializationOptions)initOptions);
        AudioDeviceModule adm = null;
        adm = this.createJavaAudioDevice(context);
        this.rootEglBase = EglBase.create();
        if (this.videoCodecHwAcceleration) {
            Log.d(TAG, "DefaultVideoEncoderFactory");
            encoderFactory = new DefaultVideoEncoderFactory(this.rootEglBase.getEglBaseContext(), true, this.enableH264HighProfile);
            decoderFactory = new DefaultVideoDecoderFactory(this.rootEglBase.getEglBaseContext());
        } else {
            Log.d(TAG, "SoftwareVideoEncoderFactory");
            encoderFactory = new SoftwareVideoEncoderFactory();
            decoderFactory = new SoftwareVideoDecoderFactory();
        }
        this.factory = PeerConnectionFactory.builder().setOptions(options).setAudioDeviceModule(adm).setVideoEncoderFactory((VideoEncoderFactory)encoderFactory).setVideoDecoderFactory((VideoDecoderFactory)decoderFactory).createPeerConnectionFactory();
        Log.d(TAG, "Peer connection factory created.");
        adm.release();
        this.setLogLevelInternal(this.logLevel);
        this.rtcConfiguration = new PeerConnection.RTCConfiguration(this.iceServerList);
        this.rtcConfiguration.rtcpMuxPolicy = PeerConnection.RtcpMuxPolicy.NEGOTIATE;
        this.rtcConfiguration.sdpSemantics = PeerConnection.SdpSemantics.UNIFIED_PLAN;
    }

    public void setIceServerList(List<PeerConnection.IceServer> iceServerList) {
        this.iceServerList = iceServerList == null ? new ArrayList<PeerConnection.IceServer>() : iceServerList;
    }

    public void deinitWebRTC() {
        Log.d(TAG, "begin of deinitWebRTC");
        Future<Void> result = this.executor.submit(new Callable<Void>(){

            @Override
            public Void call() throws Exception {
                ACWebRTCManager.this.deinitWebRTCInternal();
                Log.d(ACWebRTCManager.TAG, "end of deinitWebRTC internal");
                return null;
            }
        });
        try {
            result.get();
        }
        catch (Exception e) {
            Log.d(TAG, "Oops: " + e.getMessage());
        }
        Log.d(TAG, "end of deinitWebRTC");
    }

    private void deinitWebRTCInternal() {
        Log.d(TAG, "deinitWebRTCInternal");
        Log.d(TAG, "Closing peer connection factory.");
        if (this.factory != null) {
            this.factory.dispose();
            this.factory = null;
        }
        PeerConnectionFactory.stopInternalTracingCapture();
        PeerConnectionFactory.shutdownInternalTracer();
    }

    public ACWebRTCCall initCall(final boolean withVideo, WebRTCCallEventListener webRTCCallEventListener) {
        this.webRTCCall = new ACWebRTCCall(webRTCCallEventListener);
        Log.d(TAG, "Init webrtc call: " + withVideo);
        Future<Void> result = this.executor.submit(new Callable<Void>(){

            @Override
            public Void call() throws Exception {
                ACWebRTCManager.this.webRTCCall.initCall(ACWebRTCManager.this.factory, ACWebRTCManager.this.rootEglBase, ACWebRTCManager.this.executor, ACWebRTCManager.this.rtcConfiguration, withVideo);
                Log.d(ACWebRTCManager.TAG, "end of init call internal");
                return null;
            }
        });
        try {
            result.get();
        }
        catch (Exception e) {
            Log.d(TAG, "Oops: " + e.getMessage());
        }
        Log.d(TAG, "end of init call");
        return this.webRTCCall;
    }

    public void setLogLevel(LogLevel logL) {
        this.logLevel = logL;
        Log.d(TAG, "setLogLevel: " + (Object)((Object)this.logLevel));
        if (this.factory == null) {
            Log.d(TAG, "Log level for WebRTC can only be set after initializing WebRTC");
            return;
        }
        Future<Void> result = this.executor.submit(new Callable<Void>(){

            @Override
            public Void call() throws Exception {
                ACWebRTCManager.this.setLogLevelInternal(ACWebRTCManager.this.logLevel);
                Log.d(ACWebRTCManager.TAG, "end of setLogLevel internal");
                return null;
            }
        });
        try {
            result.get();
        }
        catch (Exception e) {
            Log.d(TAG, "Oops: " + e.getMessage());
        }
        Log.d(TAG, "end of setLogLevel");
    }

    private void setLogLevelInternal(LogLevel logLevel) {
        Logging.Severity logL = Logging.Severity.LS_INFO;
        switch (logLevel) {
            case VERBOSE: {
                logL = Logging.Severity.LS_VERBOSE;
                Logging.enableLogToDebugOutput((Logging.Severity)logL);
                break;
            }
            case DEBUG: {
                logL = Logging.Severity.LS_VERBOSE;
                Logging.enableLogToDebugOutput((Logging.Severity)logL);
                break;
            }
            case INFO: {
                logL = Logging.Severity.LS_INFO;
                break;
            }
            case WARN: {
                logL = Logging.Severity.LS_WARNING;
                break;
            }
            case ERROR: {
                logL = Logging.Severity.LS_ERROR;
                break;
            }
            case ASSERT: {
                logL = Logging.Severity.LS_ERROR;
                break;
            }
            case NONE: {
                logL = Logging.Severity.LS_NONE;
            }
        }
    }

    public void setVideoCodecHwAcceleration(boolean videoCodecHwAcceleration) {
        this.videoCodecHwAcceleration = videoCodecHwAcceleration;
    }

    private AudioDeviceModule createJavaAudioDevice(Context context) {
        if (!this.useOpenSLES) {
            Log.w(TAG, "External OpenSLES ADM not implemented yet.");
        }
        JavaAudioDeviceModule.AudioRecordErrorCallback audioRecordErrorCallback = new JavaAudioDeviceModule.AudioRecordErrorCallback(){

            public void onWebRtcAudioRecordInitError(String errorMessage) {
                Log.e(ACWebRTCManager.TAG, "onWebRtcAudioRecordInitError: " + errorMessage);
            }

            public void onWebRtcAudioRecordStartError(JavaAudioDeviceModule.AudioRecordStartErrorCode errorCode, String errorMessage) {
                Log.e(ACWebRTCManager.TAG, "onWebRtcAudioRecordStartError: " + errorCode + ". " + errorMessage);
            }

            public void onWebRtcAudioRecordError(String errorMessage) {
                Log.e(ACWebRTCManager.TAG, "onWebRtcAudioRecordError: " + errorMessage);
            }
        };
        JavaAudioDeviceModule.AudioTrackErrorCallback audioTrackErrorCallback = new JavaAudioDeviceModule.AudioTrackErrorCallback(){

            public void onWebRtcAudioTrackInitError(String errorMessage) {
                Log.e(ACWebRTCManager.TAG, "onWebRtcAudioTrackInitError: " + errorMessage);
            }

            public void onWebRtcAudioTrackStartError(JavaAudioDeviceModule.AudioTrackStartErrorCode errorCode, String errorMessage) {
                Log.e(ACWebRTCManager.TAG, "onWebRtcAudioTrackStartError: " + errorCode + ". " + errorMessage);
            }

            public void onWebRtcAudioTrackError(String errorMessage) {
                Log.e(ACWebRTCManager.TAG, "onWebRtcAudioTrackError: " + errorMessage);
            }
        };
        return JavaAudioDeviceModule.builder((Context)context).setUseHardwareAcousticEchoCanceler(!this.disableBuiltInAEC).setUseHardwareNoiseSuppressor(!this.disableBuiltInNS).setAudioRecordErrorCallback(audioRecordErrorCallback).setAudioTrackErrorCallback(audioTrackErrorCallback).createAudioDeviceModule();
    }
}

