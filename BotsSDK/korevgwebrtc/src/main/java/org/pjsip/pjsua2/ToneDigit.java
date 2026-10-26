/*
 * Decompiled with CFR 0.152.
 */
package org.pjsip.pjsua2;

import org.pjsip.pjsua2.pjmedia_tone_digit;
import org.pjsip.pjsua2.pjsua2JNI;

public class ToneDigit
extends pjmedia_tone_digit {
    private transient long swigCPtr;

    protected ToneDigit(long cPtr, boolean cMemoryOwn) {
        super(pjsua2JNI.ToneDigit_SWIGUpcast(cPtr), cMemoryOwn);
        this.swigCPtr = cPtr;
    }

    protected static long getCPtr(ToneDigit obj) {
        return obj == null ? 0L : obj.swigCPtr;
    }

    @Override
    protected void finalize() {
        this.delete();
    }

    @Override
    public synchronized void delete() {
        if (this.swigCPtr != 0L) {
            if (this.swigCMemOwn) {
                this.swigCMemOwn = false;
                pjsua2JNI.delete_ToneDigit(this.swigCPtr);
            }
            this.swigCPtr = 0L;
        }
        super.delete();
    }

    public ToneDigit() {
        this(pjsua2JNI.new_ToneDigit(), true);
    }
}

