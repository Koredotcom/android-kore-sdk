/*
 * Decompiled with CFR 0.152.
 */
package org.pjsip.pjsua2;

import org.pjsip.pjsua2.pjsua2JNI;

public class CodecInfo {
    private transient long swigCPtr;
    protected transient boolean swigCMemOwn;

    protected CodecInfo(long cPtr, boolean cMemoryOwn) {
        this.swigCMemOwn = cMemoryOwn;
        this.swigCPtr = cPtr;
    }

    protected static long getCPtr(CodecInfo obj) {
        return obj == null ? 0L : obj.swigCPtr;
    }

    protected void finalize() {
        this.delete();
    }

    public synchronized void delete() {
        if (this.swigCPtr != 0L) {
            if (this.swigCMemOwn) {
                this.swigCMemOwn = false;
                pjsua2JNI.delete_CodecInfo(this.swigCPtr);
            }
            this.swigCPtr = 0L;
        }
    }

    public void setCodecId(String value) {
        pjsua2JNI.CodecInfo_codecId_set(this.swigCPtr, this, value);
    }

    public String getCodecId() {
        return pjsua2JNI.CodecInfo_codecId_get(this.swigCPtr, this);
    }

    public void setPriority(short value) {
        pjsua2JNI.CodecInfo_priority_set(this.swigCPtr, this, value);
    }

    public short getPriority() {
        return pjsua2JNI.CodecInfo_priority_get(this.swigCPtr, this);
    }

    public void setDesc(String value) {
        pjsua2JNI.CodecInfo_desc_set(this.swigCPtr, this, value);
    }

    public String getDesc() {
        return pjsua2JNI.CodecInfo_desc_get(this.swigCPtr, this);
    }

    public CodecInfo() {
        this(pjsua2JNI.new_CodecInfo(), true);
    }
}

