/*
 * Decompiled with CFR 0.152.
 */
package org.pjsip.pjsua2;

import org.pjsip.pjsua2.SslCertInfo;
import org.pjsip.pjsua2.StringVector;
import org.pjsip.pjsua2.pjsua2JNI;

public class TlsInfo {
    private transient long swigCPtr;
    protected transient boolean swigCMemOwn;

    protected TlsInfo(long cPtr, boolean cMemoryOwn) {
        this.swigCMemOwn = cMemoryOwn;
        this.swigCPtr = cPtr;
    }

    protected static long getCPtr(TlsInfo obj) {
        return obj == null ? 0L : obj.swigCPtr;
    }

    protected void finalize() {
        this.delete();
    }

    public synchronized void delete() {
        if (this.swigCPtr != 0L) {
            if (this.swigCMemOwn) {
                this.swigCMemOwn = false;
                pjsua2JNI.delete_TlsInfo(this.swigCPtr);
            }
            this.swigCPtr = 0L;
        }
    }

    public void setEstablished(boolean value) {
        pjsua2JNI.TlsInfo_established_set(this.swigCPtr, this, value);
    }

    public boolean getEstablished() {
        return pjsua2JNI.TlsInfo_established_get(this.swigCPtr, this);
    }

    public void setProtocol(long value) {
        pjsua2JNI.TlsInfo_protocol_set(this.swigCPtr, this, value);
    }

    public long getProtocol() {
        return pjsua2JNI.TlsInfo_protocol_get(this.swigCPtr, this);
    }

    public void setCipher(int value) {
        pjsua2JNI.TlsInfo_cipher_set(this.swigCPtr, this, value);
    }

    public int getCipher() {
        return pjsua2JNI.TlsInfo_cipher_get(this.swigCPtr, this);
    }

    public void setCipherName(String value) {
        pjsua2JNI.TlsInfo_cipherName_set(this.swigCPtr, this, value);
    }

    public String getCipherName() {
        return pjsua2JNI.TlsInfo_cipherName_get(this.swigCPtr, this);
    }

    public void setLocalAddr(String value) {
        pjsua2JNI.TlsInfo_localAddr_set(this.swigCPtr, this, value);
    }

    public String getLocalAddr() {
        return pjsua2JNI.TlsInfo_localAddr_get(this.swigCPtr, this);
    }

    public void setRemoteAddr(String value) {
        pjsua2JNI.TlsInfo_remoteAddr_set(this.swigCPtr, this, value);
    }

    public String getRemoteAddr() {
        return pjsua2JNI.TlsInfo_remoteAddr_get(this.swigCPtr, this);
    }

    public void setLocalCertInfo(SslCertInfo value) {
        pjsua2JNI.TlsInfo_localCertInfo_set(this.swigCPtr, this, SslCertInfo.getCPtr(value), value);
    }

    public SslCertInfo getLocalCertInfo() {
        long cPtr = pjsua2JNI.TlsInfo_localCertInfo_get(this.swigCPtr, this);
        return cPtr == 0L ? null : new SslCertInfo(cPtr, false);
    }

    public void setRemoteCertInfo(SslCertInfo value) {
        pjsua2JNI.TlsInfo_remoteCertInfo_set(this.swigCPtr, this, SslCertInfo.getCPtr(value), value);
    }

    public SslCertInfo getRemoteCertInfo() {
        long cPtr = pjsua2JNI.TlsInfo_remoteCertInfo_get(this.swigCPtr, this);
        return cPtr == 0L ? null : new SslCertInfo(cPtr, false);
    }

    public void setVerifyStatus(long value) {
        pjsua2JNI.TlsInfo_verifyStatus_set(this.swigCPtr, this, value);
    }

    public long getVerifyStatus() {
        return pjsua2JNI.TlsInfo_verifyStatus_get(this.swigCPtr, this);
    }

    public void setVerifyMsgs(StringVector value) {
        pjsua2JNI.TlsInfo_verifyMsgs_set(this.swigCPtr, this, StringVector.getCPtr(value), value);
    }

    public StringVector getVerifyMsgs() {
        long cPtr = pjsua2JNI.TlsInfo_verifyMsgs_get(this.swigCPtr, this);
        return cPtr == 0L ? null : new StringVector(cPtr, false);
    }

    public TlsInfo() {
        this(pjsua2JNI.new_TlsInfo(), true);
    }

    public boolean isEmpty() {
        return pjsua2JNI.TlsInfo_isEmpty(this.swigCPtr, this);
    }
}

