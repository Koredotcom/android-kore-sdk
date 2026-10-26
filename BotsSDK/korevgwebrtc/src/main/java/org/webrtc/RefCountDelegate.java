/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  androidx.annotation.Nullable
 */
package org.webrtc;

import androidx.annotation.Nullable;
import java.util.concurrent.atomic.AtomicInteger;
import org.webrtc.RefCounted;

class RefCountDelegate
implements RefCounted {
    private final AtomicInteger refCount = new AtomicInteger(1);
    @Nullable
    private final Runnable releaseCallback;

    public RefCountDelegate(@Nullable Runnable releaseCallback) {
        this.releaseCallback = releaseCallback;
    }

    @Override
    public void retain() {
        int updated_count = this.refCount.incrementAndGet();
        if (updated_count < 2) {
            throw new IllegalStateException("retain() called on an object with refcount < 1");
        }
    }

    @Override
    public void release() {
        int updated_count = this.refCount.decrementAndGet();
        if (updated_count < 0) {
            throw new IllegalStateException("release() called on an object with refcount < 1");
        }
        if (updated_count == 0 && this.releaseCallback != null) {
            this.releaseCallback.run();
        }
    }

    boolean safeRetain() {
        int currentRefCount = this.refCount.get();
        while (currentRefCount != 0) {
            if (this.refCount.weakCompareAndSet(currentRefCount, currentRefCount + 1)) {
                return true;
            }
            currentRefCount = this.refCount.get();
        }
        return false;
    }
}

