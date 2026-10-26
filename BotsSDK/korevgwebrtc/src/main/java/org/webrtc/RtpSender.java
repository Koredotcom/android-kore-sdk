/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  androidx.annotation.Nullable
 */
package org.webrtc;

import androidx.annotation.Nullable;
import java.util.List;
import org.webrtc.CalledByNative;
import org.webrtc.DtmfSender;
import org.webrtc.FrameEncryptor;
import org.webrtc.JniCommon;
import org.webrtc.MediaStreamTrack;
import org.webrtc.RtpParameters;

public class RtpSender {
    private long nativeRtpSender;
    @Nullable
    private MediaStreamTrack cachedTrack;
    private boolean ownsTrack = true;
    @Nullable
    private final DtmfSender dtmfSender;

    @CalledByNative
    public RtpSender(long nativeRtpSender) {
        long nativeDtmfSender;
        this.nativeRtpSender = nativeRtpSender;
        long nativeTrack = RtpSender.nativeGetTrack(nativeRtpSender);
        this.cachedTrack = MediaStreamTrack.createMediaStreamTrack(nativeTrack);
        this.dtmfSender = RtpSender.nativeGetMediaType(nativeRtpSender).equalsIgnoreCase("audio") ? ((nativeDtmfSender = RtpSender.nativeGetDtmfSender(nativeRtpSender)) != 0L ? new DtmfSender(nativeDtmfSender) : null) : null;
    }

    public boolean setTrack(@Nullable MediaStreamTrack track, boolean takeOwnership) {
        this.checkRtpSenderExists();
        if (!RtpSender.nativeSetTrack(this.nativeRtpSender, track == null ? 0L : track.getNativeMediaStreamTrack())) {
            return false;
        }
        if (this.cachedTrack != null && this.ownsTrack) {
            this.cachedTrack.dispose();
        }
        this.cachedTrack = track;
        this.ownsTrack = takeOwnership;
        return true;
    }

    @Nullable
    public MediaStreamTrack track() {
        return this.cachedTrack;
    }

    public void setStreams(List<String> streamIds) {
        this.checkRtpSenderExists();
        RtpSender.nativeSetStreams(this.nativeRtpSender, streamIds);
    }

    public List<String> getStreams() {
        this.checkRtpSenderExists();
        return RtpSender.nativeGetStreams(this.nativeRtpSender);
    }

    public boolean setParameters(RtpParameters parameters) {
        this.checkRtpSenderExists();
        return RtpSender.nativeSetParameters(this.nativeRtpSender, parameters);
    }

    public RtpParameters getParameters() {
        this.checkRtpSenderExists();
        return RtpSender.nativeGetParameters(this.nativeRtpSender);
    }

    public String id() {
        this.checkRtpSenderExists();
        return RtpSender.nativeGetId(this.nativeRtpSender);
    }

    @Nullable
    public DtmfSender dtmf() {
        return this.dtmfSender;
    }

    public void setFrameEncryptor(FrameEncryptor frameEncryptor) {
        this.checkRtpSenderExists();
        RtpSender.nativeSetFrameEncryptor(this.nativeRtpSender, frameEncryptor.getNativeFrameEncryptor());
    }

    public void dispose() {
        this.checkRtpSenderExists();
        if (this.dtmfSender != null) {
            this.dtmfSender.dispose();
        }
        if (this.cachedTrack != null && this.ownsTrack) {
            this.cachedTrack.dispose();
        }
        JniCommon.nativeReleaseRef(this.nativeRtpSender);
        this.nativeRtpSender = 0L;
    }

    long getNativeRtpSender() {
        this.checkRtpSenderExists();
        return this.nativeRtpSender;
    }

    private void checkRtpSenderExists() {
        if (this.nativeRtpSender == 0L) {
            throw new IllegalStateException("RtpSender has been disposed.");
        }
    }

    private static native boolean nativeSetTrack(long var0, long var2);

    private static native long nativeGetTrack(long var0);

    private static native void nativeSetStreams(long var0, List<String> var2);

    private static native List<String> nativeGetStreams(long var0);

    private static native long nativeGetDtmfSender(long var0);

    private static native boolean nativeSetParameters(long var0, RtpParameters var2);

    private static native RtpParameters nativeGetParameters(long var0);

    private static native String nativeGetId(long var0);

    private static native void nativeSetFrameEncryptor(long var0, long var2);

    private static native String nativeGetMediaType(long var0);
}

