/*
 * Decompiled with CFR 0.152.
 */
package com.korevg.mv.webrtcsdk.session;

public class DTMFOptions {
    public DTMFMethod dtmfMethod = DTMFMethod.WEBRTC;
    public int duration = 100;
    public int intervalGap = 70;

    public static enum DTMFMethod {
        SIP_INFO,
        WEBRTC;

    }
}

