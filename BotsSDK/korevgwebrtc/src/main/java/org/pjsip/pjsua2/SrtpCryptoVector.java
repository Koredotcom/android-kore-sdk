/*
 * Decompiled with CFR 0.152.
 */
package org.pjsip.pjsua2;

import org.pjsip.pjsua2.SrtpCrypto;
import org.pjsip.pjsua2.pjsua2JNI;

public class SrtpCryptoVector {
    private transient long swigCPtr;
    protected transient boolean swigCMemOwn;

    protected SrtpCryptoVector(long cPtr, boolean cMemoryOwn) {
        this.swigCMemOwn = cMemoryOwn;
        this.swigCPtr = cPtr;
    }

    protected static long getCPtr(SrtpCryptoVector obj) {
        return obj == null ? 0L : obj.swigCPtr;
    }

    protected void finalize() {
        this.delete();
    }

    public synchronized void delete() {
        if (this.swigCPtr != 0L) {
            if (this.swigCMemOwn) {
                this.swigCMemOwn = false;
                pjsua2JNI.delete_SrtpCryptoVector(this.swigCPtr);
            }
            this.swigCPtr = 0L;
        }
    }

    public SrtpCryptoVector() {
        this(pjsua2JNI.new_SrtpCryptoVector__SWIG_0(), true);
    }

    public SrtpCryptoVector(long n) {
        this(pjsua2JNI.new_SrtpCryptoVector__SWIG_1(n), true);
    }

    public long size() {
        return pjsua2JNI.SrtpCryptoVector_size(this.swigCPtr, this);
    }

    public long capacity() {
        return pjsua2JNI.SrtpCryptoVector_capacity(this.swigCPtr, this);
    }

    public void reserve(long n) {
        pjsua2JNI.SrtpCryptoVector_reserve(this.swigCPtr, this, n);
    }

    public boolean isEmpty() {
        return pjsua2JNI.SrtpCryptoVector_isEmpty(this.swigCPtr, this);
    }

    public void clear() {
        pjsua2JNI.SrtpCryptoVector_clear(this.swigCPtr, this);
    }

    public void add(SrtpCrypto x) {
        pjsua2JNI.SrtpCryptoVector_add(this.swigCPtr, this, SrtpCrypto.getCPtr(x), x);
    }

    public SrtpCrypto get(int i) {
        return new SrtpCrypto(pjsua2JNI.SrtpCryptoVector_get(this.swigCPtr, this, i), false);
    }

    public void set(int i, SrtpCrypto val) {
        pjsua2JNI.SrtpCryptoVector_set(this.swigCPtr, this, i, SrtpCrypto.getCPtr(val), val);
    }
}

