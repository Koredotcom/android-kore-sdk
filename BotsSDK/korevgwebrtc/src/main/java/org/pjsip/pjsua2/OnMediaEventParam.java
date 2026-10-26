/*
 * Decompiled with CFR 0.152.
 */
package org.pjsip.pjsua2;

import org.pjsip.pjsua2.MediaEvent;
import org.pjsip.pjsua2.pjsua2JNI;

public class OnMediaEventParam {
    private transient long swigCPtr;
    protected transient boolean swigCMemOwn;

    protected OnMediaEventParam(long cPtr, boolean cMemoryOwn) {
        this.swigCMemOwn = cMemoryOwn;
        this.swigCPtr = cPtr;
    }

    protected static long getCPtr(OnMediaEventParam obj) {
        return obj == null ? 0L : obj.swigCPtr;
    }

    protected void finalize() {
        this.delete();
    }

    public synchronized void delete() {
        if (this.swigCPtr != 0L) {
            if (this.swigCMemOwn) {
                this.swigCMemOwn = false;
                pjsua2JNI.delete_OnMediaEventParam(this.swigCPtr);
            }
            this.swigCPtr = 0L;
        }
    }

    public void setEv(MediaEvent value) {
        pjsua2JNI.OnMediaEventParam_ev_set(this.swigCPtr, this, MediaEvent.getCPtr(value), value);
    }

    public MediaEvent getEv() {
        long cPtr = pjsua2JNI.OnMediaEventParam_ev_get(this.swigCPtr, this);
        return cPtr == 0L ? null : new MediaEvent(cPtr, false);
    }

    public OnMediaEventParam() {
        this(pjsua2JNI.new_OnMediaEventParam(), true);
    }
}

