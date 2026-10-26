/*
 * Decompiled with CFR 0.152.
 */
package org.pjsip.pjsua2;

import org.pjsip.pjsua2.pjsua2JNI;

public class LossType {
    private transient long swigCPtr;
    protected transient boolean swigCMemOwn;

    protected LossType(long cPtr, boolean cMemoryOwn) {
        this.swigCMemOwn = cMemoryOwn;
        this.swigCPtr = cPtr;
    }

    protected static long getCPtr(LossType obj) {
        return obj == null ? 0L : obj.swigCPtr;
    }

    protected void finalize() {
        this.delete();
    }

    public synchronized void delete() {
        if (this.swigCPtr != 0L) {
            if (this.swigCMemOwn) {
                this.swigCMemOwn = false;
                pjsua2JNI.delete_LossType(this.swigCPtr);
            }
            this.swigCPtr = 0L;
        }
    }

    public void setBurst(long value) {
        pjsua2JNI.LossType_burst_set(this.swigCPtr, this, value);
    }

    public long getBurst() {
        return pjsua2JNI.LossType_burst_get(this.swigCPtr, this);
    }

    public void setRandom(long value) {
        pjsua2JNI.LossType_random_set(this.swigCPtr, this, value);
    }

    public long getRandom() {
        return pjsua2JNI.LossType_random_get(this.swigCPtr, this);
    }

    public LossType() {
        this(pjsua2JNI.new_LossType(), true);
    }
}

