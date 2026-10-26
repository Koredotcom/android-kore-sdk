/*
 * Decompiled with CFR 0.152.
 */
package org.pjsip.pjsua2;

import org.pjsip.pjsua2.SWIGTYPE_p_void;
import org.pjsip.pjsua2.pjsua2JNI;

public class SdpSession {
    private transient long swigCPtr;
    protected transient boolean swigCMemOwn;

    protected SdpSession(long cPtr, boolean cMemoryOwn) {
        this.swigCMemOwn = cMemoryOwn;
        this.swigCPtr = cPtr;
    }

    protected static long getCPtr(SdpSession obj) {
        return obj == null ? 0L : obj.swigCPtr;
    }

    protected void finalize() {
        this.delete();
    }

    public synchronized void delete() {
        if (this.swigCPtr != 0L) {
            if (this.swigCMemOwn) {
                this.swigCMemOwn = false;
                pjsua2JNI.delete_SdpSession(this.swigCPtr);
            }
            this.swigCPtr = 0L;
        }
    }

    public void setWholeSdp(String value) {
        pjsua2JNI.SdpSession_wholeSdp_set(this.swigCPtr, this, value);
    }

    public String getWholeSdp() {
        return pjsua2JNI.SdpSession_wholeSdp_get(this.swigCPtr, this);
    }

    public void setPjSdpSession(SWIGTYPE_p_void value) {
        pjsua2JNI.SdpSession_pjSdpSession_set(this.swigCPtr, this, SWIGTYPE_p_void.getCPtr(value));
    }

    public SWIGTYPE_p_void getPjSdpSession() {
        long cPtr = pjsua2JNI.SdpSession_pjSdpSession_get(this.swigCPtr, this);
        return cPtr == 0L ? null : new SWIGTYPE_p_void(cPtr, false);
    }

    public SdpSession() {
        this(pjsua2JNI.new_SdpSession(), true);
    }
}

