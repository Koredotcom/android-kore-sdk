/*
 * Decompiled with CFR 0.152.
 */
package org.pjsip.pjsua2;

import org.pjsip.pjsua2.MediaFormatAudioVector;
import org.pjsip.pjsua2.pjsua2JNI;

public class AudioDevInfo {
    private transient long swigCPtr;
    protected transient boolean swigCMemOwn;

    protected AudioDevInfo(long cPtr, boolean cMemoryOwn) {
        this.swigCMemOwn = cMemoryOwn;
        this.swigCPtr = cPtr;
    }

    protected static long getCPtr(AudioDevInfo obj) {
        return obj == null ? 0L : obj.swigCPtr;
    }

    protected void finalize() {
        this.delete();
    }

    public synchronized void delete() {
        if (this.swigCPtr != 0L) {
            if (this.swigCMemOwn) {
                this.swigCMemOwn = false;
                pjsua2JNI.delete_AudioDevInfo(this.swigCPtr);
            }
            this.swigCPtr = 0L;
        }
    }

    public void setName(String value) {
        pjsua2JNI.AudioDevInfo_name_set(this.swigCPtr, this, value);
    }

    public String getName() {
        return pjsua2JNI.AudioDevInfo_name_get(this.swigCPtr, this);
    }

    public void setInputCount(long value) {
        pjsua2JNI.AudioDevInfo_inputCount_set(this.swigCPtr, this, value);
    }

    public long getInputCount() {
        return pjsua2JNI.AudioDevInfo_inputCount_get(this.swigCPtr, this);
    }

    public void setOutputCount(long value) {
        pjsua2JNI.AudioDevInfo_outputCount_set(this.swigCPtr, this, value);
    }

    public long getOutputCount() {
        return pjsua2JNI.AudioDevInfo_outputCount_get(this.swigCPtr, this);
    }

    public void setDefaultSamplesPerSec(long value) {
        pjsua2JNI.AudioDevInfo_defaultSamplesPerSec_set(this.swigCPtr, this, value);
    }

    public long getDefaultSamplesPerSec() {
        return pjsua2JNI.AudioDevInfo_defaultSamplesPerSec_get(this.swigCPtr, this);
    }

    public void setDriver(String value) {
        pjsua2JNI.AudioDevInfo_driver_set(this.swigCPtr, this, value);
    }

    public String getDriver() {
        return pjsua2JNI.AudioDevInfo_driver_get(this.swigCPtr, this);
    }

    public void setCaps(long value) {
        pjsua2JNI.AudioDevInfo_caps_set(this.swigCPtr, this, value);
    }

    public long getCaps() {
        return pjsua2JNI.AudioDevInfo_caps_get(this.swigCPtr, this);
    }

    public void setRoutes(long value) {
        pjsua2JNI.AudioDevInfo_routes_set(this.swigCPtr, this, value);
    }

    public long getRoutes() {
        return pjsua2JNI.AudioDevInfo_routes_get(this.swigCPtr, this);
    }

    public void setExtFmt(MediaFormatAudioVector value) {
        pjsua2JNI.AudioDevInfo_extFmt_set(this.swigCPtr, this, MediaFormatAudioVector.getCPtr(value), value);
    }

    public MediaFormatAudioVector getExtFmt() {
        long cPtr = pjsua2JNI.AudioDevInfo_extFmt_get(this.swigCPtr, this);
        return cPtr == 0L ? null : new MediaFormatAudioVector(cPtr, false);
    }

    public AudioDevInfo() {
        this(pjsua2JNI.new_AudioDevInfo(), true);
    }
}

