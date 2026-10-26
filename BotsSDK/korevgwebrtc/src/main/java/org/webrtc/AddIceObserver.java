/*
 * Decompiled with CFR 0.152.
 */
package org.webrtc;

import org.webrtc.CalledByNative;

public interface AddIceObserver {
    @CalledByNative
    public void onAddSuccess();

    @CalledByNative
    public void onAddFailure(String var1);
}

