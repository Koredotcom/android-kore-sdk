/*
 * Decompiled with CFR 0.152.
 */
package org.pjsip.pjsua2;

import org.pjsip.pjsua2.MediaFormat;
import org.pjsip.pjsua2.pjsua2JNI;

public class MediaFormatAudio
extends MediaFormat {
    private transient long swigCPtr;

    protected MediaFormatAudio(long cPtr, boolean cMemoryOwn) {
        super(pjsua2JNI.MediaFormatAudio_SWIGUpcast(cPtr), cMemoryOwn);
        this.swigCPtr = cPtr;
    }

    protected static long getCPtr(MediaFormatAudio obj) {
        return obj == null ? 0L : obj.swigCPtr;
    }

    @Override
    protected void finalize() {
        this.delete();
    }

    @Override
    public synchronized void delete() {
        if (this.swigCPtr != 0L) {
            if (this.swigCMemOwn) {
                this.swigCMemOwn = false;
                pjsua2JNI.delete_MediaFormatAudio(this.swigCPtr);
            }
            this.swigCPtr = 0L;
        }
        super.delete();
    }

    public void setClockRate(long value) {
        pjsua2JNI.MediaFormatAudio_clockRate_set(this.swigCPtr, this, value);
    }

    public long getClockRate() {
        return pjsua2JNI.MediaFormatAudio_clockRate_get(this.swigCPtr, this);
    }

    public void setChannelCount(long value) {
        pjsua2JNI.MediaFormatAudio_channelCount_set(this.swigCPtr, this, value);
    }

    public long getChannelCount() {
        return pjsua2JNI.MediaFormatAudio_channelCount_get(this.swigCPtr, this);
    }

    public void setFrameTimeUsec(long value) {
        pjsua2JNI.MediaFormatAudio_frameTimeUsec_set(this.swigCPtr, this, value);
    }

    public long getFrameTimeUsec() {
        return pjsua2JNI.MediaFormatAudio_frameTimeUsec_get(this.swigCPtr, this);
    }

    public void setBitsPerSample(long value) {
        pjsua2JNI.MediaFormatAudio_bitsPerSample_set(this.swigCPtr, this, value);
    }

    public long getBitsPerSample() {
        return pjsua2JNI.MediaFormatAudio_bitsPerSample_get(this.swigCPtr, this);
    }

    public void setAvgBps(long value) {
        pjsua2JNI.MediaFormatAudio_avgBps_set(this.swigCPtr, this, value);
    }

    public long getAvgBps() {
        return pjsua2JNI.MediaFormatAudio_avgBps_get(this.swigCPtr, this);
    }

    public void setMaxBps(long value) {
        pjsua2JNI.MediaFormatAudio_maxBps_set(this.swigCPtr, this, value);
    }

    public long getMaxBps() {
        return pjsua2JNI.MediaFormatAudio_maxBps_get(this.swigCPtr, this);
    }

    public MediaFormatAudio() {
        this(pjsua2JNI.new_MediaFormatAudio(), true);
    }
}

