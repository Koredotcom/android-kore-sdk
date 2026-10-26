/*
 * Decompiled with CFR 0.152.
 */
package org.pjsip.pjsua2;

import org.pjsip.pjsua2.PresenceStatus;
import org.pjsip.pjsua2.pjsua2JNI;

public class BuddyInfo {
    private transient long swigCPtr;
    protected transient boolean swigCMemOwn;

    protected BuddyInfo(long cPtr, boolean cMemoryOwn) {
        this.swigCMemOwn = cMemoryOwn;
        this.swigCPtr = cPtr;
    }

    protected static long getCPtr(BuddyInfo obj) {
        return obj == null ? 0L : obj.swigCPtr;
    }

    protected void finalize() {
        this.delete();
    }

    public synchronized void delete() {
        if (this.swigCPtr != 0L) {
            if (this.swigCMemOwn) {
                this.swigCMemOwn = false;
                pjsua2JNI.delete_BuddyInfo(this.swigCPtr);
            }
            this.swigCPtr = 0L;
        }
    }

    public void setUri(String value) {
        pjsua2JNI.BuddyInfo_uri_set(this.swigCPtr, this, value);
    }

    public String getUri() {
        return pjsua2JNI.BuddyInfo_uri_get(this.swigCPtr, this);
    }

    public void setContact(String value) {
        pjsua2JNI.BuddyInfo_contact_set(this.swigCPtr, this, value);
    }

    public String getContact() {
        return pjsua2JNI.BuddyInfo_contact_get(this.swigCPtr, this);
    }

    public void setPresMonitorEnabled(boolean value) {
        pjsua2JNI.BuddyInfo_presMonitorEnabled_set(this.swigCPtr, this, value);
    }

    public boolean getPresMonitorEnabled() {
        return pjsua2JNI.BuddyInfo_presMonitorEnabled_get(this.swigCPtr, this);
    }

    public void setSubState(int value) {
        pjsua2JNI.BuddyInfo_subState_set(this.swigCPtr, this, value);
    }

    public int getSubState() {
        return pjsua2JNI.BuddyInfo_subState_get(this.swigCPtr, this);
    }

    public void setSubStateName(String value) {
        pjsua2JNI.BuddyInfo_subStateName_set(this.swigCPtr, this, value);
    }

    public String getSubStateName() {
        return pjsua2JNI.BuddyInfo_subStateName_get(this.swigCPtr, this);
    }

    public void setSubTermCode(int value) {
        pjsua2JNI.BuddyInfo_subTermCode_set(this.swigCPtr, this, value);
    }

    public int getSubTermCode() {
        return pjsua2JNI.BuddyInfo_subTermCode_get(this.swigCPtr, this);
    }

    public void setSubTermReason(String value) {
        pjsua2JNI.BuddyInfo_subTermReason_set(this.swigCPtr, this, value);
    }

    public String getSubTermReason() {
        return pjsua2JNI.BuddyInfo_subTermReason_get(this.swigCPtr, this);
    }

    public void setPresStatus(PresenceStatus value) {
        pjsua2JNI.BuddyInfo_presStatus_set(this.swigCPtr, this, PresenceStatus.getCPtr(value), value);
    }

    public PresenceStatus getPresStatus() {
        long cPtr = pjsua2JNI.BuddyInfo_presStatus_get(this.swigCPtr, this);
        return cPtr == 0L ? null : new PresenceStatus(cPtr, false);
    }

    public BuddyInfo() {
        this(pjsua2JNI.new_BuddyInfo(), true);
    }
}

