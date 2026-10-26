/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  androidx.annotation.Nullable
 */
package org.webrtc;

import androidx.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashMap;
import org.webrtc.LibaomAv1Encoder;
import org.webrtc.LibvpxVp8Encoder;
import org.webrtc.LibvpxVp9Encoder;
import org.webrtc.VideoCodecInfo;
import org.webrtc.VideoCodecMimeType;
import org.webrtc.VideoEncoder;
import org.webrtc.VideoEncoderFactory;

public class SoftwareVideoEncoderFactory
implements VideoEncoderFactory {
    @Override
    @Nullable
    public VideoEncoder createEncoder(VideoCodecInfo codecInfo) {
        String codecName = codecInfo.getName();
        if (codecName.equalsIgnoreCase(VideoCodecMimeType.VP8.toSdpCodecName())) {
            return new LibvpxVp8Encoder();
        }
        if (codecName.equalsIgnoreCase(VideoCodecMimeType.VP9.toSdpCodecName()) && LibvpxVp9Encoder.nativeIsSupported()) {
            return new LibvpxVp9Encoder();
        }
        if (codecName.equalsIgnoreCase(VideoCodecMimeType.AV1.toSdpCodecName()) && LibaomAv1Encoder.nativeIsSupported()) {
            return new LibaomAv1Encoder();
        }
        return null;
    }

    @Override
    public VideoCodecInfo[] getSupportedCodecs() {
        return SoftwareVideoEncoderFactory.supportedCodecs();
    }

    static VideoCodecInfo[] supportedCodecs() {
        ArrayList<VideoCodecInfo> codecs = new ArrayList<VideoCodecInfo>();
        codecs.add(new VideoCodecInfo(VideoCodecMimeType.VP8.toSdpCodecName(), new HashMap<String, String>()));
        if (LibvpxVp9Encoder.nativeIsSupported()) {
            codecs.add(new VideoCodecInfo(VideoCodecMimeType.VP9.toSdpCodecName(), new HashMap<String, String>()));
        }
        if (LibaomAv1Encoder.nativeIsSupported()) {
            codecs.add(new VideoCodecInfo(VideoCodecMimeType.AV1.toSdpCodecName(), new HashMap<String, String>()));
        }
        return codecs.toArray(new VideoCodecInfo[codecs.size()]);
    }
}

