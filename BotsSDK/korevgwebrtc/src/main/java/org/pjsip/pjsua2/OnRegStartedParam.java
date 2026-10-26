/*
 * Decompiled with CFR 0.152.
 */
package org.pjsip.pjsua2;

import org.pjsip.pjsua2.pjsua2JNI;

public class OnRegStartedParam {
    private transient long swigCPtr;
    protected transient boolean swigCMemOwn;

    protected OnRegStartedParam(long cPtr, boolean cMemoryOwn) {
        this.swigCMemOwn = cMemoryOwn;
        this.swigCPtr = cPtr;
    }

    protected static long getCPtr(OnRegStartedParam obj) {
        return obj == null ? 0L : obj.swigCPtr;
    }

    protected void finalize() {
        this.delete();
    }

    public synchronized void delete() {
        if (this.swigCPtr != 0L) {
            if (this.swigCMemOwn) {
                this.swigCMemOwn = false;
                pjsua2JNI.delete_OnRegStartedParam(this.swigCPtr);
            }
            this.swigCPtr = 0L;
        }
    }

    public void setRenew(boolean value) {
        pjsua2JNI.OnRegStartedParam_renew_set(this.swigCPtr, this, value);
    }

    public boolean getRenew() {
        return pjsua2JNI.OnRegStartedParam_renew_get(this.swigCPtr, this);
    }

    public OnRegStartedParam() {
        this(pjsua2JNI.new_OnRegStartedParam(), true);
    }
}

