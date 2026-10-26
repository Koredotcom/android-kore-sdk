/*
 * Decompiled with CFR 0.152.
 */
package org.pjsip.pjsua2;

import org.pjsip.pjsua2.SWIGTYPE_p_void;
import org.pjsip.pjsua2.pjsua2JNI;

public class OnTimerParam {
    private transient long swigCPtr;
    protected transient boolean swigCMemOwn;

    protected OnTimerParam(long cPtr, boolean cMemoryOwn) {
        this.swigCMemOwn = cMemoryOwn;
        this.swigCPtr = cPtr;
    }

    protected static long getCPtr(OnTimerParam obj) {
        return obj == null ? 0L : obj.swigCPtr;
    }

    protected void finalize() {
        this.delete();
    }

    public synchronized void delete() {
        if (this.swigCPtr != 0L) {
            if (this.swigCMemOwn) {
                this.swigCMemOwn = false;
                pjsua2JNI.delete_OnTimerParam(this.swigCPtr);
            }
            this.swigCPtr = 0L;
        }
    }

    public void setUserData(SWIGTYPE_p_void value) {
        pjsua2JNI.OnTimerParam_userData_set(this.swigCPtr, this, SWIGTYPE_p_void.getCPtr(value));
    }

    public SWIGTYPE_p_void getUserData() {
        long cPtr = pjsua2JNI.OnTimerParam_userData_get(this.swigCPtr, this);
        return cPtr == 0L ? null : new SWIGTYPE_p_void(cPtr, false);
    }

    public void setMsecDelay(long value) {
        pjsua2JNI.OnTimerParam_msecDelay_set(this.swigCPtr, this, value);
    }

    public long getMsecDelay() {
        return pjsua2JNI.OnTimerParam_msecDelay_get(this.swigCPtr, this);
    }

    public OnTimerParam() {
        this(pjsua2JNI.new_OnTimerParam(), true);
    }
}

