/*
 * Decompiled with CFR 0.152.
 */
package org.pjsip.pjsua2;

import org.pjsip.pjsua2.ToneDesc;
import org.pjsip.pjsua2.pjsua2JNI;

public class ToneDescVector {
    private transient long swigCPtr;
    protected transient boolean swigCMemOwn;

    protected ToneDescVector(long cPtr, boolean cMemoryOwn) {
        this.swigCMemOwn = cMemoryOwn;
        this.swigCPtr = cPtr;
    }

    protected static long getCPtr(ToneDescVector obj) {
        return obj == null ? 0L : obj.swigCPtr;
    }

    protected void finalize() {
        this.delete();
    }

    public synchronized void delete() {
        if (this.swigCPtr != 0L) {
            if (this.swigCMemOwn) {
                this.swigCMemOwn = false;
                pjsua2JNI.delete_ToneDescVector(this.swigCPtr);
            }
            this.swigCPtr = 0L;
        }
    }

    public ToneDescVector() {
        this(pjsua2JNI.new_ToneDescVector__SWIG_0(), true);
    }

    public ToneDescVector(long n) {
        this(pjsua2JNI.new_ToneDescVector__SWIG_1(n), true);
    }

    public long size() {
        return pjsua2JNI.ToneDescVector_size(this.swigCPtr, this);
    }

    public long capacity() {
        return pjsua2JNI.ToneDescVector_capacity(this.swigCPtr, this);
    }

    public void reserve(long n) {
        pjsua2JNI.ToneDescVector_reserve(this.swigCPtr, this, n);
    }

    public boolean isEmpty() {
        return pjsua2JNI.ToneDescVector_isEmpty(this.swigCPtr, this);
    }

    public void clear() {
        pjsua2JNI.ToneDescVector_clear(this.swigCPtr, this);
    }

    public void add(ToneDesc x) {
        pjsua2JNI.ToneDescVector_add(this.swigCPtr, this, ToneDesc.getCPtr(x), x);
    }

    public ToneDesc get(int i) {
        return new ToneDesc(pjsua2JNI.ToneDescVector_get(this.swigCPtr, this, i), false);
    }

    public void set(int i, ToneDesc val) {
        pjsua2JNI.ToneDescVector_set(this.swigCPtr, this, i, ToneDesc.getCPtr(val), val);
    }
}

