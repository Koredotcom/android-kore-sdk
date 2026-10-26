/*
 * Decompiled with CFR 0.152.
 */
package org.webrtc;

import org.webrtc.WrappedNativeVideoDecoder;

public class LibvpxVp9Decoder
extends WrappedNativeVideoDecoder {
    @Override
    public long createNativeVideoDecoder() {
        return LibvpxVp9Decoder.nativeCreateDecoder();
    }

    static native long nativeCreateDecoder();

    static native boolean nativeIsSupported();
}

