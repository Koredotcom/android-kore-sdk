/*
 * Decompiled with CFR 0.152.
 */
package org.pjsip.pjsua2;

import org.pjsip.pjsua2.MathStat;
import org.pjsip.pjsua2.RtcpSdes;
import org.pjsip.pjsua2.RtcpStreamStat;
import org.pjsip.pjsua2.TimeVal;
import org.pjsip.pjsua2.pjsua2JNI;

public class RtcpStat {
    private transient long swigCPtr;
    protected transient boolean swigCMemOwn;

    protected RtcpStat(long cPtr, boolean cMemoryOwn) {
        this.swigCMemOwn = cMemoryOwn;
        this.swigCPtr = cPtr;
    }

    protected static long getCPtr(RtcpStat obj) {
        return obj == null ? 0L : obj.swigCPtr;
    }

    protected void finalize() {
        this.delete();
    }

    public synchronized void delete() {
        if (this.swigCPtr != 0L) {
            if (this.swigCMemOwn) {
                this.swigCMemOwn = false;
                pjsua2JNI.delete_RtcpStat(this.swigCPtr);
            }
            this.swigCPtr = 0L;
        }
    }

    public void setStart(TimeVal value) {
        pjsua2JNI.RtcpStat_start_set(this.swigCPtr, this, TimeVal.getCPtr(value), value);
    }

    public TimeVal getStart() {
        long cPtr = pjsua2JNI.RtcpStat_start_get(this.swigCPtr, this);
        return cPtr == 0L ? null : new TimeVal(cPtr, false);
    }

    public void setTxStat(RtcpStreamStat value) {
        pjsua2JNI.RtcpStat_txStat_set(this.swigCPtr, this, RtcpStreamStat.getCPtr(value), value);
    }

    public RtcpStreamStat getTxStat() {
        long cPtr = pjsua2JNI.RtcpStat_txStat_get(this.swigCPtr, this);
        return cPtr == 0L ? null : new RtcpStreamStat(cPtr, false);
    }

    public void setRxStat(RtcpStreamStat value) {
        pjsua2JNI.RtcpStat_rxStat_set(this.swigCPtr, this, RtcpStreamStat.getCPtr(value), value);
    }

    public RtcpStreamStat getRxStat() {
        long cPtr = pjsua2JNI.RtcpStat_rxStat_get(this.swigCPtr, this);
        return cPtr == 0L ? null : new RtcpStreamStat(cPtr, false);
    }

    public void setRttUsec(MathStat value) {
        pjsua2JNI.RtcpStat_rttUsec_set(this.swigCPtr, this, MathStat.getCPtr(value), value);
    }

    public MathStat getRttUsec() {
        long cPtr = pjsua2JNI.RtcpStat_rttUsec_get(this.swigCPtr, this);
        return cPtr == 0L ? null : new MathStat(cPtr, false);
    }

    public void setRtpTxLastTs(long value) {
        pjsua2JNI.RtcpStat_rtpTxLastTs_set(this.swigCPtr, this, value);
    }

    public long getRtpTxLastTs() {
        return pjsua2JNI.RtcpStat_rtpTxLastTs_get(this.swigCPtr, this);
    }

    public void setRtpTxLastSeq(int value) {
        pjsua2JNI.RtcpStat_rtpTxLastSeq_set(this.swigCPtr, this, value);
    }

    public int getRtpTxLastSeq() {
        return pjsua2JNI.RtcpStat_rtpTxLastSeq_get(this.swigCPtr, this);
    }

    public void setRxIpdvUsec(MathStat value) {
        pjsua2JNI.RtcpStat_rxIpdvUsec_set(this.swigCPtr, this, MathStat.getCPtr(value), value);
    }

    public MathStat getRxIpdvUsec() {
        long cPtr = pjsua2JNI.RtcpStat_rxIpdvUsec_get(this.swigCPtr, this);
        return cPtr == 0L ? null : new MathStat(cPtr, false);
    }

    public void setRxRawJitterUsec(MathStat value) {
        pjsua2JNI.RtcpStat_rxRawJitterUsec_set(this.swigCPtr, this, MathStat.getCPtr(value), value);
    }

    public MathStat getRxRawJitterUsec() {
        long cPtr = pjsua2JNI.RtcpStat_rxRawJitterUsec_get(this.swigCPtr, this);
        return cPtr == 0L ? null : new MathStat(cPtr, false);
    }

    public void setPeerSdes(RtcpSdes value) {
        pjsua2JNI.RtcpStat_peerSdes_set(this.swigCPtr, this, RtcpSdes.getCPtr(value), value);
    }

    public RtcpSdes getPeerSdes() {
        long cPtr = pjsua2JNI.RtcpStat_peerSdes_get(this.swigCPtr, this);
        return cPtr == 0L ? null : new RtcpSdes(cPtr, false);
    }

    public RtcpStat() {
        this(pjsua2JNI.new_RtcpStat(), true);
    }
}

