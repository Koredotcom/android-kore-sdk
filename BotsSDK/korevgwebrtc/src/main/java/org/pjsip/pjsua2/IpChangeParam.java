/*
 * Decompiled with CFR 0.152.
 */
package org.pjsip.pjsua2;

import org.pjsip.pjsua2.pjsua2JNI;

public class IpChangeParam {
    private transient long swigCPtr;
    protected transient boolean swigCMemOwn;

    protected IpChangeParam(long cPtr, boolean cMemoryOwn) {
        this.swigCMemOwn = cMemoryOwn;
        this.swigCPtr = cPtr;
    }

    protected static long getCPtr(IpChangeParam obj) {
        return obj == null ? 0L : obj.swigCPtr;
    }

    protected void finalize() {
        this.delete();
    }

    public synchronized void delete() {
        if (this.swigCPtr != 0L) {
            if (this.swigCMemOwn) {
                this.swigCMemOwn = false;
                pjsua2JNI.delete_IpChangeParam(this.swigCPtr);
            }
            this.swigCPtr = 0L;
        }
    }

    public void setRestartListener(boolean value) {
        pjsua2JNI.IpChangeParam_restartListener_set(this.swigCPtr, this, value);
    }

    public boolean getRestartListener() {
        return pjsua2JNI.IpChangeParam_restartListener_get(this.swigCPtr, this);
    }

    public void setRestartLisDelay(long value) {
        pjsua2JNI.IpChangeParam_restartLisDelay_set(this.swigCPtr, this, value);
    }

    public long getRestartLisDelay() {
        return pjsua2JNI.IpChangeParam_restartLisDelay_get(this.swigCPtr, this);
    }

    public IpChangeParam() {
        this(pjsua2JNI.new_IpChangeParam(), true);
    }
}

