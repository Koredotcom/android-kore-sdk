/*
 * Decompiled with CFR 0.152.
 */
package org.pjsip.pjsua2;

import org.pjsip.pjsua2.pjsua2JNI;

public class VideoSwitchParam {
    private transient long swigCPtr;
    protected transient boolean swigCMemOwn;

    protected VideoSwitchParam(long cPtr, boolean cMemoryOwn) {
        this.swigCMemOwn = cMemoryOwn;
        this.swigCPtr = cPtr;
    }

    protected static long getCPtr(VideoSwitchParam obj) {
        return obj == null ? 0L : obj.swigCPtr;
    }

    protected void finalize() {
        this.delete();
    }

    public synchronized void delete() {
        if (this.swigCPtr != 0L) {
            if (this.swigCMemOwn) {
                this.swigCMemOwn = false;
                pjsua2JNI.delete_VideoSwitchParam(this.swigCPtr);
            }
            this.swigCPtr = 0L;
        }
    }

    public void setTarget_id(int value) {
        pjsua2JNI.VideoSwitchParam_target_id_set(this.swigCPtr, this, value);
    }

    public int getTarget_id() {
        return pjsua2JNI.VideoSwitchParam_target_id_get(this.swigCPtr, this);
    }

    public VideoSwitchParam() {
        this(pjsua2JNI.new_VideoSwitchParam(), true);
    }
}

