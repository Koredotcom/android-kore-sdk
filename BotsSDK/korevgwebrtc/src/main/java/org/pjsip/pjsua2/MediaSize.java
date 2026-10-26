/*
 * Decompiled with CFR 0.152.
 */
package org.pjsip.pjsua2;

import org.pjsip.pjsua2.pjsua2JNI;

public class MediaSize {
    private transient long swigCPtr;
    protected transient boolean swigCMemOwn;

    protected MediaSize(long cPtr, boolean cMemoryOwn) {
        this.swigCMemOwn = cMemoryOwn;
        this.swigCPtr = cPtr;
    }

    protected static long getCPtr(MediaSize obj) {
        return obj == null ? 0L : obj.swigCPtr;
    }

    protected void finalize() {
        this.delete();
    }

    public synchronized void delete() {
        if (this.swigCPtr != 0L) {
            if (this.swigCMemOwn) {
                this.swigCMemOwn = false;
                pjsua2JNI.delete_MediaSize(this.swigCPtr);
            }
            this.swigCPtr = 0L;
        }
    }

    public void setW(long value) {
        pjsua2JNI.MediaSize_w_set(this.swigCPtr, this, value);
    }

    public long getW() {
        return pjsua2JNI.MediaSize_w_get(this.swigCPtr, this);
    }

    public void setH(long value) {
        pjsua2JNI.MediaSize_h_set(this.swigCPtr, this, value);
    }

    public long getH() {
        return pjsua2JNI.MediaSize_h_get(this.swigCPtr, this);
    }

    public MediaSize() {
        this(pjsua2JNI.new_MediaSize(), true);
    }
}

