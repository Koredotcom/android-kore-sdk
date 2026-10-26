/*
 * Decompiled with CFR 0.152.
 */
package com.korevg.mv.webrtcsdk.webrtc.listeners;

public interface WebRTCCallEventListener {
    public void cameraSwitched(boolean var1);

    public void onIceGatheringComlete();

    public void callFailed(int var1);

    public void mediaFailed();
}

