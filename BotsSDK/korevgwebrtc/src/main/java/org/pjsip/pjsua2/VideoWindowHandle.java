/*
 * Decompiled with CFR 0.152.
 */
package org.pjsip.pjsua2;

import org.pjsip.pjsua2.SWIGTYPE_p_pjmedia_vid_dev_hwnd_type;
import org.pjsip.pjsua2.WindowHandle;
import org.pjsip.pjsua2.pjsua2JNI;

public class VideoWindowHandle {
    private transient long swigCPtr;
    protected transient boolean swigCMemOwn;

    protected VideoWindowHandle(long cPtr, boolean cMemoryOwn) {
        this.swigCMemOwn = cMemoryOwn;
        this.swigCPtr = cPtr;
    }

    protected static long getCPtr(VideoWindowHandle obj) {
        return obj == null ? 0L : obj.swigCPtr;
    }

    protected void finalize() {
        this.delete();
    }

    public synchronized void delete() {
        if (this.swigCPtr != 0L) {
            if (this.swigCMemOwn) {
                this.swigCMemOwn = false;
                pjsua2JNI.delete_VideoWindowHandle(this.swigCPtr);
            }
            this.swigCPtr = 0L;
        }
    }

    public void setType(SWIGTYPE_p_pjmedia_vid_dev_hwnd_type value) {
        pjsua2JNI.VideoWindowHandle_type_set(this.swigCPtr, this, SWIGTYPE_p_pjmedia_vid_dev_hwnd_type.getCPtr(value));
    }

    public SWIGTYPE_p_pjmedia_vid_dev_hwnd_type getType() {
        return new SWIGTYPE_p_pjmedia_vid_dev_hwnd_type(pjsua2JNI.VideoWindowHandle_type_get(this.swigCPtr, this), true);
    }

    public void setHandle(WindowHandle value) {
        pjsua2JNI.VideoWindowHandle_handle_set(this.swigCPtr, this, WindowHandle.getCPtr(value), value);
    }

    public WindowHandle getHandle() {
        long cPtr = pjsua2JNI.VideoWindowHandle_handle_get(this.swigCPtr, this);
        return cPtr == 0L ? null : new WindowHandle(cPtr, false);
    }

    public VideoWindowHandle() {
        this(pjsua2JNI.new_VideoWindowHandle(), true);
    }
}

