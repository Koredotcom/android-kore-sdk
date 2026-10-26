/*
 * Decompiled with CFR 0.152.
 */
package org.pjsip.pjsua2;

import org.pjsip.pjsua2.ToneDigitMapDigit;
import org.pjsip.pjsua2.pjsua2JNI;

public class ToneDigitMapVector {
    private transient long swigCPtr;
    protected transient boolean swigCMemOwn;

    protected ToneDigitMapVector(long cPtr, boolean cMemoryOwn) {
        this.swigCMemOwn = cMemoryOwn;
        this.swigCPtr = cPtr;
    }

    protected static long getCPtr(ToneDigitMapVector obj) {
        return obj == null ? 0L : obj.swigCPtr;
    }

    protected void finalize() {
        this.delete();
    }

    public synchronized void delete() {
        if (this.swigCPtr != 0L) {
            if (this.swigCMemOwn) {
                this.swigCMemOwn = false;
                pjsua2JNI.delete_ToneDigitMapVector(this.swigCPtr);
            }
            this.swigCPtr = 0L;
        }
    }

    public ToneDigitMapVector() {
        this(pjsua2JNI.new_ToneDigitMapVector__SWIG_0(), true);
    }

    public ToneDigitMapVector(long n) {
        this(pjsua2JNI.new_ToneDigitMapVector__SWIG_1(n), true);
    }

    public long size() {
        return pjsua2JNI.ToneDigitMapVector_size(this.swigCPtr, this);
    }

    public long capacity() {
        return pjsua2JNI.ToneDigitMapVector_capacity(this.swigCPtr, this);
    }

    public void reserve(long n) {
        pjsua2JNI.ToneDigitMapVector_reserve(this.swigCPtr, this, n);
    }

    public boolean isEmpty() {
        return pjsua2JNI.ToneDigitMapVector_isEmpty(this.swigCPtr, this);
    }

    public void clear() {
        pjsua2JNI.ToneDigitMapVector_clear(this.swigCPtr, this);
    }

    public void add(ToneDigitMapDigit x) {
        pjsua2JNI.ToneDigitMapVector_add(this.swigCPtr, this, ToneDigitMapDigit.getCPtr(x), x);
    }

    public ToneDigitMapDigit get(int i) {
        return new ToneDigitMapDigit(pjsua2JNI.ToneDigitMapVector_get(this.swigCPtr, this, i), false);
    }

    public void set(int i, ToneDigitMapDigit val) {
        pjsua2JNI.ToneDigitMapVector_set(this.swigCPtr, this, i, ToneDigitMapDigit.getCPtr(val), val);
    }
}

