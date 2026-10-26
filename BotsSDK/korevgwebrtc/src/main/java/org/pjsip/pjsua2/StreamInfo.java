/*
 * Decompiled with CFR 0.152.
 */
package org.pjsip.pjsua2;

import org.pjsip.pjsua2.CodecParam;
import org.pjsip.pjsua2.VidCodecParam;
import org.pjsip.pjsua2.pjsua2JNI;

public class StreamInfo {
    private transient long swigCPtr;
    protected transient boolean swigCMemOwn;

    protected StreamInfo(long cPtr, boolean cMemoryOwn) {
        this.swigCMemOwn = cMemoryOwn;
        this.swigCPtr = cPtr;
    }

    protected static long getCPtr(StreamInfo obj) {
        return obj == null ? 0L : obj.swigCPtr;
    }

    protected void finalize() {
        this.delete();
    }

    public synchronized void delete() {
        if (this.swigCPtr != 0L) {
            if (this.swigCMemOwn) {
                this.swigCMemOwn = false;
                pjsua2JNI.delete_StreamInfo(this.swigCPtr);
            }
            this.swigCPtr = 0L;
        }
    }

    public void setType(int value) {
        pjsua2JNI.StreamInfo_type_set(this.swigCPtr, this, value);
    }

    public int getType() {
        return pjsua2JNI.StreamInfo_type_get(this.swigCPtr, this);
    }

    public void setProto(int value) {
        pjsua2JNI.StreamInfo_proto_set(this.swigCPtr, this, value);
    }

    public int getProto() {
        return pjsua2JNI.StreamInfo_proto_get(this.swigCPtr, this);
    }

    public void setDir(int value) {
        pjsua2JNI.StreamInfo_dir_set(this.swigCPtr, this, value);
    }

    public int getDir() {
        return pjsua2JNI.StreamInfo_dir_get(this.swigCPtr, this);
    }

    public void setRemoteRtpAddress(String value) {
        pjsua2JNI.StreamInfo_remoteRtpAddress_set(this.swigCPtr, this, value);
    }

    public String getRemoteRtpAddress() {
        return pjsua2JNI.StreamInfo_remoteRtpAddress_get(this.swigCPtr, this);
    }

    public void setRemoteRtcpAddress(String value) {
        pjsua2JNI.StreamInfo_remoteRtcpAddress_set(this.swigCPtr, this, value);
    }

    public String getRemoteRtcpAddress() {
        return pjsua2JNI.StreamInfo_remoteRtcpAddress_get(this.swigCPtr, this);
    }

    public void setTxPt(long value) {
        pjsua2JNI.StreamInfo_txPt_set(this.swigCPtr, this, value);
    }

    public long getTxPt() {
        return pjsua2JNI.StreamInfo_txPt_get(this.swigCPtr, this);
    }

    public void setRxPt(long value) {
        pjsua2JNI.StreamInfo_rxPt_set(this.swigCPtr, this, value);
    }

    public long getRxPt() {
        return pjsua2JNI.StreamInfo_rxPt_get(this.swigCPtr, this);
    }

    public void setCodecName(String value) {
        pjsua2JNI.StreamInfo_codecName_set(this.swigCPtr, this, value);
    }

    public String getCodecName() {
        return pjsua2JNI.StreamInfo_codecName_get(this.swigCPtr, this);
    }

    public void setCodecClockRate(long value) {
        pjsua2JNI.StreamInfo_codecClockRate_set(this.swigCPtr, this, value);
    }

    public long getCodecClockRate() {
        return pjsua2JNI.StreamInfo_codecClockRate_get(this.swigCPtr, this);
    }

    public void setAudCodecParam(CodecParam value) {
        pjsua2JNI.StreamInfo_audCodecParam_set(this.swigCPtr, this, CodecParam.getCPtr(value), value);
    }

    public CodecParam getAudCodecParam() {
        long cPtr = pjsua2JNI.StreamInfo_audCodecParam_get(this.swigCPtr, this);
        return cPtr == 0L ? null : new CodecParam(cPtr, false);
    }

    public void setVidCodecParam(VidCodecParam value) {
        pjsua2JNI.StreamInfo_vidCodecParam_set(this.swigCPtr, this, VidCodecParam.getCPtr(value), value);
    }

    public VidCodecParam getVidCodecParam() {
        long cPtr = pjsua2JNI.StreamInfo_vidCodecParam_get(this.swigCPtr, this);
        return cPtr == 0L ? null : new VidCodecParam(cPtr, false);
    }

    public StreamInfo() {
        this(pjsua2JNI.new_StreamInfo(), true);
    }
}

