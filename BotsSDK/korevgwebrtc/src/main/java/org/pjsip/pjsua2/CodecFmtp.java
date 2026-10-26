/*
 * Decompiled with CFR 0.152.
 */
package org.pjsip.pjsua2;

import org.pjsip.pjsua2.pjsua2JNI;

public class CodecFmtp {
    private transient long swigCPtr;
    protected transient boolean swigCMemOwn;

    protected CodecFmtp(long cPtr, boolean cMemoryOwn) {
        this.swigCMemOwn = cMemoryOwn;
        this.swigCPtr = cPtr;
    }

    protected static long getCPtr(CodecFmtp obj) {
        return obj == null ? 0L : obj.swigCPtr;
    }

    protected void finalize() {
        this.delete();
    }

    public synchronized void delete() {
        if (this.swigCPtr != 0L) {
            if (this.swigCMemOwn) {
                this.swigCMemOwn = false;
                pjsua2JNI.delete_CodecFmtp(this.swigCPtr);
            }
            this.swigCPtr = 0L;
        }
    }

    public void setName(String value) {
        pjsua2JNI.CodecFmtp_name_set(this.swigCPtr, this, value);
    }

    public String getName() {
        return pjsua2JNI.CodecFmtp_name_get(this.swigCPtr, this);
    }

    public void setVal(String value) {
        pjsua2JNI.CodecFmtp_val_set(this.swigCPtr, this, value);
    }

    public String getVal() {
        return pjsua2JNI.CodecFmtp_val_get(this.swigCPtr, this);
    }

    public CodecFmtp() {
        this(pjsua2JNI.new_CodecFmtp(), true);
    }
}

