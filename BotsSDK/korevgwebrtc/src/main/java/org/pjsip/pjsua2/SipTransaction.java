/*
 * Decompiled with CFR 0.152.
 */
package org.pjsip.pjsua2;

import org.pjsip.pjsua2.SWIGTYPE_p_void;
import org.pjsip.pjsua2.SipTxData;
import org.pjsip.pjsua2.pjsua2JNI;

public class SipTransaction {
    private transient long swigCPtr;
    protected transient boolean swigCMemOwn;

    protected SipTransaction(long cPtr, boolean cMemoryOwn) {
        this.swigCMemOwn = cMemoryOwn;
        this.swigCPtr = cPtr;
    }

    protected static long getCPtr(SipTransaction obj) {
        return obj == null ? 0L : obj.swigCPtr;
    }

    protected void finalize() {
        this.delete();
    }

    public synchronized void delete() {
        if (this.swigCPtr != 0L) {
            if (this.swigCMemOwn) {
                this.swigCMemOwn = false;
                pjsua2JNI.delete_SipTransaction(this.swigCPtr);
            }
            this.swigCPtr = 0L;
        }
    }

    public void setRole(int value) {
        pjsua2JNI.SipTransaction_role_set(this.swigCPtr, this, value);
    }

    public int getRole() {
        return pjsua2JNI.SipTransaction_role_get(this.swigCPtr, this);
    }

    public void setMethod(String value) {
        pjsua2JNI.SipTransaction_method_set(this.swigCPtr, this, value);
    }

    public String getMethod() {
        return pjsua2JNI.SipTransaction_method_get(this.swigCPtr, this);
    }

    public void setStatusCode(int value) {
        pjsua2JNI.SipTransaction_statusCode_set(this.swigCPtr, this, value);
    }

    public int getStatusCode() {
        return pjsua2JNI.SipTransaction_statusCode_get(this.swigCPtr, this);
    }

    public void setStatusText(String value) {
        pjsua2JNI.SipTransaction_statusText_set(this.swigCPtr, this, value);
    }

    public String getStatusText() {
        return pjsua2JNI.SipTransaction_statusText_get(this.swigCPtr, this);
    }

    public void setState(int value) {
        pjsua2JNI.SipTransaction_state_set(this.swigCPtr, this, value);
    }

    public int getState() {
        return pjsua2JNI.SipTransaction_state_get(this.swigCPtr, this);
    }

    public void setLastTx(SipTxData value) {
        pjsua2JNI.SipTransaction_lastTx_set(this.swigCPtr, this, SipTxData.getCPtr(value), value);
    }

    public SipTxData getLastTx() {
        long cPtr = pjsua2JNI.SipTransaction_lastTx_get(this.swigCPtr, this);
        return cPtr == 0L ? null : new SipTxData(cPtr, false);
    }

    public void setPjTransaction(SWIGTYPE_p_void value) {
        pjsua2JNI.SipTransaction_pjTransaction_set(this.swigCPtr, this, SWIGTYPE_p_void.getCPtr(value));
    }

    public SWIGTYPE_p_void getPjTransaction() {
        long cPtr = pjsua2JNI.SipTransaction_pjTransaction_get(this.swigCPtr, this);
        return cPtr == 0L ? null : new SWIGTYPE_p_void(cPtr, false);
    }

    public SipTransaction() {
        this(pjsua2JNI.new_SipTransaction(), true);
    }
}

