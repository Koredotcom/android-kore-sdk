/*
 * Decompiled with CFR 0.152.
 */
package org.pjsip.pjsua2;

import org.pjsip.pjsua2.ContainerNode;
import org.pjsip.pjsua2.PersistentObject;
import org.pjsip.pjsua2.pjsua2JNI;

public class AccountMwiConfig
extends PersistentObject {
    private transient long swigCPtr;

    protected AccountMwiConfig(long cPtr, boolean cMemoryOwn) {
        super(pjsua2JNI.AccountMwiConfig_SWIGUpcast(cPtr), cMemoryOwn);
        this.swigCPtr = cPtr;
    }

    protected static long getCPtr(AccountMwiConfig obj) {
        return obj == null ? 0L : obj.swigCPtr;
    }

    @Override
    protected void finalize() {
        this.delete();
    }

    @Override
    public synchronized void delete() {
        if (this.swigCPtr != 0L) {
            if (this.swigCMemOwn) {
                this.swigCMemOwn = false;
                pjsua2JNI.delete_AccountMwiConfig(this.swigCPtr);
            }
            this.swigCPtr = 0L;
        }
        super.delete();
    }

    public void setEnabled(boolean value) {
        pjsua2JNI.AccountMwiConfig_enabled_set(this.swigCPtr, this, value);
    }

    public boolean getEnabled() {
        return pjsua2JNI.AccountMwiConfig_enabled_get(this.swigCPtr, this);
    }

    public void setExpirationSec(long value) {
        pjsua2JNI.AccountMwiConfig_expirationSec_set(this.swigCPtr, this, value);
    }

    public long getExpirationSec() {
        return pjsua2JNI.AccountMwiConfig_expirationSec_get(this.swigCPtr, this);
    }

    @Override
    public void readObject(ContainerNode node) throws Exception {
        pjsua2JNI.AccountMwiConfig_readObject(this.swigCPtr, this, ContainerNode.getCPtr(node), node);
    }

    @Override
    public void writeObject(ContainerNode node) throws Exception {
        pjsua2JNI.AccountMwiConfig_writeObject(this.swigCPtr, this, ContainerNode.getCPtr(node), node);
    }

    public AccountMwiConfig() {
        this(pjsua2JNI.new_AccountMwiConfig(), true);
    }
}

