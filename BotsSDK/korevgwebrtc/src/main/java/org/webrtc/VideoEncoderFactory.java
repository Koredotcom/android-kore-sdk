/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  androidx.annotation.Nullable
 */
package org.webrtc;

import androidx.annotation.Nullable;
import org.webrtc.CalledByNative;
import org.webrtc.VideoCodecInfo;
import org.webrtc.VideoEncoder;

public interface VideoEncoderFactory {
    @Nullable
    @CalledByNative
    public VideoEncoder createEncoder(VideoCodecInfo var1);

    @CalledByNative
    public VideoCodecInfo[] getSupportedCodecs();

    @CalledByNative
    default public VideoCodecInfo[] getImplementations() {
        return this.getSupportedCodecs();
    }

    @CalledByNative
    default public VideoEncoderSelector getEncoderSelector() {
        return null;
    }

    public static interface VideoEncoderSelector {
        @CalledByNative(value="VideoEncoderSelector")
        public void onCurrentEncoder(VideoCodecInfo var1);

        @Nullable
        @CalledByNative(value="VideoEncoderSelector")
        public VideoCodecInfo onAvailableBitrate(int var1);

        @Nullable
        @CalledByNative(value="VideoEncoderSelector")
        default public VideoCodecInfo onResolutionChange(int widht, int height) {
            return null;
        }

        @Nullable
        @CalledByNative(value="VideoEncoderSelector")
        public VideoCodecInfo onEncoderBroken();
    }
}

