/*
 * Decompiled with CFR 0.152.
 */
package org.pjsip.pjsua2;

import org.pjsip.pjsua2.AudioMediaTransmitParam;
import org.pjsip.pjsua2.ConfPortInfo;
import org.pjsip.pjsua2.Media;
import org.pjsip.pjsua2.pjsua2JNI;

public class AudioMedia
extends Media {
    private transient long swigCPtr;

    protected AudioMedia(long cPtr, boolean cMemoryOwn) {
        super(pjsua2JNI.AudioMedia_SWIGUpcast(cPtr), cMemoryOwn);
        this.swigCPtr = cPtr;
    }

    protected static long getCPtr(AudioMedia obj) {
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
                pjsua2JNI.delete_AudioMedia(this.swigCPtr);
            }
            this.swigCPtr = 0L;
        }
        super.delete();
    }

    public ConfPortInfo getPortInfo() throws Exception {
        return new ConfPortInfo(pjsua2JNI.AudioMedia_getPortInfo(this.swigCPtr, this), true);
    }

    public int getPortId() {
        return pjsua2JNI.AudioMedia_getPortId(this.swigCPtr, this);
    }

    public static ConfPortInfo getPortInfoFromId(int port_id) throws Exception {
        return new ConfPortInfo(pjsua2JNI.AudioMedia_getPortInfoFromId(port_id), true);
    }

    public void startTransmit(AudioMedia sink) throws Exception {
        pjsua2JNI.AudioMedia_startTransmit(this.swigCPtr, this, AudioMedia.getCPtr(sink), sink);
    }

    public void startTransmit2(AudioMedia sink, AudioMediaTransmitParam param) throws Exception {
        pjsua2JNI.AudioMedia_startTransmit2(this.swigCPtr, this, AudioMedia.getCPtr(sink), sink, AudioMediaTransmitParam.getCPtr(param), param);
    }

    public void stopTransmit(AudioMedia sink) throws Exception {
        pjsua2JNI.AudioMedia_stopTransmit(this.swigCPtr, this, AudioMedia.getCPtr(sink), sink);
    }

    public void adjustRxLevel(float level) throws Exception {
        pjsua2JNI.AudioMedia_adjustRxLevel(this.swigCPtr, this, level);
    }

    public void adjustTxLevel(float level) throws Exception {
        pjsua2JNI.AudioMedia_adjustTxLevel(this.swigCPtr, this, level);
    }

    public long getRxLevel() throws Exception {
        return pjsua2JNI.AudioMedia_getRxLevel(this.swigCPtr, this);
    }

    public long getTxLevel() throws Exception {
        return pjsua2JNI.AudioMedia_getTxLevel(this.swigCPtr, this);
    }

    public static AudioMedia typecastFromMedia(Media media) {
        long cPtr = pjsua2JNI.AudioMedia_typecastFromMedia(Media.getCPtr(media), media);
        return cPtr == 0L ? null : new AudioMedia(cPtr, false);
    }

    public AudioMedia() {
        this(pjsua2JNI.new_AudioMedia(), true);
    }
}

