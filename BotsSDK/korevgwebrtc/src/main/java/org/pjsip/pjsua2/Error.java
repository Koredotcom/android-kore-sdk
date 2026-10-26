/*
 * Decompiled with CFR 0.152.
 */
package org.pjsip.pjsua2;

import java.io.IOException;
import java.io.NotSerializableException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import org.pjsip.pjsua2.pjsua2JNI;

public class Error
extends Exception {
    private transient long swigCPtr;
    protected transient boolean swigCMemOwn;

    protected Error(long cPtr, boolean cMemoryOwn) {
        this.swigCMemOwn = cMemoryOwn;
        this.swigCPtr = cPtr;
    }

    protected static long getCPtr(Error obj) {
        return obj == null ? 0L : obj.swigCPtr;
    }

    protected void finalize() {
        this.delete();
    }

    public synchronized void delete() {
        if (this.swigCPtr != 0L) {
            if (this.swigCMemOwn) {
                this.swigCMemOwn = false;
                pjsua2JNI.delete_Error(this.swigCPtr);
            }
            this.swigCPtr = 0L;
        }
    }

    @Override
    public String getMessage() {
        return this.getTitle();
    }

    private void writeObject(ObjectOutputStream out) throws IOException {
        throw new NotSerializableException("Check ticket #1868!");
    }

    private void readObject(ObjectInputStream in) throws IOException {
        throw new NotSerializableException("Check ticket #1868!");
    }

    public void setStatus(int value) {
        pjsua2JNI.Error_status_set(this.swigCPtr, this, value);
    }

    public int getStatus() {
        return pjsua2JNI.Error_status_get(this.swigCPtr, this);
    }

    public void setTitle(String value) {
        pjsua2JNI.Error_title_set(this.swigCPtr, this, value);
    }

    public String getTitle() {
        return pjsua2JNI.Error_title_get(this.swigCPtr, this);
    }

    public void setReason(String value) {
        pjsua2JNI.Error_reason_set(this.swigCPtr, this, value);
    }

    public String getReason() {
        return pjsua2JNI.Error_reason_get(this.swigCPtr, this);
    }

    public void setSrcFile(String value) {
        pjsua2JNI.Error_srcFile_set(this.swigCPtr, this, value);
    }

    public String getSrcFile() {
        return pjsua2JNI.Error_srcFile_get(this.swigCPtr, this);
    }

    public void setSrcLine(int value) {
        pjsua2JNI.Error_srcLine_set(this.swigCPtr, this, value);
    }

    public int getSrcLine() {
        return pjsua2JNI.Error_srcLine_get(this.swigCPtr, this);
    }

    public String info(boolean multi_line) {
        return pjsua2JNI.Error_info__SWIG_0(this.swigCPtr, this, multi_line);
    }

    public String info() {
        return pjsua2JNI.Error_info__SWIG_1(this.swigCPtr, this);
    }

    public Error() {
        this(pjsua2JNI.new_Error__SWIG_0(), true);
    }

    public Error(int prm_status, String prm_title, String prm_reason, String prm_src_file, int prm_src_line) {
        this(pjsua2JNI.new_Error__SWIG_1(prm_status, prm_title, prm_reason, prm_src_file, prm_src_line), true);
    }
}

