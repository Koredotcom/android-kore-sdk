/*
 * Decompiled with CFR 0.152.
 */
package org.pjsip.pjsua2;

import org.pjsip.pjsua2.SipTransaction;
import org.pjsip.pjsua2.SipTxData;
import org.pjsip.pjsua2.pjsua2JNI;

public class TxErrorEvent {
    private transient long swigCPtr;
    protected transient boolean swigCMemOwn;

    protected TxErrorEvent(long cPtr, boolean cMemoryOwn) {
        this.swigCMemOwn = cMemoryOwn;
        this.swigCPtr = cPtr;
    }

    protected static long getCPtr(TxErrorEvent obj) {
        return obj == null ? 0L : obj.swigCPtr;
    }

    protected void finalize() {
        this.delete();
    }

    public synchronized void delete() {
        if (this.swigCPtr != 0L) {
            if (this.swigCMemOwn) {
                this.swigCMemOwn = false;
                pjsua2JNI.delete_TxErrorEvent(this.swigCPtr);
            }
            this.swigCPtr = 0L;
        }
    }

    public void setTdata(SipTxData value) {
        pjsua2JNI.TxErrorEvent_tdata_set(this.swigCPtr, this, SipTxData.getCPtr(value), value);
    }

    public SipTxData getTdata() {
        long cPtr = pjsua2JNI.TxErrorEvent_tdata_get(this.swigCPtr, this);
        return cPtr == 0L ? null : new SipTxData(cPtr, false);
    }

    public void setTsx(SipTransaction value) {
        pjsua2JNI.TxErrorEvent_tsx_set(this.swigCPtr, this, SipTransaction.getCPtr(value), value);
    }

    public SipTransaction getTsx() {
        long cPtr = pjsua2JNI.TxErrorEvent_tsx_get(this.swigCPtr, this);
        return cPtr == 0L ? null : new SipTransaction(cPtr, false);
    }

    public TxErrorEvent() {
        this(pjsua2JNI.new_TxErrorEvent(), true);
    }
}

