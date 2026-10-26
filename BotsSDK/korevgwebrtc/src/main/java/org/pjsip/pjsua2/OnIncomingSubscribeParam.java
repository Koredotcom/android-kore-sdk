/*
 * Decompiled with CFR 0.152.
 */
package org.pjsip.pjsua2;

import org.pjsip.pjsua2.SWIGTYPE_p_void;
import org.pjsip.pjsua2.SipRxData;
import org.pjsip.pjsua2.SipTxOption;
import org.pjsip.pjsua2.pjsua2JNI;

public class OnIncomingSubscribeParam {
    private transient long swigCPtr;
    protected transient boolean swigCMemOwn;

    protected OnIncomingSubscribeParam(long cPtr, boolean cMemoryOwn) {
        this.swigCMemOwn = cMemoryOwn;
        this.swigCPtr = cPtr;
    }

    protected static long getCPtr(OnIncomingSubscribeParam obj) {
        return obj == null ? 0L : obj.swigCPtr;
    }

    protected void finalize() {
        this.delete();
    }

    public synchronized void delete() {
        if (this.swigCPtr != 0L) {
            if (this.swigCMemOwn) {
                this.swigCMemOwn = false;
                pjsua2JNI.delete_OnIncomingSubscribeParam(this.swigCPtr);
            }
            this.swigCPtr = 0L;
        }
    }

    public void setSrvPres(SWIGTYPE_p_void value) {
        pjsua2JNI.OnIncomingSubscribeParam_srvPres_set(this.swigCPtr, this, SWIGTYPE_p_void.getCPtr(value));
    }

    public SWIGTYPE_p_void getSrvPres() {
        long cPtr = pjsua2JNI.OnIncomingSubscribeParam_srvPres_get(this.swigCPtr, this);
        return cPtr == 0L ? null : new SWIGTYPE_p_void(cPtr, false);
    }

    public void setFromUri(String value) {
        pjsua2JNI.OnIncomingSubscribeParam_fromUri_set(this.swigCPtr, this, value);
    }

    public String getFromUri() {
        return pjsua2JNI.OnIncomingSubscribeParam_fromUri_get(this.swigCPtr, this);
    }

    public void setRdata(SipRxData value) {
        pjsua2JNI.OnIncomingSubscribeParam_rdata_set(this.swigCPtr, this, SipRxData.getCPtr(value), value);
    }

    public SipRxData getRdata() {
        long cPtr = pjsua2JNI.OnIncomingSubscribeParam_rdata_get(this.swigCPtr, this);
        return cPtr == 0L ? null : new SipRxData(cPtr, false);
    }

    public void setCode(int value) {
        pjsua2JNI.OnIncomingSubscribeParam_code_set(this.swigCPtr, this, value);
    }

    public int getCode() {
        return pjsua2JNI.OnIncomingSubscribeParam_code_get(this.swigCPtr, this);
    }

    public void setReason(String value) {
        pjsua2JNI.OnIncomingSubscribeParam_reason_set(this.swigCPtr, this, value);
    }

    public String getReason() {
        return pjsua2JNI.OnIncomingSubscribeParam_reason_get(this.swigCPtr, this);
    }

    public void setTxOption(SipTxOption value) {
        pjsua2JNI.OnIncomingSubscribeParam_txOption_set(this.swigCPtr, this, SipTxOption.getCPtr(value), value);
    }

    public SipTxOption getTxOption() {
        long cPtr = pjsua2JNI.OnIncomingSubscribeParam_txOption_get(this.swigCPtr, this);
        return cPtr == 0L ? null : new SipTxOption(cPtr, false);
    }

    public OnIncomingSubscribeParam() {
        this(pjsua2JNI.new_OnIncomingSubscribeParam(), true);
    }
}

