/*
 * Decompiled with CFR 0.152.
 */
package org.webrtc;

import org.webrtc.CalledByNative;

public interface SSLCertificateVerifier {
    @CalledByNative
    public boolean verify(byte[] var1);
}

