/*
 * Decompiled with CFR 0.152.
 */
package org.pjsip.pjsua2;

import org.pjsip.pjsua2.pjsua2JNI;

public class CallVidSetStreamParam {
    private transient long swigCPtr;
    protected transient boolean swigCMemOwn;

    protected CallVidSetStreamParam(long cPtr, boolean cMemoryOwn) {
        this.swigCMemOwn = cMemoryOwn;
        this.swigCPtr = cPtr;
    }

    protected static long getCPtr(CallVidSetStreamParam obj) {
        return obj == null ? 0L : obj.swigCPtr;
    }

    protected void finalize() {
        this.delete();
    }

    public synchronized void delete() {
        if (this.swigCPtr != 0L) {
            if (this.swigCMemOwn) {
                this.swigCMemOwn = false;
                pjsua2JNI.delete_CallVidSetStreamParam(this.swigCPtr);
            }
            this.swigCPtr = 0L;
        }
    }

    public void setMedIdx(int value) {
        pjsua2JNI.CallVidSetStreamParam_medIdx_set(this.swigCPtr, this, value);
    }

    public int getMedIdx() {
        return pjsua2JNI.CallVidSetStreamParam_medIdx_get(this.swigCPtr, this);
    }

    public void setDir(int value) {
        pjsua2JNI.CallVidSetStreamParam_dir_set(this.swigCPtr, this, value);
    }

    public int getDir() {
        return pjsua2JNI.CallVidSetStreamParam_dir_get(this.swigCPtr, this);
    }

    public void setCapDev(int value) {
        pjsua2JNI.CallVidSetStreamParam_capDev_set(this.swigCPtr, this, value);
    }

    public int getCapDev() {
        return pjsua2JNI.CallVidSetStreamParam_capDev_get(this.swigCPtr, this);
    }

    public CallVidSetStreamParam() {
        this(pjsua2JNI.new_CallVidSetStreamParam(), true);
    }
}

