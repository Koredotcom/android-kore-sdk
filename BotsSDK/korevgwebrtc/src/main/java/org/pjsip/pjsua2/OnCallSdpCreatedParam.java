/*
 * Decompiled with CFR 0.152.
 */
package org.pjsip.pjsua2;

import org.pjsip.pjsua2.SdpSession;
import org.pjsip.pjsua2.pjsua2JNI;

public class OnCallSdpCreatedParam {
    private transient long swigCPtr;
    protected transient boolean swigCMemOwn;

    protected OnCallSdpCreatedParam(long cPtr, boolean cMemoryOwn) {
        this.swigCMemOwn = cMemoryOwn;
        this.swigCPtr = cPtr;
    }

    protected static long getCPtr(OnCallSdpCreatedParam obj) {
        return obj == null ? 0L : obj.swigCPtr;
    }

    protected void finalize() {
        this.delete();
    }

    public synchronized void delete() {
        if (this.swigCPtr != 0L) {
            if (this.swigCMemOwn) {
                this.swigCMemOwn = false;
                pjsua2JNI.delete_OnCallSdpCreatedParam(this.swigCPtr);
            }
            this.swigCPtr = 0L;
        }
    }

    public void setSdp(SdpSession value) {
        pjsua2JNI.OnCallSdpCreatedParam_sdp_set(this.swigCPtr, this, SdpSession.getCPtr(value), value);
    }

    public SdpSession getSdp() {
        long cPtr = pjsua2JNI.OnCallSdpCreatedParam_sdp_get(this.swigCPtr, this);
        return cPtr == 0L ? null : new SdpSession(cPtr, false);
    }

    public void setRemSdp(SdpSession value) {
        pjsua2JNI.OnCallSdpCreatedParam_remSdp_set(this.swigCPtr, this, SdpSession.getCPtr(value), value);
    }

    public SdpSession getRemSdp() {
        long cPtr = pjsua2JNI.OnCallSdpCreatedParam_remSdp_get(this.swigCPtr, this);
        return cPtr == 0L ? null : new SdpSession(cPtr, false);
    }

    public OnCallSdpCreatedParam() {
        this(pjsua2JNI.new_OnCallSdpCreatedParam(), true);
    }
}

