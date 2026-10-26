/*
 * Decompiled with CFR 0.152.
 */
package org.webrtc;

import org.webrtc.EncodedImage;
import org.webrtc.VideoCodecStatus;
import org.webrtc.VideoDecoder;

public abstract class WrappedNativeVideoDecoder
implements VideoDecoder {
    @Override
    public abstract long createNative(long var1);

    @Override
    public final VideoCodecStatus initDecode(VideoDecoder.Settings settings, VideoDecoder.Callback decodeCallback) {
        throw new UnsupportedOperationException("Not implemented.");
    }

    @Override
    public final VideoCodecStatus release() {
        throw new UnsupportedOperationException("Not implemented.");
    }

    @Override
    public final VideoCodecStatus decode(EncodedImage frame, VideoDecoder.DecodeInfo info) {
        throw new UnsupportedOperationException("Not implemented.");
    }

    @Override
    public final String getImplementationName() {
        throw new UnsupportedOperationException("Not implemented.");
    }
}

