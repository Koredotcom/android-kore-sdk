/*
 * Decompiled with CFR 0.152.
 */
package org.pjsip.pjsua2;

import org.pjsip.pjsua2.CodecFmtpVector;
import org.pjsip.pjsua2.pjsua2JNI;

public class CodecParamSetting {
    private transient long swigCPtr;
    protected transient boolean swigCMemOwn;

    protected CodecParamSetting(long cPtr, boolean cMemoryOwn) {
        this.swigCMemOwn = cMemoryOwn;
        this.swigCPtr = cPtr;
    }

    protected static long getCPtr(CodecParamSetting obj) {
        return obj == null ? 0L : obj.swigCPtr;
    }

    protected void finalize() {
        this.delete();
    }

    public synchronized void delete() {
        if (this.swigCPtr != 0L) {
            if (this.swigCMemOwn) {
                this.swigCMemOwn = false;
                pjsua2JNI.delete_CodecParamSetting(this.swigCPtr);
            }
            this.swigCPtr = 0L;
        }
    }

    public void setFrmPerPkt(long value) {
        pjsua2JNI.CodecParamSetting_frmPerPkt_set(this.swigCPtr, this, value);
    }

    public long getFrmPerPkt() {
        return pjsua2JNI.CodecParamSetting_frmPerPkt_get(this.swigCPtr, this);
    }

    public void setVad(boolean value) {
        pjsua2JNI.CodecParamSetting_vad_set(this.swigCPtr, this, value);
    }

    public boolean getVad() {
        return pjsua2JNI.CodecParamSetting_vad_get(this.swigCPtr, this);
    }

    public void setCng(boolean value) {
        pjsua2JNI.CodecParamSetting_cng_set(this.swigCPtr, this, value);
    }

    public boolean getCng() {
        return pjsua2JNI.CodecParamSetting_cng_get(this.swigCPtr, this);
    }

    public void setPenh(boolean value) {
        pjsua2JNI.CodecParamSetting_penh_set(this.swigCPtr, this, value);
    }

    public boolean getPenh() {
        return pjsua2JNI.CodecParamSetting_penh_get(this.swigCPtr, this);
    }

    public void setPlc(boolean value) {
        pjsua2JNI.CodecParamSetting_plc_set(this.swigCPtr, this, value);
    }

    public boolean getPlc() {
        return pjsua2JNI.CodecParamSetting_plc_get(this.swigCPtr, this);
    }

    public void setReserved(boolean value) {
        pjsua2JNI.CodecParamSetting_reserved_set(this.swigCPtr, this, value);
    }

    public boolean getReserved() {
        return pjsua2JNI.CodecParamSetting_reserved_get(this.swigCPtr, this);
    }

    public void setEncFmtp(CodecFmtpVector value) {
        pjsua2JNI.CodecParamSetting_encFmtp_set(this.swigCPtr, this, CodecFmtpVector.getCPtr(value), value);
    }

    public CodecFmtpVector getEncFmtp() {
        long cPtr = pjsua2JNI.CodecParamSetting_encFmtp_get(this.swigCPtr, this);
        return cPtr == 0L ? null : new CodecFmtpVector(cPtr, false);
    }

    public void setDecFmtp(CodecFmtpVector value) {
        pjsua2JNI.CodecParamSetting_decFmtp_set(this.swigCPtr, this, CodecFmtpVector.getCPtr(value), value);
    }

    public CodecFmtpVector getDecFmtp() {
        long cPtr = pjsua2JNI.CodecParamSetting_decFmtp_get(this.swigCPtr, this);
        return cPtr == 0L ? null : new CodecFmtpVector(cPtr, false);
    }

    public CodecParamSetting() {
        this(pjsua2JNI.new_CodecParamSetting(), true);
    }
}

