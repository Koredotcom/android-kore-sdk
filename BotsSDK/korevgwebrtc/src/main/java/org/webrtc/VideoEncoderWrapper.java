/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  androidx.annotation.Nullable
 */
package org.webrtc;

import androidx.annotation.Nullable;
import org.webrtc.CalledByNative;
import org.webrtc.EncodedImage;
import org.webrtc.VideoEncoder;

class VideoEncoderWrapper {
    VideoEncoderWrapper() {
    }

    @CalledByNative
    static boolean getScalingSettingsOn(VideoEncoder.ScalingSettings scalingSettings) {
        return scalingSettings.on;
    }

    @Nullable
    @CalledByNative
    static Integer getScalingSettingsLow(VideoEncoder.ScalingSettings scalingSettings) {
        return scalingSettings.low;
    }

    @Nullable
    @CalledByNative
    static Integer getScalingSettingsHigh(VideoEncoder.ScalingSettings scalingSettings) {
        return scalingSettings.high;
    }

    @CalledByNative
    static VideoEncoder.Callback createEncoderCallback(long nativeEncoder) {
        return (frame, info) -> VideoEncoderWrapper.nativeOnEncodedFrame(nativeEncoder, frame);
    }

    private static native void nativeOnEncodedFrame(long var0, EncodedImage var2);
}

