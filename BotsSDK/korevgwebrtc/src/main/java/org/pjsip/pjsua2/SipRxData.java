/*
 * Decompiled with CFR 0.152.
 */
package org.pjsip.pjsua2;

import org.pjsip.pjsua2.SWIGTYPE_p_void;
import org.pjsip.pjsua2.pjsua2JNI;

public class SipRxData {
    private transient long swigCPtr;
    protected transient boolean swigCMemOwn;

    protected SipRxData(long cPtr, boolean cMemoryOwn) {
        this.swigCMemOwn = cMemoryOwn;
        this.swigCPtr = cPtr;
    }

    protected static long getCPtr(SipRxData obj) {
        return obj == null ? 0L : obj.swigCPtr;
    }

    protected void finalize() {
        this.delete();
    }

    public synchronized void delete() {
        if (this.swigCPtr != 0L) {
            if (this.swigCMemOwn) {
                this.swigCMemOwn = false;
                pjsua2JNI.delete_SipRxData(this.swigCPtr);
            }
            this.swigCPtr = 0L;
        }
    }

    public void setInfo(String value) {
        pjsua2JNI.SipRxData_info_set(this.swigCPtr, this, value);
    }

    public String getInfo() {
        return pjsua2JNI.SipRxData_info_get(this.swigCPtr, this);
    }

    public void setWholeMsg(String value) {
        pjsua2JNI.SipRxData_wholeMsg_set(this.swigCPtr, this, value);
    }

    public String getWholeMsg() {
        return pjsua2JNI.SipRxData_wholeMsg_get(this.swigCPtr, this);
    }

    public void setSrcAddress(String value) {
        pjsua2JNI.SipRxData_srcAddress_set(this.swigCPtr, this, value);
    }

    public String getSrcAddress() {
        return pjsua2JNI.SipRxData_srcAddress_get(this.swigCPtr, this);
    }

    public void setPjRxData(SWIGTYPE_p_void value) {
        pjsua2JNI.SipRxData_pjRxData_set(this.swigCPtr, this, SWIGTYPE_p_void.getCPtr(value));
    }

    public SWIGTYPE_p_void getPjRxData() {
        long cPtr = pjsua2JNI.SipRxData_pjRxData_get(this.swigCPtr, this);
        return cPtr == 0L ? null : new SWIGTYPE_p_void(cPtr, false);
    }

    public SipRxData() {
        this(pjsua2JNI.new_SipRxData(), true);
    }
}

