/*
 * Decompiled with CFR 0.152.
 */
package org.pjsip.pjsua2;

import org.pjsip.pjsua2.VideoMedia;
import org.pjsip.pjsua2.pjsua2JNI;

public class VideoMediaVector {
    private transient long swigCPtr;
    protected transient boolean swigCMemOwn;

    protected VideoMediaVector(long cPtr, boolean cMemoryOwn) {
        this.swigCMemOwn = cMemoryOwn;
        this.swigCPtr = cPtr;
    }

    protected static long getCPtr(VideoMediaVector obj) {
        return obj == null ? 0L : obj.swigCPtr;
    }

    protected void finalize() {
        this.delete();
    }

    public synchronized void delete() {
        if (this.swigCPtr != 0L) {
            if (this.swigCMemOwn) {
                this.swigCMemOwn = false;
                pjsua2JNI.delete_VideoMediaVector(this.swigCPtr);
            }
            this.swigCPtr = 0L;
        }
    }

    public VideoMediaVector() {
        this(pjsua2JNI.new_VideoMediaVector__SWIG_0(), true);
    }

    public VideoMediaVector(long n) {
        this(pjsua2JNI.new_VideoMediaVector__SWIG_1(n), true);
    }

    public long size() {
        return pjsua2JNI.VideoMediaVector_size(this.swigCPtr, this);
    }

    public long capacity() {
        return pjsua2JNI.VideoMediaVector_capacity(this.swigCPtr, this);
    }

    public void reserve(long n) {
        pjsua2JNI.VideoMediaVector_reserve(this.swigCPtr, this, n);
    }

    public boolean isEmpty() {
        return pjsua2JNI.VideoMediaVector_isEmpty(this.swigCPtr, this);
    }

    public void clear() {
        pjsua2JNI.VideoMediaVector_clear(this.swigCPtr, this);
    }

    public void add(VideoMedia x) {
        pjsua2JNI.VideoMediaVector_add(this.swigCPtr, this, VideoMedia.getCPtr(x), x);
    }

    public VideoMedia get(int i) {
        return new VideoMedia(pjsua2JNI.VideoMediaVector_get(this.swigCPtr, this, i), false);
    }

    public void set(int i, VideoMedia val) {
        pjsua2JNI.VideoMediaVector_set(this.swigCPtr, this, i, VideoMedia.getCPtr(val), val);
    }
}

