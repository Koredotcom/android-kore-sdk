/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  androidx.annotation.Nullable
 */
package org.webrtc;

import androidx.annotation.Nullable;
import java.util.List;
import java.util.Map;
import org.webrtc.CalledByNative;
import org.webrtc.MediaStreamTrack;

public class RtpParameters {
    public final String transactionId;
    @Nullable
    public DegradationPreference degradationPreference;
    private final Rtcp rtcp;
    private final List<HeaderExtension> headerExtensions;
    public final List<Encoding> encodings;
    public final List<Codec> codecs;

    @CalledByNative
    RtpParameters(String transactionId, DegradationPreference degradationPreference, Rtcp rtcp, List<HeaderExtension> headerExtensions, List<Encoding> encodings, List<Codec> codecs) {
        this.transactionId = transactionId;
        this.degradationPreference = degradationPreference;
        this.rtcp = rtcp;
        this.headerExtensions = headerExtensions;
        this.encodings = encodings;
        this.codecs = codecs;
    }

    @CalledByNative
    String getTransactionId() {
        return this.transactionId;
    }

    @CalledByNative
    DegradationPreference getDegradationPreference() {
        return this.degradationPreference;
    }

    @CalledByNative
    public Rtcp getRtcp() {
        return this.rtcp;
    }

    @CalledByNative
    public List<HeaderExtension> getHeaderExtensions() {
        return this.headerExtensions;
    }

    @CalledByNative
    List<Encoding> getEncodings() {
        return this.encodings;
    }

    @CalledByNative
    List<Codec> getCodecs() {
        return this.codecs;
    }

    public static class HeaderExtension {
        private final String uri;
        private final int id;
        private final boolean encrypted;

        @CalledByNative(value="HeaderExtension")
        HeaderExtension(String uri, int id, boolean encrypted) {
            this.uri = uri;
            this.id = id;
            this.encrypted = encrypted;
        }

        @CalledByNative(value="HeaderExtension")
        public String getUri() {
            return this.uri;
        }

        @CalledByNative(value="HeaderExtension")
        public int getId() {
            return this.id;
        }

        @CalledByNative(value="HeaderExtension")
        public boolean getEncrypted() {
            return this.encrypted;
        }
    }

    public static class Rtcp {
        private final String cname;
        private final boolean reducedSize;

        @CalledByNative(value="Rtcp")
        Rtcp(String cname, boolean reducedSize) {
            this.cname = cname;
            this.reducedSize = reducedSize;
        }

        @CalledByNative(value="Rtcp")
        public String getCname() {
            return this.cname;
        }

        @CalledByNative(value="Rtcp")
        public boolean getReducedSize() {
            return this.reducedSize;
        }
    }

    public static class Codec {
        public int payloadType;
        public String name;
        MediaStreamTrack.MediaType kind;
        public Integer clockRate;
        public Integer numChannels;
        public Map<String, String> parameters;

        @CalledByNative(value="Codec")
        Codec(int payloadType, String name, MediaStreamTrack.MediaType kind, Integer clockRate, Integer numChannels, Map<String, String> parameters) {
            this.payloadType = payloadType;
            this.name = name;
            this.kind = kind;
            this.clockRate = clockRate;
            this.numChannels = numChannels;
            this.parameters = parameters;
        }

        @CalledByNative(value="Codec")
        int getPayloadType() {
            return this.payloadType;
        }

        @CalledByNative(value="Codec")
        String getName() {
            return this.name;
        }

        @CalledByNative(value="Codec")
        MediaStreamTrack.MediaType getKind() {
            return this.kind;
        }

        @CalledByNative(value="Codec")
        Integer getClockRate() {
            return this.clockRate;
        }

        @CalledByNative(value="Codec")
        Integer getNumChannels() {
            return this.numChannels;
        }

        @CalledByNative(value="Codec")
        Map getParameters() {
            return this.parameters;
        }
    }

    public static class Encoding {
        @Nullable
        public String rid;
        public boolean active = true;
        public double bitratePriority = 1.0;
        public int networkPriority = 1;
        @Nullable
        public Integer maxBitrateBps;
        @Nullable
        public Integer minBitrateBps;
        @Nullable
        public Integer maxFramerate;
        @Nullable
        public Integer numTemporalLayers;
        @Nullable
        public Double scaleResolutionDownBy;
        public Long ssrc;
        public boolean adaptiveAudioPacketTime;

        public Encoding(String rid, boolean active, Double scaleResolutionDownBy) {
            this.rid = rid;
            this.active = active;
            this.scaleResolutionDownBy = scaleResolutionDownBy;
        }

        @CalledByNative(value="Encoding")
        Encoding(String rid, boolean active, double bitratePriority, int networkPriority, Integer maxBitrateBps, Integer minBitrateBps, Integer maxFramerate, Integer numTemporalLayers, Double scaleResolutionDownBy, Long ssrc, boolean adaptiveAudioPacketTime) {
            this.rid = rid;
            this.active = active;
            this.bitratePriority = bitratePriority;
            this.networkPriority = networkPriority;
            this.maxBitrateBps = maxBitrateBps;
            this.minBitrateBps = minBitrateBps;
            this.maxFramerate = maxFramerate;
            this.numTemporalLayers = numTemporalLayers;
            this.scaleResolutionDownBy = scaleResolutionDownBy;
            this.ssrc = ssrc;
            this.adaptiveAudioPacketTime = adaptiveAudioPacketTime;
        }

        @Nullable
        @CalledByNative(value="Encoding")
        String getRid() {
            return this.rid;
        }

        @CalledByNative(value="Encoding")
        boolean getActive() {
            return this.active;
        }

        @CalledByNative(value="Encoding")
        double getBitratePriority() {
            return this.bitratePriority;
        }

        @CalledByNative(value="Encoding")
        int getNetworkPriority() {
            return this.networkPriority;
        }

        @Nullable
        @CalledByNative(value="Encoding")
        Integer getMaxBitrateBps() {
            return this.maxBitrateBps;
        }

        @Nullable
        @CalledByNative(value="Encoding")
        Integer getMinBitrateBps() {
            return this.minBitrateBps;
        }

        @Nullable
        @CalledByNative(value="Encoding")
        Integer getMaxFramerate() {
            return this.maxFramerate;
        }

        @Nullable
        @CalledByNative(value="Encoding")
        Integer getNumTemporalLayers() {
            return this.numTemporalLayers;
        }

        @Nullable
        @CalledByNative(value="Encoding")
        Double getScaleResolutionDownBy() {
            return this.scaleResolutionDownBy;
        }

        @CalledByNative(value="Encoding")
        Long getSsrc() {
            return this.ssrc;
        }

        @CalledByNative(value="Encoding")
        boolean getAdaptivePTime() {
            return this.adaptiveAudioPacketTime;
        }
    }

    public static enum DegradationPreference {
        DISABLED,
        MAINTAIN_FRAMERATE,
        MAINTAIN_RESOLUTION,
        BALANCED;


        @CalledByNative(value="DegradationPreference")
        static DegradationPreference fromNativeIndex(int nativeIndex) {
            return DegradationPreference.values()[nativeIndex];
        }
    }
}

