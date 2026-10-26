/*
 * Decompiled with CFR 0.152.
 */
package org.pjsip.pjsua2;

import org.pjsip.pjsua2.CallMediaInfo;
import org.pjsip.pjsua2.pjsua2JNI;

public class CallMediaInfoVector {
    private transient long swigCPtr;
    protected transient boolean swigCMemOwn;

    protected CallMediaInfoVector(long cPtr, boolean cMemoryOwn) {
        this.swigCMemOwn = cMemoryOwn;
        this.swigCPtr = cPtr;
    }

    protected static long getCPtr(CallMediaInfoVector obj) {
        return obj == null ? 0L : obj.swigCPtr;
    }

    protected void finalize() {
        this.delete();
    }

    public synchronized void delete() {
        if (this.swigCPtr != 0L) {
            if (this.swigCMemOwn) {
                this.swigCMemOwn = false;
                pjsua2JNI.delete_CallMediaInfoVector(this.swigCPtr);
            }
            this.swigCPtr = 0L;
        }
    }

    public CallMediaInfoVector() {
        this(pjsua2JNI.new_CallMediaInfoVector__SWIG_0(), true);
    }

    public CallMediaInfoVector(long n) {
        this(pjsua2JNI.new_CallMediaInfoVector__SWIG_1(n), true);
    }

    public long size() {
        return pjsua2JNI.CallMediaInfoVector_size(this.swigCPtr, this);
    }

    public long capacity() {
        return pjsua2JNI.CallMediaInfoVector_capacity(this.swigCPtr, this);
    }

    public void reserve(long n) {
        pjsua2JNI.CallMediaInfoVector_reserve(this.swigCPtr, this, n);
    }

    public boolean isEmpty() {
        return pjsua2JNI.CallMediaInfoVector_isEmpty(this.swigCPtr, this);
    }

    public void clear() {
        pjsua2JNI.CallMediaInfoVector_clear(this.swigCPtr, this);
    }

    public void add(CallMediaInfo x) {
        pjsua2JNI.CallMediaInfoVector_add(this.swigCPtr, this, CallMediaInfo.getCPtr(x), x);
    }

    public CallMediaInfo get(int i) {
        return new CallMediaInfo(pjsua2JNI.CallMediaInfoVector_get(this.swigCPtr, this, i), false);
    }

    public void set(int i, CallMediaInfo val) {
        pjsua2JNI.CallMediaInfoVector_set(this.swigCPtr, this, i, CallMediaInfo.getCPtr(val), val);
    }
}

