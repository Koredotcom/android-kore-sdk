/*
 * Decompiled with CFR 0.152.
 */
package com.korevg.mv.webrtcsdk.audio;

import com.korevg.mv.webrtcsdk.audio.WebRTCAudioManager;
import java.util.List;

public interface WebRTCAudioRouteListener {
    public void audioRoutesChanged(List<WebRTCAudioManager.AudioRoute> var1);

    public void currentAudioRouteChanged(WebRTCAudioManager.AudioRoute var1);
}

