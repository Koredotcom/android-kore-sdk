/*
 * Decompiled with CFR 0.152.
 */
package org.pjsip.pjsua2;

import org.pjsip.pjsua2.SipEvent;
import org.pjsip.pjsua2.pjsua2JNI;

public class OnCallRedirectedParam {
    private transient long swigCPtr;
    protected transient boolean swigCMemOwn;

    protected OnCallRedirectedParam(long cPtr, boolean cMemoryOwn) {
        this.swigCMemOwn = cMemoryOwn;
        this.swigCPtr = cPtr;
    }

    protected static long getCPtr(OnCallRedirectedParam obj) {
        return obj == null ? 0L : obj.swigCPtr;
    }

    protected void finalize() {
        this.delete();
    }

    public synchronized void delete() {
        if (this.swigCPtr != 0L) {
            if (this.swigCMemOwn) {
                this.swigCMemOwn = false;
                pjsua2JNI.delete_OnCallRedirectedParam(this.swigCPtr);
            }
            this.swigCPtr = 0L;
        }
    }

    public void setTargetUri(String value) {
        pjsua2JNI.OnCallRedirectedParam_targetUri_set(this.swigCPtr, this, value);
    }

    public String getTargetUri() {
        return pjsua2JNI.OnCallRedirectedParam_targetUri_get(this.swigCPtr, this);
    }

    public void setE(SipEvent value) {
        pjsua2JNI.OnCallRedirectedParam_e_set(this.swigCPtr, this, SipEvent.getCPtr(value), value);
    }

    public SipEvent getE() {
        long cPtr = pjsua2JNI.OnCallRedirectedParam_e_get(this.swigCPtr, this);
        return cPtr == 0L ? null : new SipEvent(cPtr, false);
    }

    public OnCallRedirectedParam() {
        this(pjsua2JNI.new_OnCallRedirectedParam(), true);
    }
}

