/*
 * Decompiled with CFR 0.152.
 */
package org.pjsip.pjsua2;

import org.pjsip.pjsua2.SipRxData;
import org.pjsip.pjsua2.pjsua2JNI;

public class OnInstantMessageParam {
    private transient long swigCPtr;
    protected transient boolean swigCMemOwn;

    protected OnInstantMessageParam(long cPtr, boolean cMemoryOwn) {
        this.swigCMemOwn = cMemoryOwn;
        this.swigCPtr = cPtr;
    }

    protected static long getCPtr(OnInstantMessageParam obj) {
        return obj == null ? 0L : obj.swigCPtr;
    }

    protected void finalize() {
        this.delete();
    }

    public synchronized void delete() {
        if (this.swigCPtr != 0L) {
            if (this.swigCMemOwn) {
                this.swigCMemOwn = false;
                pjsua2JNI.delete_OnInstantMessageParam(this.swigCPtr);
            }
            this.swigCPtr = 0L;
        }
    }

    public void setFromUri(String value) {
        pjsua2JNI.OnInstantMessageParam_fromUri_set(this.swigCPtr, this, value);
    }

    public String getFromUri() {
        return pjsua2JNI.OnInstantMessageParam_fromUri_get(this.swigCPtr, this);
    }

    public void setToUri(String value) {
        pjsua2JNI.OnInstantMessageParam_toUri_set(this.swigCPtr, this, value);
    }

    public String getToUri() {
        return pjsua2JNI.OnInstantMessageParam_toUri_get(this.swigCPtr, this);
    }

    public void setContactUri(String value) {
        pjsua2JNI.OnInstantMessageParam_contactUri_set(this.swigCPtr, this, value);
    }

    public String getContactUri() {
        return pjsua2JNI.OnInstantMessageParam_contactUri_get(this.swigCPtr, this);
    }

    public void setContentType(String value) {
        pjsua2JNI.OnInstantMessageParam_contentType_set(this.swigCPtr, this, value);
    }

    public String getContentType() {
        return pjsua2JNI.OnInstantMessageParam_contentType_get(this.swigCPtr, this);
    }

    public void setMsgBody(String value) {
        pjsua2JNI.OnInstantMessageParam_msgBody_set(this.swigCPtr, this, value);
    }

    public String getMsgBody() {
        return pjsua2JNI.OnInstantMessageParam_msgBody_get(this.swigCPtr, this);
    }

    public void setRdata(SipRxData value) {
        pjsua2JNI.OnInstantMessageParam_rdata_set(this.swigCPtr, this, SipRxData.getCPtr(value), value);
    }

    public SipRxData getRdata() {
        long cPtr = pjsua2JNI.OnInstantMessageParam_rdata_get(this.swigCPtr, this);
        return cPtr == 0L ? null : new SipRxData(cPtr, false);
    }

    public OnInstantMessageParam() {
        this(pjsua2JNI.new_OnInstantMessageParam(), true);
    }
}

