/*
 * Decompiled with CFR 0.152.
 */
package org.pjsip.pjsua2;

import org.pjsip.pjsua2.IntVector;
import org.pjsip.pjsua2.MediaFormatAudio;
import org.pjsip.pjsua2.pjsua2JNI;

public class ConfPortInfo {
    private transient long swigCPtr;
    protected transient boolean swigCMemOwn;

    protected ConfPortInfo(long cPtr, boolean cMemoryOwn) {
        this.swigCMemOwn = cMemoryOwn;
        this.swigCPtr = cPtr;
    }

    protected static long getCPtr(ConfPortInfo obj) {
        return obj == null ? 0L : obj.swigCPtr;
    }

    protected void finalize() {
        this.delete();
    }

    public synchronized void delete() {
        if (this.swigCPtr != 0L) {
            if (this.swigCMemOwn) {
                this.swigCMemOwn = false;
                pjsua2JNI.delete_ConfPortInfo(this.swigCPtr);
            }
            this.swigCPtr = 0L;
        }
    }

    public void setPortId(int value) {
        pjsua2JNI.ConfPortInfo_portId_set(this.swigCPtr, this, value);
    }

    public int getPortId() {
        return pjsua2JNI.ConfPortInfo_portId_get(this.swigCPtr, this);
    }

    public void setName(String value) {
        pjsua2JNI.ConfPortInfo_name_set(this.swigCPtr, this, value);
    }

    public String getName() {
        return pjsua2JNI.ConfPortInfo_name_get(this.swigCPtr, this);
    }

    public void setFormat(MediaFormatAudio value) {
        pjsua2JNI.ConfPortInfo_format_set(this.swigCPtr, this, MediaFormatAudio.getCPtr(value), value);
    }

    public MediaFormatAudio getFormat() {
        long cPtr = pjsua2JNI.ConfPortInfo_format_get(this.swigCPtr, this);
        return cPtr == 0L ? null : new MediaFormatAudio(cPtr, false);
    }

    public void setTxLevelAdj(float value) {
        pjsua2JNI.ConfPortInfo_txLevelAdj_set(this.swigCPtr, this, value);
    }

    public float getTxLevelAdj() {
        return pjsua2JNI.ConfPortInfo_txLevelAdj_get(this.swigCPtr, this);
    }

    public void setRxLevelAdj(float value) {
        pjsua2JNI.ConfPortInfo_rxLevelAdj_set(this.swigCPtr, this, value);
    }

    public float getRxLevelAdj() {
        return pjsua2JNI.ConfPortInfo_rxLevelAdj_get(this.swigCPtr, this);
    }

    public void setListeners(IntVector value) {
        pjsua2JNI.ConfPortInfo_listeners_set(this.swigCPtr, this, IntVector.getCPtr(value), value);
    }

    public IntVector getListeners() {
        long cPtr = pjsua2JNI.ConfPortInfo_listeners_get(this.swigCPtr, this);
        return cPtr == 0L ? null : new IntVector(cPtr, false);
    }

    public ConfPortInfo() {
        this(pjsua2JNI.new_ConfPortInfo(), true);
    }
}

