/*
 * Decompiled with CFR 0.152.
 */
package org.pjsip.pjsua2;

import org.pjsip.pjsua2.LossType;
import org.pjsip.pjsua2.MathStat;
import org.pjsip.pjsua2.TimeVal;
import org.pjsip.pjsua2.pjsua2JNI;

public class RtcpStreamStat {
    private transient long swigCPtr;
    protected transient boolean swigCMemOwn;

    protected RtcpStreamStat(long cPtr, boolean cMemoryOwn) {
        this.swigCMemOwn = cMemoryOwn;
        this.swigCPtr = cPtr;
    }

    protected static long getCPtr(RtcpStreamStat obj) {
        return obj == null ? 0L : obj.swigCPtr;
    }

    protected void finalize() {
        this.delete();
    }

    public synchronized void delete() {
        if (this.swigCPtr != 0L) {
            if (this.swigCMemOwn) {
                this.swigCMemOwn = false;
                pjsua2JNI.delete_RtcpStreamStat(this.swigCPtr);
            }
            this.swigCPtr = 0L;
        }
    }

    public void setUpdate(TimeVal value) {
        pjsua2JNI.RtcpStreamStat_update_set(this.swigCPtr, this, TimeVal.getCPtr(value), value);
    }

    public TimeVal getUpdate() {
        long cPtr = pjsua2JNI.RtcpStreamStat_update_get(this.swigCPtr, this);
        return cPtr == 0L ? null : new TimeVal(cPtr, false);
    }

    public void setUpdateCount(long value) {
        pjsua2JNI.RtcpStreamStat_updateCount_set(this.swigCPtr, this, value);
    }

    public long getUpdateCount() {
        return pjsua2JNI.RtcpStreamStat_updateCount_get(this.swigCPtr, this);
    }

    public void setPkt(long value) {
        pjsua2JNI.RtcpStreamStat_pkt_set(this.swigCPtr, this, value);
    }

    public long getPkt() {
        return pjsua2JNI.RtcpStreamStat_pkt_get(this.swigCPtr, this);
    }

    public void setBytes(long value) {
        pjsua2JNI.RtcpStreamStat_bytes_set(this.swigCPtr, this, value);
    }

    public long getBytes() {
        return pjsua2JNI.RtcpStreamStat_bytes_get(this.swigCPtr, this);
    }

    public void setDiscard(long value) {
        pjsua2JNI.RtcpStreamStat_discard_set(this.swigCPtr, this, value);
    }

    public long getDiscard() {
        return pjsua2JNI.RtcpStreamStat_discard_get(this.swigCPtr, this);
    }

    public void setLoss(long value) {
        pjsua2JNI.RtcpStreamStat_loss_set(this.swigCPtr, this, value);
    }

    public long getLoss() {
        return pjsua2JNI.RtcpStreamStat_loss_get(this.swigCPtr, this);
    }

    public void setReorder(long value) {
        pjsua2JNI.RtcpStreamStat_reorder_set(this.swigCPtr, this, value);
    }

    public long getReorder() {
        return pjsua2JNI.RtcpStreamStat_reorder_get(this.swigCPtr, this);
    }

    public void setDup(long value) {
        pjsua2JNI.RtcpStreamStat_dup_set(this.swigCPtr, this, value);
    }

    public long getDup() {
        return pjsua2JNI.RtcpStreamStat_dup_get(this.swigCPtr, this);
    }

    public void setLossPeriodUsec(MathStat value) {
        pjsua2JNI.RtcpStreamStat_lossPeriodUsec_set(this.swigCPtr, this, MathStat.getCPtr(value), value);
    }

    public MathStat getLossPeriodUsec() {
        long cPtr = pjsua2JNI.RtcpStreamStat_lossPeriodUsec_get(this.swigCPtr, this);
        return cPtr == 0L ? null : new MathStat(cPtr, false);
    }

    public void setLossType(LossType value) {
        pjsua2JNI.RtcpStreamStat_lossType_set(this.swigCPtr, this, LossType.getCPtr(value), value);
    }

    public LossType getLossType() {
        long cPtr = pjsua2JNI.RtcpStreamStat_lossType_get(this.swigCPtr, this);
        return cPtr == 0L ? null : new LossType(cPtr, false);
    }

    public void setJitterUsec(MathStat value) {
        pjsua2JNI.RtcpStreamStat_jitterUsec_set(this.swigCPtr, this, MathStat.getCPtr(value), value);
    }

    public MathStat getJitterUsec() {
        long cPtr = pjsua2JNI.RtcpStreamStat_jitterUsec_get(this.swigCPtr, this);
        return cPtr == 0L ? null : new MathStat(cPtr, false);
    }

    public RtcpStreamStat() {
        this(pjsua2JNI.new_RtcpStreamStat(), true);
    }
}

