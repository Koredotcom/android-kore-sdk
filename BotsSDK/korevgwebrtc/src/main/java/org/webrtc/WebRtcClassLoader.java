/*
 * Decompiled with CFR 0.152.
 */
package org.webrtc;

import org.webrtc.CalledByNative;

class WebRtcClassLoader {
    WebRtcClassLoader() {
    }

    @CalledByNative
    static Object getClassLoader() {
        ClassLoader loader = WebRtcClassLoader.class.getClassLoader();
        if (loader == null) {
            throw new RuntimeException("Failed to get WebRTC class loader.");
        }
        return loader;
    }
}

