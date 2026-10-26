/*
 * Decompiled with CFR 0.152.
 */
package org.pjsip.pjsua2;

import org.pjsip.pjsua2.MediaCoordinate;
import org.pjsip.pjsua2.MediaSize;
import org.pjsip.pjsua2.VideoMedia;
import org.pjsip.pjsua2.VideoWindowHandle;
import org.pjsip.pjsua2.VideoWindowInfo;
import org.pjsip.pjsua2.pjsua2JNI;

public class VideoWindow {
    private transient long swigCPtr;
    protected transient boolean swigCMemOwn;

    protected VideoWindow(long cPtr, boolean cMemoryOwn) {
        this.swigCMemOwn = cMemoryOwn;
        this.swigCPtr = cPtr;
    }

    protected static long getCPtr(VideoWindow obj) {
        return obj == null ? 0L : obj.swigCPtr;
    }

    protected void finalize() {
        this.delete();
    }

    public synchronized void delete() {
        if (this.swigCPtr != 0L) {
            if (this.swigCMemOwn) {
                this.swigCMemOwn = false;
                pjsua2JNI.delete_VideoWindow(this.swigCPtr);
            }
            this.swigCPtr = 0L;
        }
    }

    public VideoWindow(int win_id) {
        this(pjsua2JNI.new_VideoWindow(win_id), true);
    }

    public VideoWindowInfo getInfo() throws Exception {
        return new VideoWindowInfo(pjsua2JNI.VideoWindow_getInfo(this.swigCPtr, this), true);
    }

    public VideoMedia getVideoMedia() throws Exception {
        return new VideoMedia(pjsua2JNI.VideoWindow_getVideoMedia(this.swigCPtr, this), true);
    }

    public void Show(boolean show) throws Exception {
        pjsua2JNI.VideoWindow_Show(this.swigCPtr, this, show);
    }

    public void setPos(MediaCoordinate pos) throws Exception {
        pjsua2JNI.VideoWindow_setPos(this.swigCPtr, this, MediaCoordinate.getCPtr(pos), pos);
    }

    public void setSize(MediaSize size) throws Exception {
        pjsua2JNI.VideoWindow_setSize(this.swigCPtr, this, MediaSize.getCPtr(size), size);
    }

    public void rotate(int angle) throws Exception {
        pjsua2JNI.VideoWindow_rotate(this.swigCPtr, this, angle);
    }

    public void setWindow(VideoWindowHandle win) throws Exception {
        pjsua2JNI.VideoWindow_setWindow(this.swigCPtr, this, VideoWindowHandle.getCPtr(win), win);
    }

    public void setFullScreen(boolean enabled) throws Exception {
        pjsua2JNI.VideoWindow_setFullScreen(this.swigCPtr, this, enabled);
    }
}

