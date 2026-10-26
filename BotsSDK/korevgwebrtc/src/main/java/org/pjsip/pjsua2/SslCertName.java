/*
 * Decompiled with CFR 0.152.
 */
package org.pjsip.pjsua2;

import org.pjsip.pjsua2.pjsua2JNI;

public class SslCertName {
    private transient long swigCPtr;
    protected transient boolean swigCMemOwn;

    protected SslCertName(long cPtr, boolean cMemoryOwn) {
        this.swigCMemOwn = cMemoryOwn;
        this.swigCPtr = cPtr;
    }

    protected static long getCPtr(SslCertName obj) {
        return obj == null ? 0L : obj.swigCPtr;
    }

    protected void finalize() {
        this.delete();
    }

    public synchronized void delete() {
        if (this.swigCPtr != 0L) {
            if (this.swigCMemOwn) {
                this.swigCMemOwn = false;
                pjsua2JNI.delete_SslCertName(this.swigCPtr);
            }
            this.swigCPtr = 0L;
        }
    }

    public void setType(int value) {
        pjsua2JNI.SslCertName_type_set(this.swigCPtr, this, value);
    }

    public int getType() {
        return pjsua2JNI.SslCertName_type_get(this.swigCPtr, this);
    }

    public void setName(String value) {
        pjsua2JNI.SslCertName_name_set(this.swigCPtr, this, value);
    }

    public String getName() {
        return pjsua2JNI.SslCertName_name_get(this.swigCPtr, this);
    }

    public SslCertName() {
        this(pjsua2JNI.new_SslCertName(), true);
    }
}

