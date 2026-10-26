/*
 * Decompiled with CFR 0.152.
 */
package org.pjsip.pjsua2;

import org.pjsip.pjsua2.SipTransaction;
import org.pjsip.pjsua2.TsxStateEventSrc;
import org.pjsip.pjsua2.pjsua2JNI;

public class TsxStateEvent {
    private transient long swigCPtr;
    protected transient boolean swigCMemOwn;

    protected TsxStateEvent(long cPtr, boolean cMemoryOwn) {
        this.swigCMemOwn = cMemoryOwn;
        this.swigCPtr = cPtr;
    }

    protected static long getCPtr(TsxStateEvent obj) {
        return obj == null ? 0L : obj.swigCPtr;
    }

    protected void finalize() {
        this.delete();
    }

    public synchronized void delete() {
        if (this.swigCPtr != 0L) {
            if (this.swigCMemOwn) {
                this.swigCMemOwn = false;
                pjsua2JNI.delete_TsxStateEvent(this.swigCPtr);
            }
            this.swigCPtr = 0L;
        }
    }

    public void setSrc(TsxStateEventSrc value) {
        pjsua2JNI.TsxStateEvent_src_set(this.swigCPtr, this, TsxStateEventSrc.getCPtr(value), value);
    }

    public TsxStateEventSrc getSrc() {
        long cPtr = pjsua2JNI.TsxStateEvent_src_get(this.swigCPtr, this);
        return cPtr == 0L ? null : new TsxStateEventSrc(cPtr, false);
    }

    public void setTsx(SipTransaction value) {
        pjsua2JNI.TsxStateEvent_tsx_set(this.swigCPtr, this, SipTransaction.getCPtr(value), value);
    }

    public SipTransaction getTsx() {
        long cPtr = pjsua2JNI.TsxStateEvent_tsx_get(this.swigCPtr, this);
        return cPtr == 0L ? null : new SipTransaction(cPtr, false);
    }

    public void setPrevState(int value) {
        pjsua2JNI.TsxStateEvent_prevState_set(this.swigCPtr, this, value);
    }

    public int getPrevState() {
        return pjsua2JNI.TsxStateEvent_prevState_get(this.swigCPtr, this);
    }

    public void setType(int value) {
        pjsua2JNI.TsxStateEvent_type_set(this.swigCPtr, this, value);
    }

    public int getType() {
        return pjsua2JNI.TsxStateEvent_type_get(this.swigCPtr, this);
    }

    public TsxStateEvent() {
        this(pjsua2JNI.new_TsxStateEvent(), true);
    }
}

