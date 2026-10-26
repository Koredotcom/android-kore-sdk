/*
 * Decompiled with CFR 0.152.
 */
package org.pjsip.pjsua2;

import org.pjsip.pjsua2.pjsua2JNI;

public class pj_qos_params {
    private transient long swigCPtr;
    protected transient boolean swigCMemOwn;

    protected pj_qos_params(long cPtr, boolean cMemoryOwn) {
        this.swigCMemOwn = cMemoryOwn;
        this.swigCPtr = cPtr;
    }

    protected static long getCPtr(pj_qos_params obj) {
        return obj == null ? 0L : obj.swigCPtr;
    }

    protected void finalize() {
        this.delete();
    }

    public synchronized void delete() {
        if (this.swigCPtr != 0L) {
            if (this.swigCMemOwn) {
                this.swigCMemOwn = false;
                pjsua2JNI.delete_pj_qos_params(this.swigCPtr);
            }
            this.swigCPtr = 0L;
        }
    }

    public void setFlags(short value) {
        pjsua2JNI.pj_qos_params_flags_set(this.swigCPtr, this, value);
    }

    public short getFlags() {
        return pjsua2JNI.pj_qos_params_flags_get(this.swigCPtr, this);
    }

    public void setDscp_val(short value) {
        pjsua2JNI.pj_qos_params_dscp_val_set(this.swigCPtr, this, value);
    }

    public short getDscp_val() {
        return pjsua2JNI.pj_qos_params_dscp_val_get(this.swigCPtr, this);
    }

    public void setSo_prio(short value) {
        pjsua2JNI.pj_qos_params_so_prio_set(this.swigCPtr, this, value);
    }

    public short getSo_prio() {
        return pjsua2JNI.pj_qos_params_so_prio_get(this.swigCPtr, this);
    }

    public void setWmm_prio(int value) {
        pjsua2JNI.pj_qos_params_wmm_prio_set(this.swigCPtr, this, value);
    }

    public int getWmm_prio() {
        return pjsua2JNI.pj_qos_params_wmm_prio_get(this.swigCPtr, this);
    }

    public pj_qos_params() {
        this(pjsua2JNI.new_pj_qos_params(), true);
    }
}

