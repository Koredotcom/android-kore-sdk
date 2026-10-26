/*
 * Decompiled with CFR 0.152.
 */
package org.pjsip.pjsua2;

import org.pjsip.pjsua2.AudioMedia;
import org.pjsip.pjsua2.ToneDescVector;
import org.pjsip.pjsua2.ToneDigitMapVector;
import org.pjsip.pjsua2.ToneDigitVector;
import org.pjsip.pjsua2.pjsua2JNI;

public class ToneGenerator
extends AudioMedia {
    private transient long swigCPtr;

    protected ToneGenerator(long cPtr, boolean cMemoryOwn) {
        super(pjsua2JNI.ToneGenerator_SWIGUpcast(cPtr), cMemoryOwn);
        this.swigCPtr = cPtr;
    }

    protected static long getCPtr(ToneGenerator obj) {
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
                pjsua2JNI.delete_ToneGenerator(this.swigCPtr);
            }
            this.swigCPtr = 0L;
        }
        super.delete();
    }

    public ToneGenerator() {
        this(pjsua2JNI.new_ToneGenerator(), true);
    }

    public void createToneGenerator(long clock_rate, long channel_count) throws Exception {
        pjsua2JNI.ToneGenerator_createToneGenerator__SWIG_0(this.swigCPtr, this, clock_rate, channel_count);
    }

    public void createToneGenerator(long clock_rate) throws Exception {
        pjsua2JNI.ToneGenerator_createToneGenerator__SWIG_1(this.swigCPtr, this, clock_rate);
    }

    public void createToneGenerator() throws Exception {
        pjsua2JNI.ToneGenerator_createToneGenerator__SWIG_2(this.swigCPtr, this);
    }

    public boolean isBusy() {
        return pjsua2JNI.ToneGenerator_isBusy(this.swigCPtr, this);
    }

    public void stop() throws Exception {
        pjsua2JNI.ToneGenerator_stop(this.swigCPtr, this);
    }

    public void rewind() throws Exception {
        pjsua2JNI.ToneGenerator_rewind(this.swigCPtr, this);
    }

    public void play(ToneDescVector tones, boolean loop) throws Exception {
        pjsua2JNI.ToneGenerator_play__SWIG_0(this.swigCPtr, this, ToneDescVector.getCPtr(tones), tones, loop);
    }

    public void play(ToneDescVector tones) throws Exception {
        pjsua2JNI.ToneGenerator_play__SWIG_1(this.swigCPtr, this, ToneDescVector.getCPtr(tones), tones);
    }

    public void playDigits(ToneDigitVector digits, boolean loop) throws Exception {
        pjsua2JNI.ToneGenerator_playDigits__SWIG_0(this.swigCPtr, this, ToneDigitVector.getCPtr(digits), digits, loop);
    }

    public void playDigits(ToneDigitVector digits) throws Exception {
        pjsua2JNI.ToneGenerator_playDigits__SWIG_1(this.swigCPtr, this, ToneDigitVector.getCPtr(digits), digits);
    }

    public ToneDigitMapVector getDigitMap() throws Exception {
        return new ToneDigitMapVector(pjsua2JNI.ToneGenerator_getDigitMap(this.swigCPtr, this), true);
    }

    public void setDigitMap(ToneDigitMapVector digit_map) throws Exception {
        pjsua2JNI.ToneGenerator_setDigitMap(this.swigCPtr, this, ToneDigitMapVector.getCPtr(digit_map), digit_map);
    }
}

