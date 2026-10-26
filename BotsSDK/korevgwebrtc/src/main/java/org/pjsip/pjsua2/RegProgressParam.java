/*
 * Decompiled with CFR 0.152.
 */
package org.pjsip.pjsua2;

import org.pjsip.pjsua2.pjsua2JNI;

public class RegProgressParam {
    private transient long swigCPtr;
    protected transient boolean swigCMemOwn;

    protected RegProgressParam(long cPtr, boolean cMemoryOwn) {
        this.swigCMemOwn = cMemoryOwn;
        this.swigCPtr = cPtr;
    }

    protected static long getCPtr(RegProgressParam obj) {
        return obj == null ? 0L : obj.swigCPtr;
    }

    protected void finalize() {
        this.delete();
    }

    public synchronized void delete() {
        if (this.swigCPtr != 0L) {
            if (this.swigCMemOwn) {
                this.swigCMemOwn = false;
                pjsua2JNI.delete_RegProgressParam(this.swigCPtr);
            }
            this.swigCPtr = 0L;
        }
    }

    public void setIsRegister(boolean value) {
        pjsua2JNI.RegProgressParam_isRegister_set(this.swigCPtr, this, value);
    }

    public boolean getIsRegister() {
        return pjsua2JNI.RegProgressParam_isRegister_get(this.swigCPtr, this);
    }

    public void setCode(int value) {
        pjsua2JNI.RegProgressParam_code_set(this.swigCPtr, this, value);
    }

    public int getCode() {
        return pjsua2JNI.RegProgressParam_code_get(this.swigCPtr, this);
    }

    public RegProgressParam() {
        this(pjsua2JNI.new_RegProgressParam(), true);
    }
}

