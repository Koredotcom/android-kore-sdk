/*
 * Decompiled with CFR 0.152.
 */
package org.pjsip.pjsua2;

import org.pjsip.pjsua2.pjsua2JNI;

public class MediaFmtChangedEvent {
    private transient long swigCPtr;
    protected transient boolean swigCMemOwn;

    protected MediaFmtChangedEvent(long cPtr, boolean cMemoryOwn) {
        this.swigCMemOwn = cMemoryOwn;
        this.swigCPtr = cPtr;
    }

    protected static long getCPtr(MediaFmtChangedEvent obj) {
        return obj == null ? 0L : obj.swigCPtr;
    }

    protected void finalize() {
        this.delete();
    }

    public synchronized void delete() {
        if (this.swigCPtr != 0L) {
            if (this.swigCMemOwn) {
                this.swigCMemOwn = false;
                pjsua2JNI.delete_MediaFmtChangedEvent(this.swigCPtr);
            }
            this.swigCPtr = 0L;
        }
    }

    public void setNewWidth(long value) {
        pjsua2JNI.MediaFmtChangedEvent_newWidth_set(this.swigCPtr, this, value);
    }

    public long getNewWidth() {
        return pjsua2JNI.MediaFmtChangedEvent_newWidth_get(this.swigCPtr, this);
    }

    public void setNewHeight(long value) {
        pjsua2JNI.MediaFmtChangedEvent_newHeight_set(this.swigCPtr, this, value);
    }

    public long getNewHeight() {
        return pjsua2JNI.MediaFmtChangedEvent_newHeight_get(this.swigCPtr, this);
    }

    public MediaFmtChangedEvent() {
        this(pjsua2JNI.new_MediaFmtChangedEvent(), true);
    }
}

