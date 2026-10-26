/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.Context
 */
package org.webrtc;

import android.content.Context;
import org.webrtc.CapturerObserver;
import org.webrtc.SurfaceTextureHelper;

public interface VideoCapturer {
    public void initialize(SurfaceTextureHelper var1, Context var2, CapturerObserver var3);

    public void startCapture(int var1, int var2, int var3);

    public void stopCapture() throws InterruptedException;

    public void changeCaptureFormat(int var1, int var2, int var3);

    public void dispose();

    public boolean isScreencast();
}

