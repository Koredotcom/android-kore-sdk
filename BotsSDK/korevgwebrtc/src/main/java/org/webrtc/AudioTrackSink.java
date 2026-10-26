/*
 * Decompiled with CFR 0.152.
 */
package org.webrtc;

import java.nio.ByteBuffer;
import org.webrtc.CalledByNative;

public interface AudioTrackSink {
    @CalledByNative
    public void onData(ByteBuffer var1, int var2, int var3, int var4, int var5, long var6);
}

