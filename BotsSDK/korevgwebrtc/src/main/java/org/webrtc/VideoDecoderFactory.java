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
import org.webrtc.VideoDecoder;

public interface VideoDecoderFactory {
    @Nullable
    @CalledByNative
    public VideoDecoder createDecoder(VideoCodecInfo var1);

    @CalledByNative
    default public VideoCodecInfo[] getSupportedCodecs() {
        return new VideoCodecInfo[0];
    }
}

