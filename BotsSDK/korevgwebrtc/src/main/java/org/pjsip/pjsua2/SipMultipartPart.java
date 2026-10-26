/*
 * Decompiled with CFR 0.152.
 */
package org.pjsip.pjsua2;

import org.pjsip.pjsua2.SipHeaderVector;
import org.pjsip.pjsua2.SipMediaType;
import org.pjsip.pjsua2.pjsua2JNI;

public class SipMultipartPart {
    private transient long swigCPtr;
    protected transient boolean swigCMemOwn;

    protected SipMultipartPart(long cPtr, boolean cMemoryOwn) {
        this.swigCMemOwn = cMemoryOwn;
        this.swigCPtr = cPtr;
    }

    protected static long getCPtr(SipMultipartPart obj) {
        return obj == null ? 0L : obj.swigCPtr;
    }

    protected void finalize() {
        this.delete();
    }

    public synchronized void delete() {
        if (this.swigCPtr != 0L) {
            if (this.swigCMemOwn) {
                this.swigCMemOwn = false;
                pjsua2JNI.delete_SipMultipartPart(this.swigCPtr);
            }
            this.swigCPtr = 0L;
        }
    }

    public void setHeaders(SipHeaderVector value) {
        pjsua2JNI.SipMultipartPart_headers_set(this.swigCPtr, this, SipHeaderVector.getCPtr(value), value);
    }

    public SipHeaderVector getHeaders() {
        long cPtr = pjsua2JNI.SipMultipartPart_headers_get(this.swigCPtr, this);
        return cPtr == 0L ? null : new SipHeaderVector(cPtr, false);
    }

    public void setContentType(SipMediaType value) {
        pjsua2JNI.SipMultipartPart_contentType_set(this.swigCPtr, this, SipMediaType.getCPtr(value), value);
    }

    public SipMediaType getContentType() {
        long cPtr = pjsua2JNI.SipMultipartPart_contentType_get(this.swigCPtr, this);
        return cPtr == 0L ? null : new SipMediaType(cPtr, false);
    }

    public void setBody(String value) {
        pjsua2JNI.SipMultipartPart_body_set(this.swigCPtr, this, value);
    }

    public String getBody() {
        return pjsua2JNI.SipMultipartPart_body_get(this.swigCPtr, this);
    }

    public SipMultipartPart() {
        this(pjsua2JNI.new_SipMultipartPart(), true);
    }
}

