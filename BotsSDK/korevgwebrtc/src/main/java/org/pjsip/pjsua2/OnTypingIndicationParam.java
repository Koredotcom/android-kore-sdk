/*
 * Decompiled with CFR 0.152.
 */
package org.pjsip.pjsua2;

import org.pjsip.pjsua2.SipRxData;
import org.pjsip.pjsua2.pjsua2JNI;

public class OnTypingIndicationParam {
    private transient long swigCPtr;
    protected transient boolean swigCMemOwn;

    protected OnTypingIndicationParam(long cPtr, boolean cMemoryOwn) {
        this.swigCMemOwn = cMemoryOwn;
        this.swigCPtr = cPtr;
    }

    protected static long getCPtr(OnTypingIndicationParam obj) {
        return obj == null ? 0L : obj.swigCPtr;
    }

    protected void finalize() {
        this.delete();
    }

    public synchronized void delete() {
        if (this.swigCPtr != 0L) {
            if (this.swigCMemOwn) {
                this.swigCMemOwn = false;
                pjsua2JNI.delete_OnTypingIndicationParam(this.swigCPtr);
            }
            this.swigCPtr = 0L;
        }
    }

    public void setFromUri(String value) {
        pjsua2JNI.OnTypingIndicationParam_fromUri_set(this.swigCPtr, this, value);
    }

    public String getFromUri() {
        return pjsua2JNI.OnTypingIndicationParam_fromUri_get(this.swigCPtr, this);
    }

    public void setToUri(String value) {
        pjsua2JNI.OnTypingIndicationParam_toUri_set(this.swigCPtr, this, value);
    }

    public String getToUri() {
        return pjsua2JNI.OnTypingIndicationParam_toUri_get(this.swigCPtr, this);
    }

    public void setContactUri(String value) {
        pjsua2JNI.OnTypingIndicationParam_contactUri_set(this.swigCPtr, this, value);
    }

    public String getContactUri() {
        return pjsua2JNI.OnTypingIndicationParam_contactUri_get(this.swigCPtr, this);
    }

    public void setIsTyping(boolean value) {
        pjsua2JNI.OnTypingIndicationParam_isTyping_set(this.swigCPtr, this, value);
    }

    public boolean getIsTyping() {
        return pjsua2JNI.OnTypingIndicationParam_isTyping_get(this.swigCPtr, this);
    }

    public void setRdata(SipRxData value) {
        pjsua2JNI.OnTypingIndicationParam_rdata_set(this.swigCPtr, this, SipRxData.getCPtr(value), value);
    }

    public SipRxData getRdata() {
        long cPtr = pjsua2JNI.OnTypingIndicationParam_rdata_get(this.swigCPtr, this);
        return cPtr == 0L ? null : new SipRxData(cPtr, false);
    }

    public OnTypingIndicationParam() {
        this(pjsua2JNI.new_OnTypingIndicationParam(), true);
    }
}

