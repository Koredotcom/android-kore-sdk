/*
 * Decompiled with CFR 0.152.
 */
package org.pjsip.pjsua2;

import org.pjsip.pjsua2.AudioMedia;
import org.pjsip.pjsua2.pjsua2JNI;

public class ExtraAudioDevice
extends AudioMedia {
    private transient long swigCPtr;

    protected ExtraAudioDevice(long cPtr, boolean cMemoryOwn) {
        super(pjsua2JNI.ExtraAudioDevice_SWIGUpcast(cPtr), cMemoryOwn);
        this.swigCPtr = cPtr;
    }

    protected static long getCPtr(ExtraAudioDevice obj) {
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
                pjsua2JNI.delete_ExtraAudioDevice(this.swigCPtr);
            }
            this.swigCPtr = 0L;
        }
        super.delete();
    }

    public ExtraAudioDevice(int playdev, int recdev) {
        this(pjsua2JNI.new_ExtraAudioDevice(playdev, recdev), true);
    }

    public void open() {
        pjsua2JNI.ExtraAudioDevice_open(this.swigCPtr, this);
    }

    public void close() {
        pjsua2JNI.ExtraAudioDevice_close(this.swigCPtr, this);
    }

    public boolean isOpened() {
        return pjsua2JNI.ExtraAudioDevice_isOpened(this.swigCPtr, this);
    }
}

