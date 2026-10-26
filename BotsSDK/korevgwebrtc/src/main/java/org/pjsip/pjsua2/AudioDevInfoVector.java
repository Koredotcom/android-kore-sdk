/*
 * Decompiled with CFR 0.152.
 */
package org.pjsip.pjsua2;

import org.pjsip.pjsua2.AudioDevInfo;
import org.pjsip.pjsua2.pjsua2JNI;

public class AudioDevInfoVector {
    private transient long swigCPtr;
    protected transient boolean swigCMemOwn;

    protected AudioDevInfoVector(long cPtr, boolean cMemoryOwn) {
        this.swigCMemOwn = cMemoryOwn;
        this.swigCPtr = cPtr;
    }

    protected static long getCPtr(AudioDevInfoVector obj) {
        return obj == null ? 0L : obj.swigCPtr;
    }

    protected void finalize() {
        this.delete();
    }

    public synchronized void delete() {
        if (this.swigCPtr != 0L) {
            if (this.swigCMemOwn) {
                this.swigCMemOwn = false;
                pjsua2JNI.delete_AudioDevInfoVector(this.swigCPtr);
            }
            this.swigCPtr = 0L;
        }
    }

    public AudioDevInfoVector() {
        this(pjsua2JNI.new_AudioDevInfoVector__SWIG_0(), true);
    }

    public AudioDevInfoVector(long n) {
        this(pjsua2JNI.new_AudioDevInfoVector__SWIG_1(n), true);
    }

    public long size() {
        return pjsua2JNI.AudioDevInfoVector_size(this.swigCPtr, this);
    }

    public long capacity() {
        return pjsua2JNI.AudioDevInfoVector_capacity(this.swigCPtr, this);
    }

    public void reserve(long n) {
        pjsua2JNI.AudioDevInfoVector_reserve(this.swigCPtr, this, n);
    }

    public boolean isEmpty() {
        return pjsua2JNI.AudioDevInfoVector_isEmpty(this.swigCPtr, this);
    }

    public void clear() {
        pjsua2JNI.AudioDevInfoVector_clear(this.swigCPtr, this);
    }

    public void add(AudioDevInfo x) {
        pjsua2JNI.AudioDevInfoVector_add(this.swigCPtr, this, AudioDevInfo.getCPtr(x), x);
    }

    public AudioDevInfo get(int i) {
        long cPtr = pjsua2JNI.AudioDevInfoVector_get(this.swigCPtr, this, i);
        return cPtr == 0L ? null : new AudioDevInfo(cPtr, false);
    }

    public void set(int i, AudioDevInfo val) {
        pjsua2JNI.AudioDevInfoVector_set(this.swigCPtr, this, i, AudioDevInfo.getCPtr(val), val);
    }
}

