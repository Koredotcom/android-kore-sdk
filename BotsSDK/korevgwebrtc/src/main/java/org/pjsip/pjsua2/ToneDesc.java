/*
 * Decompiled with CFR 0.152.
 */
package org.pjsip.pjsua2;

import org.pjsip.pjsua2.pjmedia_tone_desc;
import org.pjsip.pjsua2.pjsua2JNI;

public class ToneDesc
extends pjmedia_tone_desc {
    private transient long swigCPtr;

    protected ToneDesc(long cPtr, boolean cMemoryOwn) {
        super(pjsua2JNI.ToneDesc_SWIGUpcast(cPtr), cMemoryOwn);
        this.swigCPtr = cPtr;
    }

    protected static long getCPtr(ToneDesc obj) {
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
                pjsua2JNI.delete_ToneDesc(this.swigCPtr);
            }
            this.swigCPtr = 0L;
        }
        super.delete();
    }

    public ToneDesc() {
        this(pjsua2JNI.new_ToneDesc(), true);
    }
}

