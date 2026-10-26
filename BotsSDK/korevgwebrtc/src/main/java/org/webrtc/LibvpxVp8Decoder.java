/*
 * Decompiled with CFR 0.152.
 */
package org.webrtc;

import org.webrtc.WrappedNativeVideoDecoder;

public class LibvpxVp8Decoder
extends WrappedNativeVideoDecoder {
    @Override
    public long createNative(long webrtcEnvRef) {
        return LibvpxVp8Decoder.nativeCreateDecoder(webrtcEnvRef);
    }

    static native long nativeCreateDecoder(long var0);
}

