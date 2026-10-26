/*
 * Decompiled with CFR 0.152.
 */
package org.pjsip.pjsua2;

import org.pjsip.pjsua2.pjsua2JNI;

public class SrtpCrypto {
    private transient long swigCPtr;
    protected transient boolean swigCMemOwn;

    protected SrtpCrypto(long cPtr, boolean cMemoryOwn) {
        this.swigCMemOwn = cMemoryOwn;
        this.swigCPtr = cPtr;
    }

    protected static long getCPtr(SrtpCrypto obj) {
        return obj == null ? 0L : obj.swigCPtr;
    }

    protected void finalize() {
        this.delete();
    }

    public synchronized void delete() {
        if (this.swigCPtr != 0L) {
            if (this.swigCMemOwn) {
                this.swigCMemOwn = false;
                pjsua2JNI.delete_SrtpCrypto(this.swigCPtr);
            }
            this.swigCPtr = 0L;
        }
    }

    public void setKey(String value) {
        pjsua2JNI.SrtpCrypto_key_set(this.swigCPtr, this, value);
    }

    public String getKey() {
        return pjsua2JNI.SrtpCrypto_key_get(this.swigCPtr, this);
    }

    public void setName(String value) {
        pjsua2JNI.SrtpCrypto_name_set(this.swigCPtr, this, value);
    }

    public String getName() {
        return pjsua2JNI.SrtpCrypto_name_get(this.swigCPtr, this);
    }

    public void setFlags(long value) {
        pjsua2JNI.SrtpCrypto_flags_set(this.swigCPtr, this, value);
    }

    public long getFlags() {
        return pjsua2JNI.SrtpCrypto_flags_get(this.swigCPtr, this);
    }

    public SrtpCrypto() {
        this(pjsua2JNI.new_SrtpCrypto(), true);
    }
}

