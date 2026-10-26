/*
 * Decompiled with CFR 0.152.
 */
package org.pjsip.pjsua2;

import org.pjsip.pjsua2.ContainerNode;
import org.pjsip.pjsua2.pjsua2JNI;

public class AccountIpChangeConfig {
    private transient long swigCPtr;
    protected transient boolean swigCMemOwn;

    protected AccountIpChangeConfig(long cPtr, boolean cMemoryOwn) {
        this.swigCMemOwn = cMemoryOwn;
        this.swigCPtr = cPtr;
    }

    protected static long getCPtr(AccountIpChangeConfig obj) {
        return obj == null ? 0L : obj.swigCPtr;
    }

    protected void finalize() {
        this.delete();
    }

    public synchronized void delete() {
        if (this.swigCPtr != 0L) {
            if (this.swigCMemOwn) {
                this.swigCMemOwn = false;
                pjsua2JNI.delete_AccountIpChangeConfig(this.swigCPtr);
            }
            this.swigCPtr = 0L;
        }
    }

    public void setShutdownTp(boolean value) {
        pjsua2JNI.AccountIpChangeConfig_shutdownTp_set(this.swigCPtr, this, value);
    }

    public boolean getShutdownTp() {
        return pjsua2JNI.AccountIpChangeConfig_shutdownTp_get(this.swigCPtr, this);
    }

    public void setHangupCalls(boolean value) {
        pjsua2JNI.AccountIpChangeConfig_hangupCalls_set(this.swigCPtr, this, value);
    }

    public boolean getHangupCalls() {
        return pjsua2JNI.AccountIpChangeConfig_hangupCalls_get(this.swigCPtr, this);
    }

    public void setReinviteFlags(long value) {
        pjsua2JNI.AccountIpChangeConfig_reinviteFlags_set(this.swigCPtr, this, value);
    }

    public long getReinviteFlags() {
        return pjsua2JNI.AccountIpChangeConfig_reinviteFlags_get(this.swigCPtr, this);
    }

    public void readObject(ContainerNode node) throws Exception {
        pjsua2JNI.AccountIpChangeConfig_readObject(this.swigCPtr, this, ContainerNode.getCPtr(node), node);
    }

    public void writeObject(ContainerNode node) throws Exception {
        pjsua2JNI.AccountIpChangeConfig_writeObject(this.swigCPtr, this, ContainerNode.getCPtr(node), node);
    }

    public AccountIpChangeConfig() {
        this(pjsua2JNI.new_AccountIpChangeConfig(), true);
    }
}

