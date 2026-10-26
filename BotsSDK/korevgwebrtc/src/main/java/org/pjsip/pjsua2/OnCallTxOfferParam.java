/*
 * Decompiled with CFR 0.152.
 */
package org.pjsip.pjsua2;

import org.pjsip.pjsua2.CallSetting;
import org.pjsip.pjsua2.pjsua2JNI;

public class OnCallTxOfferParam {
    private transient long swigCPtr;
    protected transient boolean swigCMemOwn;

    protected OnCallTxOfferParam(long cPtr, boolean cMemoryOwn) {
        this.swigCMemOwn = cMemoryOwn;
        this.swigCPtr = cPtr;
    }

    protected static long getCPtr(OnCallTxOfferParam obj) {
        return obj == null ? 0L : obj.swigCPtr;
    }

    protected void finalize() {
        this.delete();
    }

    public synchronized void delete() {
        if (this.swigCPtr != 0L) {
            if (this.swigCMemOwn) {
                this.swigCMemOwn = false;
                pjsua2JNI.delete_OnCallTxOfferParam(this.swigCPtr);
            }
            this.swigCPtr = 0L;
        }
    }

    public void setOpt(CallSetting value) {
        pjsua2JNI.OnCallTxOfferParam_opt_set(this.swigCPtr, this, CallSetting.getCPtr(value), value);
    }

    public CallSetting getOpt() {
        long cPtr = pjsua2JNI.OnCallTxOfferParam_opt_get(this.swigCPtr, this);
        return cPtr == 0L ? null : new CallSetting(cPtr, false);
    }

    public OnCallTxOfferParam() {
        this(pjsua2JNI.new_OnCallTxOfferParam(), true);
    }
}

