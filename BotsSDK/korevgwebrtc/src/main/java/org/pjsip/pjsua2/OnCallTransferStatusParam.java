/*
 * Decompiled with CFR 0.152.
 */
package org.pjsip.pjsua2;

import org.pjsip.pjsua2.pjsua2JNI;

public class OnCallTransferStatusParam {
    private transient long swigCPtr;
    protected transient boolean swigCMemOwn;

    protected OnCallTransferStatusParam(long cPtr, boolean cMemoryOwn) {
        this.swigCMemOwn = cMemoryOwn;
        this.swigCPtr = cPtr;
    }

    protected static long getCPtr(OnCallTransferStatusParam obj) {
        return obj == null ? 0L : obj.swigCPtr;
    }

    protected void finalize() {
        this.delete();
    }

    public synchronized void delete() {
        if (this.swigCPtr != 0L) {
            if (this.swigCMemOwn) {
                this.swigCMemOwn = false;
                pjsua2JNI.delete_OnCallTransferStatusParam(this.swigCPtr);
            }
            this.swigCPtr = 0L;
        }
    }

    public void setStatusCode(int value) {
        pjsua2JNI.OnCallTransferStatusParam_statusCode_set(this.swigCPtr, this, value);
    }

    public int getStatusCode() {
        return pjsua2JNI.OnCallTransferStatusParam_statusCode_get(this.swigCPtr, this);
    }

    public void setReason(String value) {
        pjsua2JNI.OnCallTransferStatusParam_reason_set(this.swigCPtr, this, value);
    }

    public String getReason() {
        return pjsua2JNI.OnCallTransferStatusParam_reason_get(this.swigCPtr, this);
    }

    public void setFinalNotify(boolean value) {
        pjsua2JNI.OnCallTransferStatusParam_finalNotify_set(this.swigCPtr, this, value);
    }

    public boolean getFinalNotify() {
        return pjsua2JNI.OnCallTransferStatusParam_finalNotify_get(this.swigCPtr, this);
    }

    public void setCont(boolean value) {
        pjsua2JNI.OnCallTransferStatusParam_cont_set(this.swigCPtr, this, value);
    }

    public boolean getCont() {
        return pjsua2JNI.OnCallTransferStatusParam_cont_get(this.swigCPtr, this);
    }

    public OnCallTransferStatusParam() {
        this(pjsua2JNI.new_OnCallTransferStatusParam(), true);
    }
}

