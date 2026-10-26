/*
 * Decompiled with CFR 0.152.
 */
package org.pjsip.pjsua2;

import org.pjsip.pjsua2.CallSetting;
import org.pjsip.pjsua2.SdpSession;
import org.pjsip.pjsua2.SipTxOption;
import org.pjsip.pjsua2.pjsua2JNI;

public class CallOpParam {
    private transient long swigCPtr;
    protected transient boolean swigCMemOwn;

    protected CallOpParam(long cPtr, boolean cMemoryOwn) {
        this.swigCMemOwn = cMemoryOwn;
        this.swigCPtr = cPtr;
    }

    protected static long getCPtr(CallOpParam obj) {
        return obj == null ? 0L : obj.swigCPtr;
    }

    protected void finalize() {
        this.delete();
    }

    public synchronized void delete() {
        if (this.swigCPtr != 0L) {
            if (this.swigCMemOwn) {
                this.swigCMemOwn = false;
                pjsua2JNI.delete_CallOpParam(this.swigCPtr);
            }
            this.swigCPtr = 0L;
        }
    }

    public void setOpt(CallSetting value) {
        pjsua2JNI.CallOpParam_opt_set(this.swigCPtr, this, CallSetting.getCPtr(value), value);
    }

    public CallSetting getOpt() {
        long cPtr = pjsua2JNI.CallOpParam_opt_get(this.swigCPtr, this);
        return cPtr == 0L ? null : new CallSetting(cPtr, false);
    }

    public void setStatusCode(int value) {
        pjsua2JNI.CallOpParam_statusCode_set(this.swigCPtr, this, value);
    }

    public int getStatusCode() {
        return pjsua2JNI.CallOpParam_statusCode_get(this.swigCPtr, this);
    }

    public void setReason(String value) {
        pjsua2JNI.CallOpParam_reason_set(this.swigCPtr, this, value);
    }

    public String getReason() {
        return pjsua2JNI.CallOpParam_reason_get(this.swigCPtr, this);
    }

    public void setOptions(long value) {
        pjsua2JNI.CallOpParam_options_set(this.swigCPtr, this, value);
    }

    public long getOptions() {
        return pjsua2JNI.CallOpParam_options_get(this.swigCPtr, this);
    }

    public void setTxOption(SipTxOption value) {
        pjsua2JNI.CallOpParam_txOption_set(this.swigCPtr, this, SipTxOption.getCPtr(value), value);
    }

    public SipTxOption getTxOption() {
        long cPtr = pjsua2JNI.CallOpParam_txOption_get(this.swigCPtr, this);
        return cPtr == 0L ? null : new SipTxOption(cPtr, false);
    }

    public void setSdp(SdpSession value) {
        pjsua2JNI.CallOpParam_sdp_set(this.swigCPtr, this, SdpSession.getCPtr(value), value);
    }

    public SdpSession getSdp() {
        long cPtr = pjsua2JNI.CallOpParam_sdp_get(this.swigCPtr, this);
        return cPtr == 0L ? null : new SdpSession(cPtr, false);
    }

    public CallOpParam(boolean useDefaultCallSetting) {
        this(pjsua2JNI.new_CallOpParam__SWIG_0(useDefaultCallSetting), true);
    }

    public CallOpParam() {
        this(pjsua2JNI.new_CallOpParam__SWIG_1(), true);
    }
}

