/*
 * Decompiled with CFR 0.152.
 */
package org.pjsip.pjsua2;

import org.pjsip.pjsua2.pjsua2JNI;

public class OnCallMediaTransportStateParam {
    private transient long swigCPtr;
    protected transient boolean swigCMemOwn;

    protected OnCallMediaTransportStateParam(long cPtr, boolean cMemoryOwn) {
        this.swigCMemOwn = cMemoryOwn;
        this.swigCPtr = cPtr;
    }

    protected static long getCPtr(OnCallMediaTransportStateParam obj) {
        return obj == null ? 0L : obj.swigCPtr;
    }

    protected void finalize() {
        this.delete();
    }

    public synchronized void delete() {
        if (this.swigCPtr != 0L) {
            if (this.swigCMemOwn) {
                this.swigCMemOwn = false;
                pjsua2JNI.delete_OnCallMediaTransportStateParam(this.swigCPtr);
            }
            this.swigCPtr = 0L;
        }
    }

    public void setMedIdx(long value) {
        pjsua2JNI.OnCallMediaTransportStateParam_medIdx_set(this.swigCPtr, this, value);
    }

    public long getMedIdx() {
        return pjsua2JNI.OnCallMediaTransportStateParam_medIdx_get(this.swigCPtr, this);
    }

    public void setState(int value) {
        pjsua2JNI.OnCallMediaTransportStateParam_state_set(this.swigCPtr, this, value);
    }

    public int getState() {
        return pjsua2JNI.OnCallMediaTransportStateParam_state_get(this.swigCPtr, this);
    }

    public void setStatus(int value) {
        pjsua2JNI.OnCallMediaTransportStateParam_status_set(this.swigCPtr, this, value);
    }

    public int getStatus() {
        return pjsua2JNI.OnCallMediaTransportStateParam_status_get(this.swigCPtr, this);
    }

    public void setSipErrorCode(int value) {
        pjsua2JNI.OnCallMediaTransportStateParam_sipErrorCode_set(this.swigCPtr, this, value);
    }

    public int getSipErrorCode() {
        return pjsua2JNI.OnCallMediaTransportStateParam_sipErrorCode_get(this.swigCPtr, this);
    }

    public OnCallMediaTransportStateParam() {
        this(pjsua2JNI.new_OnCallMediaTransportStateParam(), true);
    }
}

