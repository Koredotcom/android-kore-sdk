/*
 * Decompiled with CFR 0.152.
 */
package org.pjsip.pjsua2;

import org.pjsip.pjsua2.MediaEvent;
import org.pjsip.pjsua2.pjsua2JNI;

public class OnCallMediaEventParam {
    private transient long swigCPtr;
    protected transient boolean swigCMemOwn;

    protected OnCallMediaEventParam(long cPtr, boolean cMemoryOwn) {
        this.swigCMemOwn = cMemoryOwn;
        this.swigCPtr = cPtr;
    }

    protected static long getCPtr(OnCallMediaEventParam obj) {
        return obj == null ? 0L : obj.swigCPtr;
    }

    protected void finalize() {
        this.delete();
    }

    public synchronized void delete() {
        if (this.swigCPtr != 0L) {
            if (this.swigCMemOwn) {
                this.swigCMemOwn = false;
                pjsua2JNI.delete_OnCallMediaEventParam(this.swigCPtr);
            }
            this.swigCPtr = 0L;
        }
    }

    public void setMedIdx(long value) {
        pjsua2JNI.OnCallMediaEventParam_medIdx_set(this.swigCPtr, this, value);
    }

    public long getMedIdx() {
        return pjsua2JNI.OnCallMediaEventParam_medIdx_get(this.swigCPtr, this);
    }

    public void setEv(MediaEvent value) {
        pjsua2JNI.OnCallMediaEventParam_ev_set(this.swigCPtr, this, MediaEvent.getCPtr(value), value);
    }

    public MediaEvent getEv() {
        long cPtr = pjsua2JNI.OnCallMediaEventParam_ev_get(this.swigCPtr, this);
        return cPtr == 0L ? null : new MediaEvent(cPtr, false);
    }

    public OnCallMediaEventParam() {
        this(pjsua2JNI.new_OnCallMediaEventParam(), true);
    }
}

