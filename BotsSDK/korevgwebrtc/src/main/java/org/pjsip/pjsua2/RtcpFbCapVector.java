/*
 * Decompiled with CFR 0.152.
 */
package org.pjsip.pjsua2;

import org.pjsip.pjsua2.RtcpFbCap;
import org.pjsip.pjsua2.pjsua2JNI;

public class RtcpFbCapVector {
    private transient long swigCPtr;
    protected transient boolean swigCMemOwn;

    protected RtcpFbCapVector(long cPtr, boolean cMemoryOwn) {
        this.swigCMemOwn = cMemoryOwn;
        this.swigCPtr = cPtr;
    }

    protected static long getCPtr(RtcpFbCapVector obj) {
        return obj == null ? 0L : obj.swigCPtr;
    }

    protected void finalize() {
        this.delete();
    }

    public synchronized void delete() {
        if (this.swigCPtr != 0L) {
            if (this.swigCMemOwn) {
                this.swigCMemOwn = false;
                pjsua2JNI.delete_RtcpFbCapVector(this.swigCPtr);
            }
            this.swigCPtr = 0L;
        }
    }

    public RtcpFbCapVector() {
        this(pjsua2JNI.new_RtcpFbCapVector__SWIG_0(), true);
    }

    public RtcpFbCapVector(long n) {
        this(pjsua2JNI.new_RtcpFbCapVector__SWIG_1(n), true);
    }

    public long size() {
        return pjsua2JNI.RtcpFbCapVector_size(this.swigCPtr, this);
    }

    public long capacity() {
        return pjsua2JNI.RtcpFbCapVector_capacity(this.swigCPtr, this);
    }

    public void reserve(long n) {
        pjsua2JNI.RtcpFbCapVector_reserve(this.swigCPtr, this, n);
    }

    public boolean isEmpty() {
        return pjsua2JNI.RtcpFbCapVector_isEmpty(this.swigCPtr, this);
    }

    public void clear() {
        pjsua2JNI.RtcpFbCapVector_clear(this.swigCPtr, this);
    }

    public void add(RtcpFbCap x) {
        pjsua2JNI.RtcpFbCapVector_add(this.swigCPtr, this, RtcpFbCap.getCPtr(x), x);
    }

    public RtcpFbCap get(int i) {
        return new RtcpFbCap(pjsua2JNI.RtcpFbCapVector_get(this.swigCPtr, this, i), false);
    }

    public void set(int i, RtcpFbCap val) {
        pjsua2JNI.RtcpFbCapVector_set(this.swigCPtr, this, i, RtcpFbCap.getCPtr(val), val);
    }
}

