/*
 * Decompiled with CFR 0.152.
 */
package org.pjsip.pjsua2;

import org.pjsip.pjsua2.pjsua2JNI;

public class CodecOpusConfig {
    private transient long swigCPtr;
    protected transient boolean swigCMemOwn;

    protected CodecOpusConfig(long cPtr, boolean cMemoryOwn) {
        this.swigCMemOwn = cMemoryOwn;
        this.swigCPtr = cPtr;
    }

    protected static long getCPtr(CodecOpusConfig obj) {
        return obj == null ? 0L : obj.swigCPtr;
    }

    protected void finalize() {
        this.delete();
    }

    public synchronized void delete() {
        if (this.swigCPtr != 0L) {
            if (this.swigCMemOwn) {
                this.swigCMemOwn = false;
                pjsua2JNI.delete_CodecOpusConfig(this.swigCPtr);
            }
            this.swigCPtr = 0L;
        }
    }

    public void setSample_rate(long value) {
        pjsua2JNI.CodecOpusConfig_sample_rate_set(this.swigCPtr, this, value);
    }

    public long getSample_rate() {
        return pjsua2JNI.CodecOpusConfig_sample_rate_get(this.swigCPtr, this);
    }

    public void setChannel_cnt(long value) {
        pjsua2JNI.CodecOpusConfig_channel_cnt_set(this.swigCPtr, this, value);
    }

    public long getChannel_cnt() {
        return pjsua2JNI.CodecOpusConfig_channel_cnt_get(this.swigCPtr, this);
    }

    public void setFrm_ptime(long value) {
        pjsua2JNI.CodecOpusConfig_frm_ptime_set(this.swigCPtr, this, value);
    }

    public long getFrm_ptime() {
        return pjsua2JNI.CodecOpusConfig_frm_ptime_get(this.swigCPtr, this);
    }

    public void setBit_rate(long value) {
        pjsua2JNI.CodecOpusConfig_bit_rate_set(this.swigCPtr, this, value);
    }

    public long getBit_rate() {
        return pjsua2JNI.CodecOpusConfig_bit_rate_get(this.swigCPtr, this);
    }

    public void setPacket_loss(long value) {
        pjsua2JNI.CodecOpusConfig_packet_loss_set(this.swigCPtr, this, value);
    }

    public long getPacket_loss() {
        return pjsua2JNI.CodecOpusConfig_packet_loss_get(this.swigCPtr, this);
    }

    public void setComplexity(long value) {
        pjsua2JNI.CodecOpusConfig_complexity_set(this.swigCPtr, this, value);
    }

    public long getComplexity() {
        return pjsua2JNI.CodecOpusConfig_complexity_get(this.swigCPtr, this);
    }

    public void setCbr(boolean value) {
        pjsua2JNI.CodecOpusConfig_cbr_set(this.swigCPtr, this, value);
    }

    public boolean getCbr() {
        return pjsua2JNI.CodecOpusConfig_cbr_get(this.swigCPtr, this);
    }

    public CodecOpusConfig() {
        this(pjsua2JNI.new_CodecOpusConfig(), true);
    }
}

