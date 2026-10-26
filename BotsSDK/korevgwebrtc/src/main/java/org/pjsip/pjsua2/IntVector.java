/*
 * Decompiled with CFR 0.152.
 */
package org.pjsip.pjsua2;

import org.pjsip.pjsua2.pjsua2JNI;

public class IntVector {
    private transient long swigCPtr;
    protected transient boolean swigCMemOwn;

    protected IntVector(long cPtr, boolean cMemoryOwn) {
        this.swigCMemOwn = cMemoryOwn;
        this.swigCPtr = cPtr;
    }

    protected static long getCPtr(IntVector obj) {
        return obj == null ? 0L : obj.swigCPtr;
    }

    protected void finalize() {
        this.delete();
    }

    public synchronized void delete() {
        if (this.swigCPtr != 0L) {
            if (this.swigCMemOwn) {
                this.swigCMemOwn = false;
                pjsua2JNI.delete_IntVector(this.swigCPtr);
            }
            this.swigCPtr = 0L;
        }
    }

    public IntVector() {
        this(pjsua2JNI.new_IntVector__SWIG_0(), true);
    }

    public IntVector(long n) {
        this(pjsua2JNI.new_IntVector__SWIG_1(n), true);
    }

    public long size() {
        return pjsua2JNI.IntVector_size(this.swigCPtr, this);
    }

    public long capacity() {
        return pjsua2JNI.IntVector_capacity(this.swigCPtr, this);
    }

    public void reserve(long n) {
        pjsua2JNI.IntVector_reserve(this.swigCPtr, this, n);
    }

    public boolean isEmpty() {
        return pjsua2JNI.IntVector_isEmpty(this.swigCPtr, this);
    }

    public void clear() {
        pjsua2JNI.IntVector_clear(this.swigCPtr, this);
    }

    public void add(int x) {
        pjsua2JNI.IntVector_add(this.swigCPtr, this, x);
    }

    public int get(int i) {
        return pjsua2JNI.IntVector_get(this.swigCPtr, this, i);
    }

    public void set(int i, int val) {
        pjsua2JNI.IntVector_set(this.swigCPtr, this, i, val);
    }
}

