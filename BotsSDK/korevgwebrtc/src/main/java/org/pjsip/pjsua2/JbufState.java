/*
 * Decompiled with CFR 0.152.
 */
package org.pjsip.pjsua2;

import org.pjsip.pjsua2.pjsua2JNI;

public class JbufState {
    private transient long swigCPtr;
    protected transient boolean swigCMemOwn;

    protected JbufState(long cPtr, boolean cMemoryOwn) {
        this.swigCMemOwn = cMemoryOwn;
        this.swigCPtr = cPtr;
    }

    protected static long getCPtr(JbufState obj) {
        return obj == null ? 0L : obj.swigCPtr;
    }

    protected void finalize() {
        this.delete();
    }

    public synchronized void delete() {
        if (this.swigCPtr != 0L) {
            if (this.swigCMemOwn) {
                this.swigCMemOwn = false;
                pjsua2JNI.delete_JbufState(this.swigCPtr);
            }
            this.swigCPtr = 0L;
        }
    }

    public void setFrameSize(long value) {
        pjsua2JNI.JbufState_frameSize_set(this.swigCPtr, this, value);
    }

    public long getFrameSize() {
        return pjsua2JNI.JbufState_frameSize_get(this.swigCPtr, this);
    }

    public void setMinPrefetch(long value) {
        pjsua2JNI.JbufState_minPrefetch_set(this.swigCPtr, this, value);
    }

    public long getMinPrefetch() {
        return pjsua2JNI.JbufState_minPrefetch_get(this.swigCPtr, this);
    }

    public void setMaxPrefetch(long value) {
        pjsua2JNI.JbufState_maxPrefetch_set(this.swigCPtr, this, value);
    }

    public long getMaxPrefetch() {
        return pjsua2JNI.JbufState_maxPrefetch_get(this.swigCPtr, this);
    }

    public void setBurst(long value) {
        pjsua2JNI.JbufState_burst_set(this.swigCPtr, this, value);
    }

    public long getBurst() {
        return pjsua2JNI.JbufState_burst_get(this.swigCPtr, this);
    }

    public void setPrefetch(long value) {
        pjsua2JNI.JbufState_prefetch_set(this.swigCPtr, this, value);
    }

    public long getPrefetch() {
        return pjsua2JNI.JbufState_prefetch_get(this.swigCPtr, this);
    }

    public void setSize(long value) {
        pjsua2JNI.JbufState_size_set(this.swigCPtr, this, value);
    }

    public long getSize() {
        return pjsua2JNI.JbufState_size_get(this.swigCPtr, this);
    }

    public void setAvgDelayMsec(long value) {
        pjsua2JNI.JbufState_avgDelayMsec_set(this.swigCPtr, this, value);
    }

    public long getAvgDelayMsec() {
        return pjsua2JNI.JbufState_avgDelayMsec_get(this.swigCPtr, this);
    }

    public void setMinDelayMsec(long value) {
        pjsua2JNI.JbufState_minDelayMsec_set(this.swigCPtr, this, value);
    }

    public long getMinDelayMsec() {
        return pjsua2JNI.JbufState_minDelayMsec_get(this.swigCPtr, this);
    }

    public void setMaxDelayMsec(long value) {
        pjsua2JNI.JbufState_maxDelayMsec_set(this.swigCPtr, this, value);
    }

    public long getMaxDelayMsec() {
        return pjsua2JNI.JbufState_maxDelayMsec_get(this.swigCPtr, this);
    }

    public void setDevDelayMsec(long value) {
        pjsua2JNI.JbufState_devDelayMsec_set(this.swigCPtr, this, value);
    }

    public long getDevDelayMsec() {
        return pjsua2JNI.JbufState_devDelayMsec_get(this.swigCPtr, this);
    }

    public void setAvgBurst(long value) {
        pjsua2JNI.JbufState_avgBurst_set(this.swigCPtr, this, value);
    }

    public long getAvgBurst() {
        return pjsua2JNI.JbufState_avgBurst_get(this.swigCPtr, this);
    }

    public void setLost(long value) {
        pjsua2JNI.JbufState_lost_set(this.swigCPtr, this, value);
    }

    public long getLost() {
        return pjsua2JNI.JbufState_lost_get(this.swigCPtr, this);
    }

    public void setDiscard(long value) {
        pjsua2JNI.JbufState_discard_set(this.swigCPtr, this, value);
    }

    public long getDiscard() {
        return pjsua2JNI.JbufState_discard_get(this.swigCPtr, this);
    }

    public void setEmpty(long value) {
        pjsua2JNI.JbufState_empty_set(this.swigCPtr, this, value);
    }

    public long getEmpty() {
        return pjsua2JNI.JbufState_empty_get(this.swigCPtr, this);
    }

    public JbufState() {
        this(pjsua2JNI.new_JbufState(), true);
    }
}

