/*
 * Decompiled with CFR 0.152.
 */
package org.pjsip.pjsua2;

import org.pjsip.pjsua2.MediaEventData;
import org.pjsip.pjsua2.SWIGTYPE_p_void;
import org.pjsip.pjsua2.pjsua2JNI;

public class MediaEvent {
    private transient long swigCPtr;
    protected transient boolean swigCMemOwn;

    protected MediaEvent(long cPtr, boolean cMemoryOwn) {
        this.swigCMemOwn = cMemoryOwn;
        this.swigCPtr = cPtr;
    }

    protected static long getCPtr(MediaEvent obj) {
        return obj == null ? 0L : obj.swigCPtr;
    }

    protected void finalize() {
        this.delete();
    }

    public synchronized void delete() {
        if (this.swigCPtr != 0L) {
            if (this.swigCMemOwn) {
                this.swigCMemOwn = false;
                pjsua2JNI.delete_MediaEvent(this.swigCPtr);
            }
            this.swigCPtr = 0L;
        }
    }

    public void setType(int value) {
        pjsua2JNI.MediaEvent_type_set(this.swigCPtr, this, value);
    }

    public int getType() {
        return pjsua2JNI.MediaEvent_type_get(this.swigCPtr, this);
    }

    public void setData(MediaEventData value) {
        pjsua2JNI.MediaEvent_data_set(this.swigCPtr, this, MediaEventData.getCPtr(value), value);
    }

    public MediaEventData getData() {
        long cPtr = pjsua2JNI.MediaEvent_data_get(this.swigCPtr, this);
        return cPtr == 0L ? null : new MediaEventData(cPtr, false);
    }

    public void setPjMediaEvent(SWIGTYPE_p_void value) {
        pjsua2JNI.MediaEvent_pjMediaEvent_set(this.swigCPtr, this, SWIGTYPE_p_void.getCPtr(value));
    }

    public SWIGTYPE_p_void getPjMediaEvent() {
        long cPtr = pjsua2JNI.MediaEvent_pjMediaEvent_get(this.swigCPtr, this);
        return cPtr == 0L ? null : new SWIGTYPE_p_void(cPtr, false);
    }

    public MediaEvent() {
        this(pjsua2JNI.new_MediaEvent(), true);
    }
}

