/*
 * Decompiled with CFR 0.152.
 */
package org.webrtc;

import org.webrtc.CalledByNative;
import org.webrtc.VideoFrame;

public interface VideoSink {
    @CalledByNative
    public void onFrame(VideoFrame var1);
}

