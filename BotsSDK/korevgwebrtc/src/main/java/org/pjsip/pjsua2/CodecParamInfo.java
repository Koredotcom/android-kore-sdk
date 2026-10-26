/*
 * Decompiled with CFR 0.152.
 */
package org.pjsip.pjsua2;

import org.pjsip.pjsua2.pjsua2JNI;

public class CodecParamInfo {
    private transient long swigCPtr;
    protected transient boolean swigCMemOwn;

    protected CodecParamInfo(long cPtr, boolean cMemoryOwn) {
        this.swigCMemOwn = cMemoryOwn;
        this.swigCPtr = cPtr;
    }

    protected static long getCPtr(CodecParamInfo obj) {
        return obj == null ? 0L : obj.swigCPtr;
    }

    protected void finalize() {
        this.delete();
    }

    public synchronized void delete() {
        if (this.swigCPtr != 0L) {
            if (this.swigCMemOwn) {
                this.swigCMemOwn = false;
                pjsua2JNI.delete_CodecParamInfo(this.swigCPtr);
            }
            this.swigCPtr = 0L;
        }
    }

    public void setClockRate(long value) {
        pjsua2JNI.CodecParamInfo_clockRate_set(this.swigCPtr, this, value);
    }

    public long getClockRate() {
        return pjsua2JNI.CodecParamInfo_clockRate_get(this.swigCPtr, this);
    }

    public void setChannelCnt(long value) {
        pjsua2JNI.CodecParamInfo_channelCnt_set(this.swigCPtr, this, value);
    }

    public long getChannelCnt() {
        return pjsua2JNI.CodecParamInfo_channelCnt_get(this.swigCPtr, this);
    }

    public void setAvgBps(long value) {
        pjsua2JNI.CodecParamInfo_avgBps_set(this.swigCPtr, this, value);
    }

    public long getAvgBps() {
        return pjsua2JNI.CodecParamInfo_avgBps_get(this.swigCPtr, this);
    }

    public void setMaxBps(long value) {
        pjsua2JNI.CodecParamInfo_maxBps_set(this.swigCPtr, this, value);
    }

    public long getMaxBps() {
        return pjsua2JNI.CodecParamInfo_maxBps_get(this.swigCPtr, this);
    }

    public void setMaxRxFrameSize(long value) {
        pjsua2JNI.CodecParamInfo_maxRxFrameSize_set(this.swigCPtr, this, value);
    }

    public long getMaxRxFrameSize() {
        return pjsua2JNI.CodecParamInfo_maxRxFrameSize_get(this.swigCPtr, this);
    }

    public void setFrameLen(long value) {
        pjsua2JNI.CodecParamInfo_frameLen_set(this.swigCPtr, this, value);
    }

    public long getFrameLen() {
        return pjsua2JNI.CodecParamInfo_frameLen_get(this.swigCPtr, this);
    }

    public void setPcmBitsPerSample(long value) {
        pjsua2JNI.CodecParamInfo_pcmBitsPerSample_set(this.swigCPtr, this, value);
    }

    public long getPcmBitsPerSample() {
        return pjsua2JNI.CodecParamInfo_pcmBitsPerSample_get(this.swigCPtr, this);
    }

    public void setPt(long value) {
        pjsua2JNI.CodecParamInfo_pt_set(this.swigCPtr, this, value);
    }

    public long getPt() {
        return pjsua2JNI.CodecParamInfo_pt_get(this.swigCPtr, this);
    }

    public void setFmtId(int value) {
        pjsua2JNI.CodecParamInfo_fmtId_set(this.swigCPtr, this, value);
    }

    public int getFmtId() {
        return pjsua2JNI.CodecParamInfo_fmtId_get(this.swigCPtr, this);
    }

    public CodecParamInfo() {
        this(pjsua2JNI.new_CodecParamInfo(), true);
    }
}

