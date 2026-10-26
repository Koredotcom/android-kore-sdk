/*
 * Decompiled with CFR 0.152.
 */
package org.pjsip.pjsua2;

import org.pjsip.pjsua2.SipTxOption;
import org.pjsip.pjsua2.pjsua2JNI;

public class SendTypingIndicationParam {
    private transient long swigCPtr;
    protected transient boolean swigCMemOwn;

    protected SendTypingIndicationParam(long cPtr, boolean cMemoryOwn) {
        this.swigCMemOwn = cMemoryOwn;
        this.swigCPtr = cPtr;
    }

    protected static long getCPtr(SendTypingIndicationParam obj) {
        return obj == null ? 0L : obj.swigCPtr;
    }

    protected void finalize() {
        this.delete();
    }

    public synchronized void delete() {
        if (this.swigCPtr != 0L) {
            if (this.swigCMemOwn) {
                this.swigCMemOwn = false;
                pjsua2JNI.delete_SendTypingIndicationParam(this.swigCPtr);
            }
            this.swigCPtr = 0L;
        }
    }

    public void setIsTyping(boolean value) {
        pjsua2JNI.SendTypingIndicationParam_isTyping_set(this.swigCPtr, this, value);
    }

    public boolean getIsTyping() {
        return pjsua2JNI.SendTypingIndicationParam_isTyping_get(this.swigCPtr, this);
    }

    public void setTxOption(SipTxOption value) {
        pjsua2JNI.SendTypingIndicationParam_txOption_set(this.swigCPtr, this, SipTxOption.getCPtr(value), value);
    }

    public SipTxOption getTxOption() {
        long cPtr = pjsua2JNI.SendTypingIndicationParam_txOption_get(this.swigCPtr, this);
        return cPtr == 0L ? null : new SipTxOption(cPtr, false);
    }

    public SendTypingIndicationParam() {
        this(pjsua2JNI.new_SendTypingIndicationParam(), true);
    }
}

