/*
 * Decompiled with CFR 0.152.
 */
package org.pjsip.pjsua2;

import org.pjsip.pjsua2.MediaFormat;
import org.pjsip.pjsua2.VideoWindowHandle;
import org.pjsip.pjsua2.pjsua2JNI;

public class VideoPreviewOpParam {
    private transient long swigCPtr;
    protected transient boolean swigCMemOwn;

    protected VideoPreviewOpParam(long cPtr, boolean cMemoryOwn) {
        this.swigCMemOwn = cMemoryOwn;
        this.swigCPtr = cPtr;
    }

    protected static long getCPtr(VideoPreviewOpParam obj) {
        return obj == null ? 0L : obj.swigCPtr;
    }

    protected void finalize() {
        this.delete();
    }

    public synchronized void delete() {
        if (this.swigCPtr != 0L) {
            if (this.swigCMemOwn) {
                this.swigCMemOwn = false;
                pjsua2JNI.delete_VideoPreviewOpParam(this.swigCPtr);
            }
            this.swigCPtr = 0L;
        }
    }

    public void setRendId(int value) {
        pjsua2JNI.VideoPreviewOpParam_rendId_set(this.swigCPtr, this, value);
    }

    public int getRendId() {
        return pjsua2JNI.VideoPreviewOpParam_rendId_get(this.swigCPtr, this);
    }

    public void setShow(boolean value) {
        pjsua2JNI.VideoPreviewOpParam_show_set(this.swigCPtr, this, value);
    }

    public boolean getShow() {
        return pjsua2JNI.VideoPreviewOpParam_show_get(this.swigCPtr, this);
    }

    public void setWindowFlags(long value) {
        pjsua2JNI.VideoPreviewOpParam_windowFlags_set(this.swigCPtr, this, value);
    }

    public long getWindowFlags() {
        return pjsua2JNI.VideoPreviewOpParam_windowFlags_get(this.swigCPtr, this);
    }

    public void setFormat(MediaFormat value) {
        pjsua2JNI.VideoPreviewOpParam_format_set(this.swigCPtr, this, MediaFormat.getCPtr(value), value);
    }

    public MediaFormat getFormat() {
        long cPtr = pjsua2JNI.VideoPreviewOpParam_format_get(this.swigCPtr, this);
        return cPtr == 0L ? null : new MediaFormat(cPtr, false);
    }

    public void setWindow(VideoWindowHandle value) {
        pjsua2JNI.VideoPreviewOpParam_window_set(this.swigCPtr, this, VideoWindowHandle.getCPtr(value), value);
    }

    public VideoWindowHandle getWindow() {
        long cPtr = pjsua2JNI.VideoPreviewOpParam_window_get(this.swigCPtr, this);
        return cPtr == 0L ? null : new VideoWindowHandle(cPtr, false);
    }

    public VideoPreviewOpParam() {
        this(pjsua2JNI.new_VideoPreviewOpParam(), true);
    }
}

