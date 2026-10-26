/*
 * Decompiled with CFR 0.152.
 */
package org.pjsip.pjsua2;

import org.pjsip.pjsua2.AuthCredInfo;
import org.pjsip.pjsua2.pjsua2JNI;

public class AuthCredInfoVector {
    private transient long swigCPtr;
    protected transient boolean swigCMemOwn;

    protected AuthCredInfoVector(long cPtr, boolean cMemoryOwn) {
        this.swigCMemOwn = cMemoryOwn;
        this.swigCPtr = cPtr;
    }

    protected static long getCPtr(AuthCredInfoVector obj) {
        return obj == null ? 0L : obj.swigCPtr;
    }

    protected void finalize() {
        this.delete();
    }

    public synchronized void delete() {
        if (this.swigCPtr != 0L) {
            if (this.swigCMemOwn) {
                this.swigCMemOwn = false;
                pjsua2JNI.delete_AuthCredInfoVector(this.swigCPtr);
            }
            this.swigCPtr = 0L;
        }
    }

    public AuthCredInfoVector() {
        this(pjsua2JNI.new_AuthCredInfoVector__SWIG_0(), true);
    }

    public AuthCredInfoVector(long n) {
        this(pjsua2JNI.new_AuthCredInfoVector__SWIG_1(n), true);
    }

    public long size() {
        return pjsua2JNI.AuthCredInfoVector_size(this.swigCPtr, this);
    }

    public long capacity() {
        return pjsua2JNI.AuthCredInfoVector_capacity(this.swigCPtr, this);
    }

    public void reserve(long n) {
        pjsua2JNI.AuthCredInfoVector_reserve(this.swigCPtr, this, n);
    }

    public boolean isEmpty() {
        return pjsua2JNI.AuthCredInfoVector_isEmpty(this.swigCPtr, this);
    }

    public void clear() {
        pjsua2JNI.AuthCredInfoVector_clear(this.swigCPtr, this);
    }

    public void add(AuthCredInfo x) {
        pjsua2JNI.AuthCredInfoVector_add(this.swigCPtr, this, AuthCredInfo.getCPtr(x), x);
    }

    public AuthCredInfo get(int i) {
        return new AuthCredInfo(pjsua2JNI.AuthCredInfoVector_get(this.swigCPtr, this, i), false);
    }

    public void set(int i, AuthCredInfo val) {
        pjsua2JNI.AuthCredInfoVector_set(this.swigCPtr, this, i, AuthCredInfo.getCPtr(val), val);
    }
}

