/*
 * Decompiled with CFR 0.152.
 */
package org.pjsip.pjsua2;

import org.pjsip.pjsua2.SWIGTYPE_p_void;
import org.pjsip.pjsua2.pjsua2JNI;

public class OnNatCheckStunServersCompleteParam {
    private transient long swigCPtr;
    protected transient boolean swigCMemOwn;

    protected OnNatCheckStunServersCompleteParam(long cPtr, boolean cMemoryOwn) {
        this.swigCMemOwn = cMemoryOwn;
        this.swigCPtr = cPtr;
    }

    protected static long getCPtr(OnNatCheckStunServersCompleteParam obj) {
        return obj == null ? 0L : obj.swigCPtr;
    }

    protected void finalize() {
        this.delete();
    }

    public synchronized void delete() {
        if (this.swigCPtr != 0L) {
            if (this.swigCMemOwn) {
                this.swigCMemOwn = false;
                pjsua2JNI.delete_OnNatCheckStunServersCompleteParam(this.swigCPtr);
            }
            this.swigCPtr = 0L;
        }
    }

    public void setUserData(SWIGTYPE_p_void value) {
        pjsua2JNI.OnNatCheckStunServersCompleteParam_userData_set(this.swigCPtr, this, SWIGTYPE_p_void.getCPtr(value));
    }

    public SWIGTYPE_p_void getUserData() {
        long cPtr = pjsua2JNI.OnNatCheckStunServersCompleteParam_userData_get(this.swigCPtr, this);
        return cPtr == 0L ? null : new SWIGTYPE_p_void(cPtr, false);
    }

    public void setStatus(int value) {
        pjsua2JNI.OnNatCheckStunServersCompleteParam_status_set(this.swigCPtr, this, value);
    }

    public int getStatus() {
        return pjsua2JNI.OnNatCheckStunServersCompleteParam_status_get(this.swigCPtr, this);
    }

    public void setName(String value) {
        pjsua2JNI.OnNatCheckStunServersCompleteParam_name_set(this.swigCPtr, this, value);
    }

    public String getName() {
        return pjsua2JNI.OnNatCheckStunServersCompleteParam_name_get(this.swigCPtr, this);
    }

    public void setAddr(String value) {
        pjsua2JNI.OnNatCheckStunServersCompleteParam_addr_set(this.swigCPtr, this, value);
    }

    public String getAddr() {
        return pjsua2JNI.OnNatCheckStunServersCompleteParam_addr_get(this.swigCPtr, this);
    }

    public OnNatCheckStunServersCompleteParam() {
        this(pjsua2JNI.new_OnNatCheckStunServersCompleteParam(), true);
    }
}

