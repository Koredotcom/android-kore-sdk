/*
 * Decompiled with CFR 0.152.
 */
package org.pjsip.pjsua2;

import org.pjsip.pjsua2.MediaFormatVideo;
import org.pjsip.pjsua2.MediaSize;
import org.pjsip.pjsua2.VideoDevInfo;
import org.pjsip.pjsua2.VideoDevInfoVector2;
import org.pjsip.pjsua2.VideoSwitchParam;
import org.pjsip.pjsua2.pjsua2JNI;

public class VidDevManager {
    private transient long swigCPtr;
    protected transient boolean swigCMemOwn;

    protected VidDevManager(long cPtr, boolean cMemoryOwn) {
        this.swigCMemOwn = cMemoryOwn;
        this.swigCPtr = cPtr;
    }

    protected static long getCPtr(VidDevManager obj) {
        return obj == null ? 0L : obj.swigCPtr;
    }

    public synchronized void delete() {
        if (this.swigCPtr != 0L) {
            if (this.swigCMemOwn) {
                this.swigCMemOwn = false;
                throw new UnsupportedOperationException("C++ destructor does not have public access");
            }
            this.swigCPtr = 0L;
        }
    }

    public void refreshDevs() throws Exception {
        pjsua2JNI.VidDevManager_refreshDevs(this.swigCPtr, this);
    }

    public long getDevCount() {
        return pjsua2JNI.VidDevManager_getDevCount(this.swigCPtr, this);
    }

    public VideoDevInfo getDevInfo(int dev_id) throws Exception {
        return new VideoDevInfo(pjsua2JNI.VidDevManager_getDevInfo(this.swigCPtr, this, dev_id), true);
    }

    public VideoDevInfoVector2 enumDev2() throws Exception {
        return new VideoDevInfoVector2(pjsua2JNI.VidDevManager_enumDev2(this.swigCPtr, this), true);
    }

    public int lookupDev(String drv_name, String dev_name) throws Exception {
        return pjsua2JNI.VidDevManager_lookupDev(this.swigCPtr, this, drv_name, dev_name);
    }

    public String capName(int cap) {
        return pjsua2JNI.VidDevManager_capName(this.swigCPtr, this, cap);
    }

    public void setFormat(int dev_id, MediaFormatVideo format, boolean keep) throws Exception {
        pjsua2JNI.VidDevManager_setFormat(this.swigCPtr, this, dev_id, MediaFormatVideo.getCPtr(format), format, keep);
    }

    public MediaFormatVideo getFormat(int dev_id) throws Exception {
        return new MediaFormatVideo(pjsua2JNI.VidDevManager_getFormat(this.swigCPtr, this, dev_id), true);
    }

    public void setInputScale(int dev_id, MediaSize scale, boolean keep) throws Exception {
        pjsua2JNI.VidDevManager_setInputScale(this.swigCPtr, this, dev_id, MediaSize.getCPtr(scale), scale, keep);
    }

    public MediaSize getInputScale(int dev_id) throws Exception {
        return new MediaSize(pjsua2JNI.VidDevManager_getInputScale(this.swigCPtr, this, dev_id), true);
    }

    public void setOutputWindowFlags(int dev_id, int flags, boolean keep) throws Exception {
        pjsua2JNI.VidDevManager_setOutputWindowFlags(this.swigCPtr, this, dev_id, flags, keep);
    }

    public int getOutputWindowFlags(int dev_id) throws Exception {
        return pjsua2JNI.VidDevManager_getOutputWindowFlags(this.swigCPtr, this, dev_id);
    }

    public void switchDev(int dev_id, VideoSwitchParam param) throws Exception {
        pjsua2JNI.VidDevManager_switchDev(this.swigCPtr, this, dev_id, VideoSwitchParam.getCPtr(param), param);
    }

    public boolean isCaptureActive(int dev_id) {
        return pjsua2JNI.VidDevManager_isCaptureActive(this.swigCPtr, this, dev_id);
    }

    public void setCaptureOrient(int dev_id, int orient, boolean keep) throws Exception {
        pjsua2JNI.VidDevManager_setCaptureOrient__SWIG_0(this.swigCPtr, this, dev_id, orient, keep);
    }

    public void setCaptureOrient(int dev_id, int orient) throws Exception {
        pjsua2JNI.VidDevManager_setCaptureOrient__SWIG_1(this.swigCPtr, this, dev_id, orient);
    }
}

