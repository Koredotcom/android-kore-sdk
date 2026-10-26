/*
 * Decompiled with CFR 0.152.
 */
package org.pjsip.pjsua2;

import org.pjsip.pjsua2.SWIGTYPE_p_void;
import org.pjsip.pjsua2.TlsInfo;
import org.pjsip.pjsua2.pjsua2JNI;

public class OnTransportStateParam {
    private transient long swigCPtr;
    protected transient boolean swigCMemOwn;

    protected OnTransportStateParam(long cPtr, boolean cMemoryOwn) {
        this.swigCMemOwn = cMemoryOwn;
        this.swigCPtr = cPtr;
    }

    protected static long getCPtr(OnTransportStateParam obj) {
        return obj == null ? 0L : obj.swigCPtr;
    }

    protected void finalize() {
        this.delete();
    }

    public synchronized void delete() {
        if (this.swigCPtr != 0L) {
            if (this.swigCMemOwn) {
                this.swigCMemOwn = false;
                pjsua2JNI.delete_OnTransportStateParam(this.swigCPtr);
            }
            this.swigCPtr = 0L;
        }
    }

    public void setHnd(SWIGTYPE_p_void value) {
        pjsua2JNI.OnTransportStateParam_hnd_set(this.swigCPtr, this, SWIGTYPE_p_void.getCPtr(value));
    }

    public SWIGTYPE_p_void getHnd() {
        long cPtr = pjsua2JNI.OnTransportStateParam_hnd_get(this.swigCPtr, this);
        return cPtr == 0L ? null : new SWIGTYPE_p_void(cPtr, false);
    }

    public void setType(String value) {
        pjsua2JNI.OnTransportStateParam_type_set(this.swigCPtr, this, value);
    }

    public String getType() {
        return pjsua2JNI.OnTransportStateParam_type_get(this.swigCPtr, this);
    }

    public void setState(int value) {
        pjsua2JNI.OnTransportStateParam_state_set(this.swigCPtr, this, value);
    }

    public int getState() {
        return pjsua2JNI.OnTransportStateParam_state_get(this.swigCPtr, this);
    }

    public void setLastError(int value) {
        pjsua2JNI.OnTransportStateParam_lastError_set(this.swigCPtr, this, value);
    }

    public int getLastError() {
        return pjsua2JNI.OnTransportStateParam_lastError_get(this.swigCPtr, this);
    }

    public void setTlsInfo(TlsInfo value) {
        pjsua2JNI.OnTransportStateParam_tlsInfo_set(this.swigCPtr, this, TlsInfo.getCPtr(value), value);
    }

    public TlsInfo getTlsInfo() {
        long cPtr = pjsua2JNI.OnTransportStateParam_tlsInfo_get(this.swigCPtr, this);
        return cPtr == 0L ? null : new TlsInfo(cPtr, false);
    }

    public OnTransportStateParam() {
        this(pjsua2JNI.new_OnTransportStateParam(), true);
    }
}

