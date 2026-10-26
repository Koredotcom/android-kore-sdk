/*
 * Decompiled with CFR 0.152.
 */
package org.pjsip.pjsua2;

import org.pjsip.pjsua2.SWIGTYPE_p_unsigned_char;
import org.pjsip.pjsua2.StringVector;
import org.pjsip.pjsua2.TimeVal;
import org.pjsip.pjsua2.pjsua2JNI;

public class SslCertInfo {
    private transient long swigCPtr;
    protected transient boolean swigCMemOwn;

    protected SslCertInfo(long cPtr, boolean cMemoryOwn) {
        this.swigCMemOwn = cMemoryOwn;
        this.swigCPtr = cPtr;
    }

    protected static long getCPtr(SslCertInfo obj) {
        return obj == null ? 0L : obj.swigCPtr;
    }

    protected void finalize() {
        this.delete();
    }

    public synchronized void delete() {
        if (this.swigCPtr != 0L) {
            if (this.swigCMemOwn) {
                this.swigCMemOwn = false;
                pjsua2JNI.delete_SslCertInfo(this.swigCPtr);
            }
            this.swigCPtr = 0L;
        }
    }

    public void setVersion(long value) {
        pjsua2JNI.SslCertInfo_version_set(this.swigCPtr, this, value);
    }

    public long getVersion() {
        return pjsua2JNI.SslCertInfo_version_get(this.swigCPtr, this);
    }

    public void setSerialNo(SWIGTYPE_p_unsigned_char value) {
        pjsua2JNI.SslCertInfo_serialNo_set(this.swigCPtr, this, SWIGTYPE_p_unsigned_char.getCPtr(value));
    }

    public SWIGTYPE_p_unsigned_char getSerialNo() {
        long cPtr = pjsua2JNI.SslCertInfo_serialNo_get(this.swigCPtr, this);
        return cPtr == 0L ? null : new SWIGTYPE_p_unsigned_char(cPtr, false);
    }

    public void setSubjectCn(String value) {
        pjsua2JNI.SslCertInfo_subjectCn_set(this.swigCPtr, this, value);
    }

    public String getSubjectCn() {
        return pjsua2JNI.SslCertInfo_subjectCn_get(this.swigCPtr, this);
    }

    public void setSubjectInfo(String value) {
        pjsua2JNI.SslCertInfo_subjectInfo_set(this.swigCPtr, this, value);
    }

    public String getSubjectInfo() {
        return pjsua2JNI.SslCertInfo_subjectInfo_get(this.swigCPtr, this);
    }

    public void setIssuerCn(String value) {
        pjsua2JNI.SslCertInfo_issuerCn_set(this.swigCPtr, this, value);
    }

    public String getIssuerCn() {
        return pjsua2JNI.SslCertInfo_issuerCn_get(this.swigCPtr, this);
    }

    public void setIssuerInfo(String value) {
        pjsua2JNI.SslCertInfo_issuerInfo_set(this.swigCPtr, this, value);
    }

    public String getIssuerInfo() {
        return pjsua2JNI.SslCertInfo_issuerInfo_get(this.swigCPtr, this);
    }

    public void setValidityStart(TimeVal value) {
        pjsua2JNI.SslCertInfo_validityStart_set(this.swigCPtr, this, TimeVal.getCPtr(value), value);
    }

    public TimeVal getValidityStart() {
        long cPtr = pjsua2JNI.SslCertInfo_validityStart_get(this.swigCPtr, this);
        return cPtr == 0L ? null : new TimeVal(cPtr, false);
    }

    public void setValidityEnd(TimeVal value) {
        pjsua2JNI.SslCertInfo_validityEnd_set(this.swigCPtr, this, TimeVal.getCPtr(value), value);
    }

    public TimeVal getValidityEnd() {
        long cPtr = pjsua2JNI.SslCertInfo_validityEnd_get(this.swigCPtr, this);
        return cPtr == 0L ? null : new TimeVal(cPtr, false);
    }

    public void setValidityGmt(boolean value) {
        pjsua2JNI.SslCertInfo_validityGmt_set(this.swigCPtr, this, value);
    }

    public boolean getValidityGmt() {
        return pjsua2JNI.SslCertInfo_validityGmt_get(this.swigCPtr, this);
    }

    public void setSubjectAltName(StringVector value) {
        pjsua2JNI.SslCertInfo_subjectAltName_set(this.swigCPtr, this, StringVector.getCPtr(value), value);
    }

    public StringVector getSubjectAltName() {
        long cPtr = pjsua2JNI.SslCertInfo_subjectAltName_get(this.swigCPtr, this);
        return cPtr == 0L ? null : new StringVector(cPtr, false);
    }

    public void setRaw(String value) {
        pjsua2JNI.SslCertInfo_raw_set(this.swigCPtr, this, value);
    }

    public String getRaw() {
        return pjsua2JNI.SslCertInfo_raw_get(this.swigCPtr, this);
    }

    public SslCertInfo() {
        this(pjsua2JNI.new_SslCertInfo(), true);
    }

    public boolean isEmpty() {
        return pjsua2JNI.SslCertInfo_isEmpty(this.swigCPtr, this);
    }
}

