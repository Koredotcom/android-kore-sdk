/*
 * Decompiled with CFR 0.152.
 */
package org.pjsip.pjsua2;

import org.pjsip.pjsua2.ContainerNode;
import org.pjsip.pjsua2.PersistentObject;
import org.pjsip.pjsua2.pjsua2JNI;

public class MediaConfig
extends PersistentObject {
    private transient long swigCPtr;

    protected MediaConfig(long cPtr, boolean cMemoryOwn) {
        super(pjsua2JNI.MediaConfig_SWIGUpcast(cPtr), cMemoryOwn);
        this.swigCPtr = cPtr;
    }

    protected static long getCPtr(MediaConfig obj) {
        return obj == null ? 0L : obj.swigCPtr;
    }

    @Override
    protected void finalize() {
        this.delete();
    }

    @Override
    public synchronized void delete() {
        if (this.swigCPtr != 0L) {
            if (this.swigCMemOwn) {
                this.swigCMemOwn = false;
                pjsua2JNI.delete_MediaConfig(this.swigCPtr);
            }
            this.swigCPtr = 0L;
        }
        super.delete();
    }

    public void setClockRate(long value) {
        pjsua2JNI.MediaConfig_clockRate_set(this.swigCPtr, this, value);
    }

    public long getClockRate() {
        return pjsua2JNI.MediaConfig_clockRate_get(this.swigCPtr, this);
    }

    public void setSndClockRate(long value) {
        pjsua2JNI.MediaConfig_sndClockRate_set(this.swigCPtr, this, value);
    }

    public long getSndClockRate() {
        return pjsua2JNI.MediaConfig_sndClockRate_get(this.swigCPtr, this);
    }

    public void setChannelCount(long value) {
        pjsua2JNI.MediaConfig_channelCount_set(this.swigCPtr, this, value);
    }

    public long getChannelCount() {
        return pjsua2JNI.MediaConfig_channelCount_get(this.swigCPtr, this);
    }

    public void setAudioFramePtime(long value) {
        pjsua2JNI.MediaConfig_audioFramePtime_set(this.swigCPtr, this, value);
    }

    public long getAudioFramePtime() {
        return pjsua2JNI.MediaConfig_audioFramePtime_get(this.swigCPtr, this);
    }

    public void setMaxMediaPorts(long value) {
        pjsua2JNI.MediaConfig_maxMediaPorts_set(this.swigCPtr, this, value);
    }

    public long getMaxMediaPorts() {
        return pjsua2JNI.MediaConfig_maxMediaPorts_get(this.swigCPtr, this);
    }

    public void setHasIoqueue(boolean value) {
        pjsua2JNI.MediaConfig_hasIoqueue_set(this.swigCPtr, this, value);
    }

    public boolean getHasIoqueue() {
        return pjsua2JNI.MediaConfig_hasIoqueue_get(this.swigCPtr, this);
    }

    public void setThreadCnt(long value) {
        pjsua2JNI.MediaConfig_threadCnt_set(this.swigCPtr, this, value);
    }

    public long getThreadCnt() {
        return pjsua2JNI.MediaConfig_threadCnt_get(this.swigCPtr, this);
    }

    public void setQuality(long value) {
        pjsua2JNI.MediaConfig_quality_set(this.swigCPtr, this, value);
    }

    public long getQuality() {
        return pjsua2JNI.MediaConfig_quality_get(this.swigCPtr, this);
    }

    public void setPtime(long value) {
        pjsua2JNI.MediaConfig_ptime_set(this.swigCPtr, this, value);
    }

    public long getPtime() {
        return pjsua2JNI.MediaConfig_ptime_get(this.swigCPtr, this);
    }

    public void setNoVad(boolean value) {
        pjsua2JNI.MediaConfig_noVad_set(this.swigCPtr, this, value);
    }

    public boolean getNoVad() {
        return pjsua2JNI.MediaConfig_noVad_get(this.swigCPtr, this);
    }

    public void setIlbcMode(long value) {
        pjsua2JNI.MediaConfig_ilbcMode_set(this.swigCPtr, this, value);
    }

    public long getIlbcMode() {
        return pjsua2JNI.MediaConfig_ilbcMode_get(this.swigCPtr, this);
    }

    public void setTxDropPct(long value) {
        pjsua2JNI.MediaConfig_txDropPct_set(this.swigCPtr, this, value);
    }

    public long getTxDropPct() {
        return pjsua2JNI.MediaConfig_txDropPct_get(this.swigCPtr, this);
    }

    public void setRxDropPct(long value) {
        pjsua2JNI.MediaConfig_rxDropPct_set(this.swigCPtr, this, value);
    }

    public long getRxDropPct() {
        return pjsua2JNI.MediaConfig_rxDropPct_get(this.swigCPtr, this);
    }

    public void setEcOptions(long value) {
        pjsua2JNI.MediaConfig_ecOptions_set(this.swigCPtr, this, value);
    }

    public long getEcOptions() {
        return pjsua2JNI.MediaConfig_ecOptions_get(this.swigCPtr, this);
    }

    public void setEcTailLen(long value) {
        pjsua2JNI.MediaConfig_ecTailLen_set(this.swigCPtr, this, value);
    }

    public long getEcTailLen() {
        return pjsua2JNI.MediaConfig_ecTailLen_get(this.swigCPtr, this);
    }

    public void setSndRecLatency(long value) {
        pjsua2JNI.MediaConfig_sndRecLatency_set(this.swigCPtr, this, value);
    }

    public long getSndRecLatency() {
        return pjsua2JNI.MediaConfig_sndRecLatency_get(this.swigCPtr, this);
    }

    public void setSndPlayLatency(long value) {
        pjsua2JNI.MediaConfig_sndPlayLatency_set(this.swigCPtr, this, value);
    }

    public long getSndPlayLatency() {
        return pjsua2JNI.MediaConfig_sndPlayLatency_get(this.swigCPtr, this);
    }

    public void setJbInit(int value) {
        pjsua2JNI.MediaConfig_jbInit_set(this.swigCPtr, this, value);
    }

    public int getJbInit() {
        return pjsua2JNI.MediaConfig_jbInit_get(this.swigCPtr, this);
    }

    public void setJbMinPre(int value) {
        pjsua2JNI.MediaConfig_jbMinPre_set(this.swigCPtr, this, value);
    }

    public int getJbMinPre() {
        return pjsua2JNI.MediaConfig_jbMinPre_get(this.swigCPtr, this);
    }

    public void setJbMaxPre(int value) {
        pjsua2JNI.MediaConfig_jbMaxPre_set(this.swigCPtr, this, value);
    }

    public int getJbMaxPre() {
        return pjsua2JNI.MediaConfig_jbMaxPre_get(this.swigCPtr, this);
    }

    public void setJbMax(int value) {
        pjsua2JNI.MediaConfig_jbMax_set(this.swigCPtr, this, value);
    }

    public int getJbMax() {
        return pjsua2JNI.MediaConfig_jbMax_get(this.swigCPtr, this);
    }

    public void setSndAutoCloseTime(int value) {
        pjsua2JNI.MediaConfig_sndAutoCloseTime_set(this.swigCPtr, this, value);
    }

    public int getSndAutoCloseTime() {
        return pjsua2JNI.MediaConfig_sndAutoCloseTime_get(this.swigCPtr, this);
    }

    public void setVidPreviewEnableNative(boolean value) {
        pjsua2JNI.MediaConfig_vidPreviewEnableNative_set(this.swigCPtr, this, value);
    }

    public boolean getVidPreviewEnableNative() {
        return pjsua2JNI.MediaConfig_vidPreviewEnableNative_get(this.swigCPtr, this);
    }

    public MediaConfig() {
        this(pjsua2JNI.new_MediaConfig(), true);
    }

    @Override
    public void readObject(ContainerNode node) throws Exception {
        pjsua2JNI.MediaConfig_readObject(this.swigCPtr, this, ContainerNode.getCPtr(node), node);
    }

    @Override
    public void writeObject(ContainerNode node) throws Exception {
        pjsua2JNI.MediaConfig_writeObject(this.swigCPtr, this, ContainerNode.getCPtr(node), node);
    }
}

