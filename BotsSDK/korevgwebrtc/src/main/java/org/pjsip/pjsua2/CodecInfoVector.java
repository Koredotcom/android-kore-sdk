/*
 * Decompiled with CFR 0.152.
 */
package org.pjsip.pjsua2;

import org.pjsip.pjsua2.CodecInfo;
import org.pjsip.pjsua2.pjsua2JNI;

public class CodecInfoVector {
    private transient long swigCPtr;
    protected transient boolean swigCMemOwn;

    protected CodecInfoVector(long cPtr, boolean cMemoryOwn) {
        this.swigCMemOwn = cMemoryOwn;
        this.swigCPtr = cPtr;
    }

    protected static long getCPtr(CodecInfoVector obj) {
        return obj == null ? 0L : obj.swigCPtr;
    }

    protected void finalize() {
        this.delete();
    }

    public synchronized void delete() {
        if (this.swigCPtr != 0L) {
            if (this.swigCMemOwn) {
                this.swigCMemOwn = false;
                pjsua2JNI.delete_CodecInfoVector(this.swigCPtr);
            }
            this.swigCPtr = 0L;
        }
    }

    public CodecInfoVector() {
        this(pjsua2JNI.new_CodecInfoVector__SWIG_0(), true);
    }

    public CodecInfoVector(long n) {
        this(pjsua2JNI.new_CodecInfoVector__SWIG_1(n), true);
    }

    public long size() {
        return pjsua2JNI.CodecInfoVector_size(this.swigCPtr, this);
    }

    public long capacity() {
        return pjsua2JNI.CodecInfoVector_capacity(this.swigCPtr, this);
    }

    public void reserve(long n) {
        pjsua2JNI.CodecInfoVector_reserve(this.swigCPtr, this, n);
    }

    public boolean isEmpty() {
        return pjsua2JNI.CodecInfoVector_isEmpty(this.swigCPtr, this);
    }

    public void clear() {
        pjsua2JNI.CodecInfoVector_clear(this.swigCPtr, this);
    }

    public void add(CodecInfo x) {
        pjsua2JNI.CodecInfoVector_add(this.swigCPtr, this, CodecInfo.getCPtr(x), x);
    }

    public CodecInfo get(int i) {
        long cPtr = pjsua2JNI.CodecInfoVector_get(this.swigCPtr, this, i);
        return cPtr == 0L ? null : new CodecInfo(cPtr, false);
    }

    public void set(int i, CodecInfo val) {
        pjsua2JNI.CodecInfoVector_set(this.swigCPtr, this, i, CodecInfo.getCPtr(val), val);
    }
}

