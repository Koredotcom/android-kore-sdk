/*
 * Decompiled with CFR 0.152.
 */
package org.webrtc;

import org.webrtc.VideoDecoder;
import org.webrtc.WrappedNativeVideoDecoder;

public class VideoDecoderFallback
extends WrappedNativeVideoDecoder {
    private final VideoDecoder fallback;
    private final VideoDecoder primary;

    public VideoDecoderFallback(VideoDecoder fallback, VideoDecoder primary) {
        this.fallback = fallback;
        this.primary = primary;
    }

    @Override
    public long createNative(long webrtcEnvRef) {
        return VideoDecoderFallback.nativeCreate(webrtcEnvRef, this.fallback, this.primary);
    }

    private static native long nativeCreate(long var0, VideoDecoder var2, VideoDecoder var3);
}

