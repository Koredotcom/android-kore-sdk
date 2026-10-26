/*
 * Decompiled with CFR 0.152.
 */
package org.pjsip.pjsua2;

import org.pjsip.pjsua2.RegProgressParam;
import org.pjsip.pjsua2.pjsua2JNI;

public class OnIpChangeProgressParam {
    private transient long swigCPtr;
    protected transient boolean swigCMemOwn;

    protected OnIpChangeProgressParam(long cPtr, boolean cMemoryOwn) {
        this.swigCMemOwn = cMemoryOwn;
        this.swigCPtr = cPtr;
    }

    protected static long getCPtr(OnIpChangeProgressParam obj) {
        return obj == null ? 0L : obj.swigCPtr;
    }

    protected void finalize() {
        this.delete();
    }

    public synchronized void delete() {
        if (this.swigCPtr != 0L) {
            if (this.swigCMemOwn) {
                this.swigCMemOwn = false;
                pjsua2JNI.delete_OnIpChangeProgressParam(this.swigCPtr);
            }
            this.swigCPtr = 0L;
        }
    }

    public void setOp(int value) {
        pjsua2JNI.OnIpChangeProgressParam_op_set(this.swigCPtr, this, value);
    }

    public int getOp() {
        return pjsua2JNI.OnIpChangeProgressParam_op_get(this.swigCPtr, this);
    }

    public void setStatus(int value) {
        pjsua2JNI.OnIpChangeProgressParam_status_set(this.swigCPtr, this, value);
    }

    public int getStatus() {
        return pjsua2JNI.OnIpChangeProgressParam_status_get(this.swigCPtr, this);
    }

    public void setTransportId(int value) {
        pjsua2JNI.OnIpChangeProgressParam_transportId_set(this.swigCPtr, this, value);
    }

    public int getTransportId() {
        return pjsua2JNI.OnIpChangeProgressParam_transportId_get(this.swigCPtr, this);
    }

    public void setAccId(int value) {
        pjsua2JNI.OnIpChangeProgressParam_accId_set(this.swigCPtr, this, value);
    }

    public int getAccId() {
        return pjsua2JNI.OnIpChangeProgressParam_accId_get(this.swigCPtr, this);
    }

    public void setCallId(int value) {
        pjsua2JNI.OnIpChangeProgressParam_callId_set(this.swigCPtr, this, value);
    }

    public int getCallId() {
        return pjsua2JNI.OnIpChangeProgressParam_callId_get(this.swigCPtr, this);
    }

    public void setRegInfo(RegProgressParam value) {
        pjsua2JNI.OnIpChangeProgressParam_regInfo_set(this.swigCPtr, this, RegProgressParam.getCPtr(value), value);
    }

    public RegProgressParam getRegInfo() {
        long cPtr = pjsua2JNI.OnIpChangeProgressParam_regInfo_get(this.swigCPtr, this);
        return cPtr == 0L ? null : new RegProgressParam(cPtr, false);
    }

    public OnIpChangeProgressParam() {
        this(pjsua2JNI.new_OnIpChangeProgressParam(), true);
    }
}

