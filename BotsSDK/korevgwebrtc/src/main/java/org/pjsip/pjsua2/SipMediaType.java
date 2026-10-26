/*
 * Decompiled with CFR 0.152.
 */
package org.pjsip.pjsua2;

import org.pjsip.pjsua2.pjsua2JNI;

public class SipMediaType {
    private transient long swigCPtr;
    protected transient boolean swigCMemOwn;

    protected SipMediaType(long cPtr, boolean cMemoryOwn) {
        this.swigCMemOwn = cMemoryOwn;
        this.swigCPtr = cPtr;
    }

    protected static long getCPtr(SipMediaType obj) {
        return obj == null ? 0L : obj.swigCPtr;
    }

    protected void finalize() {
        this.delete();
    }

    public synchronized void delete() {
        if (this.swigCPtr != 0L) {
            if (this.swigCMemOwn) {
                this.swigCMemOwn = false;
                pjsua2JNI.delete_SipMediaType(this.swigCPtr);
            }
            this.swigCPtr = 0L;
        }
    }

    public void setType(String value) {
        pjsua2JNI.SipMediaType_type_set(this.swigCPtr, this, value);
    }

    public String getType() {
        return pjsua2JNI.SipMediaType_type_get(this.swigCPtr, this);
    }

    public void setSubType(String value) {
        pjsua2JNI.SipMediaType_subType_set(this.swigCPtr, this, value);
    }

    public String getSubType() {
        return pjsua2JNI.SipMediaType_subType_get(this.swigCPtr, this);
    }

    public SipMediaType() {
        this(pjsua2JNI.new_SipMediaType(), true);
    }
}

