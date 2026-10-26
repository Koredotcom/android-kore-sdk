/*
 * Decompiled with CFR 0.152.
 */
package org.webrtc;

import org.webrtc.VideoFrame;

public interface CapturerObserver {
    public void onCapturerStarted(boolean var1);

    public void onCapturerStopped();

    public void onFrameCaptured(VideoFrame var1);
}

