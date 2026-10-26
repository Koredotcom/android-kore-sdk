/*
 * Decompiled with CFR 0.152.
 */
package org.pjsip.pjsua2;

import org.pjsip.pjsua2.Media;
import org.pjsip.pjsua2.VidConfPortInfo;
import org.pjsip.pjsua2.VideoMediaTransmitParam;
import org.pjsip.pjsua2.pjsua2JNI;

public class VideoMedia
extends Media {
    private transient long swigCPtr;

    protected VideoMedia(long cPtr, boolean cMemoryOwn) {
        super(pjsua2JNI.VideoMedia_SWIGUpcast(cPtr), cMemoryOwn);
        this.swigCPtr = cPtr;
    }

    protected static long getCPtr(VideoMedia obj) {
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
                pjsua2JNI.delete_VideoMedia(this.swigCPtr);
            }
            this.swigCPtr = 0L;
        }
        super.delete();
    }

    public VidConfPortInfo getPortInfo() throws Exception {
        return new VidConfPortInfo(pjsua2JNI.VideoMedia_getPortInfo(this.swigCPtr, this), true);
    }

    public int getPortId() {
        return pjsua2JNI.VideoMedia_getPortId(this.swigCPtr, this);
    }

    public static VidConfPortInfo getPortInfoFromId(int port_id) throws Exception {
        return new VidConfPortInfo(pjsua2JNI.VideoMedia_getPortInfoFromId(port_id), true);
    }

    public void startTransmit(VideoMedia sink, VideoMediaTransmitParam param) throws Exception {
        pjsua2JNI.VideoMedia_startTransmit(this.swigCPtr, this, VideoMedia.getCPtr(sink), sink, VideoMediaTransmitParam.getCPtr(param), param);
    }

    public void stopTransmit(VideoMedia sink) throws Exception {
        pjsua2JNI.VideoMedia_stopTransmit(this.swigCPtr, this, VideoMedia.getCPtr(sink), sink);
    }

    public VideoMedia() {
        this(pjsua2JNI.new_VideoMedia(), true);
    }
}

