/*
 * Decompiled with CFR 0.152.
 */
package org.pjsip.pjsua2;

import org.pjsip.pjsua2.SWIGTYPE_p_void;
import org.pjsip.pjsua2.pjsua2JNI;

public class SipTxData {
    private transient long swigCPtr;
    protected transient boolean swigCMemOwn;

    protected SipTxData(long cPtr, boolean cMemoryOwn) {
        this.swigCMemOwn = cMemoryOwn;
        this.swigCPtr = cPtr;
    }

    protected static long getCPtr(SipTxData obj) {
        return obj == null ? 0L : obj.swigCPtr;
    }

    protected void finalize() {
        this.delete();
    }

    public synchronized void delete() {
        if (this.swigCPtr != 0L) {
            if (this.swigCMemOwn) {
                this.swigCMemOwn = false;
                pjsua2JNI.delete_SipTxData(this.swigCPtr);
            }
            this.swigCPtr = 0L;
        }
    }

    public void setInfo(String value) {
        pjsua2JNI.SipTxData_info_set(this.swigCPtr, this, value);
    }

    public String getInfo() {
        return pjsua2JNI.SipTxData_info_get(this.swigCPtr, this);
    }

    public void setWholeMsg(String value) {
        pjsua2JNI.SipTxData_wholeMsg_set(this.swigCPtr, this, value);
    }

    public String getWholeMsg() {
        return pjsua2JNI.SipTxData_wholeMsg_get(this.swigCPtr, this);
    }

    public void setDstAddress(String value) {
        pjsua2JNI.SipTxData_dstAddress_set(this.swigCPtr, this, value);
    }

    public String getDstAddress() {
        return pjsua2JNI.SipTxData_dstAddress_get(this.swigCPtr, this);
    }

    public void setPjTxData(SWIGTYPE_p_void value) {
        pjsua2JNI.SipTxData_pjTxData_set(this.swigCPtr, this, SWIGTYPE_p_void.getCPtr(value));
    }

    public SWIGTYPE_p_void getPjTxData() {
        long cPtr = pjsua2JNI.SipTxData_pjTxData_get(this.swigCPtr, this);
        return cPtr == 0L ? null : new SWIGTYPE_p_void(cPtr, false);
    }

    public SipTxData() {
        this(pjsua2JNI.new_SipTxData(), true);
    }
}

