/*
 * Decompiled with CFR 0.152.
 */
package org.pjsip.pjsua2;

import org.pjsip.pjsua2.MediaCoordinate;
import org.pjsip.pjsua2.MediaSize;
import org.pjsip.pjsua2.VideoWindowHandle;
import org.pjsip.pjsua2.pjsua2JNI;

public class VideoWindowInfo {
    private transient long swigCPtr;
    protected transient boolean swigCMemOwn;

    protected VideoWindowInfo(long cPtr, boolean cMemoryOwn) {
        this.swigCMemOwn = cMemoryOwn;
        this.swigCPtr = cPtr;
    }

    protected static long getCPtr(VideoWindowInfo obj) {
        return obj == null ? 0L : obj.swigCPtr;
    }

    protected void finalize() {
        this.delete();
    }

    public synchronized void delete() {
        if (this.swigCPtr != 0L) {
            if (this.swigCMemOwn) {
                this.swigCMemOwn = false;
                pjsua2JNI.delete_VideoWindowInfo(this.swigCPtr);
            }
            this.swigCPtr = 0L;
        }
    }

    public void setIsNative(boolean value) {
        pjsua2JNI.VideoWindowInfo_isNative_set(this.swigCPtr, this, value);
    }

    public boolean getIsNative() {
        return pjsua2JNI.VideoWindowInfo_isNative_get(this.swigCPtr, this);
    }

    public void setWinHandle(VideoWindowHandle value) {
        pjsua2JNI.VideoWindowInfo_winHandle_set(this.swigCPtr, this, VideoWindowHandle.getCPtr(value), value);
    }

    public VideoWindowHandle getWinHandle() {
        long cPtr = pjsua2JNI.VideoWindowInfo_winHandle_get(this.swigCPtr, this);
        return cPtr == 0L ? null : new VideoWindowHandle(cPtr, false);
    }

    public void setRenderDeviceId(int value) {
        pjsua2JNI.VideoWindowInfo_renderDeviceId_set(this.swigCPtr, this, value);
    }

    public int getRenderDeviceId() {
        return pjsua2JNI.VideoWindowInfo_renderDeviceId_get(this.swigCPtr, this);
    }

    public void setShow(boolean value) {
        pjsua2JNI.VideoWindowInfo_show_set(this.swigCPtr, this, value);
    }

    public boolean getShow() {
        return pjsua2JNI.VideoWindowInfo_show_get(this.swigCPtr, this);
    }

    public void setPos(MediaCoordinate value) {
        pjsua2JNI.VideoWindowInfo_pos_set(this.swigCPtr, this, MediaCoordinate.getCPtr(value), value);
    }

    public MediaCoordinate getPos() {
        long cPtr = pjsua2JNI.VideoWindowInfo_pos_get(this.swigCPtr, this);
        return cPtr == 0L ? null : new MediaCoordinate(cPtr, false);
    }

    public void setSize(MediaSize value) {
        pjsua2JNI.VideoWindowInfo_size_set(this.swigCPtr, this, MediaSize.getCPtr(value), value);
    }

    public MediaSize getSize() {
        long cPtr = pjsua2JNI.VideoWindowInfo_size_get(this.swigCPtr, this);
        return cPtr == 0L ? null : new MediaSize(cPtr, false);
    }

    public VideoWindowInfo() {
        this(pjsua2JNI.new_VideoWindowInfo(), true);
    }
}

