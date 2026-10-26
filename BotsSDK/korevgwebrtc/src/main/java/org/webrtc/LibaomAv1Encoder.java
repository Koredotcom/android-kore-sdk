/*
 * Decompiled with CFR 0.152.
 */
package org.webrtc;

import java.util.List;
import org.webrtc.WrappedNativeVideoEncoder;

public class LibaomAv1Encoder
extends WrappedNativeVideoEncoder {
    @Override
    public long createNative(long webrtcEnvRef) {
        return LibaomAv1Encoder.nativeCreate(webrtcEnvRef);
    }

    static native long nativeCreate(long var0);

    @Override
    public boolean isHardwareEncoder() {
        return false;
    }

    static List<String> scalabilityModes() {
        return LibaomAv1Encoder.nativeGetSupportedScalabilityModes();
    }

    static native List<String> nativeGetSupportedScalabilityModes();
}

