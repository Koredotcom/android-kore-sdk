/*
 * Decompiled with CFR 0.152.
 */
package org.pjsip.pjsua2;

import org.pjsip.pjsua2.CallSetting;
import org.pjsip.pjsua2.SdpSession;
import org.pjsip.pjsua2.pjsua2JNI;

public class OnCallRxOfferParam {
    private transient long swigCPtr;
    protected transient boolean swigCMemOwn;

    protected OnCallRxOfferParam(long cPtr, boolean cMemoryOwn) {
        this.swigCMemOwn = cMemoryOwn;
        this.swigCPtr = cPtr;
    }

    protected static long getCPtr(OnCallRxOfferParam obj) {
        return obj == null ? 0L : obj.swigCPtr;
    }

    protected void finalize() {
        this.delete();
    }

    public synchronized void delete() {
        if (this.swigCPtr != 0L) {
            if (this.swigCMemOwn) {
                this.swigCMemOwn = false;
                pjsua2JNI.delete_OnCallRxOfferParam(this.swigCPtr);
            }
            this.swigCPtr = 0L;
        }
    }

    public void setOffer(SdpSession value) {
        pjsua2JNI.OnCallRxOfferParam_offer_set(this.swigCPtr, this, SdpSession.getCPtr(value), value);
    }

    public SdpSession getOffer() {
        long cPtr = pjsua2JNI.OnCallRxOfferParam_offer_get(this.swigCPtr, this);
        return cPtr == 0L ? null : new SdpSession(cPtr, false);
    }

    public void setStatusCode(int value) {
        pjsua2JNI.OnCallRxOfferParam_statusCode_set(this.swigCPtr, this, value);
    }

    public int getStatusCode() {
        return pjsua2JNI.OnCallRxOfferParam_statusCode_get(this.swigCPtr, this);
    }

    public void setOpt(CallSetting value) {
        pjsua2JNI.OnCallRxOfferParam_opt_set(this.swigCPtr, this, CallSetting.getCPtr(value), value);
    }

    public CallSetting getOpt() {
        long cPtr = pjsua2JNI.OnCallRxOfferParam_opt_get(this.swigCPtr, this);
        return cPtr == 0L ? null : new CallSetting(cPtr, false);
    }

    public OnCallRxOfferParam() {
        this(pjsua2JNI.new_OnCallRxOfferParam(), true);
    }
}

