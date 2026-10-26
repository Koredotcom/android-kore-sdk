/*
 * Decompiled with CFR 0.152.
 */
package org.pjsip.pjsua2;

import org.pjsip.pjsua2.pjsua2JNI;

public class OnDtmfDigitParam {
    private transient long swigCPtr;
    protected transient boolean swigCMemOwn;

    protected OnDtmfDigitParam(long cPtr, boolean cMemoryOwn) {
        this.swigCMemOwn = cMemoryOwn;
        this.swigCPtr = cPtr;
    }

    protected static long getCPtr(OnDtmfDigitParam obj) {
        return obj == null ? 0L : obj.swigCPtr;
    }

    protected void finalize() {
        this.delete();
    }

    public synchronized void delete() {
        if (this.swigCPtr != 0L) {
            if (this.swigCMemOwn) {
                this.swigCMemOwn = false;
                pjsua2JNI.delete_OnDtmfDigitParam(this.swigCPtr);
            }
            this.swigCPtr = 0L;
        }
    }

    public void setMethod(int value) {
        pjsua2JNI.OnDtmfDigitParam_method_set(this.swigCPtr, this, value);
    }

    public int getMethod() {
        return pjsua2JNI.OnDtmfDigitParam_method_get(this.swigCPtr, this);
    }

    public void setDigit(String value) {
        pjsua2JNI.OnDtmfDigitParam_digit_set(this.swigCPtr, this, value);
    }

    public String getDigit() {
        return pjsua2JNI.OnDtmfDigitParam_digit_get(this.swigCPtr, this);
    }

    public void setDuration(long value) {
        pjsua2JNI.OnDtmfDigitParam_duration_set(this.swigCPtr, this, value);
    }

    public long getDuration() {
        return pjsua2JNI.OnDtmfDigitParam_duration_get(this.swigCPtr, this);
    }

    public OnDtmfDigitParam() {
        this(pjsua2JNI.new_OnDtmfDigitParam(), true);
    }
}

