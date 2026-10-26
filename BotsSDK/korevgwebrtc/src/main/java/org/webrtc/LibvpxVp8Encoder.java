/*
 * Decompiled with CFR 0.152.
 */
package org.webrtc;

import org.webrtc.WrappedNativeVideoEncoder;

public class LibvpxVp8Encoder
extends WrappedNativeVideoEncoder {
    @Override
    public long createNative(long webrtcEnvRef) {
        return LibvpxVp8Encoder.nativeCreate(webrtcEnvRef);
    }

    static native long nativeCreate(long var0);

    @Override
    public boolean isHardwareEncoder() {
        return false;
    }
}

