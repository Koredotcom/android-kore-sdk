/*
 * Decompiled with CFR 0.152.
 */
package org.pjsip.pjsua2;

import org.pjsip.pjsua2.pjsua2JNI;

public class PresenceStatus {
    private transient long swigCPtr;
    protected transient boolean swigCMemOwn;

    protected PresenceStatus(long cPtr, boolean cMemoryOwn) {
        this.swigCMemOwn = cMemoryOwn;
        this.swigCPtr = cPtr;
    }

    protected static long getCPtr(PresenceStatus obj) {
        return obj == null ? 0L : obj.swigCPtr;
    }

    protected void finalize() {
        this.delete();
    }

    public synchronized void delete() {
        if (this.swigCPtr != 0L) {
            if (this.swigCMemOwn) {
                this.swigCMemOwn = false;
                pjsua2JNI.delete_PresenceStatus(this.swigCPtr);
            }
            this.swigCPtr = 0L;
        }
    }

    public void setStatus(int value) {
        pjsua2JNI.PresenceStatus_status_set(this.swigCPtr, this, value);
    }

    public int getStatus() {
        return pjsua2JNI.PresenceStatus_status_get(this.swigCPtr, this);
    }

    public void setStatusText(String value) {
        pjsua2JNI.PresenceStatus_statusText_set(this.swigCPtr, this, value);
    }

    public String getStatusText() {
        return pjsua2JNI.PresenceStatus_statusText_get(this.swigCPtr, this);
    }

    public void setActivity(int value) {
        pjsua2JNI.PresenceStatus_activity_set(this.swigCPtr, this, value);
    }

    public int getActivity() {
        return pjsua2JNI.PresenceStatus_activity_get(this.swigCPtr, this);
    }

    public void setNote(String value) {
        pjsua2JNI.PresenceStatus_note_set(this.swigCPtr, this, value);
    }

    public String getNote() {
        return pjsua2JNI.PresenceStatus_note_get(this.swigCPtr, this);
    }

    public void setRpidId(String value) {
        pjsua2JNI.PresenceStatus_rpidId_set(this.swigCPtr, this, value);
    }

    public String getRpidId() {
        return pjsua2JNI.PresenceStatus_rpidId_get(this.swigCPtr, this);
    }

    public PresenceStatus() {
        this(pjsua2JNI.new_PresenceStatus(), true);
    }
}

