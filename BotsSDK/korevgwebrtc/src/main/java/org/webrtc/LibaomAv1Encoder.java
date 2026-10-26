/*
 * Decompiled with CFR 0.152.
 */
package org.webrtc;

import org.webrtc.WrappedNativeVideoEncoder;

public class LibaomAv1Encoder
extends WrappedNativeVideoEncoder {
    @Override
    public long createNativeVideoEncoder() {
        return LibaomAv1Encoder.nativeCreateEncoder();
    }

    static native long nativeCreateEncoder();

    @Override
    public boolean isHardwareEncoder() {
        return false;
    }

    static native boolean nativeIsSupported();
}

