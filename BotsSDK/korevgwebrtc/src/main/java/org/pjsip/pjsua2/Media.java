/*
 * Decompiled with CFR 0.152.
 */
package org.pjsip.pjsua2;

import org.pjsip.pjsua2.pjsua2JNI;

public class Media {
    private transient long swigCPtr;
    protected transient boolean swigCMemOwn;

    protected Media(long cPtr, boolean cMemoryOwn) {
        this.swigCMemOwn = cMemoryOwn;
        this.swigCPtr = cPtr;
    }

    protected static long getCPtr(Media obj) {
        return obj == null ? 0L : obj.swigCPtr;
    }

    protected void finalize() {
        this.delete();
    }

    public synchronized void delete() {
        if (this.swigCPtr != 0L) {
            if (this.swigCMemOwn) {
                this.swigCMemOwn = false;
                pjsua2JNI.delete_Media(this.swigCPtr);
            }
            this.swigCPtr = 0L;
        }
    }

    public int getType() {
        return pjsua2JNI.Media_getType(this.swigCPtr, this);
    }
}

