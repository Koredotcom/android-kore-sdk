/*
 * Decompiled with CFR 0.152.
 */
package org.webrtc;

import org.webrtc.CalledByNative;
import org.webrtc.SessionDescription;

public interface SdpObserver {
    @CalledByNative
    public void onCreateSuccess(SessionDescription var1);

    @CalledByNative
    public void onSetSuccess();

    @CalledByNative
    public void onCreateFailure(String var1);

    @CalledByNative
    public void onSetFailure(String var1);
}

