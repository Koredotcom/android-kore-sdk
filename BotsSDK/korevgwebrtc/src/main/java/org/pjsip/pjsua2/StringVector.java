/*
 * Decompiled with CFR 0.152.
 */
package org.pjsip.pjsua2;

import org.pjsip.pjsua2.pjsua2JNI;

public class StringVector {
    private transient long swigCPtr;
    protected transient boolean swigCMemOwn;

    protected StringVector(long cPtr, boolean cMemoryOwn) {
        this.swigCMemOwn = cMemoryOwn;
        this.swigCPtr = cPtr;
    }

    protected static long getCPtr(StringVector obj) {
        return obj == null ? 0L : obj.swigCPtr;
    }

    protected void finalize() {
        this.delete();
    }

    public synchronized void delete() {
        if (this.swigCPtr != 0L) {
            if (this.swigCMemOwn) {
                this.swigCMemOwn = false;
                pjsua2JNI.delete_StringVector(this.swigCPtr);
            }
            this.swigCPtr = 0L;
        }
    }

    public StringVector() {
        this(pjsua2JNI.new_StringVector__SWIG_0(), true);
    }

    public StringVector(long n) {
        this(pjsua2JNI.new_StringVector__SWIG_1(n), true);
    }

    public long size() {
        return pjsua2JNI.StringVector_size(this.swigCPtr, this);
    }

    public long capacity() {
        return pjsua2JNI.StringVector_capacity(this.swigCPtr, this);
    }

    public void reserve(long n) {
        pjsua2JNI.StringVector_reserve(this.swigCPtr, this, n);
    }

    public boolean isEmpty() {
        return pjsua2JNI.StringVector_isEmpty(this.swigCPtr, this);
    }

    public void clear() {
        pjsua2JNI.StringVector_clear(this.swigCPtr, this);
    }

    public void add(String x) {
        pjsua2JNI.StringVector_add(this.swigCPtr, this, x);
    }

    public String get(int i) {
        return pjsua2JNI.StringVector_get(this.swigCPtr, this, i);
    }

    public void set(int i, String val) {
        pjsua2JNI.StringVector_set(this.swigCPtr, this, i, val);
    }
}

