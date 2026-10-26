/*
 * Decompiled with CFR 0.152.
 */
package org.webrtc;

import org.webrtc.VideoCodecStatus;
import org.webrtc.VideoEncoder;
import org.webrtc.VideoFrame;

public abstract class WrappedNativeVideoEncoder
implements VideoEncoder {
    @Override
    public abstract long createNative(long var1);

    @Override
    public abstract boolean isHardwareEncoder();

    @Override
    public final VideoCodecStatus initEncode(VideoEncoder.Settings settings, VideoEncoder.Callback encodeCallback) {
        throw new UnsupportedOperationException("Not implemented.");
    }

    @Override
    public final VideoCodecStatus release() {
        throw new UnsupportedOperationException("Not implemented.");
    }

    @Override
    public final VideoCodecStatus encode(VideoFrame frame, VideoEncoder.EncodeInfo info) {
        throw new UnsupportedOperationException("Not implemented.");
    }

    @Override
    public final VideoCodecStatus setRateAllocation(VideoEncoder.BitrateAllocation allocation, int framerate) {
        throw new UnsupportedOperationException("Not implemented.");
    }

    @Override
    public final VideoEncoder.ScalingSettings getScalingSettings() {
        throw new UnsupportedOperationException("Not implemented.");
    }

    @Override
    public final String getImplementationName() {
        throw new UnsupportedOperationException("Not implemented.");
    }
}

