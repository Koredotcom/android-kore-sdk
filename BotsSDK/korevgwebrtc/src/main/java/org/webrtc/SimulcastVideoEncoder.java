/*
 * Decompiled with CFR 0.152.
 */
package org.webrtc;

import org.webrtc.VideoCodecInfo;
import org.webrtc.VideoEncoderFactory;
import org.webrtc.WrappedNativeVideoEncoder;

public class SimulcastVideoEncoder
extends WrappedNativeVideoEncoder {
    VideoEncoderFactory primary;
    VideoEncoderFactory fallback;
    VideoCodecInfo info;

    static native long nativeCreateEncoder(long var0, VideoEncoderFactory var2, VideoEncoderFactory var3, VideoCodecInfo var4);

    public SimulcastVideoEncoder(VideoEncoderFactory primary, VideoEncoderFactory fallback, VideoCodecInfo info) {
        this.primary = primary;
        this.fallback = fallback;
        this.info = info;
    }

    @Override
    public long createNative(long webrtcEnvRef) {
        return SimulcastVideoEncoder.nativeCreateEncoder(webrtcEnvRef, this.primary, this.fallback, this.info);
    }

    @Override
    public boolean isHardwareEncoder() {
        return false;
    }
}

