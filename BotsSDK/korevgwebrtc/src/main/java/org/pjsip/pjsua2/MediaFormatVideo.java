/*
 * Decompiled with CFR 0.152.
 */
package org.pjsip.pjsua2;

import org.pjsip.pjsua2.MediaFormat;
import org.pjsip.pjsua2.pjsua2JNI;

public class MediaFormatVideo
extends MediaFormat {
    private transient long swigCPtr;

    protected MediaFormatVideo(long cPtr, boolean cMemoryOwn) {
        super(pjsua2JNI.MediaFormatVideo_SWIGUpcast(cPtr), cMemoryOwn);
        this.swigCPtr = cPtr;
    }

    protected static long getCPtr(MediaFormatVideo obj) {
        return obj == null ? 0L : obj.swigCPtr;
    }

    @Override
    protected void finalize() {
        this.delete();
    }

    @Override
    public synchronized void delete() {
        if (this.swigCPtr != 0L) {
            if (this.swigCMemOwn) {
                this.swigCMemOwn = false;
                pjsua2JNI.delete_MediaFormatVideo(this.swigCPtr);
            }
            this.swigCPtr = 0L;
        }
        super.delete();
    }

    public void setWidth(long value) {
        pjsua2JNI.MediaFormatVideo_width_set(this.swigCPtr, this, value);
    }

    public long getWidth() {
        return pjsua2JNI.MediaFormatVideo_width_get(this.swigCPtr, this);
    }

    public void setHeight(long value) {
        pjsua2JNI.MediaFormatVideo_height_set(this.swigCPtr, this, value);
    }

    public long getHeight() {
        return pjsua2JNI.MediaFormatVideo_height_get(this.swigCPtr, this);
    }

    public void setFpsNum(int value) {
        pjsua2JNI.MediaFormatVideo_fpsNum_set(this.swigCPtr, this, value);
    }

    public int getFpsNum() {
        return pjsua2JNI.MediaFormatVideo_fpsNum_get(this.swigCPtr, this);
    }

    public void setFpsDenum(int value) {
        pjsua2JNI.MediaFormatVideo_fpsDenum_set(this.swigCPtr, this, value);
    }

    public int getFpsDenum() {
        return pjsua2JNI.MediaFormatVideo_fpsDenum_get(this.swigCPtr, this);
    }

    public void setAvgBps(long value) {
        pjsua2JNI.MediaFormatVideo_avgBps_set(this.swigCPtr, this, value);
    }

    public long getAvgBps() {
        return pjsua2JNI.MediaFormatVideo_avgBps_get(this.swigCPtr, this);
    }

    public void setMaxBps(long value) {
        pjsua2JNI.MediaFormatVideo_maxBps_set(this.swigCPtr, this, value);
    }

    public long getMaxBps() {
        return pjsua2JNI.MediaFormatVideo_maxBps_get(this.swigCPtr, this);
    }

    public MediaFormatVideo() {
        this(pjsua2JNI.new_MediaFormatVideo(), true);
    }
}

