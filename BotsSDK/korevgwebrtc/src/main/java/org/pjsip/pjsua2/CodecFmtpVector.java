/*
 * Decompiled with CFR 0.152.
 */
package org.pjsip.pjsua2;

import org.pjsip.pjsua2.CodecFmtp;
import org.pjsip.pjsua2.pjsua2JNI;

public class CodecFmtpVector {
    private transient long swigCPtr;
    protected transient boolean swigCMemOwn;

    protected CodecFmtpVector(long cPtr, boolean cMemoryOwn) {
        this.swigCMemOwn = cMemoryOwn;
        this.swigCPtr = cPtr;
    }

    protected static long getCPtr(CodecFmtpVector obj) {
        return obj == null ? 0L : obj.swigCPtr;
    }

    protected void finalize() {
        this.delete();
    }

    public synchronized void delete() {
        if (this.swigCPtr != 0L) {
            if (this.swigCMemOwn) {
                this.swigCMemOwn = false;
                pjsua2JNI.delete_CodecFmtpVector(this.swigCPtr);
            }
            this.swigCPtr = 0L;
        }
    }

    public CodecFmtpVector() {
        this(pjsua2JNI.new_CodecFmtpVector__SWIG_0(), true);
    }

    public CodecFmtpVector(long n) {
        this(pjsua2JNI.new_CodecFmtpVector__SWIG_1(n), true);
    }

    public long size() {
        return pjsua2JNI.CodecFmtpVector_size(this.swigCPtr, this);
    }

    public long capacity() {
        return pjsua2JNI.CodecFmtpVector_capacity(this.swigCPtr, this);
    }

    public void reserve(long n) {
        pjsua2JNI.CodecFmtpVector_reserve(this.swigCPtr, this, n);
    }

    public boolean isEmpty() {
        return pjsua2JNI.CodecFmtpVector_isEmpty(this.swigCPtr, this);
    }

    public void clear() {
        pjsua2JNI.CodecFmtpVector_clear(this.swigCPtr, this);
    }

    public void add(CodecFmtp x) {
        pjsua2JNI.CodecFmtpVector_add(this.swigCPtr, this, CodecFmtp.getCPtr(x), x);
    }

    public CodecFmtp get(int i) {
        return new CodecFmtp(pjsua2JNI.CodecFmtpVector_get(this.swigCPtr, this, i), false);
    }

    public void set(int i, CodecFmtp val) {
        pjsua2JNI.CodecFmtpVector_set(this.swigCPtr, this, i, CodecFmtp.getCPtr(val), val);
    }
}

