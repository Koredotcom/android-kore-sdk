/*
 * Decompiled with CFR 0.152.
 */
package org.webrtc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.webrtc.CalledByNative;
import org.webrtc.JniCommon;
import org.webrtc.MediaStreamTrack;
import org.webrtc.RtpCapabilities;
import org.webrtc.RtpParameters;
import org.webrtc.RtpReceiver;
import org.webrtc.RtpSender;

public class RtpTransceiver {
    private long nativeRtpTransceiver;
    private RtpSender cachedSender;
    private RtpReceiver cachedReceiver;

    @CalledByNative
    protected RtpTransceiver(long nativeRtpTransceiver) {
        this.nativeRtpTransceiver = nativeRtpTransceiver;
        this.cachedSender = RtpTransceiver.nativeGetSender(nativeRtpTransceiver);
        this.cachedReceiver = RtpTransceiver.nativeGetReceiver(nativeRtpTransceiver);
    }

    public MediaStreamTrack.MediaType getMediaType() {
        this.checkRtpTransceiverExists();
        return RtpTransceiver.nativeGetMediaType(this.nativeRtpTransceiver);
    }

    public String getMid() {
        this.checkRtpTransceiverExists();
        return RtpTransceiver.nativeGetMid(this.nativeRtpTransceiver);
    }

    public RtpSender getSender() {
        return this.cachedSender;
    }

    public RtpReceiver getReceiver() {
        return this.cachedReceiver;
    }

    public boolean isStopped() {
        this.checkRtpTransceiverExists();
        return RtpTransceiver.nativeStopped(this.nativeRtpTransceiver);
    }

    public RtpTransceiverDirection getDirection() {
        this.checkRtpTransceiverExists();
        return RtpTransceiver.nativeDirection(this.nativeRtpTransceiver);
    }

    public RtpTransceiverDirection getCurrentDirection() {
        this.checkRtpTransceiverExists();
        return RtpTransceiver.nativeCurrentDirection(this.nativeRtpTransceiver);
    }

    public boolean setDirection(RtpTransceiverDirection rtpTransceiverDirection) {
        this.checkRtpTransceiverExists();
        return RtpTransceiver.nativeSetDirection(this.nativeRtpTransceiver, rtpTransceiverDirection);
    }

    public void stop() {
        this.checkRtpTransceiverExists();
        RtpTransceiver.nativeStopInternal(this.nativeRtpTransceiver);
    }

    public void setCodecPreferences(List<RtpCapabilities.CodecCapability> codecs) {
        this.checkRtpTransceiverExists();
        RtpTransceiver.nativeSetCodecPreferences(this.nativeRtpTransceiver, codecs);
    }

    public void stopInternal() {
        this.checkRtpTransceiverExists();
        RtpTransceiver.nativeStopInternal(this.nativeRtpTransceiver);
    }

    public void stopStandard() {
        this.checkRtpTransceiverExists();
        RtpTransceiver.nativeStopStandard(this.nativeRtpTransceiver);
    }

    @CalledByNative
    public void dispose() {
        this.checkRtpTransceiverExists();
        this.cachedSender.dispose();
        this.cachedReceiver.dispose();
        JniCommon.nativeReleaseRef(this.nativeRtpTransceiver);
        this.nativeRtpTransceiver = 0L;
    }

    private void checkRtpTransceiverExists() {
        if (this.nativeRtpTransceiver == 0L) {
            throw new IllegalStateException("RtpTransceiver has been disposed.");
        }
    }

    private static native MediaStreamTrack.MediaType nativeGetMediaType(long var0);

    private static native String nativeGetMid(long var0);

    private static native RtpSender nativeGetSender(long var0);

    private static native RtpReceiver nativeGetReceiver(long var0);

    private static native boolean nativeStopped(long var0);

    private static native RtpTransceiverDirection nativeDirection(long var0);

    private static native RtpTransceiverDirection nativeCurrentDirection(long var0);

    private static native void nativeStopInternal(long var0);

    private static native void nativeStopStandard(long var0);

    private static native boolean nativeSetDirection(long var0, RtpTransceiverDirection var2);

    private static native void nativeSetCodecPreferences(long var0, List<RtpCapabilities.CodecCapability> var2);

    public static enum RtpTransceiverDirection {
        SEND_RECV(0),
        SEND_ONLY(1),
        RECV_ONLY(2),
        INACTIVE(3),
        STOPPED(4);

        private final int nativeIndex;

        private RtpTransceiverDirection(int nativeIndex) {
            this.nativeIndex = nativeIndex;
        }

        @CalledByNative(value="RtpTransceiverDirection")
        int getNativeIndex() {
            return this.nativeIndex;
        }

        @CalledByNative(value="RtpTransceiverDirection")
        static RtpTransceiverDirection fromNativeIndex(int nativeIndex) {
            for (RtpTransceiverDirection type : RtpTransceiverDirection.values()) {
                if (type.getNativeIndex() != nativeIndex) continue;
                return type;
            }
            throw new IllegalArgumentException("Uknown native RtpTransceiverDirection type" + nativeIndex);
        }
    }

    public static final class RtpTransceiverInit {
        private final RtpTransceiverDirection direction;
        private final List<String> streamIds;
        private final List<RtpParameters.Encoding> sendEncodings;

        public RtpTransceiverInit() {
            this(RtpTransceiverDirection.SEND_RECV);
        }

        public RtpTransceiverInit(RtpTransceiverDirection direction) {
            this(direction, Collections.emptyList(), Collections.emptyList());
        }

        public RtpTransceiverInit(RtpTransceiverDirection direction, List<String> streamIds) {
            this(direction, streamIds, Collections.emptyList());
        }

        public RtpTransceiverInit(RtpTransceiverDirection direction, List<String> streamIds, List<RtpParameters.Encoding> sendEncodings) {
            this.direction = direction;
            this.streamIds = new ArrayList<String>(streamIds);
            this.sendEncodings = new ArrayList<RtpParameters.Encoding>(sendEncodings);
        }

        @CalledByNative(value="RtpTransceiverInit")
        int getDirectionNativeIndex() {
            return this.direction.getNativeIndex();
        }

        @CalledByNative(value="RtpTransceiverInit")
        List<String> getStreamIds() {
            return new ArrayList<String>(this.streamIds);
        }

        @CalledByNative(value="RtpTransceiverInit")
        List<RtpParameters.Encoding> getSendEncodings() {
            return new ArrayList<RtpParameters.Encoding>(this.sendEncodings);
        }
    }
}

