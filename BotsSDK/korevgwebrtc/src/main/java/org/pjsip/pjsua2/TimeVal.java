/*
 * Decompiled with CFR 0.152.
 */
package org.pjsip.pjsua2;

import org.pjsip.pjsua2.pjsua2JNI;

public class TimeVal {
    private transient long swigCPtr;
    protected transient boolean swigCMemOwn;

    protected TimeVal(long cPtr, boolean cMemoryOwn) {
        this.swigCMemOwn = cMemoryOwn;
        this.swigCPtr = cPtr;
    }

    protected static long getCPtr(TimeVal obj) {
        return obj == null ? 0L : obj.swigCPtr;
    }

    protected void finalize() {
        this.delete();
    }

    public synchronized void delete() {
        if (this.swigCPtr != 0L) {
            if (this.swigCMemOwn) {
                this.swigCMemOwn = false;
                pjsua2JNI.delete_TimeVal(this.swigCPtr);
            }
            this.swigCPtr = 0L;
        }
    }

    public void setSec(int value) {
        pjsua2JNI.TimeVal_sec_set(this.swigCPtr, this, value);
    }

    public int getSec() {
        return pjsua2JNI.TimeVal_sec_get(this.swigCPtr, this);
    }

    public void setMsec(int value) {
        pjsua2JNI.TimeVal_msec_set(this.swigCPtr, this, value);
    }

    public int getMsec() {
        return pjsua2JNI.TimeVal_msec_get(this.swigCPtr, this);
    }

    public TimeVal() {
        this(pjsua2JNI.new_TimeVal(), true);
    }
}

