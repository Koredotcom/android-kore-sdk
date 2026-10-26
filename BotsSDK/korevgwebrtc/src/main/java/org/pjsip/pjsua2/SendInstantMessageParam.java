/*
 * Decompiled with CFR 0.152.
 */
package org.pjsip.pjsua2;

import org.pjsip.pjsua2.SWIGTYPE_p_void;
import org.pjsip.pjsua2.SipTxOption;
import org.pjsip.pjsua2.pjsua2JNI;

public class SendInstantMessageParam {
    private transient long swigCPtr;
    protected transient boolean swigCMemOwn;

    protected SendInstantMessageParam(long cPtr, boolean cMemoryOwn) {
        this.swigCMemOwn = cMemoryOwn;
        this.swigCPtr = cPtr;
    }

    protected static long getCPtr(SendInstantMessageParam obj) {
        return obj == null ? 0L : obj.swigCPtr;
    }

    protected void finalize() {
        this.delete();
    }

    public synchronized void delete() {
        if (this.swigCPtr != 0L) {
            if (this.swigCMemOwn) {
                this.swigCMemOwn = false;
                pjsua2JNI.delete_SendInstantMessageParam(this.swigCPtr);
            }
            this.swigCPtr = 0L;
        }
    }

    public void setContentType(String value) {
        pjsua2JNI.SendInstantMessageParam_contentType_set(this.swigCPtr, this, value);
    }

    public String getContentType() {
        return pjsua2JNI.SendInstantMessageParam_contentType_get(this.swigCPtr, this);
    }

    public void setContent(String value) {
        pjsua2JNI.SendInstantMessageParam_content_set(this.swigCPtr, this, value);
    }

    public String getContent() {
        return pjsua2JNI.SendInstantMessageParam_content_get(this.swigCPtr, this);
    }

    public void setTxOption(SipTxOption value) {
        pjsua2JNI.SendInstantMessageParam_txOption_set(this.swigCPtr, this, SipTxOption.getCPtr(value), value);
    }

    public SipTxOption getTxOption() {
        long cPtr = pjsua2JNI.SendInstantMessageParam_txOption_get(this.swigCPtr, this);
        return cPtr == 0L ? null : new SipTxOption(cPtr, false);
    }

    public void setUserData(SWIGTYPE_p_void value) {
        pjsua2JNI.SendInstantMessageParam_userData_set(this.swigCPtr, this, SWIGTYPE_p_void.getCPtr(value));
    }

    public SWIGTYPE_p_void getUserData() {
        long cPtr = pjsua2JNI.SendInstantMessageParam_userData_get(this.swigCPtr, this);
        return cPtr == 0L ? null : new SWIGTYPE_p_void(cPtr, false);
    }

    public SendInstantMessageParam() {
        this(pjsua2JNI.new_SendInstantMessageParam(), true);
    }
}

