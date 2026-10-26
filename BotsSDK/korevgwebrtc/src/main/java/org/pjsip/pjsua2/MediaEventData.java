/*
 * Decompiled with CFR 0.152.
 */
package org.pjsip.pjsua2;

import org.pjsip.pjsua2.AudDevErrorEvent;
import org.pjsip.pjsua2.MediaFmtChangedEvent;
import org.pjsip.pjsua2.SWIGTYPE_p_void;
import org.pjsip.pjsua2.pjsua2JNI;

public class MediaEventData {
    private transient long swigCPtr;
    protected transient boolean swigCMemOwn;

    protected MediaEventData(long cPtr, boolean cMemoryOwn) {
        this.swigCMemOwn = cMemoryOwn;
        this.swigCPtr = cPtr;
    }

    protected static long getCPtr(MediaEventData obj) {
        return obj == null ? 0L : obj.swigCPtr;
    }

    protected void finalize() {
        this.delete();
    }

    public synchronized void delete() {
        if (this.swigCPtr != 0L) {
            if (this.swigCMemOwn) {
                this.swigCMemOwn = false;
                pjsua2JNI.delete_MediaEventData(this.swigCPtr);
            }
            this.swigCPtr = 0L;
        }
    }

    public void setFmtChanged(MediaFmtChangedEvent value) {
        pjsua2JNI.MediaEventData_fmtChanged_set(this.swigCPtr, this, MediaFmtChangedEvent.getCPtr(value), value);
    }

    public MediaFmtChangedEvent getFmtChanged() {
        long cPtr = pjsua2JNI.MediaEventData_fmtChanged_get(this.swigCPtr, this);
        return cPtr == 0L ? null : new MediaFmtChangedEvent(cPtr, false);
    }

    public void setAudDevError(AudDevErrorEvent value) {
        pjsua2JNI.MediaEventData_audDevError_set(this.swigCPtr, this, AudDevErrorEvent.getCPtr(value), value);
    }

    public AudDevErrorEvent getAudDevError() {
        long cPtr = pjsua2JNI.MediaEventData_audDevError_get(this.swigCPtr, this);
        return cPtr == 0L ? null : new AudDevErrorEvent(cPtr, false);
    }

    public void setPtr(SWIGTYPE_p_void value) {
        pjsua2JNI.MediaEventData_ptr_set(this.swigCPtr, this, SWIGTYPE_p_void.getCPtr(value));
    }

    public SWIGTYPE_p_void getPtr() {
        long cPtr = pjsua2JNI.MediaEventData_ptr_get(this.swigCPtr, this);
        return cPtr == 0L ? null : new SWIGTYPE_p_void(cPtr, false);
    }

    public MediaEventData() {
        this(pjsua2JNI.new_MediaEventData(), true);
    }
}

