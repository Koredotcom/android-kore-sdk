/*
 * Decompiled with CFR 0.152.
 */
package org.pjsip.pjsua2;

import org.pjsip.pjsua2.SipRxData;
import org.pjsip.pjsua2.pjsua2JNI;

public class OnRegStateParam {
    private transient long swigCPtr;
    protected transient boolean swigCMemOwn;

    protected OnRegStateParam(long cPtr, boolean cMemoryOwn) {
        this.swigCMemOwn = cMemoryOwn;
        this.swigCPtr = cPtr;
    }

    protected static long getCPtr(OnRegStateParam obj) {
        return obj == null ? 0L : obj.swigCPtr;
    }

    protected void finalize() {
        this.delete();
    }

    public synchronized void delete() {
        if (this.swigCPtr != 0L) {
            if (this.swigCMemOwn) {
                this.swigCMemOwn = false;
                pjsua2JNI.delete_OnRegStateParam(this.swigCPtr);
            }
            this.swigCPtr = 0L;
        }
    }

    public void setStatus(int value) {
        pjsua2JNI.OnRegStateParam_status_set(this.swigCPtr, this, value);
    }

    public int getStatus() {
        return pjsua2JNI.OnRegStateParam_status_get(this.swigCPtr, this);
    }

    public void setCode(int value) {
        pjsua2JNI.OnRegStateParam_code_set(this.swigCPtr, this, value);
    }

    public int getCode() {
        return pjsua2JNI.OnRegStateParam_code_get(this.swigCPtr, this);
    }

    public void setReason(String value) {
        pjsua2JNI.OnRegStateParam_reason_set(this.swigCPtr, this, value);
    }

    public String getReason() {
        return pjsua2JNI.OnRegStateParam_reason_get(this.swigCPtr, this);
    }

    public void setRdata(SipRxData value) {
        pjsua2JNI.OnRegStateParam_rdata_set(this.swigCPtr, this, SipRxData.getCPtr(value), value);
    }

    public SipRxData getRdata() {
        long cPtr = pjsua2JNI.OnRegStateParam_rdata_get(this.swigCPtr, this);
        return cPtr == 0L ? null : new SipRxData(cPtr, false);
    }

    public void setExpiration(long value) {
        pjsua2JNI.OnRegStateParam_expiration_set(this.swigCPtr, this, value);
    }

    public long getExpiration() {
        return pjsua2JNI.OnRegStateParam_expiration_get(this.swigCPtr, this);
    }

    public OnRegStateParam() {
        this(pjsua2JNI.new_OnRegStateParam(), true);
    }
}

