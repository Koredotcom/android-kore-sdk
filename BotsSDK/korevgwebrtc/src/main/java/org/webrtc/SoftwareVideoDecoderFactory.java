/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  androidx.annotation.Nullable
 */
package org.webrtc;

import androidx.annotation.Nullable;
import java.util.List;
import org.webrtc.Logging;
import org.webrtc.VideoCodecInfo;
import org.webrtc.VideoDecoder;
import org.webrtc.VideoDecoderFactory;
import org.webrtc.WrappedNativeVideoDecoder;

public class SoftwareVideoDecoderFactory
implements VideoDecoderFactory {
    private static final String TAG = "SoftwareVideoDecoderFactory";
    private final long nativeFactory = SoftwareVideoDecoderFactory.nativeCreateFactory();

    @Override
    @Nullable
    public VideoDecoder createDecoder(final VideoCodecInfo info) {
        if (!SoftwareVideoDecoderFactory.nativeIsSupported(this.nativeFactory, info)) {
            Logging.w(TAG, "Trying to create decoder for unsupported format. " + info);
            return null;
        }
        return new WrappedNativeVideoDecoder(){

            @Override
            public long createNative(long webrtcEnvRef) {
                return SoftwareVideoDecoderFactory.nativeCreate(SoftwareVideoDecoderFactory.this.nativeFactory, webrtcEnvRef, info);
            }
        };
    }

    @Override
    public VideoCodecInfo[] getSupportedCodecs() {
        return SoftwareVideoDecoderFactory.nativeGetSupportedCodecs(this.nativeFactory).toArray(new VideoCodecInfo[0]);
    }

    private static native long nativeCreateFactory();

    private static native boolean nativeIsSupported(long var0, VideoCodecInfo var2);

    private static native long nativeCreate(long var0, long var2, VideoCodecInfo var4);

    private static native List<VideoCodecInfo> nativeGetSupportedCodecs(long var0);
}

