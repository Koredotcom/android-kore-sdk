/*
 * Decompiled with CFR 0.152.
 */
package org.webrtc;

import org.webrtc.CalledByNative;

public interface RefCounted {
    @CalledByNative
    public void retain();

    @CalledByNative
    public void release();
}

