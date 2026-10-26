/*
 * Decompiled with CFR 0.152.
 */
package org.pjsip.pjsua2;

import org.pjsip.pjsua2.ContainerNode;
import org.pjsip.pjsua2.PersistentObject;
import org.pjsip.pjsua2.pjsua2JNI;

public class AccountCallConfig
extends PersistentObject {
    private transient long swigCPtr;

    protected AccountCallConfig(long cPtr, boolean cMemoryOwn) {
        super(pjsua2JNI.AccountCallConfig_SWIGUpcast(cPtr), cMemoryOwn);
        this.swigCPtr = cPtr;
    }

    protected static long getCPtr(AccountCallConfig obj) {
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
                pjsua2JNI.delete_AccountCallConfig(this.swigCPtr);
            }
            this.swigCPtr = 0L;
        }
        super.delete();
    }

    public void setHoldType(int value) {
        pjsua2JNI.AccountCallConfig_holdType_set(this.swigCPtr, this, value);
    }

    public int getHoldType() {
        return pjsua2JNI.AccountCallConfig_holdType_get(this.swigCPtr, this);
    }

    public void setPrackUse(int value) {
        pjsua2JNI.AccountCallConfig_prackUse_set(this.swigCPtr, this, value);
    }

    public int getPrackUse() {
        return pjsua2JNI.AccountCallConfig_prackUse_get(this.swigCPtr, this);
    }

    public void setTimerUse(int value) {
        pjsua2JNI.AccountCallConfig_timerUse_set(this.swigCPtr, this, value);
    }

    public int getTimerUse() {
        return pjsua2JNI.AccountCallConfig_timerUse_get(this.swigCPtr, this);
    }

    public void setTimerMinSESec(long value) {
        pjsua2JNI.AccountCallConfig_timerMinSESec_set(this.swigCPtr, this, value);
    }

    public long getTimerMinSESec() {
        return pjsua2JNI.AccountCallConfig_timerMinSESec_get(this.swigCPtr, this);
    }

    public void setTimerSessExpiresSec(long value) {
        pjsua2JNI.AccountCallConfig_timerSessExpiresSec_set(this.swigCPtr, this, value);
    }

    public long getTimerSessExpiresSec() {
        return pjsua2JNI.AccountCallConfig_timerSessExpiresSec_get(this.swigCPtr, this);
    }

    public AccountCallConfig() {
        this(pjsua2JNI.new_AccountCallConfig(), true);
    }

    @Override
    public void readObject(ContainerNode node) throws Exception {
        pjsua2JNI.AccountCallConfig_readObject(this.swigCPtr, this, ContainerNode.getCPtr(node), node);
    }

    @Override
    public void writeObject(ContainerNode node) throws Exception {
        pjsua2JNI.AccountCallConfig_writeObject(this.swigCPtr, this, ContainerNode.getCPtr(node), node);
    }
}

