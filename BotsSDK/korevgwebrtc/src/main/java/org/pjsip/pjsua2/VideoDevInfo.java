/*
 * Decompiled with CFR 0.152.
 */
package org.pjsip.pjsua2;

import org.pjsip.pjsua2.MediaFormatVideoVector;
import org.pjsip.pjsua2.pjsua2JNI;

public class VideoDevInfo {
    private transient long swigCPtr;
    protected transient boolean swigCMemOwn;

    protected VideoDevInfo(long cPtr, boolean cMemoryOwn) {
        this.swigCMemOwn = cMemoryOwn;
        this.swigCPtr = cPtr;
    }

    protected static long getCPtr(VideoDevInfo obj) {
        return obj == null ? 0L : obj.swigCPtr;
    }

    protected void finalize() {
        this.delete();
    }

    public synchronized void delete() {
        if (this.swigCPtr != 0L) {
            if (this.swigCMemOwn) {
                this.swigCMemOwn = false;
                pjsua2JNI.delete_VideoDevInfo(this.swigCPtr);
            }
            this.swigCPtr = 0L;
        }
    }

    public void setId(int value) {
        pjsua2JNI.VideoDevInfo_id_set(this.swigCPtr, this, value);
    }

    public int getId() {
        return pjsua2JNI.VideoDevInfo_id_get(this.swigCPtr, this);
    }

    public void setName(String value) {
        pjsua2JNI.VideoDevInfo_name_set(this.swigCPtr, this, value);
    }

    public String getName() {
        return pjsua2JNI.VideoDevInfo_name_get(this.swigCPtr, this);
    }

    public void setDriver(String value) {
        pjsua2JNI.VideoDevInfo_driver_set(this.swigCPtr, this, value);
    }

    public String getDriver() {
        return pjsua2JNI.VideoDevInfo_driver_get(this.swigCPtr, this);
    }

    public void setDir(int value) {
        pjsua2JNI.VideoDevInfo_dir_set(this.swigCPtr, this, value);
    }

    public int getDir() {
        return pjsua2JNI.VideoDevInfo_dir_get(this.swigCPtr, this);
    }

    public void setCaps(long value) {
        pjsua2JNI.VideoDevInfo_caps_set(this.swigCPtr, this, value);
    }

    public long getCaps() {
        return pjsua2JNI.VideoDevInfo_caps_get(this.swigCPtr, this);
    }

    public void setFmt(MediaFormatVideoVector value) {
        pjsua2JNI.VideoDevInfo_fmt_set(this.swigCPtr, this, MediaFormatVideoVector.getCPtr(value), value);
    }

    public MediaFormatVideoVector getFmt() {
        long cPtr = pjsua2JNI.VideoDevInfo_fmt_get(this.swigCPtr, this);
        return cPtr == 0L ? null : new MediaFormatVideoVector(cPtr, false);
    }

    public VideoDevInfo() {
        this(pjsua2JNI.new_VideoDevInfo(), true);
    }
}

