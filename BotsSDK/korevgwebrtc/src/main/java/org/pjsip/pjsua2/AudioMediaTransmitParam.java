/*
 * Decompiled with CFR 0.152.
 */
package org.pjsip.pjsua2;

import org.pjsip.pjsua2.pjsua2JNI;

public class AudioMediaTransmitParam {
    private transient long swigCPtr;
    protected transient boolean swigCMemOwn;

    protected AudioMediaTransmitParam(long cPtr, boolean cMemoryOwn) {
        this.swigCMemOwn = cMemoryOwn;
        this.swigCPtr = cPtr;
    }

    protected static long getCPtr(AudioMediaTransmitParam obj) {
        return obj == null ? 0L : obj.swigCPtr;
    }

    protected void finalize() {
        this.delete();
    }

    public synchronized void delete() {
        if (this.swigCPtr != 0L) {
            if (this.swigCMemOwn) {
                this.swigCMemOwn = false;
                pjsua2JNI.delete_AudioMediaTransmitParam(this.swigCPtr);
            }
            this.swigCPtr = 0L;
        }
    }

    public void setLevel(float value) {
        pjsua2JNI.AudioMediaTransmitParam_level_set(this.swigCPtr, this, value);
    }

    public float getLevel() {
        return pjsua2JNI.AudioMediaTransmitParam_level_get(this.swigCPtr, this);
    }

    public AudioMediaTransmitParam() {
        this(pjsua2JNI.new_AudioMediaTransmitParam(), true);
    }
}

