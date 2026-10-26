/*
 * Decompiled with CFR 0.152.
 */
package org.pjsip.pjsua2;

import org.pjsip.pjsua2.VideoMedia;
import org.pjsip.pjsua2.VideoPreviewOpParam;
import org.pjsip.pjsua2.VideoWindow;
import org.pjsip.pjsua2.pjsua2JNI;

public class VideoPreview {
    private transient long swigCPtr;
    protected transient boolean swigCMemOwn;

    protected VideoPreview(long cPtr, boolean cMemoryOwn) {
        this.swigCMemOwn = cMemoryOwn;
        this.swigCPtr = cPtr;
    }

    protected static long getCPtr(VideoPreview obj) {
        return obj == null ? 0L : obj.swigCPtr;
    }

    protected void finalize() {
        this.delete();
    }

    public synchronized void delete() {
        if (this.swigCPtr != 0L) {
            if (this.swigCMemOwn) {
                this.swigCMemOwn = false;
                pjsua2JNI.delete_VideoPreview(this.swigCPtr);
            }
            this.swigCPtr = 0L;
        }
    }

    public VideoPreview(int dev_id) {
        this(pjsua2JNI.new_VideoPreview(dev_id), true);
    }

    public boolean hasNative() {
        return pjsua2JNI.VideoPreview_hasNative(this.swigCPtr, this);
    }

    public void start(VideoPreviewOpParam param) throws Exception {
        pjsua2JNI.VideoPreview_start(this.swigCPtr, this, VideoPreviewOpParam.getCPtr(param), param);
    }

    public void stop() throws Exception {
        pjsua2JNI.VideoPreview_stop(this.swigCPtr, this);
    }

    public VideoWindow getVideoWindow() {
        return new VideoWindow(pjsua2JNI.VideoPreview_getVideoWindow(this.swigCPtr, this), true);
    }

    public VideoMedia getVideoMedia() throws Exception {
        return new VideoMedia(pjsua2JNI.VideoPreview_getVideoMedia(this.swigCPtr, this), true);
    }
}

