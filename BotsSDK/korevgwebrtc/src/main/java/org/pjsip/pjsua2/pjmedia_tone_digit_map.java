/*
 * Decompiled with CFR 0.152.
 */
package org.pjsip.pjsua2;

import org.pjsip.pjsua2.pjsua2JNI;

public class pjmedia_tone_digit_map {
    private transient long swigCPtr;
    protected transient boolean swigCMemOwn;

    protected pjmedia_tone_digit_map(long cPtr, boolean cMemoryOwn) {
        this.swigCMemOwn = cMemoryOwn;
        this.swigCPtr = cPtr;
    }

    protected static long getCPtr(pjmedia_tone_digit_map obj) {
        return obj == null ? 0L : obj.swigCPtr;
    }

    protected void finalize() {
        this.delete();
    }

    public synchronized void delete() {
        if (this.swigCPtr != 0L) {
            if (this.swigCMemOwn) {
                this.swigCMemOwn = false;
                pjsua2JNI.delete_pjmedia_tone_digit_map(this.swigCPtr);
            }
            this.swigCPtr = 0L;
        }
    }

    public void setCount(long value) {
        pjsua2JNI.pjmedia_tone_digit_map_count_set(this.swigCPtr, this, value);
    }

    public long getCount() {
        return pjsua2JNI.pjmedia_tone_digit_map_count_get(this.swigCPtr, this);
    }

    public pjmedia_tone_digit_map() {
        this(pjsua2JNI.new_pjmedia_tone_digit_map(), true);
    }
}

