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
import org.webrtc.LibaomAv1Decoder;
import org.webrtc.LibvpxVp8Decoder;
import org.webrtc.LibvpxVp9Decoder;
import org.webrtc.VideoCodecInfo;
import org.webrtc.VideoCodecMimeType;
import org.webrtc.VideoDecoder;
import org.webrtc.VideoDecoderFactory;

public class SoftwareVideoDecoderFactory
implements VideoDecoderFactory {
    @Override
    @Nullable
    public VideoDecoder createDecoder(VideoCodecInfo codecInfo) {
        String codecName = codecInfo.getName();
        if (codecName.equalsIgnoreCase(VideoCodecMimeType.VP8.toSdpCodecName())) {
            return new LibvpxVp8Decoder();
        }
        if (codecName.equalsIgnoreCase(VideoCodecMimeType.VP9.toSdpCodecName()) && LibvpxVp9Decoder.nativeIsSupported()) {
            return new LibvpxVp9Decoder();
        }
        if (codecName.equalsIgnoreCase(VideoCodecMimeType.AV1.toSdpCodecName()) && LibaomAv1Decoder.nativeIsSupported()) {
            return new LibaomAv1Decoder();
        }
        return null;
    }

    @Override
    public VideoCodecInfo[] getSupportedCodecs() {
        return SoftwareVideoDecoderFactory.supportedCodecs();
    }

    static VideoCodecInfo[] supportedCodecs() {
        ArrayList<VideoCodecInfo> codecs = new ArrayList<VideoCodecInfo>();
        codecs.add(new VideoCodecInfo(VideoCodecMimeType.VP8.toSdpCodecName(), new HashMap<String, String>()));
        if (LibvpxVp9Decoder.nativeIsSupported()) {
            codecs.add(new VideoCodecInfo(VideoCodecMimeType.VP9.toSdpCodecName(), new HashMap<String, String>()));
        }
        if (LibaomAv1Decoder.nativeIsSupported()) {
            codecs.add(new VideoCodecInfo(VideoCodecMimeType.AV1.toSdpCodecName(), new HashMap<String, String>()));
        }
        return codecs.toArray(new VideoCodecInfo[codecs.size()]);
    }
}

