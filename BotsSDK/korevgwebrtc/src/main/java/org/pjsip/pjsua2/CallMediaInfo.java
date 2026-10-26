/*
 * Decompiled with CFR 0.152.
 */
package org.pjsip.pjsua2;

import org.pjsip.pjsua2.VideoWindow;
import org.pjsip.pjsua2.pjsua2JNI;

public class CallMediaInfo {
    private transient long swigCPtr;
    protected transient boolean swigCMemOwn;

    protected CallMediaInfo(long cPtr, boolean cMemoryOwn) {
        this.swigCMemOwn = cMemoryOwn;
        this.swigCPtr = cPtr;
    }

    protected static long getCPtr(CallMediaInfo obj) {
        return obj == null ? 0L : obj.swigCPtr;
    }

    protected void finalize() {
        this.delete();
    }

    public synchronized void delete() {
        if (this.swigCPtr != 0L) {
            if (this.swigCMemOwn) {
                this.swigCMemOwn = false;
                pjsua2JNI.delete_CallMediaInfo(this.swigCPtr);
            }
            this.swigCPtr = 0L;
        }
    }

    public void setIndex(long value) {
        pjsua2JNI.CallMediaInfo_index_set(this.swigCPtr, this, value);
    }

    public long getIndex() {
        return pjsua2JNI.CallMediaInfo_index_get(this.swigCPtr, this);
    }

    public void setType(int value) {
        pjsua2JNI.CallMediaInfo_type_set(this.swigCPtr, this, value);
    }

    public int getType() {
        return pjsua2JNI.CallMediaInfo_type_get(this.swigCPtr, this);
    }

    public void setDir(int value) {
        pjsua2JNI.CallMediaInfo_dir_set(this.swigCPtr, this, value);
    }

    public int getDir() {
        return pjsua2JNI.CallMediaInfo_dir_get(this.swigCPtr, this);
    }

    public void setStatus(int value) {
        pjsua2JNI.CallMediaInfo_status_set(this.swigCPtr, this, value);
    }

    public int getStatus() {
        return pjsua2JNI.CallMediaInfo_status_get(this.swigCPtr, this);
    }

    public void setAudioConfSlot(int value) {
        pjsua2JNI.CallMediaInfo_audioConfSlot_set(this.swigCPtr, this, value);
    }

    public int getAudioConfSlot() {
        return pjsua2JNI.CallMediaInfo_audioConfSlot_get(this.swigCPtr, this);
    }

    public void setVideoIncomingWindowId(int value) {
        pjsua2JNI.CallMediaInfo_videoIncomingWindowId_set(this.swigCPtr, this, value);
    }

    public int getVideoIncomingWindowId() {
        return pjsua2JNI.CallMediaInfo_videoIncomingWindowId_get(this.swigCPtr, this);
    }

    public void setVideoWindow(VideoWindow value) {
        pjsua2JNI.CallMediaInfo_videoWindow_set(this.swigCPtr, this, VideoWindow.getCPtr(value), value);
    }

    public VideoWindow getVideoWindow() {
        long cPtr = pjsua2JNI.CallMediaInfo_videoWindow_get(this.swigCPtr, this);
        return cPtr == 0L ? null : new VideoWindow(cPtr, false);
    }

    public void setVideoCapDev(int value) {
        pjsua2JNI.CallMediaInfo_videoCapDev_set(this.swigCPtr, this, value);
    }

    public int getVideoCapDev() {
        return pjsua2JNI.CallMediaInfo_videoCapDev_get(this.swigCPtr, this);
    }

    public CallMediaInfo() {
        this(pjsua2JNI.new_CallMediaInfo(), true);
    }
}

