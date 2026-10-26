/*
 * Decompiled with CFR 0.152.
 */
package org.pjsip.pjsua2;

import org.pjsip.pjsua2.pjsua2JNI;

public class Version {
    private transient long swigCPtr;
    protected transient boolean swigCMemOwn;

    protected Version(long cPtr, boolean cMemoryOwn) {
        this.swigCMemOwn = cMemoryOwn;
        this.swigCPtr = cPtr;
    }

    protected static long getCPtr(Version obj) {
        return obj == null ? 0L : obj.swigCPtr;
    }

    protected void finalize() {
        this.delete();
    }

    public synchronized void delete() {
        if (this.swigCPtr != 0L) {
            if (this.swigCMemOwn) {
                this.swigCMemOwn = false;
                pjsua2JNI.delete_Version(this.swigCPtr);
            }
            this.swigCPtr = 0L;
        }
    }

    public void setMajor(int value) {
        pjsua2JNI.Version_major_set(this.swigCPtr, this, value);
    }

    public int getMajor() {
        return pjsua2JNI.Version_major_get(this.swigCPtr, this);
    }

    public void setMinor(int value) {
        pjsua2JNI.Version_minor_set(this.swigCPtr, this, value);
    }

    public int getMinor() {
        return pjsua2JNI.Version_minor_get(this.swigCPtr, this);
    }

    public void setRev(int value) {
        pjsua2JNI.Version_rev_set(this.swigCPtr, this, value);
    }

    public int getRev() {
        return pjsua2JNI.Version_rev_get(this.swigCPtr, this);
    }

    public void setSuffix(String value) {
        pjsua2JNI.Version_suffix_set(this.swigCPtr, this, value);
    }

    public String getSuffix() {
        return pjsua2JNI.Version_suffix_get(this.swigCPtr, this);
    }

    public void setFull(String value) {
        pjsua2JNI.Version_full_set(this.swigCPtr, this, value);
    }

    public String getFull() {
        return pjsua2JNI.Version_full_get(this.swigCPtr, this);
    }

    public void setNumeric(long value) {
        pjsua2JNI.Version_numeric_set(this.swigCPtr, this, value);
    }

    public long getNumeric() {
        return pjsua2JNI.Version_numeric_get(this.swigCPtr, this);
    }

    public Version() {
        this(pjsua2JNI.new_Version(), true);
    }
}

