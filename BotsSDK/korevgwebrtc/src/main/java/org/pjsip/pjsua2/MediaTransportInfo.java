/*
 * Decompiled with CFR 0.152.
 */
package org.pjsip.pjsua2;

import org.pjsip.pjsua2.pjsua2JNI;

public class MediaTransportInfo {
    private transient long swigCPtr;
    protected transient boolean swigCMemOwn;

    protected MediaTransportInfo(long cPtr, boolean cMemoryOwn) {
        this.swigCMemOwn = cMemoryOwn;
        this.swigCPtr = cPtr;
    }

    protected static long getCPtr(MediaTransportInfo obj) {
        return obj == null ? 0L : obj.swigCPtr;
    }

    protected void finalize() {
        this.delete();
    }

    public synchronized void delete() {
        if (this.swigCPtr != 0L) {
            if (this.swigCMemOwn) {
                this.swigCMemOwn = false;
                pjsua2JNI.delete_MediaTransportInfo(this.swigCPtr);
            }
            this.swigCPtr = 0L;
        }
    }

    public void setLocalRtpName(String value) {
        pjsua2JNI.MediaTransportInfo_localRtpName_set(this.swigCPtr, this, value);
    }

    public String getLocalRtpName() {
        return pjsua2JNI.MediaTransportInfo_localRtpName_get(this.swigCPtr, this);
    }

    public void setLocalRtcpName(String value) {
        pjsua2JNI.MediaTransportInfo_localRtcpName_set(this.swigCPtr, this, value);
    }

    public String getLocalRtcpName() {
        return pjsua2JNI.MediaTransportInfo_localRtcpName_get(this.swigCPtr, this);
    }

    public void setSrcRtpName(String value) {
        pjsua2JNI.MediaTransportInfo_srcRtpName_set(this.swigCPtr, this, value);
    }

    public String getSrcRtpName() {
        return pjsua2JNI.MediaTransportInfo_srcRtpName_get(this.swigCPtr, this);
    }

    public void setSrcRtcpName(String value) {
        pjsua2JNI.MediaTransportInfo_srcRtcpName_set(this.swigCPtr, this, value);
    }

    public String getSrcRtcpName() {
        return pjsua2JNI.MediaTransportInfo_srcRtcpName_get(this.swigCPtr, this);
    }

    public MediaTransportInfo() {
        this(pjsua2JNI.new_MediaTransportInfo(), true);
    }
}

