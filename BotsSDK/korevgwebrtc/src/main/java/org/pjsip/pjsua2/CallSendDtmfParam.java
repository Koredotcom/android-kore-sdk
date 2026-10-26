/*
 * Decompiled with CFR 0.152.
 */
package org.pjsip.pjsua2;

import org.pjsip.pjsua2.pjsua2JNI;

public class CallSendDtmfParam {
    private transient long swigCPtr;
    protected transient boolean swigCMemOwn;

    protected CallSendDtmfParam(long cPtr, boolean cMemoryOwn) {
        this.swigCMemOwn = cMemoryOwn;
        this.swigCPtr = cPtr;
    }

    protected static long getCPtr(CallSendDtmfParam obj) {
        return obj == null ? 0L : obj.swigCPtr;
    }

    protected void finalize() {
        this.delete();
    }

    public synchronized void delete() {
        if (this.swigCPtr != 0L) {
            if (this.swigCMemOwn) {
                this.swigCMemOwn = false;
                pjsua2JNI.delete_CallSendDtmfParam(this.swigCPtr);
            }
            this.swigCPtr = 0L;
        }
    }

    public void setMethod(int value) {
        pjsua2JNI.CallSendDtmfParam_method_set(this.swigCPtr, this, value);
    }

    public int getMethod() {
        return pjsua2JNI.CallSendDtmfParam_method_get(this.swigCPtr, this);
    }

    public void setDuration(long value) {
        pjsua2JNI.CallSendDtmfParam_duration_set(this.swigCPtr, this, value);
    }

    public long getDuration() {
        return pjsua2JNI.CallSendDtmfParam_duration_get(this.swigCPtr, this);
    }

    public void setDigits(String value) {
        pjsua2JNI.CallSendDtmfParam_digits_set(this.swigCPtr, this, value);
    }

    public String getDigits() {
        return pjsua2JNI.CallSendDtmfParam_digits_get(this.swigCPtr, this);
    }

    public CallSendDtmfParam() {
        this(pjsua2JNI.new_CallSendDtmfParam(), true);
    }
}

