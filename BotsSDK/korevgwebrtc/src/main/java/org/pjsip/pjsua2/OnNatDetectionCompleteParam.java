/*
 * Decompiled with CFR 0.152.
 */
package org.pjsip.pjsua2;

import org.pjsip.pjsua2.pjsua2JNI;

public class OnNatDetectionCompleteParam {
    private transient long swigCPtr;
    protected transient boolean swigCMemOwn;

    protected OnNatDetectionCompleteParam(long cPtr, boolean cMemoryOwn) {
        this.swigCMemOwn = cMemoryOwn;
        this.swigCPtr = cPtr;
    }

    protected static long getCPtr(OnNatDetectionCompleteParam obj) {
        return obj == null ? 0L : obj.swigCPtr;
    }

    protected void finalize() {
        this.delete();
    }

    public synchronized void delete() {
        if (this.swigCPtr != 0L) {
            if (this.swigCMemOwn) {
                this.swigCMemOwn = false;
                pjsua2JNI.delete_OnNatDetectionCompleteParam(this.swigCPtr);
            }
            this.swigCPtr = 0L;
        }
    }

    public void setStatus(int value) {
        pjsua2JNI.OnNatDetectionCompleteParam_status_set(this.swigCPtr, this, value);
    }

    public int getStatus() {
        return pjsua2JNI.OnNatDetectionCompleteParam_status_get(this.swigCPtr, this);
    }

    public void setReason(String value) {
        pjsua2JNI.OnNatDetectionCompleteParam_reason_set(this.swigCPtr, this, value);
    }

    public String getReason() {
        return pjsua2JNI.OnNatDetectionCompleteParam_reason_get(this.swigCPtr, this);
    }

    public void setNatType(int value) {
        pjsua2JNI.OnNatDetectionCompleteParam_natType_set(this.swigCPtr, this, value);
    }

    public int getNatType() {
        return pjsua2JNI.OnNatDetectionCompleteParam_natType_get(this.swigCPtr, this);
    }

    public void setNatTypeName(String value) {
        pjsua2JNI.OnNatDetectionCompleteParam_natTypeName_set(this.swigCPtr, this, value);
    }

    public String getNatTypeName() {
        return pjsua2JNI.OnNatDetectionCompleteParam_natTypeName_get(this.swigCPtr, this);
    }

    public OnNatDetectionCompleteParam() {
        this(pjsua2JNI.new_OnNatDetectionCompleteParam(), true);
    }
}

