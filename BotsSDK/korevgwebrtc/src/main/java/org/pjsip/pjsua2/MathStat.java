/*
 * Decompiled with CFR 0.152.
 */
package org.pjsip.pjsua2;

import org.pjsip.pjsua2.pjsua2JNI;

public class MathStat {
    private transient long swigCPtr;
    protected transient boolean swigCMemOwn;

    protected MathStat(long cPtr, boolean cMemoryOwn) {
        this.swigCMemOwn = cMemoryOwn;
        this.swigCPtr = cPtr;
    }

    protected static long getCPtr(MathStat obj) {
        return obj == null ? 0L : obj.swigCPtr;
    }

    protected void finalize() {
        this.delete();
    }

    public synchronized void delete() {
        if (this.swigCPtr != 0L) {
            if (this.swigCMemOwn) {
                this.swigCMemOwn = false;
                pjsua2JNI.delete_MathStat(this.swigCPtr);
            }
            this.swigCPtr = 0L;
        }
    }

    public void setN(int value) {
        pjsua2JNI.MathStat_n_set(this.swigCPtr, this, value);
    }

    public int getN() {
        return pjsua2JNI.MathStat_n_get(this.swigCPtr, this);
    }

    public void setMax(int value) {
        pjsua2JNI.MathStat_max_set(this.swigCPtr, this, value);
    }

    public int getMax() {
        return pjsua2JNI.MathStat_max_get(this.swigCPtr, this);
    }

    public void setMin(int value) {
        pjsua2JNI.MathStat_min_set(this.swigCPtr, this, value);
    }

    public int getMin() {
        return pjsua2JNI.MathStat_min_get(this.swigCPtr, this);
    }

    public void setLast(int value) {
        pjsua2JNI.MathStat_last_set(this.swigCPtr, this, value);
    }

    public int getLast() {
        return pjsua2JNI.MathStat_last_get(this.swigCPtr, this);
    }

    public void setMean(int value) {
        pjsua2JNI.MathStat_mean_set(this.swigCPtr, this, value);
    }

    public int getMean() {
        return pjsua2JNI.MathStat_mean_get(this.swigCPtr, this);
    }

    public MathStat() {
        this(pjsua2JNI.new_MathStat(), true);
    }
}

