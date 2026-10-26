/*
 * Decompiled with CFR 0.152.
 */
package org.pjsip.pjsua2;

import org.pjsip.pjsua2.MediaFormatAudio;
import org.pjsip.pjsua2.pjsua2JNI;

public class MediaFormatAudioVector {
    private transient long swigCPtr;
    protected transient boolean swigCMemOwn;

    protected MediaFormatAudioVector(long cPtr, boolean cMemoryOwn) {
        this.swigCMemOwn = cMemoryOwn;
        this.swigCPtr = cPtr;
    }

    protected static long getCPtr(MediaFormatAudioVector obj) {
        return obj == null ? 0L : obj.swigCPtr;
    }

    protected void finalize() {
        this.delete();
    }

    public synchronized void delete() {
        if (this.swigCPtr != 0L) {
            if (this.swigCMemOwn) {
                this.swigCMemOwn = false;
                pjsua2JNI.delete_MediaFormatAudioVector(this.swigCPtr);
            }
            this.swigCPtr = 0L;
        }
    }

    public MediaFormatAudioVector() {
        this(pjsua2JNI.new_MediaFormatAudioVector__SWIG_0(), true);
    }

    public MediaFormatAudioVector(long n) {
        this(pjsua2JNI.new_MediaFormatAudioVector__SWIG_1(n), true);
    }

    public long size() {
        return pjsua2JNI.MediaFormatAudioVector_size(this.swigCPtr, this);
    }

    public long capacity() {
        return pjsua2JNI.MediaFormatAudioVector_capacity(this.swigCPtr, this);
    }

    public void reserve(long n) {
        pjsua2JNI.MediaFormatAudioVector_reserve(this.swigCPtr, this, n);
    }

    public boolean isEmpty() {
        return pjsua2JNI.MediaFormatAudioVector_isEmpty(this.swigCPtr, this);
    }

    public void clear() {
        pjsua2JNI.MediaFormatAudioVector_clear(this.swigCPtr, this);
    }

    public void add(MediaFormatAudio x) {
        pjsua2JNI.MediaFormatAudioVector_add(this.swigCPtr, this, MediaFormatAudio.getCPtr(x), x);
    }

    public MediaFormatAudio get(int i) {
        return new MediaFormatAudio(pjsua2JNI.MediaFormatAudioVector_get(this.swigCPtr, this, i), false);
    }

    public void set(int i, MediaFormatAudio val) {
        pjsua2JNI.MediaFormatAudioVector_set(this.swigCPtr, this, i, MediaFormatAudio.getCPtr(val), val);
    }
}

