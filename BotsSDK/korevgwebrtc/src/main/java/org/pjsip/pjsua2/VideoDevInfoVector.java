/*
 * Decompiled with CFR 0.152.
 */
package org.pjsip.pjsua2;

import org.pjsip.pjsua2.VideoDevInfo;
import org.pjsip.pjsua2.pjsua2JNI;

public class VideoDevInfoVector {
    private transient long swigCPtr;
    protected transient boolean swigCMemOwn;

    protected VideoDevInfoVector(long cPtr, boolean cMemoryOwn) {
        this.swigCMemOwn = cMemoryOwn;
        this.swigCPtr = cPtr;
    }

    protected static long getCPtr(VideoDevInfoVector obj) {
        return obj == null ? 0L : obj.swigCPtr;
    }

    protected void finalize() {
        this.delete();
    }

    public synchronized void delete() {
        if (this.swigCPtr != 0L) {
            if (this.swigCMemOwn) {
                this.swigCMemOwn = false;
                pjsua2JNI.delete_VideoDevInfoVector(this.swigCPtr);
            }
            this.swigCPtr = 0L;
        }
    }

    public VideoDevInfoVector() {
        this(pjsua2JNI.new_VideoDevInfoVector__SWIG_0(), true);
    }

    public VideoDevInfoVector(long n) {
        this(pjsua2JNI.new_VideoDevInfoVector__SWIG_1(n), true);
    }

    public long size() {
        return pjsua2JNI.VideoDevInfoVector_size(this.swigCPtr, this);
    }

    public long capacity() {
        return pjsua2JNI.VideoDevInfoVector_capacity(this.swigCPtr, this);
    }

    public void reserve(long n) {
        pjsua2JNI.VideoDevInfoVector_reserve(this.swigCPtr, this, n);
    }

    public boolean isEmpty() {
        return pjsua2JNI.VideoDevInfoVector_isEmpty(this.swigCPtr, this);
    }

    public void clear() {
        pjsua2JNI.VideoDevInfoVector_clear(this.swigCPtr, this);
    }

    public void add(VideoDevInfo x) {
        pjsua2JNI.VideoDevInfoVector_add(this.swigCPtr, this, VideoDevInfo.getCPtr(x), x);
    }

    public VideoDevInfo get(int i) {
        long cPtr = pjsua2JNI.VideoDevInfoVector_get(this.swigCPtr, this, i);
        return cPtr == 0L ? null : new VideoDevInfo(cPtr, false);
    }

    public void set(int i, VideoDevInfo val) {
        pjsua2JNI.VideoDevInfoVector_set(this.swigCPtr, this, i, VideoDevInfo.getCPtr(val), val);
    }
}

