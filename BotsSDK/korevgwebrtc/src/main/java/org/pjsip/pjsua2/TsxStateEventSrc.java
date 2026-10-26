/*
 * Decompiled with CFR 0.152.
 */
package org.pjsip.pjsua2;

import org.pjsip.pjsua2.SWIGTYPE_p_void;
import org.pjsip.pjsua2.SipRxData;
import org.pjsip.pjsua2.SipTxData;
import org.pjsip.pjsua2.pjsua2JNI;

public class TsxStateEventSrc {
    private transient long swigCPtr;
    protected transient boolean swigCMemOwn;

    protected TsxStateEventSrc(long cPtr, boolean cMemoryOwn) {
        this.swigCMemOwn = cMemoryOwn;
        this.swigCPtr = cPtr;
    }

    protected static long getCPtr(TsxStateEventSrc obj) {
        return obj == null ? 0L : obj.swigCPtr;
    }

    protected void finalize() {
        this.delete();
    }

    public synchronized void delete() {
        if (this.swigCPtr != 0L) {
            if (this.swigCMemOwn) {
                this.swigCMemOwn = false;
                pjsua2JNI.delete_TsxStateEventSrc(this.swigCPtr);
            }
            this.swigCPtr = 0L;
        }
    }

    public void setRdata(SipRxData value) {
        pjsua2JNI.TsxStateEventSrc_rdata_set(this.swigCPtr, this, SipRxData.getCPtr(value), value);
    }

    public SipRxData getRdata() {
        long cPtr = pjsua2JNI.TsxStateEventSrc_rdata_get(this.swigCPtr, this);
        return cPtr == 0L ? null : new SipRxData(cPtr, false);
    }

    public void setTdata(SipTxData value) {
        pjsua2JNI.TsxStateEventSrc_tdata_set(this.swigCPtr, this, SipTxData.getCPtr(value), value);
    }

    public SipTxData getTdata() {
        long cPtr = pjsua2JNI.TsxStateEventSrc_tdata_get(this.swigCPtr, this);
        return cPtr == 0L ? null : new SipTxData(cPtr, false);
    }

    public void setTimer(SWIGTYPE_p_void value) {
        pjsua2JNI.TsxStateEventSrc_timer_set(this.swigCPtr, this, SWIGTYPE_p_void.getCPtr(value));
    }

    public SWIGTYPE_p_void getTimer() {
        long cPtr = pjsua2JNI.TsxStateEventSrc_timer_get(this.swigCPtr, this);
        return cPtr == 0L ? null : new SWIGTYPE_p_void(cPtr, false);
    }

    public void setStatus(int value) {
        pjsua2JNI.TsxStateEventSrc_status_set(this.swigCPtr, this, value);
    }

    public int getStatus() {
        return pjsua2JNI.TsxStateEventSrc_status_get(this.swigCPtr, this);
    }

    public void setData(SWIGTYPE_p_void value) {
        pjsua2JNI.TsxStateEventSrc_data_set(this.swigCPtr, this, SWIGTYPE_p_void.getCPtr(value));
    }

    public SWIGTYPE_p_void getData() {
        long cPtr = pjsua2JNI.TsxStateEventSrc_data_get(this.swigCPtr, this);
        return cPtr == 0L ? null : new SWIGTYPE_p_void(cPtr, false);
    }

    public TsxStateEventSrc() {
        this(pjsua2JNI.new_TsxStateEventSrc(), true);
    }
}

