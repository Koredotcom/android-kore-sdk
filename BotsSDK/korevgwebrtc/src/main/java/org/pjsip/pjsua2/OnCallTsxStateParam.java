/*
 * Decompiled with CFR 0.152.
 */
package org.pjsip.pjsua2;

import org.pjsip.pjsua2.SipEvent;
import org.pjsip.pjsua2.pjsua2JNI;

public class OnCallTsxStateParam {
    private transient long swigCPtr;
    protected transient boolean swigCMemOwn;

    protected OnCallTsxStateParam(long cPtr, boolean cMemoryOwn) {
        this.swigCMemOwn = cMemoryOwn;
        this.swigCPtr = cPtr;
    }

    protected static long getCPtr(OnCallTsxStateParam obj) {
        return obj == null ? 0L : obj.swigCPtr;
    }

    protected void finalize() {
        this.delete();
    }

    public synchronized void delete() {
        if (this.swigCPtr != 0L) {
            if (this.swigCMemOwn) {
                this.swigCMemOwn = false;
                pjsua2JNI.delete_OnCallTsxStateParam(this.swigCPtr);
            }
            this.swigCPtr = 0L;
        }
    }

    public void setE(SipEvent value) {
        pjsua2JNI.OnCallTsxStateParam_e_set(this.swigCPtr, this, SipEvent.getCPtr(value), value);
    }

    public SipEvent getE() {
        long cPtr = pjsua2JNI.OnCallTsxStateParam_e_get(this.swigCPtr, this);
        return cPtr == 0L ? null : new SipEvent(cPtr, false);
    }

    public OnCallTsxStateParam() {
        this(pjsua2JNI.new_OnCallTsxStateParam(), true);
    }
}

