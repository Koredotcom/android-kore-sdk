/*
 * Decompiled with CFR 0.152.
 */
package org.webrtc;

import org.webrtc.JniCommon;

public class DtmfSender {
    private long nativeDtmfSender;

    public DtmfSender(long nativeDtmfSender) {
        this.nativeDtmfSender = nativeDtmfSender;
    }

    public boolean canInsertDtmf() {
        this.checkDtmfSenderExists();
        return DtmfSender.nativeCanInsertDtmf(this.nativeDtmfSender);
    }

    public boolean insertDtmf(String tones, int duration, int interToneGap) {
        this.checkDtmfSenderExists();
        return DtmfSender.nativeInsertDtmf(this.nativeDtmfSender, tones, duration, interToneGap);
    }

    public String tones() {
        this.checkDtmfSenderExists();
        return DtmfSender.nativeTones(this.nativeDtmfSender);
    }

    public int duration() {
        this.checkDtmfSenderExists();
        return DtmfSender.nativeDuration(this.nativeDtmfSender);
    }

    public int interToneGap() {
        this.checkDtmfSenderExists();
        return DtmfSender.nativeInterToneGap(this.nativeDtmfSender);
    }

    public void dispose() {
        this.checkDtmfSenderExists();
        JniCommon.nativeReleaseRef(this.nativeDtmfSender);
        this.nativeDtmfSender = 0L;
    }

    private void checkDtmfSenderExists() {
        if (this.nativeDtmfSender == 0L) {
            throw new IllegalStateException("DtmfSender has been disposed.");
        }
    }

    private static native boolean nativeCanInsertDtmf(long var0);

    private static native boolean nativeInsertDtmf(long var0, String var2, int var3, int var4);

    private static native String nativeTones(long var0);

    private static native int nativeDuration(long var0);

    private static native int nativeInterToneGap(long var0);
}

