/*
 * Decompiled with CFR 0.152.
 */
package org.pjsip.pjsua2;

import org.pjsip.pjsua2.SipRxData;
import org.pjsip.pjsua2.pjsua2JNI;

public class OnMwiInfoParam {
    private transient long swigCPtr;
    protected transient boolean swigCMemOwn;

    protected OnMwiInfoParam(long cPtr, boolean cMemoryOwn) {
        this.swigCMemOwn = cMemoryOwn;
        this.swigCPtr = cPtr;
    }

    protected static long getCPtr(OnMwiInfoParam obj) {
        return obj == null ? 0L : obj.swigCPtr;
    }

    protected void finalize() {
        this.delete();
    }

    public synchronized void delete() {
        if (this.swigCPtr != 0L) {
            if (this.swigCMemOwn) {
                this.swigCMemOwn = false;
                pjsua2JNI.delete_OnMwiInfoParam(this.swigCPtr);
            }
            this.swigCPtr = 0L;
        }
    }

    public void setState(int value) {
        pjsua2JNI.OnMwiInfoParam_state_set(this.swigCPtr, this, value);
    }

    public int getState() {
        return pjsua2JNI.OnMwiInfoParam_state_get(this.swigCPtr, this);
    }

    public void setRdata(SipRxData value) {
        pjsua2JNI.OnMwiInfoParam_rdata_set(this.swigCPtr, this, SipRxData.getCPtr(value), value);
    }

    public SipRxData getRdata() {
        long cPtr = pjsua2JNI.OnMwiInfoParam_rdata_get(this.swigCPtr, this);
        return cPtr == 0L ? null : new SipRxData(cPtr, false);
    }

    public OnMwiInfoParam() {
        this(pjsua2JNI.new_OnMwiInfoParam(), true);
    }
}

