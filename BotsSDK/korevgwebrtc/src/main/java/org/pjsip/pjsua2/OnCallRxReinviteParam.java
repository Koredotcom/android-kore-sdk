/*
 * Decompiled with CFR 0.152.
 */
package org.pjsip.pjsua2;

import org.pjsip.pjsua2.CallSetting;
import org.pjsip.pjsua2.SdpSession;
import org.pjsip.pjsua2.SipRxData;
import org.pjsip.pjsua2.pjsua2JNI;

public class OnCallRxReinviteParam {
    private transient long swigCPtr;
    protected transient boolean swigCMemOwn;

    protected OnCallRxReinviteParam(long cPtr, boolean cMemoryOwn) {
        this.swigCMemOwn = cMemoryOwn;
        this.swigCPtr = cPtr;
    }

    protected static long getCPtr(OnCallRxReinviteParam obj) {
        return obj == null ? 0L : obj.swigCPtr;
    }

    protected void finalize() {
        this.delete();
    }

    public synchronized void delete() {
        if (this.swigCPtr != 0L) {
            if (this.swigCMemOwn) {
                this.swigCMemOwn = false;
                pjsua2JNI.delete_OnCallRxReinviteParam(this.swigCPtr);
            }
            this.swigCPtr = 0L;
        }
    }

    public void setOffer(SdpSession value) {
        pjsua2JNI.OnCallRxReinviteParam_offer_set(this.swigCPtr, this, SdpSession.getCPtr(value), value);
    }

    public SdpSession getOffer() {
        long cPtr = pjsua2JNI.OnCallRxReinviteParam_offer_get(this.swigCPtr, this);
        return cPtr == 0L ? null : new SdpSession(cPtr, false);
    }

    public void setRdata(SipRxData value) {
        pjsua2JNI.OnCallRxReinviteParam_rdata_set(this.swigCPtr, this, SipRxData.getCPtr(value), value);
    }

    public SipRxData getRdata() {
        long cPtr = pjsua2JNI.OnCallRxReinviteParam_rdata_get(this.swigCPtr, this);
        return cPtr == 0L ? null : new SipRxData(cPtr, false);
    }

    public void setIsAsync(boolean value) {
        pjsua2JNI.OnCallRxReinviteParam_isAsync_set(this.swigCPtr, this, value);
    }

    public boolean getIsAsync() {
        return pjsua2JNI.OnCallRxReinviteParam_isAsync_get(this.swigCPtr, this);
    }

    public void setStatusCode(int value) {
        pjsua2JNI.OnCallRxReinviteParam_statusCode_set(this.swigCPtr, this, value);
    }

    public int getStatusCode() {
        return pjsua2JNI.OnCallRxReinviteParam_statusCode_get(this.swigCPtr, this);
    }

    public void setOpt(CallSetting value) {
        pjsua2JNI.OnCallRxReinviteParam_opt_set(this.swigCPtr, this, CallSetting.getCPtr(value), value);
    }

    public CallSetting getOpt() {
        long cPtr = pjsua2JNI.OnCallRxReinviteParam_opt_get(this.swigCPtr, this);
        return cPtr == 0L ? null : new CallSetting(cPtr, false);
    }

    public OnCallRxReinviteParam() {
        this(pjsua2JNI.new_OnCallRxReinviteParam(), true);
    }
}

