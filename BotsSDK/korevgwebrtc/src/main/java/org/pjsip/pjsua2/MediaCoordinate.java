/*
 * Decompiled with CFR 0.152.
 */
package org.pjsip.pjsua2;

import org.pjsip.pjsua2.pjsua2JNI;

public class MediaCoordinate {
    private transient long swigCPtr;
    protected transient boolean swigCMemOwn;

    protected MediaCoordinate(long cPtr, boolean cMemoryOwn) {
        this.swigCMemOwn = cMemoryOwn;
        this.swigCPtr = cPtr;
    }

    protected static long getCPtr(MediaCoordinate obj) {
        return obj == null ? 0L : obj.swigCPtr;
    }

    protected void finalize() {
        this.delete();
    }

    public synchronized void delete() {
        if (this.swigCPtr != 0L) {
            if (this.swigCMemOwn) {
                this.swigCMemOwn = false;
                pjsua2JNI.delete_MediaCoordinate(this.swigCPtr);
            }
            this.swigCPtr = 0L;
        }
    }

    public void setX(int value) {
        pjsua2JNI.MediaCoordinate_x_set(this.swigCPtr, this, value);
    }

    public int getX() {
        return pjsua2JNI.MediaCoordinate_x_get(this.swigCPtr, this);
    }

    public void setY(int value) {
        pjsua2JNI.MediaCoordinate_y_set(this.swigCPtr, this, value);
    }

    public int getY() {
        return pjsua2JNI.MediaCoordinate_y_get(this.swigCPtr, this);
    }

    public MediaCoordinate() {
        this(pjsua2JNI.new_MediaCoordinate(), true);
    }
}

