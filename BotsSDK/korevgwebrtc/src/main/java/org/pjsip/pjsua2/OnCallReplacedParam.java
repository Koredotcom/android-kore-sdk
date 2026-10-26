/*
 * Decompiled with CFR 0.152.
 */
package org.pjsip.pjsua2;

import org.pjsip.pjsua2.Call;
import org.pjsip.pjsua2.pjsua2JNI;

public class OnCallReplacedParam {
    private transient long swigCPtr;
    protected transient boolean swigCMemOwn;

    protected OnCallReplacedParam(long cPtr, boolean cMemoryOwn) {
        this.swigCMemOwn = cMemoryOwn;
        this.swigCPtr = cPtr;
    }

    protected static long getCPtr(OnCallReplacedParam obj) {
        return obj == null ? 0L : obj.swigCPtr;
    }

    protected void finalize() {
        this.delete();
    }

    public synchronized void delete() {
        if (this.swigCPtr != 0L) {
            if (this.swigCMemOwn) {
                this.swigCMemOwn = false;
                pjsua2JNI.delete_OnCallReplacedParam(this.swigCPtr);
            }
            this.swigCPtr = 0L;
        }
    }

    public void setNewCallId(int value) {
        pjsua2JNI.OnCallReplacedParam_newCallId_set(this.swigCPtr, this, value);
    }

    public int getNewCallId() {
        return pjsua2JNI.OnCallReplacedParam_newCallId_get(this.swigCPtr, this);
    }

    public void setNewCall(Call value) {
        pjsua2JNI.OnCallReplacedParam_newCall_set(this.swigCPtr, this, Call.getCPtr(value), value);
    }

    public Call getNewCall() {
        long cPtr = pjsua2JNI.OnCallReplacedParam_newCall_get(this.swigCPtr, this);
        return cPtr == 0L ? null : new Call(cPtr, false);
    }

    public OnCallReplacedParam() {
        this(pjsua2JNI.new_OnCallReplacedParam(), true);
    }
}

