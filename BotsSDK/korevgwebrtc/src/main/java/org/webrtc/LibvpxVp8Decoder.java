/*
 * Decompiled with CFR 0.152.
 */
package org.webrtc;

import org.webrtc.WrappedNativeVideoDecoder;

public class LibvpxVp8Decoder
extends WrappedNativeVideoDecoder {
    @Override
    public long createNativeVideoDecoder() {
        return LibvpxVp8Decoder.nativeCreateDecoder();
    }

    static native long nativeCreateDecoder();
}

