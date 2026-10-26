/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  androidx.annotation.Nullable
 */
package org.webrtc;

import androidx.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import org.webrtc.SimulcastVideoEncoder;
import org.webrtc.VideoCodecInfo;
import org.webrtc.VideoEncoder;
import org.webrtc.VideoEncoderFactory;

public class SimulcastVideoEncoderFactory
implements VideoEncoderFactory {
    VideoEncoderFactory primary;
    VideoEncoderFactory fallback;

    public SimulcastVideoEncoderFactory(VideoEncoderFactory primary, VideoEncoderFactory fallback) {
        this.primary = primary;
        this.fallback = fallback;
    }

    @Override
    @Nullable
    public VideoEncoder createEncoder(VideoCodecInfo info) {
        return new SimulcastVideoEncoder(this.primary, this.fallback, info);
    }

    @Override
    public VideoCodecInfo[] getSupportedCodecs() {
        ArrayList<VideoCodecInfo> codecs = new ArrayList<VideoCodecInfo>();
        codecs.addAll(Arrays.asList(this.primary.getSupportedCodecs()));
        codecs.addAll(Arrays.asList(this.fallback.getSupportedCodecs()));
        return codecs.toArray(new VideoCodecInfo[codecs.size()]);
    }
}

