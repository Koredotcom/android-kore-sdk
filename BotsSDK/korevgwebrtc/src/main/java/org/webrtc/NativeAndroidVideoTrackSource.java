/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  androidx.annotation.Nullable
 */
package org.webrtc;

import androidx.annotation.Nullable;
import org.webrtc.CalledByNative;
import org.webrtc.VideoFrame;
import org.webrtc.VideoProcessor;
import org.webrtc.VideoSource;

class NativeAndroidVideoTrackSource {
    private final long nativeAndroidVideoTrackSource;

    public NativeAndroidVideoTrackSource(long nativeAndroidVideoTrackSource) {
        this.nativeAndroidVideoTrackSource = nativeAndroidVideoTrackSource;
    }

    public void setState(boolean isLive) {
        NativeAndroidVideoTrackSource.nativeSetState(this.nativeAndroidVideoTrackSource, isLive);
    }

    @Nullable
    public VideoProcessor.FrameAdaptationParameters adaptFrame(VideoFrame frame) {
        return NativeAndroidVideoTrackSource.nativeAdaptFrame(this.nativeAndroidVideoTrackSource, frame.getBuffer().getWidth(), frame.getBuffer().getHeight(), frame.getRotation(), frame.getTimestampNs());
    }

    public void onFrameCaptured(VideoFrame frame) {
        NativeAndroidVideoTrackSource.nativeOnFrameCaptured(this.nativeAndroidVideoTrackSource, frame.getRotation(), frame.getTimestampNs(), frame.getBuffer());
    }

    public void adaptOutputFormat(VideoSource.AspectRatio targetLandscapeAspectRatio, @Nullable Integer maxLandscapePixelCount, VideoSource.AspectRatio targetPortraitAspectRatio, @Nullable Integer maxPortraitPixelCount, @Nullable Integer maxFps) {
        NativeAndroidVideoTrackSource.nativeAdaptOutputFormat(this.nativeAndroidVideoTrackSource, targetLandscapeAspectRatio.width, targetLandscapeAspectRatio.height, maxLandscapePixelCount, targetPortraitAspectRatio.width, targetPortraitAspectRatio.height, maxPortraitPixelCount, maxFps);
    }

    public void setIsScreencast(boolean isScreencast) {
        NativeAndroidVideoTrackSource.nativeSetIsScreencast(this.nativeAndroidVideoTrackSource, isScreencast);
    }

    @CalledByNative
    static VideoProcessor.FrameAdaptationParameters createFrameAdaptationParameters(int cropX, int cropY, int cropWidth, int cropHeight, int scaleWidth, int scaleHeight, long timestampNs, boolean drop) {
        return new VideoProcessor.FrameAdaptationParameters(cropX, cropY, cropWidth, cropHeight, scaleWidth, scaleHeight, timestampNs, drop);
    }

    private static native void nativeSetIsScreencast(long var0, boolean var2);

    private static native void nativeSetState(long var0, boolean var2);

    private static native void nativeAdaptOutputFormat(long var0, int var2, int var3, @Nullable Integer var4, int var5, int var6, @Nullable Integer var7, @Nullable Integer var8);

    @Nullable
    private static native VideoProcessor.FrameAdaptationParameters nativeAdaptFrame(long var0, int var2, int var3, int var4, long var5);

    private static native void nativeOnFrameCaptured(long var0, int var2, long var3, VideoFrame.Buffer var5);
}

