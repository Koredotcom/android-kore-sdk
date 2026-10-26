/*
 * Decompiled with CFR 0.152.
 */
package org.pjsip.pjsua2;

import org.pjsip.pjsua2.CodecParamInfo;
import org.pjsip.pjsua2.CodecParamSetting;
import org.pjsip.pjsua2.pjsua2JNI;

public class CodecParam {
    private transient long swigCPtr;
    protected transient boolean swigCMemOwn;

    protected CodecParam(long cPtr, boolean cMemoryOwn) {
        this.swigCMemOwn = cMemoryOwn;
        this.swigCPtr = cPtr;
    }

    protected static long getCPtr(CodecParam obj) {
        return obj == null ? 0L : obj.swigCPtr;
    }

    protected void finalize() {
        this.delete();
    }

    public synchronized void delete() {
        if (this.swigCPtr != 0L) {
            if (this.swigCMemOwn) {
                this.swigCMemOwn = false;
                pjsua2JNI.delete_CodecParam(this.swigCPtr);
            }
            this.swigCPtr = 0L;
        }
    }

    public void setInfo(CodecParamInfo value) {
        pjsua2JNI.CodecParam_info_set(this.swigCPtr, this, CodecParamInfo.getCPtr(value), value);
    }

    public CodecParamInfo getInfo() {
        long cPtr = pjsua2JNI.CodecParam_info_get(this.swigCPtr, this);
        return cPtr == 0L ? null : new CodecParamInfo(cPtr, false);
    }

    public void setSetting(CodecParamSetting value) {
        pjsua2JNI.CodecParam_setting_set(this.swigCPtr, this, CodecParamSetting.getCPtr(value), value);
    }

    public CodecParamSetting getSetting() {
        long cPtr = pjsua2JNI.CodecParam_setting_get(this.swigCPtr, this);
        return cPtr == 0L ? null : new CodecParamSetting(cPtr, false);
    }

    public CodecParam() {
        this(pjsua2JNI.new_CodecParam(), true);
    }
}

