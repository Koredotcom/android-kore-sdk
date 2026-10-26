/*
 * Decompiled with CFR 0.152.
 */
package org.pjsip.pjsua2;

import org.pjsip.pjsua2.SipMultipartPart;
import org.pjsip.pjsua2.pjsua2JNI;

public class SipMultipartPartVector {
    private transient long swigCPtr;
    protected transient boolean swigCMemOwn;

    protected SipMultipartPartVector(long cPtr, boolean cMemoryOwn) {
        this.swigCMemOwn = cMemoryOwn;
        this.swigCPtr = cPtr;
    }

    protected static long getCPtr(SipMultipartPartVector obj) {
        return obj == null ? 0L : obj.swigCPtr;
    }

    protected void finalize() {
        this.delete();
    }

    public synchronized void delete() {
        if (this.swigCPtr != 0L) {
            if (this.swigCMemOwn) {
                this.swigCMemOwn = false;
                pjsua2JNI.delete_SipMultipartPartVector(this.swigCPtr);
            }
            this.swigCPtr = 0L;
        }
    }

    public SipMultipartPartVector() {
        this(pjsua2JNI.new_SipMultipartPartVector__SWIG_0(), true);
    }

    public SipMultipartPartVector(long n) {
        this(pjsua2JNI.new_SipMultipartPartVector__SWIG_1(n), true);
    }

    public long size() {
        return pjsua2JNI.SipMultipartPartVector_size(this.swigCPtr, this);
    }

    public long capacity() {
        return pjsua2JNI.SipMultipartPartVector_capacity(this.swigCPtr, this);
    }

    public void reserve(long n) {
        pjsua2JNI.SipMultipartPartVector_reserve(this.swigCPtr, this, n);
    }

    public boolean isEmpty() {
        return pjsua2JNI.SipMultipartPartVector_isEmpty(this.swigCPtr, this);
    }

    public void clear() {
        pjsua2JNI.SipMultipartPartVector_clear(this.swigCPtr, this);
    }

    public void add(SipMultipartPart x) {
        pjsua2JNI.SipMultipartPartVector_add(this.swigCPtr, this, SipMultipartPart.getCPtr(x), x);
    }

    public SipMultipartPart get(int i) {
        return new SipMultipartPart(pjsua2JNI.SipMultipartPartVector_get(this.swigCPtr, this, i), false);
    }

    public void set(int i, SipMultipartPart val) {
        pjsua2JNI.SipMultipartPartVector_set(this.swigCPtr, this, i, SipMultipartPart.getCPtr(val), val);
    }
}

