/*
 * Decompiled with CFR 0.152.
 */
package org.pjsip.pjsua2;

import org.pjsip.pjsua2.CodecInfo;
import org.pjsip.pjsua2.pjsua2JNI;

public class CodecInfoVector2 {
    private transient long swigCPtr;
    protected transient boolean swigCMemOwn;

    protected CodecInfoVector2(long cPtr, boolean cMemoryOwn) {
        this.swigCMemOwn = cMemoryOwn;
        this.swigCPtr = cPtr;
    }

    protected static long getCPtr(CodecInfoVector2 obj) {
        return obj == null ? 0L : obj.swigCPtr;
    }

    protected void finalize() {
        this.delete();
    }

    public synchronized void delete() {
        if (this.swigCPtr != 0L) {
            if (this.swigCMemOwn) {
                this.swigCMemOwn = false;
                pjsua2JNI.delete_CodecInfoVector2(this.swigCPtr);
            }
            this.swigCPtr = 0L;
        }
    }

    public CodecInfoVector2() {
        this(pjsua2JNI.new_CodecInfoVector2__SWIG_0(), true);
    }

    public CodecInfoVector2(long n) {
        this(pjsua2JNI.new_CodecInfoVector2__SWIG_1(n), true);
    }

    public long size() {
        return pjsua2JNI.CodecInfoVector2_size(this.swigCPtr, this);
    }

    public long capacity() {
        return pjsua2JNI.CodecInfoVector2_capacity(this.swigCPtr, this);
    }

    public void reserve(long n) {
        pjsua2JNI.CodecInfoVector2_reserve(this.swigCPtr, this, n);
    }

    public boolean isEmpty() {
        return pjsua2JNI.CodecInfoVector2_isEmpty(this.swigCPtr, this);
    }

    public void clear() {
        pjsua2JNI.CodecInfoVector2_clear(this.swigCPtr, this);
    }

    public void add(CodecInfo x) {
        pjsua2JNI.CodecInfoVector2_add(this.swigCPtr, this, CodecInfo.getCPtr(x), x);
    }

    public CodecInfo get(int i) {
        return new CodecInfo(pjsua2JNI.CodecInfoVector2_get(this.swigCPtr, this, i), false);
    }

    public void set(int i, CodecInfo val) {
        pjsua2JNI.CodecInfoVector2_set(this.swigCPtr, this, i, CodecInfo.getCPtr(val), val);
    }
}

