/*
 * Decompiled with CFR 0.152.
 */
package org.webrtc;

import org.webrtc.CalledByNative;
import org.webrtc.StatsReport;

public interface StatsObserver {
    @CalledByNative
    public void onComplete(StatsReport[] var1);
}

