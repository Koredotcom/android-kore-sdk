/*
 * Decompiled with CFR 0.152.
 */
package org.pjsip.pjsua2;

import org.pjsip.pjsua2.SWIGTYPE_p_void;
import org.pjsip.pjsua2.pjsua2JNI;

public class UserEvent {
    private transient long swigCPtr;
    protected transient boolean swigCMemOwn;

    protected UserEvent(long cPtr, boolean cMemoryOwn) {
        this.swigCMemOwn = cMemoryOwn;
        this.swigCPtr = cPtr;
    }

    protected static long getCPtr(UserEvent obj) {
        return obj == null ? 0L : obj.swigCPtr;
    }

    protected void finalize() {
        this.delete();
    }

    public synchronized void delete() {
        if (this.swigCPtr != 0L) {
            if (this.swigCMemOwn) {
                this.swigCMemOwn = false;
                pjsua2JNI.delete_UserEvent(this.swigCPtr);
            }
            this.swigCPtr = 0L;
        }
    }

    public void setUser1(SWIGTYPE_p_void value) {
        pjsua2JNI.UserEvent_user1_set(this.swigCPtr, this, SWIGTYPE_p_void.getCPtr(value));
    }

    public SWIGTYPE_p_void getUser1() {
        long cPtr = pjsua2JNI.UserEvent_user1_get(this.swigCPtr, this);
        return cPtr == 0L ? null : new SWIGTYPE_p_void(cPtr, false);
    }

    public void setUser2(SWIGTYPE_p_void value) {
        pjsua2JNI.UserEvent_user2_set(this.swigCPtr, this, SWIGTYPE_p_void.getCPtr(value));
    }

    public SWIGTYPE_p_void getUser2() {
        long cPtr = pjsua2JNI.UserEvent_user2_get(this.swigCPtr, this);
        return cPtr == 0L ? null : new SWIGTYPE_p_void(cPtr, false);
    }

    public void setUser3(SWIGTYPE_p_void value) {
        pjsua2JNI.UserEvent_user3_set(this.swigCPtr, this, SWIGTYPE_p_void.getCPtr(value));
    }

    public SWIGTYPE_p_void getUser3() {
        long cPtr = pjsua2JNI.UserEvent_user3_get(this.swigCPtr, this);
        return cPtr == 0L ? null : new SWIGTYPE_p_void(cPtr, false);
    }

    public void setUser4(SWIGTYPE_p_void value) {
        pjsua2JNI.UserEvent_user4_set(this.swigCPtr, this, SWIGTYPE_p_void.getCPtr(value));
    }

    public SWIGTYPE_p_void getUser4() {
        long cPtr = pjsua2JNI.UserEvent_user4_get(this.swigCPtr, this);
        return cPtr == 0L ? null : new SWIGTYPE_p_void(cPtr, false);
    }

    public UserEvent() {
        this(pjsua2JNI.new_UserEvent(), true);
    }
}

