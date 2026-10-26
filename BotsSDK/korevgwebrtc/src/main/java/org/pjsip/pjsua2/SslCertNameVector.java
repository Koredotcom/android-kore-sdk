/*
 * Decompiled with CFR 0.152.
 */
package org.pjsip.pjsua2;

import org.pjsip.pjsua2.SslCertName;
import org.pjsip.pjsua2.pjsua2JNI;

public class SslCertNameVector {
    private transient long swigCPtr;
    protected transient boolean swigCMemOwn;

    protected SslCertNameVector(long cPtr, boolean cMemoryOwn) {
        this.swigCMemOwn = cMemoryOwn;
        this.swigCPtr = cPtr;
    }

    protected static long getCPtr(SslCertNameVector obj) {
        return obj == null ? 0L : obj.swigCPtr;
    }

    protected void finalize() {
        this.delete();
    }

    public synchronized void delete() {
        if (this.swigCPtr != 0L) {
            if (this.swigCMemOwn) {
                this.swigCMemOwn = false;
                pjsua2JNI.delete_SslCertNameVector(this.swigCPtr);
            }
            this.swigCPtr = 0L;
        }
    }

    public SslCertNameVector() {
        this(pjsua2JNI.new_SslCertNameVector__SWIG_0(), true);
    }

    public SslCertNameVector(long n) {
        this(pjsua2JNI.new_SslCertNameVector__SWIG_1(n), true);
    }

    public long size() {
        return pjsua2JNI.SslCertNameVector_size(this.swigCPtr, this);
    }

    public long capacity() {
        return pjsua2JNI.SslCertNameVector_capacity(this.swigCPtr, this);
    }

    public void reserve(long n) {
        pjsua2JNI.SslCertNameVector_reserve(this.swigCPtr, this, n);
    }

    public boolean isEmpty() {
        return pjsua2JNI.SslCertNameVector_isEmpty(this.swigCPtr, this);
    }

    public void clear() {
        pjsua2JNI.SslCertNameVector_clear(this.swigCPtr, this);
    }

    public void add(SslCertName x) {
        pjsua2JNI.SslCertNameVector_add(this.swigCPtr, this, SslCertName.getCPtr(x), x);
    }

    public SslCertName get(int i) {
        return new SslCertName(pjsua2JNI.SslCertNameVector_get(this.swigCPtr, this, i), false);
    }

    public void set(int i, SslCertName val) {
        pjsua2JNI.SslCertNameVector_set(this.swigCPtr, this, i, SslCertName.getCPtr(val), val);
    }
}

