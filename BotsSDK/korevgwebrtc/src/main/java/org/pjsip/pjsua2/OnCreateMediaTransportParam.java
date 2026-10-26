/*
 * Decompiled with CFR 0.152.
 */
package org.pjsip.pjsua2;

import org.pjsip.pjsua2.SWIGTYPE_p_void;
import org.pjsip.pjsua2.pjsua2JNI;

public class OnCreateMediaTransportParam {
    private transient long swigCPtr;
    protected transient boolean swigCMemOwn;

    protected OnCreateMediaTransportParam(long cPtr, boolean cMemoryOwn) {
        this.swigCMemOwn = cMemoryOwn;
        this.swigCPtr = cPtr;
    }

    protected static long getCPtr(OnCreateMediaTransportParam obj) {
        return obj == null ? 0L : obj.swigCPtr;
    }

    protected void finalize() {
        this.delete();
    }

    public synchronized void delete() {
        if (this.swigCPtr != 0L) {
            if (this.swigCMemOwn) {
                this.swigCMemOwn = false;
                pjsua2JNI.delete_OnCreateMediaTransportParam(this.swigCPtr);
            }
            this.swigCPtr = 0L;
        }
    }

    public void setMediaIdx(long value) {
        pjsua2JNI.OnCreateMediaTransportParam_mediaIdx_set(this.swigCPtr, this, value);
    }

    public long getMediaIdx() {
        return pjsua2JNI.OnCreateMediaTransportParam_mediaIdx_get(this.swigCPtr, this);
    }

    public void setMediaTp(SWIGTYPE_p_void value) {
        pjsua2JNI.OnCreateMediaTransportParam_mediaTp_set(this.swigCPtr, this, SWIGTYPE_p_void.getCPtr(value));
    }

    public SWIGTYPE_p_void getMediaTp() {
        long cPtr = pjsua2JNI.OnCreateMediaTransportParam_mediaTp_get(this.swigCPtr, this);
        return cPtr == 0L ? null : new SWIGTYPE_p_void(cPtr, false);
    }

    public void setFlags(long value) {
        pjsua2JNI.OnCreateMediaTransportParam_flags_set(this.swigCPtr, this, value);
    }

    public long getFlags() {
        return pjsua2JNI.OnCreateMediaTransportParam_flags_get(this.swigCPtr, this);
    }

    public OnCreateMediaTransportParam() {
        this(pjsua2JNI.new_OnCreateMediaTransportParam(), true);
    }
}

