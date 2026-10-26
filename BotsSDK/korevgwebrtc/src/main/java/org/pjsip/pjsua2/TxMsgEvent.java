/*
 * Decompiled with CFR 0.152.
 */
package org.pjsip.pjsua2;

import org.pjsip.pjsua2.SipTxData;
import org.pjsip.pjsua2.pjsua2JNI;

public class TxMsgEvent {
    private transient long swigCPtr;
    protected transient boolean swigCMemOwn;

    protected TxMsgEvent(long cPtr, boolean cMemoryOwn) {
        this.swigCMemOwn = cMemoryOwn;
        this.swigCPtr = cPtr;
    }

    protected static long getCPtr(TxMsgEvent obj) {
        return obj == null ? 0L : obj.swigCPtr;
    }

    protected void finalize() {
        this.delete();
    }

    public synchronized void delete() {
        if (this.swigCPtr != 0L) {
            if (this.swigCMemOwn) {
                this.swigCMemOwn = false;
                pjsua2JNI.delete_TxMsgEvent(this.swigCPtr);
            }
            this.swigCPtr = 0L;
        }
    }

    public void setTdata(SipTxData value) {
        pjsua2JNI.TxMsgEvent_tdata_set(this.swigCPtr, this, SipTxData.getCPtr(value), value);
    }

    public SipTxData getTdata() {
        long cPtr = pjsua2JNI.TxMsgEvent_tdata_get(this.swigCPtr, this);
        return cPtr == 0L ? null : new SipTxData(cPtr, false);
    }

    public TxMsgEvent() {
        this(pjsua2JNI.new_TxMsgEvent(), true);
    }
}

