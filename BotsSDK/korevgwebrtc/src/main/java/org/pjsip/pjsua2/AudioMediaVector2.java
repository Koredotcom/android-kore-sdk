/*
 * Decompiled with CFR 0.152.
 */
package org.pjsip.pjsua2;

import org.pjsip.pjsua2.AudioMedia;
import org.pjsip.pjsua2.pjsua2JNI;

public class AudioMediaVector2 {
    private transient long swigCPtr;
    protected transient boolean swigCMemOwn;

    protected AudioMediaVector2(long cPtr, boolean cMemoryOwn) {
        this.swigCMemOwn = cMemoryOwn;
        this.swigCPtr = cPtr;
    }

    protected static long getCPtr(AudioMediaVector2 obj) {
        return obj == null ? 0L : obj.swigCPtr;
    }

    protected void finalize() {
        this.delete();
    }

    public synchronized void delete() {
        if (this.swigCPtr != 0L) {
            if (this.swigCMemOwn) {
                this.swigCMemOwn = false;
                pjsua2JNI.delete_AudioMediaVector2(this.swigCPtr);
            }
            this.swigCPtr = 0L;
        }
    }

    public AudioMediaVector2() {
        this(pjsua2JNI.new_AudioMediaVector2__SWIG_0(), true);
    }

    public AudioMediaVector2(long n) {
        this(pjsua2JNI.new_AudioMediaVector2__SWIG_1(n), true);
    }

    public long size() {
        return pjsua2JNI.AudioMediaVector2_size(this.swigCPtr, this);
    }

    public long capacity() {
        return pjsua2JNI.AudioMediaVector2_capacity(this.swigCPtr, this);
    }

    public void reserve(long n) {
        pjsua2JNI.AudioMediaVector2_reserve(this.swigCPtr, this, n);
    }

    public boolean isEmpty() {
        return pjsua2JNI.AudioMediaVector2_isEmpty(this.swigCPtr, this);
    }

    public void clear() {
        pjsua2JNI.AudioMediaVector2_clear(this.swigCPtr, this);
    }

    public void add(AudioMedia x) {
        pjsua2JNI.AudioMediaVector2_add(this.swigCPtr, this, AudioMedia.getCPtr(x), x);
    }

    public AudioMedia get(int i) {
        return new AudioMedia(pjsua2JNI.AudioMediaVector2_get(this.swigCPtr, this, i), false);
    }

    public void set(int i, AudioMedia val) {
        pjsua2JNI.AudioMediaVector2_set(this.swigCPtr, this, i, AudioMedia.getCPtr(val), val);
    }
}

