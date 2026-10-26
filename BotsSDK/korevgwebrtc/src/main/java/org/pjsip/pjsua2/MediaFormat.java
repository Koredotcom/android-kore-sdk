/*
 * Decompiled with CFR 0.152.
 */
package org.pjsip.pjsua2;

import org.pjsip.pjsua2.pjsua2JNI;

public class MediaFormat {
    private transient long swigCPtr;
    protected transient boolean swigCMemOwn;

    protected MediaFormat(long cPtr, boolean cMemoryOwn) {
        this.swigCMemOwn = cMemoryOwn;
        this.swigCPtr = cPtr;
    }

    protected static long getCPtr(MediaFormat obj) {
        return obj == null ? 0L : obj.swigCPtr;
    }

    protected void finalize() {
        this.delete();
    }

    public synchronized void delete() {
        if (this.swigCPtr != 0L) {
            if (this.swigCMemOwn) {
                this.swigCMemOwn = false;
                pjsua2JNI.delete_MediaFormat(this.swigCPtr);
            }
            this.swigCPtr = 0L;
        }
    }

    public void setId(long value) {
        pjsua2JNI.MediaFormat_id_set(this.swigCPtr, this, value);
    }

    public long getId() {
        return pjsua2JNI.MediaFormat_id_get(this.swigCPtr, this);
    }

    public void setType(int value) {
        pjsua2JNI.MediaFormat_type_set(this.swigCPtr, this, value);
    }

    public int getType() {
        return pjsua2JNI.MediaFormat_type_get(this.swigCPtr, this);
    }

    public MediaFormat() {
        this(pjsua2JNI.new_MediaFormat(), true);
    }
}

