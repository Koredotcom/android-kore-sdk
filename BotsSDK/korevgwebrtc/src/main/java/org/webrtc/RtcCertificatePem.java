/*
 * Decompiled with CFR 0.152.
 */
package org.webrtc;

import org.webrtc.CalledByNative;
import org.webrtc.PeerConnection;

public class RtcCertificatePem {
    public final String privateKey;
    public final String certificate;
    private static final long DEFAULT_EXPIRY = 2592000L;

    @CalledByNative
    public RtcCertificatePem(String privateKey, String certificate) {
        this.privateKey = privateKey;
        this.certificate = certificate;
    }

    @CalledByNative
    String getPrivateKey() {
        return this.privateKey;
    }

    @CalledByNative
    String getCertificate() {
        return this.certificate;
    }

    public static RtcCertificatePem generateCertificate() {
        return RtcCertificatePem.nativeGenerateCertificate(PeerConnection.KeyType.ECDSA, 2592000L);
    }

    public static RtcCertificatePem generateCertificate(PeerConnection.KeyType keyType) {
        return RtcCertificatePem.nativeGenerateCertificate(keyType, 2592000L);
    }

    public static RtcCertificatePem generateCertificate(long expires) {
        return RtcCertificatePem.nativeGenerateCertificate(PeerConnection.KeyType.ECDSA, expires);
    }

    public static RtcCertificatePem generateCertificate(PeerConnection.KeyType keyType, long expires) {
        return RtcCertificatePem.nativeGenerateCertificate(keyType, expires);
    }

    private static native RtcCertificatePem nativeGenerateCertificate(PeerConnection.KeyType var0, long var1);
}

