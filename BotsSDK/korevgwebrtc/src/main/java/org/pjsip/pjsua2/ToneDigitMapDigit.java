/*
 * Decompiled with CFR 0.152.
 */
package org.pjsip.pjsua2;

import org.pjsip.pjsua2.pjsua2JNI;

public class ToneDigitMapDigit {
    private transient long swigCPtr;
    protected transient boolean swigCMemOwn;

    protected ToneDigitMapDigit(long cPtr, boolean cMemoryOwn) {
        this.swigCMemOwn = cMemoryOwn;
        this.swigCPtr = cPtr;
    }

    protected static long getCPtr(ToneDigitMapDigit obj) {
        return obj == null ? 0L : obj.swigCPtr;
    }

    protected void finalize() {
        this.delete();
    }

    public synchronized void delete() {
        if (this.swigCPtr != 0L) {
            if (this.swigCMemOwn) {
                this.swigCMemOwn = false;
                pjsua2JNI.delete_ToneDigitMapDigit(this.swigCPtr);
            }
            this.swigCPtr = 0L;
        }
    }

    public void setDigit(String value) {
        pjsua2JNI.ToneDigitMapDigit_digit_set(this.swigCPtr, this, value);
    }

    public String getDigit() {
        return pjsua2JNI.ToneDigitMapDigit_digit_get(this.swigCPtr, this);
    }

    public void setFreq1(int value) {
        pjsua2JNI.ToneDigitMapDigit_freq1_set(this.swigCPtr, this, value);
    }

    public int getFreq1() {
        return pjsua2JNI.ToneDigitMapDigit_freq1_get(this.swigCPtr, this);
    }

    public void setFreq2(int value) {
        pjsua2JNI.ToneDigitMapDigit_freq2_set(this.swigCPtr, this, value);
    }

    public int getFreq2() {
        return pjsua2JNI.ToneDigitMapDigit_freq2_get(this.swigCPtr, this);
    }

    public ToneDigitMapDigit() {
        this(pjsua2JNI.new_ToneDigitMapDigit(), true);
    }
}

