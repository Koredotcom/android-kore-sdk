/*
 * Decompiled with CFR 0.152.
 */
package org.webrtc;

import org.webrtc.CalledByNative;

public class TurnCustomizer {
    private long nativeTurnCustomizer;

    public TurnCustomizer(long nativeTurnCustomizer) {
        this.nativeTurnCustomizer = nativeTurnCustomizer;
    }

    public void dispose() {
        this.checkTurnCustomizerExists();
        TurnCustomizer.nativeFreeTurnCustomizer(this.nativeTurnCustomizer);
        this.nativeTurnCustomizer = 0L;
    }

    private static native void nativeFreeTurnCustomizer(long var0);

    @CalledByNative
    long getNativeTurnCustomizer() {
        this.checkTurnCustomizerExists();
        return this.nativeTurnCustomizer;
    }

    private void checkTurnCustomizerExists() {
        if (this.nativeTurnCustomizer == 0L) {
            throw new IllegalStateException("TurnCustomizer has been disposed.");
        }
    }
}

