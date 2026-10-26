/*
 * Decompiled with CFR 0.152.
 */
package org.pjsip.pjsua2;

import org.pjsip.pjsua2.pjsua2JNI;

public class pjmedia_tone_digit {
    private transient long swigCPtr;
    protected transient boolean swigCMemOwn;

    protected pjmedia_tone_digit(long cPtr, boolean cMemoryOwn) {
        this.swigCMemOwn = cMemoryOwn;
        this.swigCPtr = cPtr;
    }

    protected static long getCPtr(pjmedia_tone_digit obj) {
        return obj == null ? 0L : obj.swigCPtr;
    }

    protected void finalize() {
        this.delete();
    }

    public synchronized void delete() {
        if (this.swigCPtr != 0L) {
            if (this.swigCMemOwn) {
                this.swigCMemOwn = false;
                pjsua2JNI.delete_pjmedia_tone_digit(this.swigCPtr);
            }
            this.swigCPtr = 0L;
        }
    }

    public void setDigit(char value) {
        pjsua2JNI.pjmedia_tone_digit_digit_set(this.swigCPtr, this, value);
    }

    public char getDigit() {
        return pjsua2JNI.pjmedia_tone_digit_digit_get(this.swigCPtr, this);
    }

    public void setOn_msec(short value) {
        pjsua2JNI.pjmedia_tone_digit_on_msec_set(this.swigCPtr, this, value);
    }

    public short getOn_msec() {
        return pjsua2JNI.pjmedia_tone_digit_on_msec_get(this.swigCPtr, this);
    }

    public void setOff_msec(short value) {
        pjsua2JNI.pjmedia_tone_digit_off_msec_set(this.swigCPtr, this, value);
    }

    public short getOff_msec() {
        return pjsua2JNI.pjmedia_tone_digit_off_msec_get(this.swigCPtr, this);
    }

    public void setVolume(short value) {
        pjsua2JNI.pjmedia_tone_digit_volume_set(this.swigCPtr, this, value);
    }

    public short getVolume() {
        return pjsua2JNI.pjmedia_tone_digit_volume_get(this.swigCPtr, this);
    }

    public pjmedia_tone_digit() {
        this(pjsua2JNI.new_pjmedia_tone_digit(), true);
    }
}

