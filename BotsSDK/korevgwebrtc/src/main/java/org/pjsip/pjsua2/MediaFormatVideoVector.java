/*
 * Decompiled with CFR 0.152.
 */
package org.pjsip.pjsua2;

import org.pjsip.pjsua2.MediaFormatVideo;
import org.pjsip.pjsua2.pjsua2JNI;

public class MediaFormatVideoVector {
    private transient long swigCPtr;
    protected transient boolean swigCMemOwn;

    protected MediaFormatVideoVector(long cPtr, boolean cMemoryOwn) {
        this.swigCMemOwn = cMemoryOwn;
        this.swigCPtr = cPtr;
    }

    protected static long getCPtr(MediaFormatVideoVector obj) {
        return obj == null ? 0L : obj.swigCPtr;
    }

    protected void finalize() {
        this.delete();
    }

    public synchronized void delete() {
        if (this.swigCPtr != 0L) {
            if (this.swigCMemOwn) {
                this.swigCMemOwn = false;
                pjsua2JNI.delete_MediaFormatVideoVector(this.swigCPtr);
            }
            this.swigCPtr = 0L;
        }
    }

    public MediaFormatVideoVector() {
        this(pjsua2JNI.new_MediaFormatVideoVector__SWIG_0(), true);
    }

    public MediaFormatVideoVector(long n) {
        this(pjsua2JNI.new_MediaFormatVideoVector__SWIG_1(n), true);
    }

    public long size() {
        return pjsua2JNI.MediaFormatVideoVector_size(this.swigCPtr, this);
    }

    public long capacity() {
        return pjsua2JNI.MediaFormatVideoVector_capacity(this.swigCPtr, this);
    }

    public void reserve(long n) {
        pjsua2JNI.MediaFormatVideoVector_reserve(this.swigCPtr, this, n);
    }

    public boolean isEmpty() {
        return pjsua2JNI.MediaFormatVideoVector_isEmpty(this.swigCPtr, this);
    }

    public void clear() {
        pjsua2JNI.MediaFormatVideoVector_clear(this.swigCPtr, this);
    }

    public void add(MediaFormatVideo x) {
        pjsua2JNI.MediaFormatVideoVector_add(this.swigCPtr, this, MediaFormatVideo.getCPtr(x), x);
    }

    public MediaFormatVideo get(int i) {
        return new MediaFormatVideo(pjsua2JNI.MediaFormatVideoVector_get(this.swigCPtr, this, i), false);
    }

    public void set(int i, MediaFormatVideo val) {
        pjsua2JNI.MediaFormatVideoVector_set(this.swigCPtr, this, i, MediaFormatVideo.getCPtr(val), val);
    }
}

