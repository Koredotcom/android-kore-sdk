/*
 * Decompiled with CFR 0.152.
 */
package org.pjsip.pjsua2;

import org.pjsip.pjsua2.pjsua2JNI;

public class CallSetting {
    private transient long swigCPtr;
    protected transient boolean swigCMemOwn;

    protected CallSetting(long cPtr, boolean cMemoryOwn) {
        this.swigCMemOwn = cMemoryOwn;
        this.swigCPtr = cPtr;
    }

    protected static long getCPtr(CallSetting obj) {
        return obj == null ? 0L : obj.swigCPtr;
    }

    protected void finalize() {
        this.delete();
    }

    public synchronized void delete() {
        if (this.swigCPtr != 0L) {
            if (this.swigCMemOwn) {
                this.swigCMemOwn = false;
                pjsua2JNI.delete_CallSetting(this.swigCPtr);
            }
            this.swigCPtr = 0L;
        }
    }

    public void setFlag(long value) {
        pjsua2JNI.CallSetting_flag_set(this.swigCPtr, this, value);
    }

    public long getFlag() {
        return pjsua2JNI.CallSetting_flag_get(this.swigCPtr, this);
    }

    public void setReqKeyframeMethod(long value) {
        pjsua2JNI.CallSetting_reqKeyframeMethod_set(this.swigCPtr, this, value);
    }

    public long getReqKeyframeMethod() {
        return pjsua2JNI.CallSetting_reqKeyframeMethod_get(this.swigCPtr, this);
    }

    public void setAudioCount(long value) {
        pjsua2JNI.CallSetting_audioCount_set(this.swigCPtr, this, value);
    }

    public long getAudioCount() {
        return pjsua2JNI.CallSetting_audioCount_get(this.swigCPtr, this);
    }

    public void setVideoCount(long value) {
        pjsua2JNI.CallSetting_videoCount_set(this.swigCPtr, this, value);
    }

    public long getVideoCount() {
        return pjsua2JNI.CallSetting_videoCount_get(this.swigCPtr, this);
    }

    public CallSetting(boolean useDefaultValues) {
        this(pjsua2JNI.new_CallSetting__SWIG_0(useDefaultValues), true);
    }

    public CallSetting() {
        this(pjsua2JNI.new_CallSetting__SWIG_1(), true);
    }

    public boolean isEmpty() {
        return pjsua2JNI.CallSetting_isEmpty(this.swigCPtr, this);
    }
}

