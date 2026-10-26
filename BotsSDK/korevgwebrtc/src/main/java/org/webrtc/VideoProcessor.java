/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  androidx.annotation.Nullable
 */
package org.webrtc;

import androidx.annotation.Nullable;
import org.webrtc.CapturerObserver;
import org.webrtc.VideoFrame;
import org.webrtc.VideoSink;

public interface VideoProcessor
extends CapturerObserver {
    default public void onFrameCaptured(VideoFrame frame, FrameAdaptationParameters parameters) {
        VideoFrame adaptedFrame = VideoProcessor.applyFrameAdaptationParameters(frame, parameters);
        if (adaptedFrame != null) {
            this.onFrameCaptured(adaptedFrame);
            adaptedFrame.release();
        }
    }

    public void setSink(@Nullable VideoSink var1);

    @Nullable
    public static VideoFrame applyFrameAdaptationParameters(VideoFrame frame, FrameAdaptationParameters parameters) {
        if (parameters.drop) {
            return null;
        }
        VideoFrame.Buffer adaptedBuffer = frame.getBuffer().cropAndScale(parameters.cropX, parameters.cropY, parameters.cropWidth, parameters.cropHeight, parameters.scaleWidth, parameters.scaleHeight);
        return new VideoFrame(adaptedBuffer, frame.getRotation(), parameters.timestampNs);
    }

    public static class FrameAdaptationParameters {
        public final int cropX;
        public final int cropY;
        public final int cropWidth;
        public final int cropHeight;
        public final int scaleWidth;
        public final int scaleHeight;
        public final long timestampNs;
        public final boolean drop;

        public FrameAdaptationParameters(int cropX, int cropY, int cropWidth, int cropHeight, int scaleWidth, int scaleHeight, long timestampNs, boolean drop) {
            this.cropX = cropX;
            this.cropY = cropY;
            this.cropWidth = cropWidth;
            this.cropHeight = cropHeight;
            this.scaleWidth = scaleWidth;
            this.scaleHeight = scaleHeight;
            this.timestampNs = timestampNs;
            this.drop = drop;
        }
    }
}

