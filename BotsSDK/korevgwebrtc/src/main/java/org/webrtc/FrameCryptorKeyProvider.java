/*
 * Decompiled with CFR 0.152.
 */
package org.webrtc;

import org.webrtc.CalledByNative;
import org.webrtc.JniCommon;

public class FrameCryptorKeyProvider {
    private long nativeKeyProvider;

    @CalledByNative
    public FrameCryptorKeyProvider(long nativeKeyProvider) {
        this.nativeKeyProvider = nativeKeyProvider;
    }

    public long getNativeKeyProvider() {
        return this.nativeKeyProvider;
    }

    public boolean setSharedKey(int index, byte[] key) {
        this.checkKeyProviderExists();
        return FrameCryptorKeyProvider.nativeSetSharedKey(this.nativeKeyProvider, index, key);
    }

    public byte[] ratchetSharedKey(int index) {
        this.checkKeyProviderExists();
        return FrameCryptorKeyProvider.nativeRatchetSharedKey(this.nativeKeyProvider, index);
    }

    public byte[] exportSharedKey(int index) {
        this.checkKeyProviderExists();
        return FrameCryptorKeyProvider.nativeExportSharedKey(this.nativeKeyProvider, index);
    }

    public boolean setKey(String participantId, int index, byte[] key) {
        this.checkKeyProviderExists();
        return FrameCryptorKeyProvider.nativeSetKey(this.nativeKeyProvider, participantId, index, key);
    }

    public byte[] ratchetKey(String participantId, int index) {
        this.checkKeyProviderExists();
        return FrameCryptorKeyProvider.nativeRatchetKey(this.nativeKeyProvider, participantId, index);
    }

    public byte[] exportKey(String participantId, int index) {
        this.checkKeyProviderExists();
        return FrameCryptorKeyProvider.nativeExportKey(this.nativeKeyProvider, participantId, index);
    }

    public void setSifTrailer(byte[] sifTrailer) {
        this.checkKeyProviderExists();
        FrameCryptorKeyProvider.nativeSetSifTrailer(this.nativeKeyProvider, sifTrailer);
    }

    public void dispose() {
        this.checkKeyProviderExists();
        JniCommon.nativeReleaseRef(this.nativeKeyProvider);
        this.nativeKeyProvider = 0L;
    }

    private void checkKeyProviderExists() {
        if (this.nativeKeyProvider == 0L) {
            throw new IllegalStateException("FrameCryptorKeyProvider has been disposed.");
        }
    }

    private static native boolean nativeSetSharedKey(long var0, int var2, byte[] var3);

    private static native byte[] nativeRatchetSharedKey(long var0, int var2);

    private static native byte[] nativeExportSharedKey(long var0, int var2);

    private static native boolean nativeSetKey(long var0, String var2, int var3, byte[] var4);

    private static native byte[] nativeRatchetKey(long var0, String var2, int var3);

    private static native byte[] nativeExportKey(long var0, String var2, int var3);

    private static native void nativeSetSifTrailer(long var0, byte[] var2);
}

