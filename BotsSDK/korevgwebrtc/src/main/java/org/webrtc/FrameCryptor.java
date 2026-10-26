/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  androidx.annotation.Nullable
 */
package org.webrtc;

import androidx.annotation.Nullable;
import org.webrtc.CalledByNative;
import org.webrtc.JniCommon;

public class FrameCryptor {
    private long nativeFrameCryptor;
    private long observerPtr;

    public long getNativeFrameCryptor() {
        return this.nativeFrameCryptor;
    }

    @CalledByNative
    public FrameCryptor(long nativeFrameCryptor) {
        this.nativeFrameCryptor = nativeFrameCryptor;
        this.observerPtr = 0L;
    }

    public void setEnabled(boolean enabled) {
        this.checkFrameCryptorExists();
        FrameCryptor.nativeSetEnabled(this.nativeFrameCryptor, enabled);
    }

    public boolean isEnabled() {
        this.checkFrameCryptorExists();
        return FrameCryptor.nativeIsEnabled(this.nativeFrameCryptor);
    }

    public int getKeyIndex() {
        this.checkFrameCryptorExists();
        return FrameCryptor.nativeGetKeyIndex(this.nativeFrameCryptor);
    }

    public void setKeyIndex(int index) {
        this.checkFrameCryptorExists();
        FrameCryptor.nativeSetKeyIndex(this.nativeFrameCryptor, index);
    }

    public void dispose() {
        this.checkFrameCryptorExists();
        FrameCryptor.nativeUnSetObserver(this.nativeFrameCryptor);
        JniCommon.nativeReleaseRef(this.nativeFrameCryptor);
        this.nativeFrameCryptor = 0L;
        if (this.observerPtr != 0L) {
            JniCommon.nativeReleaseRef(this.observerPtr);
            this.observerPtr = 0L;
        }
    }

    public void setObserver(@Nullable Observer observer) {
        this.checkFrameCryptorExists();
        long newPtr = FrameCryptor.nativeSetObserver(this.nativeFrameCryptor, observer);
        if (this.observerPtr != 0L) {
            JniCommon.nativeReleaseRef(this.observerPtr);
            this.observerPtr = 0L;
        }
        newPtr = this.observerPtr;
    }

    private void checkFrameCryptorExists() {
        if (this.nativeFrameCryptor == 0L) {
            throw new IllegalStateException("FrameCryptor has been disposed.");
        }
    }

    private static native void nativeSetEnabled(long var0, boolean var2);

    private static native boolean nativeIsEnabled(long var0);

    private static native void nativeSetKeyIndex(long var0, int var2);

    private static native int nativeGetKeyIndex(long var0);

    private static native long nativeSetObserver(long var0, Observer var2);

    private static native void nativeUnSetObserver(long var0);

    public static interface Observer {
        @CalledByNative(value="Observer")
        public void onFrameCryptionStateChanged(String var1, FrameCryptionState var2);
    }

    public static enum FrameCryptionState {
        NEW,
        OK,
        ENCRYPTIONFAILED,
        DECRYPTIONFAILED,
        MISSINGKEY,
        KEYRATCHETED,
        INTERNALERROR;


        @CalledByNative(value="FrameCryptionState")
        static FrameCryptionState fromNativeIndex(int nativeIndex) {
            return FrameCryptionState.values()[nativeIndex];
        }
    }
}

