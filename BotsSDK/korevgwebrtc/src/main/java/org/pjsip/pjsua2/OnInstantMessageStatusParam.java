/*
 * Decompiled with CFR 0.152.
 */
package org.pjsip.pjsua2;

import org.pjsip.pjsua2.SWIGTYPE_p_void;
import org.pjsip.pjsua2.SipRxData;
import org.pjsip.pjsua2.pjsua2JNI;

public class OnInstantMessageStatusParam {
    private transient long swigCPtr;
    protected transient boolean swigCMemOwn;

    protected OnInstantMessageStatusParam(long cPtr, boolean cMemoryOwn) {
        this.swigCMemOwn = cMemoryOwn;
        this.swigCPtr = cPtr;
    }

    protected static long getCPtr(OnInstantMessageStatusParam obj) {
        return obj == null ? 0L : obj.swigCPtr;
    }

    protected void finalize() {
        this.delete();
    }

    public synchronized void delete() {
        if (this.swigCPtr != 0L) {
            if (this.swigCMemOwn) {
                this.swigCMemOwn = false;
                pjsua2JNI.delete_OnInstantMessageStatusParam(this.swigCPtr);
            }
            this.swigCPtr = 0L;
        }
    }

    public void setUserData(SWIGTYPE_p_void value) {
        pjsua2JNI.OnInstantMessageStatusParam_userData_set(this.swigCPtr, this, SWIGTYPE_p_void.getCPtr(value));
    }

    public SWIGTYPE_p_void getUserData() {
        long cPtr = pjsua2JNI.OnInstantMessageStatusParam_userData_get(this.swigCPtr, this);
        return cPtr == 0L ? null : new SWIGTYPE_p_void(cPtr, false);
    }

    public void setToUri(String value) {
        pjsua2JNI.OnInstantMessageStatusParam_toUri_set(this.swigCPtr, this, value);
    }

    public String getToUri() {
        return pjsua2JNI.OnInstantMessageStatusParam_toUri_get(this.swigCPtr, this);
    }

    public void setMsgBody(String value) {
        pjsua2JNI.OnInstantMessageStatusParam_msgBody_set(this.swigCPtr, this, value);
    }

    public String getMsgBody() {
        return pjsua2JNI.OnInstantMessageStatusParam_msgBody_get(this.swigCPtr, this);
    }

    public void setCode(int value) {
        pjsua2JNI.OnInstantMessageStatusParam_code_set(this.swigCPtr, this, value);
    }

    public int getCode() {
        return pjsua2JNI.OnInstantMessageStatusParam_code_get(this.swigCPtr, this);
    }

    public void setReason(String value) {
        pjsua2JNI.OnInstantMessageStatusParam_reason_set(this.swigCPtr, this, value);
    }

    public String getReason() {
        return pjsua2JNI.OnInstantMessageStatusParam_reason_get(this.swigCPtr, this);
    }

    public void setRdata(SipRxData value) {
        pjsua2JNI.OnInstantMessageStatusParam_rdata_set(this.swigCPtr, this, SipRxData.getCPtr(value), value);
    }

    public SipRxData getRdata() {
        long cPtr = pjsua2JNI.OnInstantMessageStatusParam_rdata_get(this.swigCPtr, this);
        return cPtr == 0L ? null : new SipRxData(cPtr, false);
    }

    public OnInstantMessageStatusParam() {
        this(pjsua2JNI.new_OnInstantMessageStatusParam(), true);
    }
}

