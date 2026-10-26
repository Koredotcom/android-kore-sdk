/*
 * Decompiled with CFR 0.152.
 */
package org.webrtc;

import org.webrtc.CalledByNative;
import org.webrtc.EncodedImage;
import org.webrtc.VideoCodecStatus;
import org.webrtc.VideoFrame;

public interface VideoDecoder {
    @CalledByNative
    default public long createNative(long webrtcEnvRef) {
        return 0L;
    }

    @CalledByNative
    public VideoCodecStatus initDecode(Settings var1, Callback var2);

    @CalledByNative
    public VideoCodecStatus release();

    @CalledByNative
    public VideoCodecStatus decode(EncodedImage var1, DecodeInfo var2);

    @CalledByNative
    public String getImplementationName();

    public static interface Callback {
        public void onDecodedFrame(VideoFrame var1, Integer var2, Integer var3);
    }

    public static class DecodeInfo {
        public final boolean isMissingFrames;
        public final long renderTimeMs;

        public DecodeInfo(boolean isMissingFrames, long renderTimeMs) {
            this.isMissingFrames = isMissingFrames;
            this.renderTimeMs = renderTimeMs;
        }
    }

    public static class Settings {
        public final int numberOfCores;
        public final int width;
        public final int height;

        @CalledByNative(value="Settings")
        public Settings(int numberOfCores, int width, int height) {
            this.numberOfCores = numberOfCores;
            this.width = width;
            this.height = height;
        }
    }
}

