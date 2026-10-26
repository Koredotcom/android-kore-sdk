/*
 * Decompiled with CFR 0.152.
 */
package org.pjsip.pjsua2;

import org.pjsip.pjsua2.SWIGTYPE_p_void;
import org.pjsip.pjsua2.pjsua2JNI;

public class OnStreamCreatedParam {
    private transient long swigCPtr;
    protected transient boolean swigCMemOwn;

    protected OnStreamCreatedParam(long cPtr, boolean cMemoryOwn) {
        this.swigCMemOwn = cMemoryOwn;
        this.swigCPtr = cPtr;
    }

    protected static long getCPtr(OnStreamCreatedParam obj) {
        return obj == null ? 0L : obj.swigCPtr;
    }

    protected void finalize() {
        this.delete();
    }

    public synchronized void delete() {
        if (this.swigCPtr != 0L) {
            if (this.swigCMemOwn) {
                this.swigCMemOwn = false;
                pjsua2JNI.delete_OnStreamCreatedParam(this.swigCPtr);
            }
            this.swigCPtr = 0L;
        }
    }

    public void setStream(SWIGTYPE_p_void value) {
        pjsua2JNI.OnStreamCreatedParam_stream_set(this.swigCPtr, this, SWIGTYPE_p_void.getCPtr(value));
    }

    public SWIGTYPE_p_void getStream() {
        long cPtr = pjsua2JNI.OnStreamCreatedParam_stream_get(this.swigCPtr, this);
        return cPtr == 0L ? null : new SWIGTYPE_p_void(cPtr, false);
    }

    public void setStreamIdx(long value) {
        pjsua2JNI.OnStreamCreatedParam_streamIdx_set(this.swigCPtr, this, value);
    }

    public long getStreamIdx() {
        return pjsua2JNI.OnStreamCreatedParam_streamIdx_get(this.swigCPtr, this);
    }

    public void setDestroyPort(boolean value) {
        pjsua2JNI.OnStreamCreatedParam_destroyPort_set(this.swigCPtr, this, value);
    }

    public boolean getDestroyPort() {
        return pjsua2JNI.OnStreamCreatedParam_destroyPort_get(this.swigCPtr, this);
    }

    public void setPPort(SWIGTYPE_p_void value) {
        pjsua2JNI.OnStreamCreatedParam_pPort_set(this.swigCPtr, this, SWIGTYPE_p_void.getCPtr(value));
    }

    public SWIGTYPE_p_void getPPort() {
        long cPtr = pjsua2JNI.OnStreamCreatedParam_pPort_get(this.swigCPtr, this);
        return cPtr == 0L ? null : new SWIGTYPE_p_void(cPtr, false);
    }

    public OnStreamCreatedParam() {
        this(pjsua2JNI.new_OnStreamCreatedParam(), true);
    }
}

