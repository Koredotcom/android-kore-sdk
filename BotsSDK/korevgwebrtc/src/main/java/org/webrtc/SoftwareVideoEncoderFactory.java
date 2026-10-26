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
import org.webrtc.VideoEncoder;
import org.webrtc.VideoEncoderFactory;
import org.webrtc.WrappedNativeVideoEncoder;

public class SoftwareVideoEncoderFactory
implements VideoEncoderFactory {
    private static final String TAG = "SoftwareVideoEncoderFactory";
    private final long nativeFactory = SoftwareVideoEncoderFactory.nativeCreateFactory();

    @Override
    @Nullable
    public VideoEncoder createEncoder(final VideoCodecInfo info) {
        if (!SoftwareVideoEncoderFactory.nativeIsSupported(this.nativeFactory, info)) {
            Logging.w(TAG, "Trying to create encoder for unsupported format. " + info);
            return null;
        }
        return new WrappedNativeVideoEncoder(){

            @Override
            public long createNative(long webrtcEnvRef) {
                return SoftwareVideoEncoderFactory.nativeCreate(SoftwareVideoEncoderFactory.this.nativeFactory, webrtcEnvRef, info);
            }

            @Override
            public boolean isHardwareEncoder() {
                return false;
            }
        };
    }

    @Override
    public VideoCodecInfo[] getSupportedCodecs() {
        return SoftwareVideoEncoderFactory.nativeGetSupportedCodecs(this.nativeFactory).toArray(new VideoCodecInfo[0]);
    }

    private static native long nativeCreateFactory();

    private static native boolean nativeIsSupported(long var0, VideoCodecInfo var2);

    private static native long nativeCreate(long var0, long var2, VideoCodecInfo var4);

    private static native List<VideoCodecInfo> nativeGetSupportedCodecs(long var0);
}

