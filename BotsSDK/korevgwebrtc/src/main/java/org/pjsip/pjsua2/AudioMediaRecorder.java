/*
 * Decompiled with CFR 0.152.
 */
package org.pjsip.pjsua2;

import org.pjsip.pjsua2.AudioMedia;
import org.pjsip.pjsua2.pjsua2JNI;

public class AudioMediaRecorder
extends AudioMedia {
    private transient long swigCPtr;

    protected AudioMediaRecorder(long cPtr, boolean cMemoryOwn) {
        super(pjsua2JNI.AudioMediaRecorder_SWIGUpcast(cPtr), cMemoryOwn);
        this.swigCPtr = cPtr;
    }

    protected static long getCPtr(AudioMediaRecorder obj) {
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
                pjsua2JNI.delete_AudioMediaRecorder(this.swigCPtr);
            }
            this.swigCPtr = 0L;
        }
        super.delete();
    }

    public AudioMediaRecorder() {
        this(pjsua2JNI.new_AudioMediaRecorder(), true);
    }

    public void createRecorder(String file_name, long enc_type, int max_size, long options) throws Exception {
        pjsua2JNI.AudioMediaRecorder_createRecorder__SWIG_0(this.swigCPtr, this, file_name, enc_type, max_size, options);
    }

    public void createRecorder(String file_name, long enc_type, int max_size) throws Exception {
        pjsua2JNI.AudioMediaRecorder_createRecorder__SWIG_1(this.swigCPtr, this, file_name, enc_type, max_size);
    }

    public void createRecorder(String file_name, long enc_type) throws Exception {
        pjsua2JNI.AudioMediaRecorder_createRecorder__SWIG_2(this.swigCPtr, this, file_name, enc_type);
    }

    public void createRecorder(String file_name) throws Exception {
        pjsua2JNI.AudioMediaRecorder_createRecorder__SWIG_3(this.swigCPtr, this, file_name);
    }

    public static AudioMediaRecorder typecastFromAudioMedia(AudioMedia media) {
        long cPtr = pjsua2JNI.AudioMediaRecorder_typecastFromAudioMedia(AudioMedia.getCPtr(media), media);
        return cPtr == 0L ? null : new AudioMediaRecorder(cPtr, false);
    }
}

