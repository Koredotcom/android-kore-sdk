/*
 * Decompiled with CFR 0.152.
 */
package org.webrtc;

import org.webrtc.CalledByNative;
import org.webrtc.RTCStatsReport;

public interface RTCStatsCollectorCallback {
    @CalledByNative
    public void onStatsDelivered(RTCStatsReport var1);
}

