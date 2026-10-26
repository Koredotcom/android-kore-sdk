/*
 * Decompiled with CFR 0.152.
 */
package org.pjsip.pjsua2;

import org.pjsip.pjsua2.SrtpCryptoVector;
import org.pjsip.pjsua2.pjsua2JNI;

public class OnCreateMediaTransportSrtpParam {
    private transient long swigCPtr;
    protected transient boolean swigCMemOwn;

    protected OnCreateMediaTransportSrtpParam(long cPtr, boolean cMemoryOwn) {
        this.swigCMemOwn = cMemoryOwn;
        this.swigCPtr = cPtr;
    }

    protected static long getCPtr(OnCreateMediaTransportSrtpParam obj) {
        return obj == null ? 0L : obj.swigCPtr;
    }

    protected void finalize() {
        this.delete();
    }

    public synchronized void delete() {
        if (this.swigCPtr != 0L) {
            if (this.swigCMemOwn) {
                this.swigCMemOwn = false;
                pjsua2JNI.delete_OnCreateMediaTransportSrtpParam(this.swigCPtr);
            }
            this.swigCPtr = 0L;
        }
    }

    public void setMediaIdx(long value) {
        pjsua2JNI.OnCreateMediaTransportSrtpParam_mediaIdx_set(this.swigCPtr, this, value);
    }

    public long getMediaIdx() {
        return pjsua2JNI.OnCreateMediaTransportSrtpParam_mediaIdx_get(this.swigCPtr, this);
    }

    public void setSrtpUse(int value) {
        pjsua2JNI.OnCreateMediaTransportSrtpParam_srtpUse_set(this.swigCPtr, this, value);
    }

    public int getSrtpUse() {
        return pjsua2JNI.OnCreateMediaTransportSrtpParam_srtpUse_get(this.swigCPtr, this);
    }

    public void setCryptos(SrtpCryptoVector value) {
        pjsua2JNI.OnCreateMediaTransportSrtpParam_cryptos_set(this.swigCPtr, this, SrtpCryptoVector.getCPtr(value), value);
    }

    public SrtpCryptoVector getCryptos() {
        long cPtr = pjsua2JNI.OnCreateMediaTransportSrtpParam_cryptos_get(this.swigCPtr, this);
        return cPtr == 0L ? null : new SrtpCryptoVector(cPtr, false);
    }

    public OnCreateMediaTransportSrtpParam() {
        this(pjsua2JNI.new_OnCreateMediaTransportSrtpParam(), true);
    }
}

