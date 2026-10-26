/*
 * Decompiled with CFR 0.152.
 */
package org.pjsip.pjsua2;

import org.pjsip.pjsua2.RxMsgEvent;
import org.pjsip.pjsua2.TimerEvent;
import org.pjsip.pjsua2.TsxStateEvent;
import org.pjsip.pjsua2.TxErrorEvent;
import org.pjsip.pjsua2.TxMsgEvent;
import org.pjsip.pjsua2.UserEvent;
import org.pjsip.pjsua2.pjsua2JNI;

public class SipEventBody {
    private transient long swigCPtr;
    protected transient boolean swigCMemOwn;

    protected SipEventBody(long cPtr, boolean cMemoryOwn) {
        this.swigCMemOwn = cMemoryOwn;
        this.swigCPtr = cPtr;
    }

    protected static long getCPtr(SipEventBody obj) {
        return obj == null ? 0L : obj.swigCPtr;
    }

    protected void finalize() {
        this.delete();
    }

    public synchronized void delete() {
        if (this.swigCPtr != 0L) {
            if (this.swigCMemOwn) {
                this.swigCMemOwn = false;
                pjsua2JNI.delete_SipEventBody(this.swigCPtr);
            }
            this.swigCPtr = 0L;
        }
    }

    public void setTimer(TimerEvent value) {
        pjsua2JNI.SipEventBody_timer_set(this.swigCPtr, this, TimerEvent.getCPtr(value), value);
    }

    public TimerEvent getTimer() {
        long cPtr = pjsua2JNI.SipEventBody_timer_get(this.swigCPtr, this);
        return cPtr == 0L ? null : new TimerEvent(cPtr, false);
    }

    public void setTsxState(TsxStateEvent value) {
        pjsua2JNI.SipEventBody_tsxState_set(this.swigCPtr, this, TsxStateEvent.getCPtr(value), value);
    }

    public TsxStateEvent getTsxState() {
        long cPtr = pjsua2JNI.SipEventBody_tsxState_get(this.swigCPtr, this);
        return cPtr == 0L ? null : new TsxStateEvent(cPtr, false);
    }

    public void setTxMsg(TxMsgEvent value) {
        pjsua2JNI.SipEventBody_txMsg_set(this.swigCPtr, this, TxMsgEvent.getCPtr(value), value);
    }

    public TxMsgEvent getTxMsg() {
        long cPtr = pjsua2JNI.SipEventBody_txMsg_get(this.swigCPtr, this);
        return cPtr == 0L ? null : new TxMsgEvent(cPtr, false);
    }

    public void setTxError(TxErrorEvent value) {
        pjsua2JNI.SipEventBody_txError_set(this.swigCPtr, this, TxErrorEvent.getCPtr(value), value);
    }

    public TxErrorEvent getTxError() {
        long cPtr = pjsua2JNI.SipEventBody_txError_get(this.swigCPtr, this);
        return cPtr == 0L ? null : new TxErrorEvent(cPtr, false);
    }

    public void setRxMsg(RxMsgEvent value) {
        pjsua2JNI.SipEventBody_rxMsg_set(this.swigCPtr, this, RxMsgEvent.getCPtr(value), value);
    }

    public RxMsgEvent getRxMsg() {
        long cPtr = pjsua2JNI.SipEventBody_rxMsg_get(this.swigCPtr, this);
        return cPtr == 0L ? null : new RxMsgEvent(cPtr, false);
    }

    public void setUser(UserEvent value) {
        pjsua2JNI.SipEventBody_user_set(this.swigCPtr, this, UserEvent.getCPtr(value), value);
    }

    public UserEvent getUser() {
        long cPtr = pjsua2JNI.SipEventBody_user_get(this.swigCPtr, this);
        return cPtr == 0L ? null : new UserEvent(cPtr, false);
    }

    public SipEventBody() {
        this(pjsua2JNI.new_SipEventBody(), true);
    }
}

