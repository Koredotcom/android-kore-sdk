/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  androidx.annotation.Nullable
 */
package org.webrtc;

import androidx.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.webrtc.AddIceObserver;
import org.webrtc.CalledByNative;
import org.webrtc.CandidatePairChangeEvent;
import org.webrtc.CryptoOptions;
import org.webrtc.DataChannel;
import org.webrtc.IceCandidate;
import org.webrtc.MediaConstraints;
import org.webrtc.MediaStream;
import org.webrtc.MediaStreamTrack;
import org.webrtc.NativePeerConnectionFactory;
import org.webrtc.RTCStatsCollectorCallback;
import org.webrtc.RtcCertificatePem;
import org.webrtc.RtpReceiver;
import org.webrtc.RtpSender;
import org.webrtc.RtpTransceiver;
import org.webrtc.SdpObserver;
import org.webrtc.SessionDescription;
import org.webrtc.StatsObserver;
import org.webrtc.TurnCustomizer;

public class PeerConnection {
    private final List<MediaStream> localStreams = new ArrayList<MediaStream>();
    private final long nativePeerConnection;
    private List<RtpSender> senders = new ArrayList<RtpSender>();
    private List<RtpReceiver> receivers = new ArrayList<RtpReceiver>();
    private List<RtpTransceiver> transceivers = new ArrayList<RtpTransceiver>();

    public PeerConnection(NativePeerConnectionFactory factory) {
        this(factory.createNativePeerConnection());
    }

    PeerConnection(long nativePeerConnection) {
        this.nativePeerConnection = nativePeerConnection;
    }

    public SessionDescription getLocalDescription() {
        return this.nativeGetLocalDescription();
    }

    public SessionDescription getRemoteDescription() {
        return this.nativeGetRemoteDescription();
    }

    public RtcCertificatePem getCertificate() {
        return this.nativeGetCertificate();
    }

    public DataChannel createDataChannel(String label, DataChannel.Init init) {
        return this.nativeCreateDataChannel(label, init);
    }

    public void createOffer(SdpObserver observer, MediaConstraints constraints) {
        this.nativeCreateOffer(observer, constraints);
    }

    public void createAnswer(SdpObserver observer, MediaConstraints constraints) {
        this.nativeCreateAnswer(observer, constraints);
    }

    public void setLocalDescription(SdpObserver observer) {
        this.nativeSetLocalDescriptionAutomatically(observer);
    }

    public void setLocalDescription(SdpObserver observer, SessionDescription sdp) {
        this.nativeSetLocalDescription(observer, sdp);
    }

    public void setRemoteDescription(SdpObserver observer, SessionDescription sdp) {
        this.nativeSetRemoteDescription(observer, sdp);
    }

    public void restartIce() {
        this.nativeRestartIce();
    }

    public void setAudioPlayout(boolean playout) {
        this.nativeSetAudioPlayout(playout);
    }

    public void setAudioRecording(boolean recording) {
        this.nativeSetAudioRecording(recording);
    }

    public boolean setConfiguration(RTCConfiguration config) {
        return this.nativeSetConfiguration(config);
    }

    public boolean addIceCandidate(IceCandidate candidate) {
        return this.nativeAddIceCandidate(candidate.sdpMid, candidate.sdpMLineIndex, candidate.sdp);
    }

    public void addIceCandidate(IceCandidate candidate, AddIceObserver observer) {
        this.nativeAddIceCandidateWithObserver(candidate.sdpMid, candidate.sdpMLineIndex, candidate.sdp, observer);
    }

    public boolean removeIceCandidates(IceCandidate[] candidates) {
        return this.nativeRemoveIceCandidates(candidates);
    }

    public boolean addStream(MediaStream stream) {
        boolean ret = this.nativeAddLocalStream(stream.getNativeMediaStream());
        if (!ret) {
            return false;
        }
        this.localStreams.add(stream);
        return true;
    }

    public void removeStream(MediaStream stream) {
        this.nativeRemoveLocalStream(stream.getNativeMediaStream());
        this.localStreams.remove(stream);
    }

    public RtpSender createSender(String kind, String stream_id) {
        RtpSender newSender = this.nativeCreateSender(kind, stream_id);
        if (newSender != null) {
            this.senders.add(newSender);
        }
        return newSender;
    }

    public List<RtpSender> getSenders() {
        for (RtpSender sender : this.senders) {
            sender.dispose();
        }
        this.senders = this.nativeGetSenders();
        return Collections.unmodifiableList(this.senders);
    }

    public List<RtpReceiver> getReceivers() {
        for (RtpReceiver receiver : this.receivers) {
            receiver.dispose();
        }
        this.receivers = this.nativeGetReceivers();
        return Collections.unmodifiableList(this.receivers);
    }

    public List<RtpTransceiver> getTransceivers() {
        for (RtpTransceiver transceiver : this.transceivers) {
            transceiver.dispose();
        }
        this.transceivers = this.nativeGetTransceivers();
        return Collections.unmodifiableList(this.transceivers);
    }

    public RtpSender addTrack(MediaStreamTrack track) {
        return this.addTrack(track, Collections.emptyList());
    }

    public RtpSender addTrack(MediaStreamTrack track, List<String> streamIds) {
        if (track == null || streamIds == null) {
            throw new NullPointerException("No MediaStreamTrack specified in addTrack.");
        }
        RtpSender newSender = this.nativeAddTrack(track.getNativeMediaStreamTrack(), streamIds);
        if (newSender == null) {
            throw new IllegalStateException("C++ addTrack failed.");
        }
        this.senders.add(newSender);
        return newSender;
    }

    public boolean removeTrack(RtpSender sender) {
        if (sender == null) {
            throw new NullPointerException("No RtpSender specified for removeTrack.");
        }
        return this.nativeRemoveTrack(sender.getNativeRtpSender());
    }

    public RtpTransceiver addTransceiver(MediaStreamTrack track) {
        return this.addTransceiver(track, new RtpTransceiver.RtpTransceiverInit());
    }

    public RtpTransceiver addTransceiver(MediaStreamTrack track, @Nullable RtpTransceiver.RtpTransceiverInit init) {
        RtpTransceiver newTransceiver;
        if (track == null) {
            throw new NullPointerException("No MediaStreamTrack specified for addTransceiver.");
        }
        if (init == null) {
            init = new RtpTransceiver.RtpTransceiverInit();
        }
        if ((newTransceiver = this.nativeAddTransceiverWithTrack(track.getNativeMediaStreamTrack(), init)) == null) {
            throw new IllegalStateException("C++ addTransceiver failed.");
        }
        this.transceivers.add(newTransceiver);
        return newTransceiver;
    }

    public RtpTransceiver addTransceiver(MediaStreamTrack.MediaType mediaType) {
        return this.addTransceiver(mediaType, new RtpTransceiver.RtpTransceiverInit());
    }

    public RtpTransceiver addTransceiver(MediaStreamTrack.MediaType mediaType, @Nullable RtpTransceiver.RtpTransceiverInit init) {
        RtpTransceiver newTransceiver;
        if (mediaType == null) {
            throw new NullPointerException("No MediaType specified for addTransceiver.");
        }
        if (init == null) {
            init = new RtpTransceiver.RtpTransceiverInit();
        }
        if ((newTransceiver = this.nativeAddTransceiverOfType(mediaType, init)) == null) {
            throw new IllegalStateException("C++ addTransceiver failed.");
        }
        this.transceivers.add(newTransceiver);
        return newTransceiver;
    }

    @Deprecated
    public boolean getStats(StatsObserver observer, @Nullable MediaStreamTrack track) {
        return this.nativeOldGetStats(observer, track == null ? 0L : track.getNativeMediaStreamTrack());
    }

    public void getStats(RTCStatsCollectorCallback callback) {
        this.nativeNewGetStats(callback);
    }

    public boolean setBitrate(Integer min, Integer current, Integer max) {
        return this.nativeSetBitrate(min, current, max);
    }

    public boolean startRtcEventLog(int file_descriptor, int max_size_bytes) {
        return this.nativeStartRtcEventLog(file_descriptor, max_size_bytes);
    }

    public void stopRtcEventLog() {
        this.nativeStopRtcEventLog();
    }

    public SignalingState signalingState() {
        return this.nativeSignalingState();
    }

    public IceConnectionState iceConnectionState() {
        return this.nativeIceConnectionState();
    }

    public PeerConnectionState connectionState() {
        return this.nativeConnectionState();
    }

    public IceGatheringState iceGatheringState() {
        return this.nativeIceGatheringState();
    }

    public void close() {
        this.nativeClose();
    }

    public void dispose() {
        this.close();
        for (MediaStream stream : this.localStreams) {
            this.nativeRemoveLocalStream(stream.getNativeMediaStream());
            stream.dispose();
        }
        this.localStreams.clear();
        for (RtpSender sender : this.senders) {
            sender.dispose();
        }
        this.senders.clear();
        for (RtpReceiver receiver : this.receivers) {
            receiver.dispose();
        }
        for (RtpTransceiver transceiver : this.transceivers) {
            transceiver.dispose();
        }
        this.transceivers.clear();
        this.receivers.clear();
        PeerConnection.nativeFreeOwnedPeerConnection(this.nativePeerConnection);
    }

    public long getNativePeerConnection() {
        return this.nativeGetNativePeerConnection();
    }

    @CalledByNative
    long getNativeOwnedPeerConnection() {
        return this.nativePeerConnection;
    }

    public static long createNativePeerConnectionObserver(Observer observer) {
        return PeerConnection.nativeCreatePeerConnectionObserver(observer);
    }

    private native long nativeGetNativePeerConnection();

    private native SessionDescription nativeGetLocalDescription();

    private native SessionDescription nativeGetRemoteDescription();

    private native RtcCertificatePem nativeGetCertificate();

    private native DataChannel nativeCreateDataChannel(String var1, DataChannel.Init var2);

    private native void nativeCreateOffer(SdpObserver var1, MediaConstraints var2);

    private native void nativeCreateAnswer(SdpObserver var1, MediaConstraints var2);

    private native void nativeSetLocalDescriptionAutomatically(SdpObserver var1);

    private native void nativeSetLocalDescription(SdpObserver var1, SessionDescription var2);

    private native void nativeSetRemoteDescription(SdpObserver var1, SessionDescription var2);

    private native void nativeRestartIce();

    private native void nativeSetAudioPlayout(boolean var1);

    private native void nativeSetAudioRecording(boolean var1);

    private native boolean nativeSetBitrate(Integer var1, Integer var2, Integer var3);

    private native SignalingState nativeSignalingState();

    private native IceConnectionState nativeIceConnectionState();

    private native PeerConnectionState nativeConnectionState();

    private native IceGatheringState nativeIceGatheringState();

    private native void nativeClose();

    private static native long nativeCreatePeerConnectionObserver(Observer var0);

    private static native void nativeFreeOwnedPeerConnection(long var0);

    private native boolean nativeSetConfiguration(RTCConfiguration var1);

    private native boolean nativeAddIceCandidate(String var1, int var2, String var3);

    private native void nativeAddIceCandidateWithObserver(String var1, int var2, String var3, AddIceObserver var4);

    private native boolean nativeRemoveIceCandidates(IceCandidate[] var1);

    private native boolean nativeAddLocalStream(long var1);

    private native void nativeRemoveLocalStream(long var1);

    private native boolean nativeOldGetStats(StatsObserver var1, long var2);

    private native void nativeNewGetStats(RTCStatsCollectorCallback var1);

    private native RtpSender nativeCreateSender(String var1, String var2);

    private native List<RtpSender> nativeGetSenders();

    private native List<RtpReceiver> nativeGetReceivers();

    private native List<RtpTransceiver> nativeGetTransceivers();

    private native RtpSender nativeAddTrack(long var1, List<String> var3);

    private native boolean nativeRemoveTrack(long var1);

    private native RtpTransceiver nativeAddTransceiverWithTrack(long var1, RtpTransceiver.RtpTransceiverInit var3);

    private native RtpTransceiver nativeAddTransceiverOfType(MediaStreamTrack.MediaType var1, RtpTransceiver.RtpTransceiverInit var2);

    private native boolean nativeStartRtcEventLog(int var1, int var2);

    private native void nativeStopRtcEventLog();

    public static class RTCConfiguration {
        public IceTransportsType iceTransportsType = IceTransportsType.ALL;
        public List<IceServer> iceServers;
        public BundlePolicy bundlePolicy = BundlePolicy.BALANCED;
        @Nullable
        public RtcCertificatePem certificate;
        public RtcpMuxPolicy rtcpMuxPolicy = RtcpMuxPolicy.REQUIRE;
        public TcpCandidatePolicy tcpCandidatePolicy = TcpCandidatePolicy.ENABLED;
        public CandidateNetworkPolicy candidateNetworkPolicy = CandidateNetworkPolicy.ALL;
        public int audioJitterBufferMaxPackets;
        public boolean audioJitterBufferFastAccelerate;
        public int iceConnectionReceivingTimeout;
        public int iceBackupCandidatePairPingInterval;
        public KeyType keyType;
        public ContinualGatheringPolicy continualGatheringPolicy;
        public int iceCandidatePoolSize;
        @Deprecated
        public boolean pruneTurnPorts;
        public PortPrunePolicy turnPortPrunePolicy;
        public boolean presumeWritableWhenFullyRelayed;
        public boolean surfaceIceCandidatesOnIceTransportTypeChanged;
        @Nullable
        public Integer iceCheckIntervalStrongConnectivityMs;
        @Nullable
        public Integer iceCheckIntervalWeakConnectivityMs;
        @Nullable
        public Integer iceCheckMinInterval;
        @Nullable
        public Integer iceUnwritableTimeMs;
        @Nullable
        public Integer iceUnwritableMinChecks;
        @Nullable
        public Integer stunCandidateKeepaliveIntervalMs;
        @Nullable
        public Integer stableWritableConnectionPingIntervalMs;
        public boolean disableIPv6OnWifi;
        public int maxIPv6Networks;
        public boolean disableIpv6;
        public boolean enableDscp;
        public boolean enableCpuOveruseDetection;
        public boolean suspendBelowMinBitrate;
        @Nullable
        public Integer screencastMinBitrate;
        @Nullable
        public Boolean combinedAudioVideoBwe;
        @Nullable
        public Boolean enableDtlsSrtp;
        public AdapterType networkPreference;
        public SdpSemantics sdpSemantics;
        @Nullable
        public TurnCustomizer turnCustomizer;
        public boolean activeResetSrtpParams;
        @Nullable
        public Boolean allowCodecSwitching;
        @Nullable
        public CryptoOptions cryptoOptions;
        @Nullable
        public String turnLoggingId;
        public boolean enableImplicitRollback;
        public boolean offerExtmapAllowMixed;

        public RTCConfiguration(List<IceServer> iceServers) {
            this.iceServers = iceServers;
            this.audioJitterBufferMaxPackets = 50;
            this.audioJitterBufferFastAccelerate = false;
            this.iceConnectionReceivingTimeout = -1;
            this.iceBackupCandidatePairPingInterval = -1;
            this.keyType = KeyType.ECDSA;
            this.continualGatheringPolicy = ContinualGatheringPolicy.GATHER_ONCE;
            this.iceCandidatePoolSize = 0;
            this.pruneTurnPorts = false;
            this.turnPortPrunePolicy = PortPrunePolicy.NO_PRUNE;
            this.presumeWritableWhenFullyRelayed = false;
            this.surfaceIceCandidatesOnIceTransportTypeChanged = false;
            this.iceCheckIntervalStrongConnectivityMs = null;
            this.iceCheckIntervalWeakConnectivityMs = null;
            this.iceCheckMinInterval = null;
            this.iceUnwritableTimeMs = null;
            this.iceUnwritableMinChecks = null;
            this.stunCandidateKeepaliveIntervalMs = null;
            this.stableWritableConnectionPingIntervalMs = null;
            this.disableIPv6OnWifi = false;
            this.maxIPv6Networks = 5;
            this.disableIpv6 = false;
            this.enableDscp = false;
            this.enableCpuOveruseDetection = true;
            this.suspendBelowMinBitrate = false;
            this.screencastMinBitrate = null;
            this.combinedAudioVideoBwe = null;
            this.enableDtlsSrtp = null;
            this.networkPreference = AdapterType.UNKNOWN;
            this.sdpSemantics = SdpSemantics.PLAN_B;
            this.activeResetSrtpParams = false;
            this.cryptoOptions = null;
            this.turnLoggingId = null;
            this.allowCodecSwitching = null;
            this.enableImplicitRollback = false;
            this.offerExtmapAllowMixed = true;
        }

        @CalledByNative(value="RTCConfiguration")
        IceTransportsType getIceTransportsType() {
            return this.iceTransportsType;
        }

        @CalledByNative(value="RTCConfiguration")
        List<IceServer> getIceServers() {
            return this.iceServers;
        }

        @CalledByNative(value="RTCConfiguration")
        BundlePolicy getBundlePolicy() {
            return this.bundlePolicy;
        }

        @CalledByNative(value="RTCConfiguration")
        PortPrunePolicy getTurnPortPrunePolicy() {
            return this.turnPortPrunePolicy;
        }

        @Nullable
        @CalledByNative(value="RTCConfiguration")
        RtcCertificatePem getCertificate() {
            return this.certificate;
        }

        @CalledByNative(value="RTCConfiguration")
        RtcpMuxPolicy getRtcpMuxPolicy() {
            return this.rtcpMuxPolicy;
        }

        @CalledByNative(value="RTCConfiguration")
        TcpCandidatePolicy getTcpCandidatePolicy() {
            return this.tcpCandidatePolicy;
        }

        @CalledByNative(value="RTCConfiguration")
        CandidateNetworkPolicy getCandidateNetworkPolicy() {
            return this.candidateNetworkPolicy;
        }

        @CalledByNative(value="RTCConfiguration")
        int getAudioJitterBufferMaxPackets() {
            return this.audioJitterBufferMaxPackets;
        }

        @CalledByNative(value="RTCConfiguration")
        boolean getAudioJitterBufferFastAccelerate() {
            return this.audioJitterBufferFastAccelerate;
        }

        @CalledByNative(value="RTCConfiguration")
        int getIceConnectionReceivingTimeout() {
            return this.iceConnectionReceivingTimeout;
        }

        @CalledByNative(value="RTCConfiguration")
        int getIceBackupCandidatePairPingInterval() {
            return this.iceBackupCandidatePairPingInterval;
        }

        @CalledByNative(value="RTCConfiguration")
        KeyType getKeyType() {
            return this.keyType;
        }

        @CalledByNative(value="RTCConfiguration")
        ContinualGatheringPolicy getContinualGatheringPolicy() {
            return this.continualGatheringPolicy;
        }

        @CalledByNative(value="RTCConfiguration")
        int getIceCandidatePoolSize() {
            return this.iceCandidatePoolSize;
        }

        @CalledByNative(value="RTCConfiguration")
        boolean getPruneTurnPorts() {
            return this.pruneTurnPorts;
        }

        @CalledByNative(value="RTCConfiguration")
        boolean getPresumeWritableWhenFullyRelayed() {
            return this.presumeWritableWhenFullyRelayed;
        }

        @CalledByNative(value="RTCConfiguration")
        boolean getSurfaceIceCandidatesOnIceTransportTypeChanged() {
            return this.surfaceIceCandidatesOnIceTransportTypeChanged;
        }

        @Nullable
        @CalledByNative(value="RTCConfiguration")
        Integer getIceCheckIntervalStrongConnectivity() {
            return this.iceCheckIntervalStrongConnectivityMs;
        }

        @Nullable
        @CalledByNative(value="RTCConfiguration")
        Integer getIceCheckIntervalWeakConnectivity() {
            return this.iceCheckIntervalWeakConnectivityMs;
        }

        @Nullable
        @CalledByNative(value="RTCConfiguration")
        Integer getIceCheckMinInterval() {
            return this.iceCheckMinInterval;
        }

        @Nullable
        @CalledByNative(value="RTCConfiguration")
        Integer getIceUnwritableTimeout() {
            return this.iceUnwritableTimeMs;
        }

        @Nullable
        @CalledByNative(value="RTCConfiguration")
        Integer getIceUnwritableMinChecks() {
            return this.iceUnwritableMinChecks;
        }

        @Nullable
        @CalledByNative(value="RTCConfiguration")
        Integer getStunCandidateKeepaliveInterval() {
            return this.stunCandidateKeepaliveIntervalMs;
        }

        @Nullable
        @CalledByNative(value="RTCConfiguration")
        Integer getStableWritableConnectionPingIntervalMs() {
            return this.stableWritableConnectionPingIntervalMs;
        }

        @CalledByNative(value="RTCConfiguration")
        boolean getDisableIPv6OnWifi() {
            return this.disableIPv6OnWifi;
        }

        @CalledByNative(value="RTCConfiguration")
        int getMaxIPv6Networks() {
            return this.maxIPv6Networks;
        }

        @Nullable
        @CalledByNative(value="RTCConfiguration")
        TurnCustomizer getTurnCustomizer() {
            return this.turnCustomizer;
        }

        @CalledByNative(value="RTCConfiguration")
        boolean getDisableIpv6() {
            return this.disableIpv6;
        }

        @CalledByNative(value="RTCConfiguration")
        boolean getEnableDscp() {
            return this.enableDscp;
        }

        @CalledByNative(value="RTCConfiguration")
        boolean getEnableCpuOveruseDetection() {
            return this.enableCpuOveruseDetection;
        }

        @CalledByNative(value="RTCConfiguration")
        boolean getSuspendBelowMinBitrate() {
            return this.suspendBelowMinBitrate;
        }

        @Nullable
        @CalledByNative(value="RTCConfiguration")
        Integer getScreencastMinBitrate() {
            return this.screencastMinBitrate;
        }

        @Nullable
        @CalledByNative(value="RTCConfiguration")
        Boolean getCombinedAudioVideoBwe() {
            return this.combinedAudioVideoBwe;
        }

        @Nullable
        @CalledByNative(value="RTCConfiguration")
        Boolean getEnableDtlsSrtp() {
            return this.enableDtlsSrtp;
        }

        @CalledByNative(value="RTCConfiguration")
        AdapterType getNetworkPreference() {
            return this.networkPreference;
        }

        @CalledByNative(value="RTCConfiguration")
        SdpSemantics getSdpSemantics() {
            return this.sdpSemantics;
        }

        @CalledByNative(value="RTCConfiguration")
        boolean getActiveResetSrtpParams() {
            return this.activeResetSrtpParams;
        }

        @Nullable
        @CalledByNative(value="RTCConfiguration")
        Boolean getAllowCodecSwitching() {
            return this.allowCodecSwitching;
        }

        @Nullable
        @CalledByNative(value="RTCConfiguration")
        CryptoOptions getCryptoOptions() {
            return this.cryptoOptions;
        }

        @Nullable
        @CalledByNative(value="RTCConfiguration")
        String getTurnLoggingId() {
            return this.turnLoggingId;
        }

        @CalledByNative(value="RTCConfiguration")
        boolean getEnableImplicitRollback() {
            return this.enableImplicitRollback;
        }

        @CalledByNative(value="RTCConfiguration")
        boolean getOfferExtmapAllowMixed() {
            return this.offerExtmapAllowMixed;
        }
    }

    public static enum SdpSemantics {
        PLAN_B,
        UNIFIED_PLAN;

    }

    public static enum PortPrunePolicy {
        NO_PRUNE,
        PRUNE_BASED_ON_PRIORITY,
        KEEP_FIRST_READY;

    }

    public static enum ContinualGatheringPolicy {
        GATHER_ONCE,
        GATHER_CONTINUALLY;

    }

    public static enum KeyType {
        RSA,
        ECDSA;

    }

    public static enum AdapterType {
        UNKNOWN(0),
        ETHERNET(1),
        WIFI(2),
        CELLULAR(4),
        VPN(8),
        LOOPBACK(16),
        ADAPTER_TYPE_ANY(32),
        CELLULAR_2G(64),
        CELLULAR_3G(128),
        CELLULAR_4G(256),
        CELLULAR_5G(512);

        public final Integer bitMask;
        private static final Map<Integer, AdapterType> BY_BITMASK;

        private AdapterType(Integer bitMask) {
            this.bitMask = bitMask;
        }

        @Nullable
        @CalledByNative(value="AdapterType")
        static AdapterType fromNativeIndex(int nativeIndex) {
            return BY_BITMASK.get(nativeIndex);
        }

        static {
            BY_BITMASK = new HashMap<Integer, AdapterType>();
            for (AdapterType t : AdapterType.values()) {
                BY_BITMASK.put(t.bitMask, t);
            }
        }
    }

    public static enum CandidateNetworkPolicy {
        ALL,
        LOW_COST;

    }

    public static enum TcpCandidatePolicy {
        ENABLED,
        DISABLED;

    }

    public static enum RtcpMuxPolicy {
        NEGOTIATE,
        REQUIRE;

    }

    public static enum BundlePolicy {
        BALANCED,
        MAXBUNDLE,
        MAXCOMPAT;

    }

    public static enum IceTransportsType {
        NONE,
        RELAY,
        NOHOST,
        ALL;

    }

    public static class IceServer {
        @Deprecated
        public final String uri;
        public final List<String> urls;
        public final String username;
        public final String password;
        public final TlsCertPolicy tlsCertPolicy;
        public final String hostname;
        public final List<String> tlsAlpnProtocols;
        public final List<String> tlsEllipticCurves;

        @Deprecated
        public IceServer(String uri) {
            this(uri, "", "");
        }

        @Deprecated
        public IceServer(String uri, String username, String password) {
            this(uri, username, password, TlsCertPolicy.TLS_CERT_POLICY_SECURE);
        }

        @Deprecated
        public IceServer(String uri, String username, String password, TlsCertPolicy tlsCertPolicy) {
            this(uri, username, password, tlsCertPolicy, "");
        }

        @Deprecated
        public IceServer(String uri, String username, String password, TlsCertPolicy tlsCertPolicy, String hostname) {
            this(uri, Collections.singletonList(uri), username, password, tlsCertPolicy, hostname, null, null);
        }

        private IceServer(String uri, List<String> urls, String username, String password, TlsCertPolicy tlsCertPolicy, String hostname, List<String> tlsAlpnProtocols, List<String> tlsEllipticCurves) {
            if (uri == null || urls == null || urls.isEmpty()) {
                throw new IllegalArgumentException("uri == null || urls == null || urls.isEmpty()");
            }
            for (String it : urls) {
                if (it != null) continue;
                throw new IllegalArgumentException("urls element is null: " + urls);
            }
            if (username == null) {
                throw new IllegalArgumentException("username == null");
            }
            if (password == null) {
                throw new IllegalArgumentException("password == null");
            }
            if (hostname == null) {
                throw new IllegalArgumentException("hostname == null");
            }
            this.uri = uri;
            this.urls = urls;
            this.username = username;
            this.password = password;
            this.tlsCertPolicy = tlsCertPolicy;
            this.hostname = hostname;
            this.tlsAlpnProtocols = tlsAlpnProtocols;
            this.tlsEllipticCurves = tlsEllipticCurves;
        }

        public String toString() {
            return this.urls + " [" + this.username + ":" + this.password + "] [" + (Object)((Object)this.tlsCertPolicy) + "] [" + this.hostname + "] [" + this.tlsAlpnProtocols + "] [" + this.tlsEllipticCurves + "]";
        }

        public boolean equals(@Nullable Object obj) {
            if (obj == null) {
                return false;
            }
            if (obj == this) {
                return true;
            }
            if (!(obj instanceof IceServer)) {
                return false;
            }
            IceServer other = (IceServer)obj;
            return this.uri.equals(other.uri) && this.urls.equals(other.urls) && this.username.equals(other.username) && this.password.equals(other.password) && this.tlsCertPolicy.equals((Object)other.tlsCertPolicy) && this.hostname.equals(other.hostname) && this.tlsAlpnProtocols.equals(other.tlsAlpnProtocols) && this.tlsEllipticCurves.equals(other.tlsEllipticCurves);
        }

        public int hashCode() {
            Object[] values = new Object[]{this.uri, this.urls, this.username, this.password, this.tlsCertPolicy, this.hostname, this.tlsAlpnProtocols, this.tlsEllipticCurves};
            return Arrays.hashCode(values);
        }

        public static Builder builder(String uri) {
            return new Builder(Collections.singletonList(uri));
        }

        public static Builder builder(List<String> urls) {
            return new Builder(urls);
        }

        @Nullable
        @CalledByNative(value="IceServer")
        List<String> getUrls() {
            return this.urls;
        }

        @Nullable
        @CalledByNative(value="IceServer")
        String getUsername() {
            return this.username;
        }

        @Nullable
        @CalledByNative(value="IceServer")
        String getPassword() {
            return this.password;
        }

        @CalledByNative(value="IceServer")
        TlsCertPolicy getTlsCertPolicy() {
            return this.tlsCertPolicy;
        }

        @Nullable
        @CalledByNative(value="IceServer")
        String getHostname() {
            return this.hostname;
        }

        @CalledByNative(value="IceServer")
        List<String> getTlsAlpnProtocols() {
            return this.tlsAlpnProtocols;
        }

        @CalledByNative(value="IceServer")
        List<String> getTlsEllipticCurves() {
            return this.tlsEllipticCurves;
        }

        public static class Builder {
            @Nullable
            private final List<String> urls;
            private String username = "";
            private String password = "";
            private TlsCertPolicy tlsCertPolicy = TlsCertPolicy.TLS_CERT_POLICY_SECURE;
            private String hostname = "";
            private List<String> tlsAlpnProtocols;
            private List<String> tlsEllipticCurves;

            private Builder(List<String> urls) {
                if (urls == null || urls.isEmpty()) {
                    throw new IllegalArgumentException("urls == null || urls.isEmpty(): " + urls);
                }
                this.urls = urls;
            }

            public Builder setUsername(String username) {
                this.username = username;
                return this;
            }

            public Builder setPassword(String password) {
                this.password = password;
                return this;
            }

            public Builder setTlsCertPolicy(TlsCertPolicy tlsCertPolicy) {
                this.tlsCertPolicy = tlsCertPolicy;
                return this;
            }

            public Builder setHostname(String hostname) {
                this.hostname = hostname;
                return this;
            }

            public Builder setTlsAlpnProtocols(List<String> tlsAlpnProtocols) {
                this.tlsAlpnProtocols = tlsAlpnProtocols;
                return this;
            }

            public Builder setTlsEllipticCurves(List<String> tlsEllipticCurves) {
                this.tlsEllipticCurves = tlsEllipticCurves;
                return this;
            }

            public IceServer createIceServer() {
                return new IceServer(this.urls.get(0), this.urls, this.username, this.password, this.tlsCertPolicy, this.hostname, this.tlsAlpnProtocols, this.tlsEllipticCurves);
            }
        }
    }

    public static interface Observer {
        @CalledByNative(value="Observer")
        public void onSignalingChange(SignalingState var1);

        @CalledByNative(value="Observer")
        public void onIceConnectionChange(IceConnectionState var1);

        @CalledByNative(value="Observer")
        default public void onStandardizedIceConnectionChange(IceConnectionState newState) {
        }

        @CalledByNative(value="Observer")
        default public void onConnectionChange(PeerConnectionState newState) {
        }

        @CalledByNative(value="Observer")
        public void onIceConnectionReceivingChange(boolean var1);

        @CalledByNative(value="Observer")
        public void onIceGatheringChange(IceGatheringState var1);

        @CalledByNative(value="Observer")
        public void onIceCandidate(IceCandidate var1);

        @CalledByNative(value="Observer")
        public void onIceCandidatesRemoved(IceCandidate[] var1);

        @CalledByNative(value="Observer")
        default public void onSelectedCandidatePairChanged(CandidatePairChangeEvent event) {
        }

        @CalledByNative(value="Observer")
        public void onAddStream(MediaStream var1);

        @CalledByNative(value="Observer")
        public void onRemoveStream(MediaStream var1);

        @CalledByNative(value="Observer")
        public void onDataChannel(DataChannel var1);

        @CalledByNative(value="Observer")
        public void onRenegotiationNeeded();

        @CalledByNative(value="Observer")
        default public void onAddTrack(RtpReceiver receiver, MediaStream[] mediaStreams) {
        }

        @CalledByNative(value="Observer")
        default public void onRemoveTrack(RtpReceiver receiver) {
        }

        @CalledByNative(value="Observer")
        default public void onTrack(RtpTransceiver transceiver) {
        }
    }

    public static enum SignalingState {
        STABLE,
        HAVE_LOCAL_OFFER,
        HAVE_LOCAL_PRANSWER,
        HAVE_REMOTE_OFFER,
        HAVE_REMOTE_PRANSWER,
        CLOSED;


        @CalledByNative(value="SignalingState")
        static SignalingState fromNativeIndex(int nativeIndex) {
            return SignalingState.values()[nativeIndex];
        }
    }

    public static enum TlsCertPolicy {
        TLS_CERT_POLICY_SECURE,
        TLS_CERT_POLICY_INSECURE_NO_CHECK;

    }

    public static enum PeerConnectionState {
        NEW,
        CONNECTING,
        CONNECTED,
        DISCONNECTED,
        FAILED,
        CLOSED;


        @CalledByNative(value="PeerConnectionState")
        static PeerConnectionState fromNativeIndex(int nativeIndex) {
            return PeerConnectionState.values()[nativeIndex];
        }
    }

    public static enum IceConnectionState {
        NEW,
        CHECKING,
        CONNECTED,
        COMPLETED,
        FAILED,
        DISCONNECTED,
        CLOSED;


        @CalledByNative(value="IceConnectionState")
        static IceConnectionState fromNativeIndex(int nativeIndex) {
            return IceConnectionState.values()[nativeIndex];
        }
    }

    public static enum IceGatheringState {
        NEW,
        GATHERING,
        COMPLETE;


        @CalledByNative(value="IceGatheringState")
        static IceGatheringState fromNativeIndex(int nativeIndex) {
            return IceGatheringState.values()[nativeIndex];
        }
    }
}

