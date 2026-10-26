/*
 * Decompiled with CFR 0.152.
 */
package org.webrtc;

import org.webrtc.CalledByNative;
import org.webrtc.VideoDecoder;
import org.webrtc.VideoFrame;

class VideoDecoderWrapper {
    VideoDecoderWrapper() {
    }

    @CalledByNative
    static VideoDecoder.Callback createDecoderCallback(long nativeDecoder) {
        return (frame, decodeTimeMs, qp) -> VideoDecoderWrapper.nativeOnDecodedFrame(nativeDecoder, frame, decodeTimeMs, qp);
    }

    private static native void nativeOnDecodedFrame(long var0, VideoFrame var2, Integer var3, Integer var4);
}

