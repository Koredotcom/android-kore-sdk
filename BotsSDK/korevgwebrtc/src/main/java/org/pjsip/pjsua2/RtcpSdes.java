/*
 * Decompiled with CFR 0.152.
 */
package org.pjsip.pjsua2;

import org.pjsip.pjsua2.pjsua2JNI;

public class RtcpSdes {
    private transient long swigCPtr;
    protected transient boolean swigCMemOwn;

    protected RtcpSdes(long cPtr, boolean cMemoryOwn) {
        this.swigCMemOwn = cMemoryOwn;
        this.swigCPtr = cPtr;
    }

    protected static long getCPtr(RtcpSdes obj) {
        return obj == null ? 0L : obj.swigCPtr;
    }

    protected void finalize() {
        this.delete();
    }

    public synchronized void delete() {
        if (this.swigCPtr != 0L) {
            if (this.swigCMemOwn) {
                this.swigCMemOwn = false;
                pjsua2JNI.delete_RtcpSdes(this.swigCPtr);
            }
            this.swigCPtr = 0L;
        }
    }

    public void setCname(String value) {
        pjsua2JNI.RtcpSdes_cname_set(this.swigCPtr, this, value);
    }

    public String getCname() {
        return pjsua2JNI.RtcpSdes_cname_get(this.swigCPtr, this);
    }

    public void setName(String value) {
        pjsua2JNI.RtcpSdes_name_set(this.swigCPtr, this, value);
    }

    public String getName() {
        return pjsua2JNI.RtcpSdes_name_get(this.swigCPtr, this);
    }

    public void setEmail(String value) {
        pjsua2JNI.RtcpSdes_email_set(this.swigCPtr, this, value);
    }

    public String getEmail() {
        return pjsua2JNI.RtcpSdes_email_get(this.swigCPtr, this);
    }

    public void setPhone(String value) {
        pjsua2JNI.RtcpSdes_phone_set(this.swigCPtr, this, value);
    }

    public String getPhone() {
        return pjsua2JNI.RtcpSdes_phone_get(this.swigCPtr, this);
    }

    public void setLoc(String value) {
        pjsua2JNI.RtcpSdes_loc_set(this.swigCPtr, this, value);
    }

    public String getLoc() {
        return pjsua2JNI.RtcpSdes_loc_get(this.swigCPtr, this);
    }

    public void setTool(String value) {
        pjsua2JNI.RtcpSdes_tool_set(this.swigCPtr, this, value);
    }

    public String getTool() {
        return pjsua2JNI.RtcpSdes_tool_get(this.swigCPtr, this);
    }

    public void setNote(String value) {
        pjsua2JNI.RtcpSdes_note_set(this.swigCPtr, this, value);
    }

    public String getNote() {
        return pjsua2JNI.RtcpSdes_note_get(this.swigCPtr, this);
    }

    public RtcpSdes() {
        this(pjsua2JNI.new_RtcpSdes(), true);
    }
}

