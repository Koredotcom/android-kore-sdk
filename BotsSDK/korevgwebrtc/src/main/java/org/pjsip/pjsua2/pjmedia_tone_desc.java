/*
 * Decompiled with CFR 0.152.
 */
package org.pjsip.pjsua2;

import org.pjsip.pjsua2.pjsua2JNI;

public class pjmedia_tone_desc {
    private transient long swigCPtr;
    protected transient boolean swigCMemOwn;

    protected pjmedia_tone_desc(long cPtr, boolean cMemoryOwn) {
        this.swigCMemOwn = cMemoryOwn;
        this.swigCPtr = cPtr;
    }

    protected static long getCPtr(pjmedia_tone_desc obj) {
        return obj == null ? 0L : obj.swigCPtr;
    }

    protected void finalize() {
        this.delete();
    }

    public synchronized void delete() {
        if (this.swigCPtr != 0L) {
            if (this.swigCMemOwn) {
                this.swigCMemOwn = false;
                pjsua2JNI.delete_pjmedia_tone_desc(this.swigCPtr);
            }
            this.swigCPtr = 0L;
        }
    }

    public void setFreq1(short value) {
        pjsua2JNI.pjmedia_tone_desc_freq1_set(this.swigCPtr, this, value);
    }

    public short getFreq1() {
        return pjsua2JNI.pjmedia_tone_desc_freq1_get(this.swigCPtr, this);
    }

    public void setFreq2(short value) {
        pjsua2JNI.pjmedia_tone_desc_freq2_set(this.swigCPtr, this, value);
    }

    public short getFreq2() {
        return pjsua2JNI.pjmedia_tone_desc_freq2_get(this.swigCPtr, this);
    }

    public void setOn_msec(short value) {
        pjsua2JNI.pjmedia_tone_desc_on_msec_set(this.swigCPtr, this, value);
    }

    public short getOn_msec() {
        return pjsua2JNI.pjmedia_tone_desc_on_msec_get(this.swigCPtr, this);
    }

    public void setOff_msec(short value) {
        pjsua2JNI.pjmedia_tone_desc_off_msec_set(this.swigCPtr, this, value);
    }

    public short getOff_msec() {
        return pjsua2JNI.pjmedia_tone_desc_off_msec_get(this.swigCPtr, this);
    }

    public void setVolume(short value) {
        pjsua2JNI.pjmedia_tone_desc_volume_set(this.swigCPtr, this, value);
    }

    public short getVolume() {
        return pjsua2JNI.pjmedia_tone_desc_volume_get(this.swigCPtr, this);
    }

    public void setFlags(short value) {
        pjsua2JNI.pjmedia_tone_desc_flags_set(this.swigCPtr, this, value);
    }

    public short getFlags() {
        return pjsua2JNI.pjmedia_tone_desc_flags_get(this.swigCPtr, this);
    }

    public pjmedia_tone_desc() {
        this(pjsua2JNI.new_pjmedia_tone_desc(), true);
    }
}

