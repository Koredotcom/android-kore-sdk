/*
 * Decompiled with CFR 0.152.
 */
package org.webrtc;

import org.webrtc.WrappedNativeVideoDecoder;

public class LibaomAv1Decoder
extends WrappedNativeVideoDecoder {
    @Override
    public long createNativeVideoDecoder() {
        return LibaomAv1Decoder.nativeCreateDecoder();
    }

    static native long nativeCreateDecoder();

    static native boolean nativeIsSupported();
}

