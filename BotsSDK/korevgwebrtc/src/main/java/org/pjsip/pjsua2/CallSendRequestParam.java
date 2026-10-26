/*
 * Decompiled with CFR 0.152.
 */
package org.pjsip.pjsua2;

import org.pjsip.pjsua2.SipTxOption;
import org.pjsip.pjsua2.pjsua2JNI;

public class CallSendRequestParam {
    private transient long swigCPtr;
    protected transient boolean swigCMemOwn;

    protected CallSendRequestParam(long cPtr, boolean cMemoryOwn) {
        this.swigCMemOwn = cMemoryOwn;
        this.swigCPtr = cPtr;
    }

    protected static long getCPtr(CallSendRequestParam obj) {
        return obj == null ? 0L : obj.swigCPtr;
    }

    protected void finalize() {
        this.delete();
    }

    public synchronized void delete() {
        if (this.swigCPtr != 0L) {
            if (this.swigCMemOwn) {
                this.swigCMemOwn = false;
                pjsua2JNI.delete_CallSendRequestParam(this.swigCPtr);
            }
            this.swigCPtr = 0L;
        }
    }

    public void setMethod(String value) {
        pjsua2JNI.CallSendRequestParam_method_set(this.swigCPtr, this, value);
    }

    public String getMethod() {
        return pjsua2JNI.CallSendRequestParam_method_get(this.swigCPtr, this);
    }

    public void setTxOption(SipTxOption value) {
        pjsua2JNI.CallSendRequestParam_txOption_set(this.swigCPtr, this, SipTxOption.getCPtr(value), value);
    }

    public SipTxOption getTxOption() {
        long cPtr = pjsua2JNI.CallSendRequestParam_txOption_get(this.swigCPtr, this);
        return cPtr == 0L ? null : new SipTxOption(cPtr, false);
    }

    public CallSendRequestParam() {
        this(pjsua2JNI.new_CallSendRequestParam(), true);
    }
}

