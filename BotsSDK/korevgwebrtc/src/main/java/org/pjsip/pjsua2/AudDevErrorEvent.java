/*
 * Decompiled with CFR 0.152.
 */
package org.pjsip.pjsua2;

import org.pjsip.pjsua2.pjsua2JNI;

public class AudDevErrorEvent {
    private transient long swigCPtr;
    protected transient boolean swigCMemOwn;

    protected AudDevErrorEvent(long cPtr, boolean cMemoryOwn) {
        this.swigCMemOwn = cMemoryOwn;
        this.swigCPtr = cPtr;
    }

    protected static long getCPtr(AudDevErrorEvent obj) {
        return obj == null ? 0L : obj.swigCPtr;
    }

    protected void finalize() {
        this.delete();
    }

    public synchronized void delete() {
        if (this.swigCPtr != 0L) {
            if (this.swigCMemOwn) {
                this.swigCMemOwn = false;
                pjsua2JNI.delete_AudDevErrorEvent(this.swigCPtr);
            }
            this.swigCPtr = 0L;
        }
    }

    public void setDir(int value) {
        pjsua2JNI.AudDevErrorEvent_dir_set(this.swigCPtr, this, value);
    }

    public int getDir() {
        return pjsua2JNI.AudDevErrorEvent_dir_get(this.swigCPtr, this);
    }

    public void setId(int value) {
        pjsua2JNI.AudDevErrorEvent_id_set(this.swigCPtr, this, value);
    }

    public int getId() {
        return pjsua2JNI.AudDevErrorEvent_id_get(this.swigCPtr, this);
    }

    public void setStatus(int value) {
        pjsua2JNI.AudDevErrorEvent_status_set(this.swigCPtr, this, value);
    }

    public int getStatus() {
        return pjsua2JNI.AudDevErrorEvent_status_get(this.swigCPtr, this);
    }

    public AudDevErrorEvent() {
        this(pjsua2JNI.new_AudDevErrorEvent(), true);
    }
}

