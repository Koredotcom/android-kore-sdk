/*
 * Decompiled with CFR 0.152.
 */
package org.pjsip.pjsua2;

import org.pjsip.pjsua2.pjsua2JNI;

public class RtcpFbCap {
    private transient long swigCPtr;
    protected transient boolean swigCMemOwn;

    protected RtcpFbCap(long cPtr, boolean cMemoryOwn) {
        this.swigCMemOwn = cMemoryOwn;
        this.swigCPtr = cPtr;
    }

    protected static long getCPtr(RtcpFbCap obj) {
        return obj == null ? 0L : obj.swigCPtr;
    }

    protected void finalize() {
        this.delete();
    }

    public synchronized void delete() {
        if (this.swigCPtr != 0L) {
            if (this.swigCMemOwn) {
                this.swigCMemOwn = false;
                pjsua2JNI.delete_RtcpFbCap(this.swigCPtr);
            }
            this.swigCPtr = 0L;
        }
    }

    public void setCodecId(String value) {
        pjsua2JNI.RtcpFbCap_codecId_set(this.swigCPtr, this, value);
    }

    public String getCodecId() {
        return pjsua2JNI.RtcpFbCap_codecId_get(this.swigCPtr, this);
    }

    public void setType(int value) {
        pjsua2JNI.RtcpFbCap_type_set(this.swigCPtr, this, value);
    }

    public int getType() {
        return pjsua2JNI.RtcpFbCap_type_get(this.swigCPtr, this);
    }

    public void setTypeName(String value) {
        pjsua2JNI.RtcpFbCap_typeName_set(this.swigCPtr, this, value);
    }

    public String getTypeName() {
        return pjsua2JNI.RtcpFbCap_typeName_get(this.swigCPtr, this);
    }

    public void setParam(String value) {
        pjsua2JNI.RtcpFbCap_param_set(this.swigCPtr, this, value);
    }

    public String getParam() {
        return pjsua2JNI.RtcpFbCap_param_get(this.swigCPtr, this);
    }

    public RtcpFbCap() {
        this(pjsua2JNI.new_RtcpFbCap(), true);
    }
}

