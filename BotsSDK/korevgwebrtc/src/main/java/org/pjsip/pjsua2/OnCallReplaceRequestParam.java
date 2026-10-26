/*
 * Decompiled with CFR 0.152.
 */
package org.pjsip.pjsua2;

import org.pjsip.pjsua2.CallSetting;
import org.pjsip.pjsua2.SipRxData;
import org.pjsip.pjsua2.pjsua2JNI;

public class OnCallReplaceRequestParam {
    private transient long swigCPtr;
    protected transient boolean swigCMemOwn;

    protected OnCallReplaceRequestParam(long cPtr, boolean cMemoryOwn) {
        this.swigCMemOwn = cMemoryOwn;
        this.swigCPtr = cPtr;
    }

    protected static long getCPtr(OnCallReplaceRequestParam obj) {
        return obj == null ? 0L : obj.swigCPtr;
    }

    protected void finalize() {
        this.delete();
    }

    public synchronized void delete() {
        if (this.swigCPtr != 0L) {
            if (this.swigCMemOwn) {
                this.swigCMemOwn = false;
                pjsua2JNI.delete_OnCallReplaceRequestParam(this.swigCPtr);
            }
            this.swigCPtr = 0L;
        }
    }

    public void setRdata(SipRxData value) {
        pjsua2JNI.OnCallReplaceRequestParam_rdata_set(this.swigCPtr, this, SipRxData.getCPtr(value), value);
    }

    public SipRxData getRdata() {
        long cPtr = pjsua2JNI.OnCallReplaceRequestParam_rdata_get(this.swigCPtr, this);
        return cPtr == 0L ? null : new SipRxData(cPtr, false);
    }

    public void setStatusCode(int value) {
        pjsua2JNI.OnCallReplaceRequestParam_statusCode_set(this.swigCPtr, this, value);
    }

    public int getStatusCode() {
        return pjsua2JNI.OnCallReplaceRequestParam_statusCode_get(this.swigCPtr, this);
    }

    public void setReason(String value) {
        pjsua2JNI.OnCallReplaceRequestParam_reason_set(this.swigCPtr, this, value);
    }

    public String getReason() {
        return pjsua2JNI.OnCallReplaceRequestParam_reason_get(this.swigCPtr, this);
    }

    public void setOpt(CallSetting value) {
        pjsua2JNI.OnCallReplaceRequestParam_opt_set(this.swigCPtr, this, CallSetting.getCPtr(value), value);
    }

    public CallSetting getOpt() {
        long cPtr = pjsua2JNI.OnCallReplaceRequestParam_opt_get(this.swigCPtr, this);
        return cPtr == 0L ? null : new CallSetting(cPtr, false);
    }

    public OnCallReplaceRequestParam() {
        this(pjsua2JNI.new_OnCallReplaceRequestParam(), true);
    }
}

