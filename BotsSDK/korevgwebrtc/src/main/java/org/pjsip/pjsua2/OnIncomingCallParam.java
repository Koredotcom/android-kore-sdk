/*
 * Decompiled with CFR 0.152.
 */
package org.pjsip.pjsua2;

import org.pjsip.pjsua2.SipRxData;
import org.pjsip.pjsua2.pjsua2JNI;

public class OnIncomingCallParam {
    private transient long swigCPtr;
    protected transient boolean swigCMemOwn;

    protected OnIncomingCallParam(long cPtr, boolean cMemoryOwn) {
        this.swigCMemOwn = cMemoryOwn;
        this.swigCPtr = cPtr;
    }

    protected static long getCPtr(OnIncomingCallParam obj) {
        return obj == null ? 0L : obj.swigCPtr;
    }

    protected void finalize() {
        this.delete();
    }

    public synchronized void delete() {
        if (this.swigCPtr != 0L) {
            if (this.swigCMemOwn) {
                this.swigCMemOwn = false;
                pjsua2JNI.delete_OnIncomingCallParam(this.swigCPtr);
            }
            this.swigCPtr = 0L;
        }
    }

    public void setCallId(int value) {
        pjsua2JNI.OnIncomingCallParam_callId_set(this.swigCPtr, this, value);
    }

    public int getCallId() {
        return pjsua2JNI.OnIncomingCallParam_callId_get(this.swigCPtr, this);
    }

    public void setRdata(SipRxData value) {
        pjsua2JNI.OnIncomingCallParam_rdata_set(this.swigCPtr, this, SipRxData.getCPtr(value), value);
    }

    public SipRxData getRdata() {
        long cPtr = pjsua2JNI.OnIncomingCallParam_rdata_get(this.swigCPtr, this);
        return cPtr == 0L ? null : new SipRxData(cPtr, false);
    }

    public OnIncomingCallParam() {
        this(pjsua2JNI.new_OnIncomingCallParam(), true);
    }
}

