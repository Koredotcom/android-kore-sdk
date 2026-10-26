/*
 * Decompiled with CFR 0.152.
 */
package org.pjsip.pjsua2;

import org.pjsip.pjsua2.ToneDigit;
import org.pjsip.pjsua2.pjsua2JNI;

public class ToneDigitVector {
    private transient long swigCPtr;
    protected transient boolean swigCMemOwn;

    protected ToneDigitVector(long cPtr, boolean cMemoryOwn) {
        this.swigCMemOwn = cMemoryOwn;
        this.swigCPtr = cPtr;
    }

    protected static long getCPtr(ToneDigitVector obj) {
        return obj == null ? 0L : obj.swigCPtr;
    }

    protected void finalize() {
        this.delete();
    }

    public synchronized void delete() {
        if (this.swigCPtr != 0L) {
            if (this.swigCMemOwn) {
                this.swigCMemOwn = false;
                pjsua2JNI.delete_ToneDigitVector(this.swigCPtr);
            }
            this.swigCPtr = 0L;
        }
    }

    public ToneDigitVector() {
        this(pjsua2JNI.new_ToneDigitVector__SWIG_0(), true);
    }

    public ToneDigitVector(long n) {
        this(pjsua2JNI.new_ToneDigitVector__SWIG_1(n), true);
    }

    public long size() {
        return pjsua2JNI.ToneDigitVector_size(this.swigCPtr, this);
    }

    public long capacity() {
        return pjsua2JNI.ToneDigitVector_capacity(this.swigCPtr, this);
    }

    public void reserve(long n) {
        pjsua2JNI.ToneDigitVector_reserve(this.swigCPtr, this, n);
    }

    public boolean isEmpty() {
        return pjsua2JNI.ToneDigitVector_isEmpty(this.swigCPtr, this);
    }

    public void clear() {
        pjsua2JNI.ToneDigitVector_clear(this.swigCPtr, this);
    }

    public void add(ToneDigit x) {
        pjsua2JNI.ToneDigitVector_add(this.swigCPtr, this, ToneDigit.getCPtr(x), x);
    }

    public ToneDigit get(int i) {
        return new ToneDigit(pjsua2JNI.ToneDigitVector_get(this.swigCPtr, this, i), false);
    }

    public void set(int i, ToneDigit val) {
        pjsua2JNI.ToneDigitVector_set(this.swigCPtr, this, i, ToneDigit.getCPtr(val), val);
    }
}

