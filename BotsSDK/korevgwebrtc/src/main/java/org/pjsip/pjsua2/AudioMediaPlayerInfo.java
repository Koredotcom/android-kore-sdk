/*
 * Decompiled with CFR 0.152.
 */
package org.pjsip.pjsua2;

import org.pjsip.pjsua2.pjsua2JNI;

public class AudioMediaPlayerInfo {
    private transient long swigCPtr;
    protected transient boolean swigCMemOwn;

    protected AudioMediaPlayerInfo(long cPtr, boolean cMemoryOwn) {
        this.swigCMemOwn = cMemoryOwn;
        this.swigCPtr = cPtr;
    }

    protected static long getCPtr(AudioMediaPlayerInfo obj) {
        return obj == null ? 0L : obj.swigCPtr;
    }

    protected void finalize() {
        this.delete();
    }

    public synchronized void delete() {
        if (this.swigCPtr != 0L) {
            if (this.swigCMemOwn) {
                this.swigCMemOwn = false;
                pjsua2JNI.delete_AudioMediaPlayerInfo(this.swigCPtr);
            }
            this.swigCPtr = 0L;
        }
    }

    public void setFormatId(int value) {
        pjsua2JNI.AudioMediaPlayerInfo_formatId_set(this.swigCPtr, this, value);
    }

    public int getFormatId() {
        return pjsua2JNI.AudioMediaPlayerInfo_formatId_get(this.swigCPtr, this);
    }

    public void setPayloadBitsPerSample(long value) {
        pjsua2JNI.AudioMediaPlayerInfo_payloadBitsPerSample_set(this.swigCPtr, this, value);
    }

    public long getPayloadBitsPerSample() {
        return pjsua2JNI.AudioMediaPlayerInfo_payloadBitsPerSample_get(this.swigCPtr, this);
    }

    public void setSizeBytes(long value) {
        pjsua2JNI.AudioMediaPlayerInfo_sizeBytes_set(this.swigCPtr, this, value);
    }

    public long getSizeBytes() {
        return pjsua2JNI.AudioMediaPlayerInfo_sizeBytes_get(this.swigCPtr, this);
    }

    public void setSizeSamples(long value) {
        pjsua2JNI.AudioMediaPlayerInfo_sizeSamples_set(this.swigCPtr, this, value);
    }

    public long getSizeSamples() {
        return pjsua2JNI.AudioMediaPlayerInfo_sizeSamples_get(this.swigCPtr, this);
    }

    public AudioMediaPlayerInfo() {
        this(pjsua2JNI.new_AudioMediaPlayerInfo(), true);
    }
}

