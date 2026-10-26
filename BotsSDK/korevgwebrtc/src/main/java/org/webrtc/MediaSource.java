/*
 * Decompiled with CFR 0.152.
 */
package org.webrtc;

import org.webrtc.CalledByNative;
import org.webrtc.JniCommon;
import org.webrtc.RefCountDelegate;

public class MediaSource {
    private long nativeSource;
    private final RefCountDelegate refCountDelegate = new RefCountDelegate(() -> JniCommon.nativeReleaseRef(nativeSource));

    public MediaSource(long nativeSource) {
        this.nativeSource = nativeSource;
    }

    public State state() {
        this.checkMediaSourceExists();
        return MediaSource.nativeGetState(this.nativeSource);
    }

    public void dispose() {
        this.checkMediaSourceExists();
        this.refCountDelegate.release();
        this.nativeSource = 0L;
    }

    protected long getNativeMediaSource() {
        this.checkMediaSourceExists();
        return this.nativeSource;
    }

    void runWithReference(Runnable runnable) {
        if (this.refCountDelegate.safeRetain()) {
            try {
                runnable.run();
            }
            finally {
                this.refCountDelegate.release();
            }
        }
    }

    private void checkMediaSourceExists() {
        if (this.nativeSource == 0L) {
            throw new IllegalStateException("MediaSource has been disposed.");
        }
    }

    private static native State nativeGetState(long var0);

    public static enum State {
        INITIALIZING,
        LIVE,
        ENDED,
        MUTED;


        @CalledByNative(value="State")
        static State fromNativeIndex(int nativeIndex) {
            return State.values()[nativeIndex];
        }
    }
}

