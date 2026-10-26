/*
 * Decompiled with CFR 0.152.
 */
package org.pjsip.pjsua2;

import org.pjsip.pjsua2.CodecFmtpVector;
import org.pjsip.pjsua2.MediaFormatVideo;
import org.pjsip.pjsua2.pjsua2JNI;

public class VidCodecParam {
    private transient long swigCPtr;
    protected transient boolean swigCMemOwn;

    protected VidCodecParam(long cPtr, boolean cMemoryOwn) {
        this.swigCMemOwn = cMemoryOwn;
        this.swigCPtr = cPtr;
    }

    protected static long getCPtr(VidCodecParam obj) {
        return obj == null ? 0L : obj.swigCPtr;
    }

    protected void finalize() {
        this.delete();
    }

    public synchronized void delete() {
        if (this.swigCPtr != 0L) {
            if (this.swigCMemOwn) {
                this.swigCMemOwn = false;
                pjsua2JNI.delete_VidCodecParam(this.swigCPtr);
            }
            this.swigCPtr = 0L;
        }
    }

    public void setDir(int value) {
        pjsua2JNI.VidCodecParam_dir_set(this.swigCPtr, this, value);
    }

    public int getDir() {
        return pjsua2JNI.VidCodecParam_dir_get(this.swigCPtr, this);
    }

    public void setPacking(int value) {
        pjsua2JNI.VidCodecParam_packing_set(this.swigCPtr, this, value);
    }

    public int getPacking() {
        return pjsua2JNI.VidCodecParam_packing_get(this.swigCPtr, this);
    }

    public void setEncFmt(MediaFormatVideo value) {
        pjsua2JNI.VidCodecParam_encFmt_set(this.swigCPtr, this, MediaFormatVideo.getCPtr(value), value);
    }

    public MediaFormatVideo getEncFmt() {
        long cPtr = pjsua2JNI.VidCodecParam_encFmt_get(this.swigCPtr, this);
        return cPtr == 0L ? null : new MediaFormatVideo(cPtr, false);
    }

    public void setEncFmtp(CodecFmtpVector value) {
        pjsua2JNI.VidCodecParam_encFmtp_set(this.swigCPtr, this, CodecFmtpVector.getCPtr(value), value);
    }

    public CodecFmtpVector getEncFmtp() {
        long cPtr = pjsua2JNI.VidCodecParam_encFmtp_get(this.swigCPtr, this);
        return cPtr == 0L ? null : new CodecFmtpVector(cPtr, false);
    }

    public void setEncMtu(long value) {
        pjsua2JNI.VidCodecParam_encMtu_set(this.swigCPtr, this, value);
    }

    public long getEncMtu() {
        return pjsua2JNI.VidCodecParam_encMtu_get(this.swigCPtr, this);
    }

    public void setDecFmt(MediaFormatVideo value) {
        pjsua2JNI.VidCodecParam_decFmt_set(this.swigCPtr, this, MediaFormatVideo.getCPtr(value), value);
    }

    public MediaFormatVideo getDecFmt() {
        long cPtr = pjsua2JNI.VidCodecParam_decFmt_get(this.swigCPtr, this);
        return cPtr == 0L ? null : new MediaFormatVideo(cPtr, false);
    }

    public void setDecFmtp(CodecFmtpVector value) {
        pjsua2JNI.VidCodecParam_decFmtp_set(this.swigCPtr, this, CodecFmtpVector.getCPtr(value), value);
    }

    public CodecFmtpVector getDecFmtp() {
        long cPtr = pjsua2JNI.VidCodecParam_decFmtp_get(this.swigCPtr, this);
        return cPtr == 0L ? null : new CodecFmtpVector(cPtr, false);
    }

    public void setIgnoreFmtp(boolean value) {
        pjsua2JNI.VidCodecParam_ignoreFmtp_set(this.swigCPtr, this, value);
    }

    public boolean getIgnoreFmtp() {
        return pjsua2JNI.VidCodecParam_ignoreFmtp_get(this.swigCPtr, this);
    }

    public VidCodecParam() {
        this(pjsua2JNI.new_VidCodecParam(), true);
    }
}

