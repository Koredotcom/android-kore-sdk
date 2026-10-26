/*
 * Decompiled with CFR 0.152.
 */
package org.pjsip.pjsua2;

import org.pjsip.pjsua2.SWIGTYPE_p_void;
import org.pjsip.pjsua2.SipTxOption;
import org.pjsip.pjsua2.pjsua2JNI;

public class PresNotifyParam {
    private transient long swigCPtr;
    protected transient boolean swigCMemOwn;

    protected PresNotifyParam(long cPtr, boolean cMemoryOwn) {
        this.swigCMemOwn = cMemoryOwn;
        this.swigCPtr = cPtr;
    }

    protected static long getCPtr(PresNotifyParam obj) {
        return obj == null ? 0L : obj.swigCPtr;
    }

    protected void finalize() {
        this.delete();
    }

    public synchronized void delete() {
        if (this.swigCPtr != 0L) {
            if (this.swigCMemOwn) {
                this.swigCMemOwn = false;
                pjsua2JNI.delete_PresNotifyParam(this.swigCPtr);
            }
            this.swigCPtr = 0L;
        }
    }

    public void setSrvPres(SWIGTYPE_p_void value) {
        pjsua2JNI.PresNotifyParam_srvPres_set(this.swigCPtr, this, SWIGTYPE_p_void.getCPtr(value));
    }

    public SWIGTYPE_p_void getSrvPres() {
        long cPtr = pjsua2JNI.PresNotifyParam_srvPres_get(this.swigCPtr, this);
        return cPtr == 0L ? null : new SWIGTYPE_p_void(cPtr, false);
    }

    public void setState(int value) {
        pjsua2JNI.PresNotifyParam_state_set(this.swigCPtr, this, value);
    }

    public int getState() {
        return pjsua2JNI.PresNotifyParam_state_get(this.swigCPtr, this);
    }

    public void setStateStr(String value) {
        pjsua2JNI.PresNotifyParam_stateStr_set(this.swigCPtr, this, value);
    }

    public String getStateStr() {
        return pjsua2JNI.PresNotifyParam_stateStr_get(this.swigCPtr, this);
    }

    public void setReason(String value) {
        pjsua2JNI.PresNotifyParam_reason_set(this.swigCPtr, this, value);
    }

    public String getReason() {
        return pjsua2JNI.PresNotifyParam_reason_get(this.swigCPtr, this);
    }

    public void setWithBody(boolean value) {
        pjsua2JNI.PresNotifyParam_withBody_set(this.swigCPtr, this, value);
    }

    public boolean getWithBody() {
        return pjsua2JNI.PresNotifyParam_withBody_get(this.swigCPtr, this);
    }

    public void setTxOption(SipTxOption value) {
        pjsua2JNI.PresNotifyParam_txOption_set(this.swigCPtr, this, SipTxOption.getCPtr(value), value);
    }

    public SipTxOption getTxOption() {
        long cPtr = pjsua2JNI.PresNotifyParam_txOption_get(this.swigCPtr, this);
        return cPtr == 0L ? null : new SipTxOption(cPtr, false);
    }

    public PresNotifyParam() {
        this(pjsua2JNI.new_PresNotifyParam(), true);
    }
}

