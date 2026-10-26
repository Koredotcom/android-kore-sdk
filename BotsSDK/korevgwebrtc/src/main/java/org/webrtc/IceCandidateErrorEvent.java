/*
 * Decompiled with CFR 0.152.
 */
package org.webrtc;

import org.webrtc.CalledByNative;

public final class IceCandidateErrorEvent {
    public final String address;
    public final int port;
    public final String url;
    public final int errorCode;
    public final String errorText;

    @CalledByNative
    public IceCandidateErrorEvent(String address, int port, String url, int errorCode, String errorText) {
        this.address = address;
        this.port = port;
        this.url = url;
        this.errorCode = errorCode;
        this.errorText = errorText;
    }
}

