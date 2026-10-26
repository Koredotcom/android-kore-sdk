/*
 * Decompiled with CFR 0.152.
 */
package org.webrtc;

import org.webrtc.WrappedNativeVideoEncoder;

public class LibvpxVp9Encoder
extends WrappedNativeVideoEncoder {
    @Override
    public long createNativeVideoEncoder() {
        return LibvpxVp9Encoder.nativeCreateEncoder();
    }

    static native long nativeCreateEncoder();

    @Override
    public boolean isHardwareEncoder() {
        return false;
    }

    static native boolean nativeIsSupported();
}

