/*
 * Decompiled with CFR 0.152.
 */
package org.webrtc;

import java.util.List;
import org.webrtc.WrappedNativeVideoEncoder;

public class LibvpxVp9Encoder
extends WrappedNativeVideoEncoder {
    @Override
    public long createNative(long webrtcEnvRef) {
        return LibvpxVp9Encoder.nativeCreate(webrtcEnvRef);
    }

    static native long nativeCreate(long var0);

    @Override
    public boolean isHardwareEncoder() {
        return false;
    }

    static native boolean nativeIsSupported();

    static List<String> scalabilityModes() {
        return LibvpxVp9Encoder.nativeGetSupportedScalabilityModes();
    }

    static native List<String> nativeGetSupportedScalabilityModes();
}

