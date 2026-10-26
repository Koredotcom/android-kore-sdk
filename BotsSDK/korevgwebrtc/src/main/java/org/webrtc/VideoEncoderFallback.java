/*
 * Decompiled with CFR 0.152.
 */
package org.webrtc;

import org.webrtc.VideoEncoder;
import org.webrtc.WrappedNativeVideoEncoder;

public class VideoEncoderFallback
extends WrappedNativeVideoEncoder {
    private final VideoEncoder fallback;
    private final VideoEncoder primary;

    public VideoEncoderFallback(VideoEncoder fallback, VideoEncoder primary) {
        this.fallback = fallback;
        this.primary = primary;
    }

    @Override
    public long createNative(long webrtcEnvRef) {
        return VideoEncoderFallback.nativeCreate(webrtcEnvRef, this.fallback, this.primary);
    }

    @Override
    public boolean isHardwareEncoder() {
        return this.primary.isHardwareEncoder();
    }

    private static native long nativeCreate(long var0, VideoEncoder var2, VideoEncoder var3);
}

