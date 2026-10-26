/*
 * Decompiled with CFR 0.152.
 */
package org.pjsip.pjsua2;

import org.pjsip.pjsua2.SipHeader;
import org.pjsip.pjsua2.pjsua2JNI;

public class SipHeaderVector {
    private transient long swigCPtr;
    protected transient boolean swigCMemOwn;

    protected SipHeaderVector(long cPtr, boolean cMemoryOwn) {
        this.swigCMemOwn = cMemoryOwn;
        this.swigCPtr = cPtr;
    }

    protected static long getCPtr(SipHeaderVector obj) {
        return obj == null ? 0L : obj.swigCPtr;
    }

    protected void finalize() {
        this.delete();
    }

    public synchronized void delete() {
        if (this.swigCPtr != 0L) {
            if (this.swigCMemOwn) {
                this.swigCMemOwn = false;
                pjsua2JNI.delete_SipHeaderVector(this.swigCPtr);
            }
            this.swigCPtr = 0L;
        }
    }

    public SipHeaderVector() {
        this(pjsua2JNI.new_SipHeaderVector__SWIG_0(), true);
    }

    public SipHeaderVector(long n) {
        this(pjsua2JNI.new_SipHeaderVector__SWIG_1(n), true);
    }

    public long size() {
        return pjsua2JNI.SipHeaderVector_size(this.swigCPtr, this);
    }

    public long capacity() {
        return pjsua2JNI.SipHeaderVector_capacity(this.swigCPtr, this);
    }

    public void reserve(long n) {
        pjsua2JNI.SipHeaderVector_reserve(this.swigCPtr, this, n);
    }

    public boolean isEmpty() {
        return pjsua2JNI.SipHeaderVector_isEmpty(this.swigCPtr, this);
    }

    public void clear() {
        pjsua2JNI.SipHeaderVector_clear(this.swigCPtr, this);
    }

    public void add(SipHeader x) {
        pjsua2JNI.SipHeaderVector_add(this.swigCPtr, this, SipHeader.getCPtr(x), x);
    }

    public SipHeader get(int i) {
        return new SipHeader(pjsua2JNI.SipHeaderVector_get(this.swigCPtr, this, i), false);
    }

    public void set(int i, SipHeader val) {
        pjsua2JNI.SipHeaderVector_set(this.swigCPtr, this, i, SipHeader.getCPtr(val), val);
    }
}

