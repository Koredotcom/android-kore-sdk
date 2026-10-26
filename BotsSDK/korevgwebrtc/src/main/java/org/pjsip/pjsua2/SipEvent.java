/*
 * Decompiled with CFR 0.152.
 */
package org.pjsip.pjsua2;

import org.pjsip.pjsua2.SWIGTYPE_p_void;
import org.pjsip.pjsua2.SipEventBody;
import org.pjsip.pjsua2.pjsua2JNI;

public class SipEvent {
    private transient long swigCPtr;
    protected transient boolean swigCMemOwn;

    protected SipEvent(long cPtr, boolean cMemoryOwn) {
        this.swigCMemOwn = cMemoryOwn;
        this.swigCPtr = cPtr;
    }

    protected static long getCPtr(SipEvent obj) {
        return obj == null ? 0L : obj.swigCPtr;
    }

    protected void finalize() {
        this.delete();
    }

    public synchronized void delete() {
        if (this.swigCPtr != 0L) {
            if (this.swigCMemOwn) {
                this.swigCMemOwn = false;
                pjsua2JNI.delete_SipEvent(this.swigCPtr);
            }
            this.swigCPtr = 0L;
        }
    }

    public void setType(int value) {
        pjsua2JNI.SipEvent_type_set(this.swigCPtr, this, value);
    }

    public int getType() {
        return pjsua2JNI.SipEvent_type_get(this.swigCPtr, this);
    }

    public void setBody(SipEventBody value) {
        pjsua2JNI.SipEvent_body_set(this.swigCPtr, this, SipEventBody.getCPtr(value), value);
    }

    public SipEventBody getBody() {
        long cPtr = pjsua2JNI.SipEvent_body_get(this.swigCPtr, this);
        return cPtr == 0L ? null : new SipEventBody(cPtr, false);
    }

    public void setPjEvent(SWIGTYPE_p_void value) {
        pjsua2JNI.SipEvent_pjEvent_set(this.swigCPtr, this, SWIGTYPE_p_void.getCPtr(value));
    }

    public SWIGTYPE_p_void getPjEvent() {
        long cPtr = pjsua2JNI.SipEvent_pjEvent_get(this.swigCPtr, this);
        return cPtr == 0L ? null : new SWIGTYPE_p_void(cPtr, false);
    }

    public SipEvent() {
        this(pjsua2JNI.new_SipEvent(), true);
    }
}

