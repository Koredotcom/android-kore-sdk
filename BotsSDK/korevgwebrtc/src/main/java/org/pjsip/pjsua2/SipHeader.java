/*
 * Decompiled with CFR 0.152.
 */
package org.pjsip.pjsua2;

import org.pjsip.pjsua2.pjsua2JNI;

public class SipHeader {
    private transient long swigCPtr;
    protected transient boolean swigCMemOwn;

    protected SipHeader(long cPtr, boolean cMemoryOwn) {
        this.swigCMemOwn = cMemoryOwn;
        this.swigCPtr = cPtr;
    }

    protected static long getCPtr(SipHeader obj) {
        return obj == null ? 0L : obj.swigCPtr;
    }

    protected void finalize() {
        this.delete();
    }

    public synchronized void delete() {
        if (this.swigCPtr != 0L) {
            if (this.swigCMemOwn) {
                this.swigCMemOwn = false;
                pjsua2JNI.delete_SipHeader(this.swigCPtr);
            }
            this.swigCPtr = 0L;
        }
    }

    public void setHName(String value) {
        pjsua2JNI.SipHeader_hName_set(this.swigCPtr, this, value);
    }

    public String getHName() {
        return pjsua2JNI.SipHeader_hName_get(this.swigCPtr, this);
    }

    public void setHValue(String value) {
        pjsua2JNI.SipHeader_hValue_set(this.swigCPtr, this, value);
    }

    public String getHValue() {
        return pjsua2JNI.SipHeader_hValue_get(this.swigCPtr, this);
    }

    public SipHeader() {
        this(pjsua2JNI.new_SipHeader(), true);
    }
}

