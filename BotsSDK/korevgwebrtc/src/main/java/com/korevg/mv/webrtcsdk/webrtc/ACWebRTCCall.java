/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  android.app.Activity
 *  android.os.Handler
 *  android.view.View
 *  android.view.ViewGroup$LayoutParams
 *  com.korevg.mv.webrtcsdk.R$id
 *  org.json.JSONException
 *  org.json.JSONObject
 *  org.webrtc.AudioSource
 *  org.webrtc.AudioTrack
 *  org.webrtc.Camera1Enumerator
 *  org.webrtc.CameraVideoCapturer
 *  org.webrtc.CameraVideoCapturer$CameraEventsHandler
 *  org.webrtc.CameraVideoCapturer$CameraSwitchHandler
 *  org.webrtc.DataChannel
 *  org.webrtc.DtmfSender
 *  org.webrtc.EglBase
 *  org.webrtc.EglBase$Context
 *  org.webrtc.IceCandidate
 *  org.webrtc.MediaConstraints
 *  org.webrtc.MediaConstraints$KeyValuePair
 *  org.webrtc.MediaStream
 *  org.webrtc.MediaStreamTrack
 *  org.webrtc.PeerConnection
 *  org.webrtc.PeerConnection$IceConnectionState
 *  org.webrtc.PeerConnection$IceGatheringState
 *  org.webrtc.PeerConnection$Observer
 *  org.webrtc.PeerConnection$RTCConfiguration
 *  org.webrtc.PeerConnection$SignalingState
 *  org.webrtc.PeerConnectionFactory
 *  org.webrtc.RTCStatsCollectorCallback
 *  org.webrtc.RTCStatsReport
 *  org.webrtc.RendererCommon$ScalingType
 *  org.webrtc.RtpParameters
 *  org.webrtc.RtpParameters$Codec
 *  org.webrtc.RtpParameters$Encoding
 *  org.webrtc.RtpReceiver
 *  org.webrtc.RtpSender
 *  org.webrtc.RtpTransceiver$RtpTransceiverDirection
 *  org.webrtc.RtpTransceiver$RtpTransceiverInit
 *  org.webrtc.SdpObserver
 *  org.webrtc.SessionDescription
 *  org.webrtc.SessionDescription$Type
 *  org.webrtc.SurfaceTextureHelper
 *  org.webrtc.SurfaceViewRenderer
 *  org.webrtc.VideoFrame
 *  org.webrtc.VideoSink
 *  org.webrtc.VideoSource
 *  org.webrtc.VideoTrack
 */
package com.korevg.mv.webrtcsdk.webrtc;

import android.app.Activity;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import com.korevg.mv.webrtcsdk.R;
import com.korevg.mv.webrtcsdk.log.Log;
import com.korevg.mv.webrtcsdk.session.ACCallStatistics;
import com.korevg.mv.webrtcsdk.session.CallTermination;
import com.korevg.mv.webrtcsdk.session.DTMF;
import com.korevg.mv.webrtcsdk.session.ui.PercentFrameLayout;
import com.korevg.mv.webrtcsdk.useragent.ACConfiguration;
import com.korevg.mv.webrtcsdk.useragent.VideoConfiguration;
import com.korevg.mv.webrtcsdk.useragent.WebRTCException;
import com.korevg.mv.webrtcsdk.webrtc.ACWebRTCManager;
import com.korevg.mv.webrtcsdk.webrtc.listeners.WebRTCCallEventListener;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import org.json.JSONException;
import org.json.JSONObject;
import org.webrtc.AudioSource;
import org.webrtc.AudioTrack;
import org.webrtc.Camera1Enumerator;
import org.webrtc.CameraVideoCapturer;
import org.webrtc.DataChannel;
import org.webrtc.DtmfSender;
import org.webrtc.EglBase;
import org.webrtc.IceCandidate;
import org.webrtc.MediaConstraints;
import org.webrtc.MediaStream;
import org.webrtc.MediaStreamTrack;
import org.webrtc.PeerConnection;
import org.webrtc.PeerConnectionFactory;
import org.webrtc.RTCStatsCollectorCallback;
import org.webrtc.RTCStatsReport;
import org.webrtc.RendererCommon;
import org.webrtc.RtpParameters;
import org.webrtc.RtpReceiver;
import org.webrtc.RtpSender;
import org.webrtc.RtpTransceiver;
import org.webrtc.SdpObserver;
import org.webrtc.SessionDescription;
import org.webrtc.SurfaceTextureHelper;
import org.webrtc.SurfaceViewRenderer;
import org.webrtc.VideoFrame;
import org.webrtc.VideoSink;
import org.webrtc.VideoSource;
import org.webrtc.VideoTrack;

public class ACWebRTCCall {
    private static final String TAG = "ACWebRTCCall";
    private static final String VIDEO_TRACK_ID = "1";
    private static final String AUDIO_TRACK_ID = "2";
    private static final String RTP_INBOUND_STATS = "RTCInboundRTPAudioStream";
    private static final String RTP_OUTBOUND_STATS = "RTCOutboundRTPAudioStream";
    private static final String AUDIO_ECHO_CANCELLATION_CONSTRAINT = "googEchoCancellation";
    private static final String AUDIO_AUTO_GAIN_CONTROL_CONSTRAINT = "googAutoGainControl";
    private static final String AUDIO_HIGH_PASS_FILTER_CONSTRAINT = "googHighpassFilter";
    private static final String AUDIO_NOISE_SUPPRESSION_CONSTRAINT = "googNoiseSuppression";
    private static boolean handleBrokenConnection;
    private static ScheduledExecutorService executor;
    private static PeerConnectionFactory factory;
    private static PeerConnection.RTCConfiguration rtcConfiguration;
    private static SurfaceViewRenderer localRender;
    private SurfaceViewRenderer remoteRender;
    private PercentFrameLayout localRenderLayout;
    private PercentFrameLayout remoteRenderLayout;
    private final ProxyVideoSink localSink = new ProxyVideoSink();
    private final ProxyVideoSink remoteSink = new ProxyVideoSink();
    private VideoTrack localVideoTrack = null;
    private VideoTrack remoteVideoTrack = null;
    private AudioTrack localAudioTrack = null;
    private MediaStream mediaStream;
    private PeerConnection peerConnection;
    private CameraVideoCapturer videoCapturer;
    private AudioSource audioSource;
    private VideoSource videoSource;
    private SurfaceTextureHelper surfaceTextureHelper;
    private boolean videoEnabled;
    private boolean videoMuted;
    private String localSDP = null;
    private ACCallStatistics stats = new ACCallStatistics();
    private String createdLocalSDP = null;
    private String remoteSDP;
    private static Handler uiHandler;
    private ACSDPObserver sdpObserver = new ACSDPObserver();
    private WebRTCCallEventListener webRTCCallEventListener;
    private static int numberOfCameras;
    private VideoConfiguration config = null;
    private boolean terminated = false;
    private int dialogId;
    private PeerConnection.IceConnectionState iceConnectionState;
    private static long captureStartTime;
    private static long captureEndTime;
    private static final long WAITING_TIME = 3000L;
    private static int webRTCCount;
    private boolean frontCamera = true;
    private CameraVideoCapturer.CameraSwitchHandler cameraSwitchHandler = new CameraVideoCapturer.CameraSwitchHandler(){

        public void onCameraSwitchDone(boolean b) {
            Log.d(ACWebRTCCall.TAG, "onCameraSwitchDone, frontCamera: " + b);
            ACWebRTCCall.this.frontCamera = b;
            if (ACWebRTCCall.this.webRTCCallEventListener != null) {
                ACWebRTCCall.this.webRTCCallEventListener.cameraSwitched(b);
            }
        }

        public void onCameraSwitchError(String s) {
            Log.d(ACWebRTCCall.TAG, "onCameraSwitchError: " + s);
        }
    };
    private EglBase rootEglBase;
    private Object syncAddTrack = new Object();
    private CameraVideoCapturer.CameraEventsHandler capturerCallback = new CameraVideoCapturer.CameraEventsHandler(){

        public void onCameraError(String s) {
            Log.d(ACWebRTCCall.TAG, "onCameraError");
        }

        public void onCameraDisconnected() {
            Log.d(ACWebRTCCall.TAG, "onCameraDisconnected");
        }

        public void onCameraFreezed(String s) {
            Log.d(ACWebRTCCall.TAG, "onCameraFreezed");
        }

        public void onCameraOpening(String s) {
            captureStartTime = System.currentTimeMillis();
            Log.d(ACWebRTCCall.TAG, "onCameraOpening");
        }

        public void onFirstFrameAvailable() {
            Log.d(ACWebRTCCall.TAG, "onFirstFrameAvailable");
        }

        public void onCameraClosed() {
            captureEndTime = System.currentTimeMillis();
            Log.d(ACWebRTCCall.TAG, "onCameraClosed");
        }
    };

    private ACWebRTCCall() {
        ++webRTCCount;
    }

    public ACWebRTCCall(WebRTCCallEventListener webRTCCallEventListener) {
        ++webRTCCount;
        this.config = ACConfiguration.getConfiguration().getVideoConfiguration();
        this.webRTCCallEventListener = webRTCCallEventListener;
    }

    public String getCreatedLocalSDP() {
        return this.createdLocalSDP;
    }

    public String getLocalSDP() {
        return this.localSDP;
    }

    public void setRemoteSDPOnAnswer(String remoteSDP) {
        Log.d(TAG, "begin of setRemoteSDPOnAnswer");
        this.remoteSDP = remoteSDP;
        Future<Void> result = executor.submit(new Callable<Void>(){

            @Override
            public Void call() throws Exception {
                ACWebRTCCall.this.setRemoteSDPOnAnswerInternal();
                Log.d(ACWebRTCCall.TAG, "end of setRemoteSDPOnAnswerInternal");
                return null;
            }
        });
        try {
            result.get();
        }
        catch (Exception exception) {
            // empty catch block
        }
        Log.d(TAG, "end of setRemoteSDPOnAnswer");
    }

    private void setRemoteSDPOnAnswerInternal() {
        SessionDescription sdpDesc = new SessionDescription(SessionDescription.Type.ANSWER, this.remoteSDP);
        Log.d(TAG, "setRemoteDescription for answered call");
        this.peerConnection.setRemoteDescription((SdpObserver)this.sdpObserver, sdpDesc);
    }

    public void createOffer(final boolean hold) {
        Log.d(TAG, "begin of createOffer");
        Future<Void> result = executor.submit(new Callable<Void>(){

            @Override
            public Void call() throws Exception {
                ACWebRTCCall.this.createOfferInternal(hold);
                Log.d(ACWebRTCCall.TAG, "end of createOfferInternal");
                return null;
            }
        });
        try {
            result.get();
        }
        catch (Exception exception) {
            // empty catch block
        }
        Log.d(TAG, "end of createOffer");
    }

    private void createOfferInternal(boolean hold) {
        Log.d(TAG, "Creating offer internal: hold =" + hold + " video= " + this.videoEnabled + " videomuted: " + this.videoMuted);
        this.localSDP = null;
        MediaConstraints sdpMediaConstraints = this.createMediaConstraits(hold);
        RtpTransceiver.RtpTransceiverInit rtpTransceiverInit = new RtpTransceiver.RtpTransceiverInit(RtpTransceiver.RtpTransceiverDirection.SEND_RECV);
        this.peerConnection.createOffer((SdpObserver)this.sdpObserver, sdpMediaConstraints);
    }

    public void createOfferForNetworkChange(final boolean hold) {
        Log.d(TAG, "begin of createOfferForNetworkChange");
        Future<Void> result = executor.submit(new Callable<Void>(){

            @Override
            public Void call() throws Exception {
                ACWebRTCCall.this.createOfferForNetworkChangeInternal(hold);
                Log.d(ACWebRTCCall.TAG, "end of createOfferForNetworkChangeInternal");
                return null;
            }
        });
        try {
            result.get();
        }
        catch (Exception exception) {
            // empty catch block
        }
        Log.d(TAG, "end of createOfferForNetworkChange");
    }

    private void createOfferForNetworkChangeInternal(boolean hold) {
        Log.d(TAG, "createOfferForNetworkChange2 offer internal: hold =" + hold + " videoEnabled= " + this.videoEnabled + "videoMuted: " + this.videoMuted);
        this.localSDP = null;
        MediaConstraints sdpMediaConstraints = this.createMediaConstraits(hold);
        sdpMediaConstraints.mandatory.add(new MediaConstraints.KeyValuePair("IceRestart", "true"));
        this.peerConnection.createOffer((SdpObserver)this.sdpObserver, sdpMediaConstraints);
    }

    private void addVideoTrack() {
        if (this.localVideoTrack != null) {
            Log.d(TAG, "local video track is not null");
            return;
        }
        List<String> mediaStreamLabels = Collections.singletonList("ACStream");
        this.createLocalVideoTrack();
        Log.d(TAG, "mediaStream.addTrack");
        try {
            this.peerConnection.addTrack((MediaStreamTrack)this.localVideoTrack, mediaStreamLabels);
        }
        catch (Throwable th) {
            Log.d(TAG, "mediaStream.addTrack error " + th.getMessage());
        }
    }

    public void createAnswer(boolean videoMuted, final String remoteSDP, final boolean hold) {
        Log.d(TAG, "begin of createAnswer: " + videoMuted);
        this.videoMuted = videoMuted;
        Future<Void> result = executor.submit(new Callable<Void>(){

            @Override
            public Void call() throws Exception {
                if (ACWebRTCCall.this.videoEnabled) {
                    ACWebRTCCall.this.addVideoTrack();
                }
                ACWebRTCCall.this.createAnswerInternal(remoteSDP, hold);
                Log.d(ACWebRTCCall.TAG, "end of createAnswerInternal");
                return null;
            }
        });
        try {
            result.get();
        }
        catch (Exception exception) {
            // empty catch block
        }
        Log.d(TAG, "end of createAnswer");
    }

    public void createAnswerInternal(String remoteSDP, boolean hold) {
        this.localSDP = null;
        MediaConstraints sdpMediaConstraints = this.createMediaConstraits(hold);
        SessionDescription sdpDesc = new SessionDescription(SessionDescription.Type.OFFER, remoteSDP);
        Log.d(TAG, "setRemoteDescription for answer");
        this.peerConnection.setRemoteDescription((SdpObserver)this.sdpObserver, sdpDesc);
        this.peerConnection.createAnswer((SdpObserver)this.sdpObserver, sdpMediaConstraints);
    }

    private MediaConstraints createMediaConstraits(boolean hold) {
        MediaConstraints sdpMediaConstraints = new MediaConstraints();
        sdpMediaConstraints.mandatory.add(new MediaConstraints.KeyValuePair("OfferToReceiveAudio", "" + !hold));
        sdpMediaConstraints.mandatory.add(new MediaConstraints.KeyValuePair("OfferToReceiveVideo", "" + (this.videoEnabled && !hold)));
        return sdpMediaConstraints;
    }

    public void setLocaLRenderPosition(int x, int y) {
        if (this.config != null) {
            this.config.LOCAL_X_CONNECTED = x;
            this.config.LOCAL_Y_CONNECTED = y;
        }
    }

    public void sendDTMF(final DTMF dtmf) {
        Log.d(TAG, "begin of sendDTMF");
        Future<Void> result = executor.submit(new Callable<Void>(){

            @Override
            public Void call() throws Exception {
                ACWebRTCCall.this.sendDTMFInternal(dtmf);
                Log.d(ACWebRTCCall.TAG, "end of sendDTMF");
                return null;
            }
        });
        try {
            result.get();
        }
        catch (Exception exception) {
            // empty catch block
        }
        Log.d(TAG, "end of sendDTMF");
    }

    public void sendDTMFInternal(DTMF dtmf) {
        List<RtpSender> senders = this.peerConnection.getSenders();
        for (RtpSender sender : senders) {
            DtmfSender dtmfSender = sender.dtmf();
            if (dtmfSender != null) {
                if (!dtmfSender.canInsertDtmf()) continue;
                Log.d(TAG, "Sending DTMF: " + dtmf.getDTMFString());
                dtmfSender.insertDtmf("" + dtmf.getDTMFString(), ACConfiguration.getConfiguration().getDtmfOptions().duration, ACConfiguration.getConfiguration().getDtmfOptions().intervalGap);
                return;
            }
            Log.d(TAG, "No dtmf sender");
        }
    }

    public void muteAudio(final boolean mute) {
        Log.d(TAG, "begin of muteAudio");
        Future<Void> result = executor.submit(new Callable<Void>(){

            @Override
            public Void call() throws Exception {
                ACWebRTCCall.this.muteAudioInternal(mute);
                Log.d(ACWebRTCCall.TAG, "end of muteAudioInternal");
                return null;
            }
        });
        try {
            result.get();
        }
        catch (Exception exception) {
            // empty catch block
        }
        Log.d(TAG, "end of muteAudio");
    }

    private void muteAudioInternal(boolean mute) {
        if (this.localAudioTrack != null) {
            this.localAudioTrack.setEnabled(!mute);
        }
    }

    public void muteVideo(final boolean mute) {
        Log.d(TAG, "begin of muteVideo");
        this.videoMuted = mute;
        Future<Void> result = executor.submit(new Callable<Void>(){

            @Override
            public Void call() throws Exception {
                ACWebRTCCall.this.muteVideoInternal(mute);
                Log.d(ACWebRTCCall.TAG, "end of muteVideoInternal");
                return null;
            }
        });
        try {
            result.get();
        }
        catch (Exception exception) {
            // empty catch block
        }
        Log.d(TAG, "end of muteVideo");
    }

    private void muteVideoInternal(boolean mute) {
        if (this.localVideoTrack != null) {
            Log.d(TAG, "Setting local video track to: " + mute);
            this.localVideoTrack.setEnabled(!mute);
            this.updateVideoView();
        }
    }

    public void stopVideo() {
        Log.d(TAG, "begin of stopVideo");
        Future<Void> result = executor.submit(new Callable<Void>(){

            @Override
            public Void call() throws Exception {
                ACWebRTCCall.this.stopVideoInternal();
                Log.d(ACWebRTCCall.TAG, "end of stopVideoInternal");
                return null;
            }
        });
        try {
            result.get();
        }
        catch (Exception exception) {
            // empty catch block
        }
        Log.d(TAG, "end of stopVideo");
    }

    private void stopVideoInternal() {
        Log.d(TAG, "stopVideoInternal");
        if (this.localVideoTrack != null) {
            this.localVideoTrack.setEnabled(false);
        }
        if (this.localSink != null) {
            if (this.localVideoTrack != null) {
                this.localVideoTrack.removeSink((VideoSink)this.localSink);
            }
            this.localSink.setTarget(null);
        }
        if (this.remoteSink != null) {
            if (this.remoteVideoTrack != null) {
                this.remoteVideoTrack.removeSink((VideoSink)this.remoteSink);
            }
            this.remoteSink.setTarget(null);
        }
    }

    public boolean isAudioEnabled() {
        if (this.localAudioTrack != null) {
            return this.localAudioTrack.enabled();
        }
        return false;
    }

    public boolean isVideoEnabled() {
        if (this.localVideoTrack != null) {
            return this.localVideoTrack.enabled();
        }
        return false;
    }

    public void removeVideoTrack() {
        this.setVideoEnabled(false);
        if (this.localVideoTrack == null) {
            Log.d(TAG, "local video track is null");
            return;
        }
        this.mediaStream.removeTrack(this.localVideoTrack);
    }

    private void addRemoteVideoTrack(final MediaStream mediaStream) {
        try {
            if (this.remoteVideoTrack != null && this.remoteVideoTrack.enabled()) {
                Log.d(TAG, "Video track is already added");
                return;
            }
        }
        catch (Throwable th) {
            Log.d("aaa", "ERROR:  " + th);
        }
        executor.execute(new Runnable(){

            @Override
            public void run() {
                if (mediaStream.videoTracks.size() == 1) {
                    Log.d(ACWebRTCCall.TAG, "adding remote renderer2");
                    Log.d(ACWebRTCCall.TAG, "videoEnabled: " + ACWebRTCCall.this.videoEnabled + ", videoMute: " + ACWebRTCCall.this.videoMuted);
                    ACWebRTCCall.this.remoteVideoTrack = (VideoTrack)mediaStream.videoTracks.get(0);
                    Log.d(ACWebRTCCall.TAG, "adding remote renderer3: " + ACWebRTCCall.this.remoteVideoTrack.id());
                    ACWebRTCCall.this.remoteVideoTrack.setEnabled(true);
                    ACWebRTCCall.this.remoteSink.setTarget((VideoSink)ACWebRTCCall.this.remoteRender);
                    ACWebRTCCall.this.remoteVideoTrack.addSink((VideoSink)ACWebRTCCall.this.remoteSink);
                    if (ACWebRTCCall.this.localVideoTrack != null) {
                        ACWebRTCCall.this.localVideoTrack.setEnabled(ACWebRTCCall.this.videoEnabled && !ACWebRTCCall.this.videoMuted);
                    }
                    ACWebRTCCall.this.updateVideoView();
                } else if (ACWebRTCCall.this.localVideoTrack != null) {
                    ACWebRTCCall.this.localVideoTrack.setEnabled(false);
                }
            }
        });
    }

    public void initCall(PeerConnectionFactory factory, EglBase rootBase, ScheduledExecutorService executor, PeerConnection.RTCConfiguration rtcConfiguration, boolean withVideo) {
        Log.d(TAG, "initCall");
        ACWebRTCCall.executor = executor;
        ACWebRTCCall.rtcConfiguration = rtcConfiguration;
        ACWebRTCCall.factory = factory;
        this.videoEnabled = withVideo;
        this.rootEglBase = rootBase;
        this.initCallInternal();
    }

    private void initCallInternal() {
        Log.d(TAG, "initCallInternal");
        MediaConstraints audioConstraints = new MediaConstraints();
        Log.d(TAG, "factory: " + factory);
        if (this.audioSource != null) {
            Log.d(TAG, "audioSource not null");
        }
        long waitTime = 3000L - (System.currentTimeMillis() - captureEndTime);
        Log.d(TAG, "createAudioSource waitTime: " + waitTime);
        if (waitTime > 0L) {
            try {
                Thread.sleep(waitTime);
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        this.audioSource = factory.createAudioSource(audioConstraints);
        Log.d(TAG, "Audio source created");
        this.localAudioTrack = factory.createAudioTrack(AUDIO_TRACK_ID, this.audioSource);
        Log.d(TAG, "Audio track created");
        this.localAudioTrack.setEnabled(false);
        MediaConstraints mediaConstraints = new MediaConstraints();
        this.peerConnection = factory.createPeerConnection(rtcConfiguration, mediaConstraints, (PeerConnection.Observer)new PCObserver());
        Log.d(TAG, "Peer connection created");
        this.retrieveStats();
        List<String> mediaStreamLabels = Collections.singletonList("ACStream");
        // WebRTC assigns Unified Plan m-line indexes in transceiver creation order.
        // Add audio first so the outgoing offer is ordered m=audio, then m=video.
        this.peerConnection.addTrack((MediaStreamTrack)this.localAudioTrack, mediaStreamLabels);
        if (this.videoEnabled) {
            this.createLocalVideoTrack();
            this.peerConnection.addTrack((MediaStreamTrack)this.localVideoTrack, mediaStreamLabels);
        }
        Log.d(TAG, "Peer stream created");
    }

    public ACCallStatistics getStats() {
        return this.stats;
    }

    private void retrieveStats() {
        this.stats = new ACCallStatistics();
        Thread t = new Thread(new Runnable(){

            @Override
            public void run() {
                while (ACWebRTCCall.this.peerConnection != null) {
                    executor.execute(new Runnable(){

                        @Override
                        public void run() {
                            if (ACWebRTCCall.this.peerConnection == null) {
                                return;
                            }
                            ACWebRTCCall.this.peerConnection.getStats(new RTCStatsCollectorCallback(){

                                public void onStatsDelivered(RTCStatsReport rtcStatsReport) {
                                    try {
                                        Map<String, org.webrtc.RTCStats> map = rtcStatsReport.getStatsMap();
                                        ((ACWebRTCCall)ACWebRTCCall.this).stats.fullStats.putAll(map);
                                        for (Map.Entry<String, org.webrtc.RTCStats> pair : map.entrySet()) {
                                            JSONObject jsonObject;
                                            String jsonStr;
                                            if (pair.getKey() != null && pair.getKey().toString().startsWith(ACWebRTCCall.RTP_INBOUND_STATS)) {
                                                jsonStr = pair.getValue().toString();
                                                try {
                                                    jsonObject = new JSONObject(jsonStr);
                                                    ((ACWebRTCCall)ACWebRTCCall.this).stats.rtpInboundStats.packetsReceived = jsonObject.getInt("packetsReceived");
                                                    ((ACWebRTCCall)ACWebRTCCall.this).stats.rtpInboundStats.bytesReceived = jsonObject.getInt("bytesReceived");
                                                    ((ACWebRTCCall)ACWebRTCCall.this).stats.rtpInboundStats.packetsLost = jsonObject.getInt("packetsLost");
                                                    ((ACWebRTCCall)ACWebRTCCall.this).stats.rtpInboundStats.jitter = BigDecimal.valueOf(jsonObject.getDouble("jitter")).floatValue();
                                                    ((ACWebRTCCall)ACWebRTCCall.this).stats.rtpInboundStats.fractionLost = BigDecimal.valueOf(jsonObject.getDouble("fractionLost")).floatValue();
                                                }
                                                catch (JSONException e) {
                                                    Log.d(ACWebRTCCall.TAG, "oops1: " + (Object)((Object)e));
                                                }
                                            }
                                            if (pair.getKey() == null || !pair.getKey().toString().startsWith(ACWebRTCCall.RTP_OUTBOUND_STATS)) continue;
                                            jsonStr = pair.getValue().toString();
                                            try {
                                                jsonObject = new JSONObject(jsonStr);
                                                ((ACWebRTCCall)ACWebRTCCall.this).stats.rtpOutboundStats.packetsSent = jsonObject.getInt("packetsSent");
                                                ((ACWebRTCCall)ACWebRTCCall.this).stats.rtpOutboundStats.bytesSent = jsonObject.getInt("bytesSent");
                                            }
                                            catch (JSONException e) {
                                                Log.d(ACWebRTCCall.TAG, "oops2: " + (Object)((Object)e));
                                            }
                                        }
                                    }
                                    catch (Throwable t) {
                                        Log.d(ACWebRTCCall.TAG, "oops: " + t.getMessage());
                                    }
                                }
                            });
                        }
                    });
                    try {
                        Thread.sleep(1000L);
                    }
                    catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        t.start();
    }

    public void showVideo(SurfaceViewRenderer localView, SurfaceViewRenderer remoteView, Handler handler) throws WebRTCException {
        if (!this.videoEnabled) {
            throw new WebRTCException(WebRTCException.WebRTCExceptionCode.VIDEO_NOT_ENABLED);
        }
        uiHandler = handler;
        this.remoteRender = remoteView;
        localRender = localView;
        this.updateRenderers();
        this.startCapture();
    }

    public void showVideo(Activity activity, Handler handler) throws WebRTCException {
        Log.d(TAG, "Show video: " + this.videoEnabled);
        uiHandler = handler;
        this.remoteRenderLayout = (PercentFrameLayout)activity.findViewById(R.id.remote_video_layout);
        this.remoteRender = (SurfaceViewRenderer)activity.findViewById(R.id.remote_video_view);
        this.localRenderLayout = (PercentFrameLayout)activity.findViewById(R.id.local_video_layout);
        localRender = (SurfaceViewRenderer)activity.findViewById(R.id.local_video_view);
        this.updateRenderers();
        this.startCapture();
    }

    private void updateRenderers() {
        if (this.localSink != null && this.localVideoTrack != null) {
            this.localVideoTrack.removeSink((VideoSink)this.localSink);
        }
        boolean remoteVideoTrackEnabled = false;
        try {
            remoteVideoTrackEnabled = this.remoteVideoTrack.enabled();
        }
        catch (Throwable th) {
            Log.d(TAG, "remoteVideoTrackEnabled error: " + th.getMessage());
        }
        if (this.remoteSink != null && this.remoteVideoTrack != null && remoteVideoTrackEnabled) {
            this.remoteSink.setTarget((VideoSink)this.remoteRender);
            this.remoteVideoTrack.addSink((VideoSink)this.remoteSink);
        }
    }

    private void startCapture() {
        Log.d(TAG, "startCapture");
        this.localSink.setTarget((VideoSink)localRender);
        while (this.localVideoTrack == null) {
            try {
                Thread.sleep(50L);
            }
            catch (InterruptedException interruptedException) {}
        }
        this.localVideoTrack.addSink((VideoSink)this.localSink);
        Log.d(TAG, "Renderer added");
        try {
            localRender.init(this.rootEglBase.getEglBaseContext(), null);
        }
        catch (Exception e) {
            Log.d(TAG, "oops: " + e.getMessage());
        }
        try {
            this.remoteRender.init(this.rootEglBase.getEglBaseContext(), null);
        }
        catch (Exception e) {
            Log.d(TAG, "oops: " + e.getMessage());
        }
        localRender.setZOrderMediaOverlay(true);
        this.localVideoTrack.setEnabled(this.videoEnabled && !this.videoMuted);
        this.videoCapturer.startCapture(this.config.cameraWidth, this.config.cameraHeight, this.config.cameraFrameRate);
        this.updateVideoView();
    }

    public void terminateCall() {
        this.terminateCall(CallTermination.TERMINATED_UNKNOWN);
    }

    public void terminateCall(final CallTermination termination) {
        if (this.iceConnectionState != null && (this.iceConnectionState == PeerConnection.IceConnectionState.DISCONNECTED || this.iceConnectionState == PeerConnection.IceConnectionState.FAILED)) {
            Log.d(TAG, "terminate webrtc not needed");
            return;
        }
        if (this.terminated) {
            Log.d(TAG, "already terminating");
            return;
        }
        Log.d(TAG, "begin of terminateCall");
        this.terminated = true;
        Log.d(TAG, "webRTCCount " + --webRTCCount);
        Future<Void> result = executor.submit(new Callable<Void>(){

            @Override
            public Void call() throws Exception {
                ACWebRTCCall.this.terminateCallInternal(termination);
                Log.d(ACWebRTCCall.TAG, "end of terminateCallInternal");
                return null;
            }
        });
        try {
            result.get();
        }
        catch (Exception e) {
            Log.d(TAG, "oops: " + e.getMessage());
        }
        Log.d(TAG, "end of terminateCall");
    }

    public void terminateCallInternal(CallTermination termination) {
        Log.d(TAG, "Begin terminateCallInternal " + this.dialogId);
        this.stopVideoInternal();
        Log.d(TAG, "Begin terminateCallInternal start" + this.dialogId);
        if (this.peerConnection == null) {
            Log.d(TAG, "Call already closed");
            return;
        }
        if (termination != CallTermination.TERMINATED_MEDIA_FAILED) {
            Log.d(TAG, "Closing peerConnection: ");
            try {
                this.peerConnection.dispose();
            }
            catch (Exception e) {
                Log.d(TAG, "oops: " + e.getMessage());
            }
        }
        this.peerConnection = null;
        if (this.audioSource != null) {
            try {
                Log.d(TAG, "Closing audio source.");
                this.audioSource.dispose();
                this.audioSource = null;
            }
            catch (Throwable th) {
                Log.d(TAG, "Closing audio source error. " + th);
            }
        }
        this.stopVideoCapturer();
        if (webRTCCount == 0) {
            uiHandler = null;
            if (localRender != null) {
                Log.d(TAG, "release localRender");
                localRender.release();
            }
            localRender = null;
            if (this.remoteRender != null) {
                Log.d(TAG, "release remoteRender");
                this.remoteRender.release();
            }
            this.remoteRender = null;
        }
        if (this.videoSource != null) {
            try {
                Log.d(TAG, "Closing video source.");
                this.videoSource.dispose();
                this.videoSource = null;
            }
            catch (Throwable th) {
                Log.d(TAG, "Closing video source error. " + th);
            }
        }
        this.remoteRenderLayout = null;
        this.localRenderLayout = null;
        if (this.localAudioTrack != null) {
            try {
                Log.d(TAG, "dispose localAudioTrack");
                this.localAudioTrack.dispose();
            }
            catch (Throwable th) {
                Log.d(TAG, "dispose localAudioTrack error. " + th);
            }
        }
        this.localAudioTrack = null;
        if (this.remoteVideoTrack != null) {
            try {
                Log.d(TAG, "dispose remoteVideoTrack");
                this.remoteVideoTrack.dispose();
            }
            catch (Throwable th) {
                Log.d(TAG, "dispose remoteVideoTrack error. " + th);
            }
        }
        this.remoteVideoTrack = null;
        if (this.localVideoTrack != null) {
            try {
                Log.d(TAG, "dispose localVideoTrack");
                this.localVideoTrack.dispose();
            }
            catch (Throwable th) {
                Log.d(TAG, "dispose localVideoTrack error. " + th);
            }
        }
        if (this.mediaStream != null) {
            try {
                Log.d(TAG, "dispose mediaStream");
                this.mediaStream.dispose();
            }
            catch (Throwable th) {
                Log.d(TAG, "dispose mediaStream error. " + th);
            }
        }
        this.mediaStream = null;
        this.localVideoTrack = null;
        this.localSDP = null;
        this.remoteSDP = null;
        Log.d(TAG, "End terminateCallInternal");
    }

    private void createLocalVideoTrack() {
        this.createCapturer();
        Log.d(TAG, "Capturer created.");
        this.videoSource = factory.createVideoSource(this.videoCapturer.isScreencast());
        this.surfaceTextureHelper = SurfaceTextureHelper.create((String)"CaptureThread", (EglBase.Context)this.rootEglBase.getEglBaseContext());
        this.videoCapturer.initialize(this.surfaceTextureHelper, ACWebRTCManager.context, this.videoSource.getCapturerObserver());
        this.localVideoTrack = factory.createVideoTrack(VIDEO_TRACK_ID, this.videoSource);
        Log.d(TAG, "Video track created");
        this.localVideoTrack.setEnabled(false);
    }

    private void updateVideoView() {
        if (this.localRenderLayout == null && this.remoteRenderLayout == null) {
            Log.d(TAG, "ac_webrtc_video UI element is not used, no need to resize the screens");
            return;
        }
        if (uiHandler == null) {
            return;
        }
        uiHandler.post(new Runnable(){

            @Override
            public void run() {
                if (ACWebRTCCall.this.localRenderLayout == null) {
                    Log.d(ACWebRTCCall.TAG, "localRenderLayout is gone");
                    return;
                }
                if (ACWebRTCCall.this.remoteVideoTrack != null) {
                    Log.d(ACWebRTCCall.TAG, "Half screen");
                    ACWebRTCCall.this.remoteRenderLayout.setPosition(((ACWebRTCCall)ACWebRTCCall.this).config.REMOTE_X, ((ACWebRTCCall)ACWebRTCCall.this).config.REMOTE_Y, ((ACWebRTCCall)ACWebRTCCall.this).config.REMOTE_WIDTH, ((ACWebRTCCall)ACWebRTCCall.this).config.REMOTE_HEIGHT);
                    ACWebRTCCall.this.remoteRender.setScalingType(RendererCommon.ScalingType.SCALE_ASPECT_FILL);
                    ACWebRTCCall.this.remoteRender.setMirror(false);
                    ACWebRTCCall.this.localRenderLayout.setPosition(((ACWebRTCCall)ACWebRTCCall.this).config.LOCAL_X_CONNECTED, ((ACWebRTCCall)ACWebRTCCall.this).config.LOCAL_Y_CONNECTED, ((ACWebRTCCall)ACWebRTCCall.this).config.LOCAL_WIDTH_CONNECTED, ((ACWebRTCCall)ACWebRTCCall.this).config.LOCAL_HEIGHT_CONNECTED);
                    localRender.setScalingType(RendererCommon.ScalingType.SCALE_ASPECT_FIT);
                } else {
                    Log.d(ACWebRTCCall.TAG, "Full screen");
                    ACWebRTCCall.this.localRenderLayout.setPosition(((ACWebRTCCall)ACWebRTCCall.this).config.LOCAL_X_CONNECTING, ((ACWebRTCCall)ACWebRTCCall.this).config.LOCAL_Y_CONNECTING, ((ACWebRTCCall)ACWebRTCCall.this).config.LOCAL_WIDTH_CONNECTING, ((ACWebRTCCall)ACWebRTCCall.this).config.LOCAL_HEIGHT_CONNECTING);
                    localRender.setScalingType(RendererCommon.ScalingType.SCALE_ASPECT_FILL);
                }
                localRender.setMirror(ACWebRTCCall.this.frontCamera);
                ACWebRTCCall.this.rearrangeVideoOrderPatch();
                localRender.requestLayout();
                ACWebRTCCall.this.remoteRender.requestLayout();
            }
        });
    }

    private void rearrangeVideoOrderPatch() {
        localRender.setVisibility(8);
        this.remoteRender.setVisibility(8);
        this.localRenderLayout.removeView((View)localRender);
        this.remoteRenderLayout.removeView((View)this.remoteRender);
        boolean localPreview = true;
        localRender.setZOrderMediaOverlay(localPreview);
        this.remoteRender.setZOrderMediaOverlay(!localPreview);
        this.localRenderLayout.addView((View)localRender, new ViewGroup.LayoutParams(-2, -2));
        this.remoteRenderLayout.addView((View)this.remoteRender, new ViewGroup.LayoutParams(-2, -2));
        localRender.setVisibility(0);
        this.remoteRender.setVisibility(0);
    }

    private void printSenders() {
        List<RtpSender> senders = this.peerConnection.getSenders();
        for (RtpSender sender : senders) {
            Log.d(TAG, "sender");
            Log.d(TAG, "sender id: " + sender.id());
            RtpParameters params = sender.getParameters();
            for (RtpParameters.Codec codec : params.codecs) {
                Log.d(TAG, "codec name: " + codec.name);
            }
            for (RtpParameters.Encoding enc : params.encodings) {
                Log.d(TAG, "enc active " + enc.active);
                Log.d(TAG, "enc ssrc " + enc.ssrc);
                Log.d(TAG, "enc maxBitrateBps " + enc.maxBitrateBps);
                enc.maxBitrateBps = 160000;
            }
        }
    }

    public void setVideoEnabled(boolean videoEnabled) {
        Log.d(TAG, "setVideoEnabled: " + videoEnabled);
        this.videoEnabled = videoEnabled;
        this.addVideoTrack();
    }

    public void setVideoMuted(boolean videoMuted) {
        this.videoMuted = videoMuted;
    }

    public void setDialogId(int dialogId) {
        this.dialogId = dialogId;
    }

    public int getDialogID() {
        return this.dialogId;
    }

    public void switchCamera() {
        Log.d(TAG, "begin of switchCamera");
        this.remoteSDP = this.remoteSDP;
        Future<Void> result = executor.submit(new Callable<Void>(){

            @Override
            public Void call() throws Exception {
                ACWebRTCCall.this.switchCameraInternal();
                Log.d(ACWebRTCCall.TAG, "end of switchCameraInternal");
                return null;
            }
        });
        try {
            result.get();
        }
        catch (Exception exception) {
            // empty catch block
        }
        Log.d(TAG, "end of switchCamera");
    }

    private void switchCameraInternal() {
        if (numberOfCameras < 2 || this.videoCapturer == null) {
            Log.d(TAG, "Failed to switch camera. Video. Number of cameras: " + numberOfCameras);
            return;
        }
        Log.d(TAG, "Switch camera");
        this.frontCamera = !this.frontCamera;
        localRender.setMirror(this.frontCamera);
        this.videoCapturer.switchCamera(this.cameraSwitchHandler);
    }

    public void stopVideoCapturer() {
        long waitTime = 3000L - (System.currentTimeMillis() - captureStartTime);
        Log.d(TAG, "stop waitTime: " + waitTime);
        if (waitTime > 0L) {
            try {
                Thread.sleep(waitTime);
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        if (this.videoCapturer != null) {
            Log.d(TAG, "Stopping capture.");
            try {
                this.videoCapturer.stopCapture();
            }
            catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            this.videoCapturer.dispose();
            if (this.surfaceTextureHelper != null) {
                Log.d("Camera1Session", "dispose surfaceTextureHelper");
                this.surfaceTextureHelper.dispose();
                this.surfaceTextureHelper = null;
            }
            this.videoCapturer = null;
        } else {
            Log.d(TAG, "videoCapturer already null");
        }
        Log.d(TAG, "end stopVideoCapturer");
    }

    private void createCapturer() {
        Camera1Enumerator enumerator = new Camera1Enumerator();
        long waitTime = 3000L - (System.currentTimeMillis() - captureEndTime);
        Log.d(TAG, "start waitTime: " + waitTime);
        if (waitTime > 0L) {
            try {
                Thread.sleep(waitTime);
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        Log.d(TAG, "createCapturer");
        numberOfCameras = enumerator.getDeviceNames().length;
        String[] deviceNames = enumerator.getDeviceNames();
        Log.d(TAG, "Looking for front facing cameras.");
        for (String deviceName : deviceNames) {
            if (!enumerator.isFrontFacing(deviceName)) continue;
            Log.d(TAG, "Creating front facing camera capturer.");
            this.videoCapturer = enumerator.createCapturer(deviceName, this.capturerCallback);
            if (this.videoCapturer == null) continue;
            Log.d(TAG, "end createCapturer");
            this.frontCamera = true;
            return;
        }
        Log.d(TAG, "Looking for other cameras.");
        for (String deviceName : deviceNames) {
            if (enumerator.isFrontFacing(deviceName)) continue;
            Log.d(TAG, "Creating other camera capturer.");
            this.videoCapturer = enumerator.createCapturer(deviceName, this.capturerCallback);
            this.frontCamera = false;
        }
    }

    public static void setHandleBrokenConnection(boolean handleBrokenConnection) {
        ACWebRTCCall.handleBrokenConnection = handleBrokenConnection;
    }

    class PCObserver
    implements PeerConnection.Observer {
        PCObserver() {
        }

        public void onSignalingChange(PeerConnection.SignalingState signalingState) {
            Log.d(ACWebRTCCall.TAG, "onSignalingChange: " + signalingState);
            if (signalingState == PeerConnection.SignalingState.STABLE) {
                // empty if block
            }
        }

        public void onIceConnectionChange(PeerConnection.IceConnectionState iceConnectionState) {
            ACWebRTCCall.this.iceConnectionState = iceConnectionState;
            Log.d(ACWebRTCCall.TAG, "onIceConnectionChange: " + iceConnectionState);
            Log.d(ACWebRTCCall.TAG, "ID: " + ACWebRTCCall.this.dialogId);
            if (iceConnectionState == PeerConnection.IceConnectionState.CONNECTED) {
                executor.execute(new Runnable(){

                    @Override
                    public void run() {
                        Log.d(ACWebRTCCall.TAG, "local sdp: " + ((ACWebRTCCall)ACWebRTCCall.this).peerConnection.getLocalDescription().description);
                    }
                });
                if (ACWebRTCCall.this.videoEnabled) {
                    if (uiHandler != null) {
                        Log.d(ACWebRTCCall.TAG, "updating video view ");
                        ACWebRTCCall.this.updateVideoView();
                    } else {
                        Log.d(ACWebRTCCall.TAG, "uiHandler null!!!");
                    }
                }
            } else if (iceConnectionState == PeerConnection.IceConnectionState.FAILED && ACWebRTCCall.this.webRTCCallEventListener != null) {
                if (!handleBrokenConnection) {
                    ACWebRTCCall.this.webRTCCallEventListener.callFailed(ACWebRTCCall.this.dialogId);
                } else {
                    ACWebRTCCall.this.webRTCCallEventListener.mediaFailed();
                }
            }
        }

        public void onIceConnectionReceivingChange(boolean b) {
            Log.d(ACWebRTCCall.TAG, "onIceConnectionReceivingChange");
        }

        public void onIceGatheringChange(PeerConnection.IceGatheringState iceGatheringState) {
            Log.d(ACWebRTCCall.TAG, "onIceGatheringChange:" + iceGatheringState);
            if (iceGatheringState == PeerConnection.IceGatheringState.COMPLETE) {
                if (ACWebRTCCall.this.terminated) {
                    Log.d(ACWebRTCCall.TAG, "connection was closed already");
                    return;
                }
                if (ACWebRTCCall.this.webRTCCallEventListener != null) {
                    ACWebRTCCall.this.webRTCCallEventListener.onIceGatheringComlete();
                }
            }
        }

        public void onIceCandidate(IceCandidate iceCandidate) {
            Log.d(ACWebRTCCall.TAG, "onIceCandidate");
            Log.d(ACWebRTCCall.TAG, "onIceCandidate sdp:" + iceCandidate.sdp);
            Log.d(ACWebRTCCall.TAG, "onIceCandidate serverUrl:" + iceCandidate.serverUrl);
            if (ACWebRTCCall.this.terminated) {
                Log.d(ACWebRTCCall.TAG, "connection was closed already");
                return;
            }
            executor.execute(new Runnable(){

                @Override
                public void run() {
                    ACWebRTCCall.this.localSDP = ((ACWebRTCCall)ACWebRTCCall.this).peerConnection.getLocalDescription().description;
                }
            });
        }

        public void onIceCandidatesRemoved(IceCandidate[] iceCandidates) {
            Log.d(ACWebRTCCall.TAG, "onIceCandidatesRemoved");
        }

        public void onAddStream(MediaStream mediaStream) {
            Log.d(ACWebRTCCall.TAG, "onAddStream");
        }

        public void onRemoveStream(MediaStream mediaStream) {
            Log.d(ACWebRTCCall.TAG, "onRemoveStream");
        }

        public void onDataChannel(DataChannel dataChannel) {
            Log.d(ACWebRTCCall.TAG, "onDataChannel");
        }

        public void onRenegotiationNeeded() {
            Log.d(ACWebRTCCall.TAG, "onRenegotiationNeeded");
        }

        public void onAddTrack(RtpReceiver rtpReceiver, MediaStream[] mediaStreams) {
            Log.d(ACWebRTCCall.TAG, "onAddTrack id: " + rtpReceiver.track().id());
            Log.d(ACWebRTCCall.TAG, "onAddTrack kind: " + rtpReceiver.track().kind());
            Log.d(ACWebRTCCall.TAG, "onAddTrack state: " + rtpReceiver.track().state());
            if (mediaStreams != null && rtpReceiver.track().kind().toLowerCase().equals("video")) {
                Log.d(ACWebRTCCall.TAG, "mediaStreams.length: " + mediaStreams.length);
                try {
                    ACWebRTCCall.this.addRemoteVideoTrack(mediaStreams[0]);
                }
                catch (Exception e) {
                    Log.e(ACWebRTCCall.TAG, "Exception: " + e);
                }
            }
            ACWebRTCCall.this.localAudioTrack.setEnabled(true);
        }
    }

    class ACSDPObserver
    implements SdpObserver {
        ACSDPObserver() {
        }

        public void onCreateSuccess(SessionDescription sessionDescription) {
            Log.d(ACWebRTCCall.TAG, "onCreateSuccess");
            Log.d(ACWebRTCCall.TAG, "sessionDescription: " + sessionDescription.description);
            Log.d(ACWebRTCCall.TAG, "setLocalDescription");
            if (ACWebRTCCall.this.peerConnection == null) {
                Log.d(ACWebRTCCall.TAG, "Webrtc call is closed already");
                return;
            }
            ACWebRTCCall.this.peerConnection.setLocalDescription((SdpObserver)this, sessionDescription);
            ACWebRTCCall.this.createdLocalSDP = sessionDescription.description;
        }

        public void onSetSuccess() {
            Log.d(ACWebRTCCall.TAG, "onSetSuccess");
        }

        public void onCreateFailure(String s) {
            Log.d(ACWebRTCCall.TAG, "onCreateFailure: " + s);
        }

        public void onSetFailure(String s) {
            Log.d(ACWebRTCCall.TAG, "onSetFailure: " + s);
        }
    }

    private static class ProxyVideoSink
    implements VideoSink {
        private VideoSink target;

        private ProxyVideoSink() {
        }

        public synchronized void onFrame(VideoFrame frame) {
            if (this.target == null) {
                Log.d(ACWebRTCCall.TAG, "Dropping frame in proxy because target is null.");
                return;
            }
            this.target.onFrame(frame);
        }

        public synchronized void setTarget(VideoSink target) {
            this.target = target;
        }
    }
}
