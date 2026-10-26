/*
 * Decompiled with CFR 0.152.
 */
package org.pjsip.pjsua2;

import org.pjsip.pjsua2.SipHeaderVector;
import org.pjsip.pjsua2.SipMediaType;
import org.pjsip.pjsua2.SipMultipartPartVector;
import org.pjsip.pjsua2.pjsua2JNI;

public class SipTxOption {
    private transient long swigCPtr;
    protected transient boolean swigCMemOwn;

    protected SipTxOption(long cPtr, boolean cMemoryOwn) {
        this.swigCMemOwn = cMemoryOwn;
        this.swigCPtr = cPtr;
    }

    protected static long getCPtr(SipTxOption obj) {
        return obj == null ? 0L : obj.swigCPtr;
    }

    protected void finalize() {
        this.delete();
    }

    public synchronized void delete() {
        if (this.swigCPtr != 0L) {
            if (this.swigCMemOwn) {
                this.swigCMemOwn = false;
                pjsua2JNI.delete_SipTxOption(this.swigCPtr);
            }
            this.swigCPtr = 0L;
        }
    }

    public void setTargetUri(String value) {
        pjsua2JNI.SipTxOption_targetUri_set(this.swigCPtr, this, value);
    }

    public String getTargetUri() {
        return pjsua2JNI.SipTxOption_targetUri_get(this.swigCPtr, this);
    }

    public void setHeaders(SipHeaderVector value) {
        pjsua2JNI.SipTxOption_headers_set(this.swigCPtr, this, SipHeaderVector.getCPtr(value), value);
    }

    public SipHeaderVector getHeaders() {
        long cPtr = pjsua2JNI.SipTxOption_headers_get(this.swigCPtr, this);
        return cPtr == 0L ? null : new SipHeaderVector(cPtr, false);
    }

    public void setContentType(String value) {
        pjsua2JNI.SipTxOption_contentType_set(this.swigCPtr, this, value);
    }

    public String getContentType() {
        return pjsua2JNI.SipTxOption_contentType_get(this.swigCPtr, this);
    }

    public void setMsgBody(String value) {
        pjsua2JNI.SipTxOption_msgBody_set(this.swigCPtr, this, value);
    }

    public String getMsgBody() {
        return pjsua2JNI.SipTxOption_msgBody_get(this.swigCPtr, this);
    }

    public void setMultipartContentType(SipMediaType value) {
        pjsua2JNI.SipTxOption_multipartContentType_set(this.swigCPtr, this, SipMediaType.getCPtr(value), value);
    }

    public SipMediaType getMultipartContentType() {
        long cPtr = pjsua2JNI.SipTxOption_multipartContentType_get(this.swigCPtr, this);
        return cPtr == 0L ? null : new SipMediaType(cPtr, false);
    }

    public void setMultipartParts(SipMultipartPartVector value) {
        pjsua2JNI.SipTxOption_multipartParts_set(this.swigCPtr, this, SipMultipartPartVector.getCPtr(value), value);
    }

    public SipMultipartPartVector getMultipartParts() {
        long cPtr = pjsua2JNI.SipTxOption_multipartParts_get(this.swigCPtr, this);
        return cPtr == 0L ? null : new SipMultipartPartVector(cPtr, false);
    }

    public boolean isEmpty() {
        return pjsua2JNI.SipTxOption_isEmpty(this.swigCPtr, this);
    }

    public SipTxOption() {
        this(pjsua2JNI.new_SipTxOption(), true);
    }
}

