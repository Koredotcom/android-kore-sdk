/*
 * Decompiled with CFR 0.152.
 */
package org.pjsip.pjsua2;

import org.pjsip.pjsua2.IntVector;
import org.pjsip.pjsua2.MediaFormatVideo;
import org.pjsip.pjsua2.pjsua2JNI;

public class VidConfPortInfo {
    private transient long swigCPtr;
    protected transient boolean swigCMemOwn;

    protected VidConfPortInfo(long cPtr, boolean cMemoryOwn) {
        this.swigCMemOwn = cMemoryOwn;
        this.swigCPtr = cPtr;
    }

    protected static long getCPtr(VidConfPortInfo obj) {
        return obj == null ? 0L : obj.swigCPtr;
    }

    protected void finalize() {
        this.delete();
    }

    public synchronized void delete() {
        if (this.swigCPtr != 0L) {
            if (this.swigCMemOwn) {
                this.swigCMemOwn = false;
                pjsua2JNI.delete_VidConfPortInfo(this.swigCPtr);
            }
            this.swigCPtr = 0L;
        }
    }

    public void setPortId(int value) {
        pjsua2JNI.VidConfPortInfo_portId_set(this.swigCPtr, this, value);
    }

    public int getPortId() {
        return pjsua2JNI.VidConfPortInfo_portId_get(this.swigCPtr, this);
    }

    public void setName(String value) {
        pjsua2JNI.VidConfPortInfo_name_set(this.swigCPtr, this, value);
    }

    public String getName() {
        return pjsua2JNI.VidConfPortInfo_name_get(this.swigCPtr, this);
    }

    public void setFormat(MediaFormatVideo value) {
        pjsua2JNI.VidConfPortInfo_format_set(this.swigCPtr, this, MediaFormatVideo.getCPtr(value), value);
    }

    public MediaFormatVideo getFormat() {
        long cPtr = pjsua2JNI.VidConfPortInfo_format_get(this.swigCPtr, this);
        return cPtr == 0L ? null : new MediaFormatVideo(cPtr, false);
    }

    public void setListeners(IntVector value) {
        pjsua2JNI.VidConfPortInfo_listeners_set(this.swigCPtr, this, IntVector.getCPtr(value), value);
    }

    public IntVector getListeners() {
        long cPtr = pjsua2JNI.VidConfPortInfo_listeners_get(this.swigCPtr, this);
        return cPtr == 0L ? null : new IntVector(cPtr, false);
    }

    public void setTransmitters(IntVector value) {
        pjsua2JNI.VidConfPortInfo_transmitters_set(this.swigCPtr, this, IntVector.getCPtr(value), value);
    }

    public IntVector getTransmitters() {
        long cPtr = pjsua2JNI.VidConfPortInfo_transmitters_get(this.swigCPtr, this);
        return cPtr == 0L ? null : new IntVector(cPtr, false);
    }

    public VidConfPortInfo() {
        this(pjsua2JNI.new_VidConfPortInfo(), true);
    }
}

