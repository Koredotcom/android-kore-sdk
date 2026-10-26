/*
 * Decompiled with CFR 0.152.
 */
package org.webrtc;

import java.util.List;
import org.webrtc.CameraEnumerationAndroid;
import org.webrtc.CameraVideoCapturer;

public interface CameraEnumerator {
    public String[] getDeviceNames();

    public boolean isFrontFacing(String var1);

    public boolean isBackFacing(String var1);

    public List<CameraEnumerationAndroid.CaptureFormat> getSupportedFormats(String var1);

    public CameraVideoCapturer createCapturer(String var1, CameraVideoCapturer.CameraEventsHandler var2);
}

