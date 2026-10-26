/*
 * Decompiled with CFR 0.152.
 */
package org.webrtc;

import org.webrtc.WrappedNativeVideoEncoder;

public class LibvpxVp8Encoder
extends WrappedNativeVideoEncoder {
    @Override
    public long createNativeVideoEncoder() {
        return LibvpxVp8Encoder.nativeCreateEncoder();
    }

    static native long nativeCreateEncoder();

    @Override
    public boolean isHardwareEncoder() {
        return false;
    }
}

