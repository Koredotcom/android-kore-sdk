/*
 * Decompiled with CFR 0.152.
 */
package org.pjsip.pjsua2;

import org.pjsip.pjsua2.SWIGTYPE_p_void;
import org.pjsip.pjsua2.pjsua2JNI;

public class OnStreamDestroyedParam {
    private transient long swigCPtr;
    protected transient boolean swigCMemOwn;

    protected OnStreamDestroyedParam(long cPtr, boolean cMemoryOwn) {
        this.swigCMemOwn = cMemoryOwn;
        this.swigCPtr = cPtr;
    }

    protected static long getCPtr(OnStreamDestroyedParam obj) {
        return obj == null ? 0L : obj.swigCPtr;
    }

    protected void finalize() {
        this.delete();
    }

    public synchronized void delete() {
        if (this.swigCPtr != 0L) {
            if (this.swigCMemOwn) {
                this.swigCMemOwn = false;
                pjsua2JNI.delete_OnStreamDestroyedParam(this.swigCPtr);
            }
            this.swigCPtr = 0L;
        }
    }

    public void setStream(SWIGTYPE_p_void value) {
        pjsua2JNI.OnStreamDestroyedParam_stream_set(this.swigCPtr, this, SWIGTYPE_p_void.getCPtr(value));
    }

    public SWIGTYPE_p_void getStream() {
        long cPtr = pjsua2JNI.OnStreamDestroyedParam_stream_get(this.swigCPtr, this);
        return cPtr == 0L ? null : new SWIGTYPE_p_void(cPtr, false);
    }

    public void setStreamIdx(long value) {
        pjsua2JNI.OnStreamDestroyedParam_streamIdx_set(this.swigCPtr, this, value);
    }

    public long getStreamIdx() {
        return pjsua2JNI.OnStreamDestroyedParam_streamIdx_get(this.swigCPtr, this);
    }

    public OnStreamDestroyedParam() {
        this(pjsua2JNI.new_OnStreamDestroyedParam(), true);
    }
}

