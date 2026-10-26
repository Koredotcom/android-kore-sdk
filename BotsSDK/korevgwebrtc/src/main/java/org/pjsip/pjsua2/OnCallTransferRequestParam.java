/*
 * Decompiled with CFR 0.152.
 */
package org.pjsip.pjsua2;

import org.pjsip.pjsua2.Call;
import org.pjsip.pjsua2.CallSetting;
import org.pjsip.pjsua2.pjsua2JNI;

public class OnCallTransferRequestParam {
    private transient long swigCPtr;
    protected transient boolean swigCMemOwn;

    protected OnCallTransferRequestParam(long cPtr, boolean cMemoryOwn) {
        this.swigCMemOwn = cMemoryOwn;
        this.swigCPtr = cPtr;
    }

    protected static long getCPtr(OnCallTransferRequestParam obj) {
        return obj == null ? 0L : obj.swigCPtr;
    }

    protected void finalize() {
        this.delete();
    }

    public synchronized void delete() {
        if (this.swigCPtr != 0L) {
            if (this.swigCMemOwn) {
                this.swigCMemOwn = false;
                pjsua2JNI.delete_OnCallTransferRequestParam(this.swigCPtr);
            }
            this.swigCPtr = 0L;
        }
    }

    public void setDstUri(String value) {
        pjsua2JNI.OnCallTransferRequestParam_dstUri_set(this.swigCPtr, this, value);
    }

    public String getDstUri() {
        return pjsua2JNI.OnCallTransferRequestParam_dstUri_get(this.swigCPtr, this);
    }

    public void setStatusCode(int value) {
        pjsua2JNI.OnCallTransferRequestParam_statusCode_set(this.swigCPtr, this, value);
    }

    public int getStatusCode() {
        return pjsua2JNI.OnCallTransferRequestParam_statusCode_get(this.swigCPtr, this);
    }

    public void setOpt(CallSetting value) {
        pjsua2JNI.OnCallTransferRequestParam_opt_set(this.swigCPtr, this, CallSetting.getCPtr(value), value);
    }

    public CallSetting getOpt() {
        long cPtr = pjsua2JNI.OnCallTransferRequestParam_opt_get(this.swigCPtr, this);
        return cPtr == 0L ? null : new CallSetting(cPtr, false);
    }

    public void setNewCall(Call value) {
        pjsua2JNI.OnCallTransferRequestParam_newCall_set(this.swigCPtr, this, Call.getCPtr(value), value);
    }

    public Call getNewCall() {
        long cPtr = pjsua2JNI.OnCallTransferRequestParam_newCall_get(this.swigCPtr, this);
        return cPtr == 0L ? null : new Call(cPtr, false);
    }

    public OnCallTransferRequestParam() {
        this(pjsua2JNI.new_OnCallTransferRequestParam(), true);
    }
}

