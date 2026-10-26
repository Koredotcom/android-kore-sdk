/*
 * Decompiled with CFR 0.152.
 */
package org.pjsip.pjsua2;

import org.pjsip.pjsua2.AccountCallConfig;
import org.pjsip.pjsua2.AccountIpChangeConfig;
import org.pjsip.pjsua2.AccountMediaConfig;
import org.pjsip.pjsua2.AccountMwiConfig;
import org.pjsip.pjsua2.AccountNatConfig;
import org.pjsip.pjsua2.AccountPresConfig;
import org.pjsip.pjsua2.AccountRegConfig;
import org.pjsip.pjsua2.AccountSipConfig;
import org.pjsip.pjsua2.AccountVideoConfig;
import org.pjsip.pjsua2.ContainerNode;
import org.pjsip.pjsua2.PersistentObject;
import org.pjsip.pjsua2.pjsua2JNI;

public class AccountConfig
extends PersistentObject {
    private transient long swigCPtr;

    protected AccountConfig(long cPtr, boolean cMemoryOwn) {
        super(pjsua2JNI.AccountConfig_SWIGUpcast(cPtr), cMemoryOwn);
        this.swigCPtr = cPtr;
    }

    protected static long getCPtr(AccountConfig obj) {
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
                pjsua2JNI.delete_AccountConfig(this.swigCPtr);
            }
            this.swigCPtr = 0L;
        }
        super.delete();
    }

    public void setPriority(int value) {
        pjsua2JNI.AccountConfig_priority_set(this.swigCPtr, this, value);
    }

    public int getPriority() {
        return pjsua2JNI.AccountConfig_priority_get(this.swigCPtr, this);
    }

    public void setIdUri(String value) {
        pjsua2JNI.AccountConfig_idUri_set(this.swigCPtr, this, value);
    }

    public String getIdUri() {
        return pjsua2JNI.AccountConfig_idUri_get(this.swigCPtr, this);
    }

    public void setRegConfig(AccountRegConfig value) {
        pjsua2JNI.AccountConfig_regConfig_set(this.swigCPtr, this, AccountRegConfig.getCPtr(value), value);
    }

    public AccountRegConfig getRegConfig() {
        long cPtr = pjsua2JNI.AccountConfig_regConfig_get(this.swigCPtr, this);
        return cPtr == 0L ? null : new AccountRegConfig(cPtr, false);
    }

    public void setSipConfig(AccountSipConfig value) {
        pjsua2JNI.AccountConfig_sipConfig_set(this.swigCPtr, this, AccountSipConfig.getCPtr(value), value);
    }

    public AccountSipConfig getSipConfig() {
        long cPtr = pjsua2JNI.AccountConfig_sipConfig_get(this.swigCPtr, this);
        return cPtr == 0L ? null : new AccountSipConfig(cPtr, false);
    }

    public void setCallConfig(AccountCallConfig value) {
        pjsua2JNI.AccountConfig_callConfig_set(this.swigCPtr, this, AccountCallConfig.getCPtr(value), value);
    }

    public AccountCallConfig getCallConfig() {
        long cPtr = pjsua2JNI.AccountConfig_callConfig_get(this.swigCPtr, this);
        return cPtr == 0L ? null : new AccountCallConfig(cPtr, false);
    }

    public void setPresConfig(AccountPresConfig value) {
        pjsua2JNI.AccountConfig_presConfig_set(this.swigCPtr, this, AccountPresConfig.getCPtr(value), value);
    }

    public AccountPresConfig getPresConfig() {
        long cPtr = pjsua2JNI.AccountConfig_presConfig_get(this.swigCPtr, this);
        return cPtr == 0L ? null : new AccountPresConfig(cPtr, false);
    }

    public void setMwiConfig(AccountMwiConfig value) {
        pjsua2JNI.AccountConfig_mwiConfig_set(this.swigCPtr, this, AccountMwiConfig.getCPtr(value), value);
    }

    public AccountMwiConfig getMwiConfig() {
        long cPtr = pjsua2JNI.AccountConfig_mwiConfig_get(this.swigCPtr, this);
        return cPtr == 0L ? null : new AccountMwiConfig(cPtr, false);
    }

    public void setNatConfig(AccountNatConfig value) {
        pjsua2JNI.AccountConfig_natConfig_set(this.swigCPtr, this, AccountNatConfig.getCPtr(value), value);
    }

    public AccountNatConfig getNatConfig() {
        long cPtr = pjsua2JNI.AccountConfig_natConfig_get(this.swigCPtr, this);
        return cPtr == 0L ? null : new AccountNatConfig(cPtr, false);
    }

    public void setMediaConfig(AccountMediaConfig value) {
        pjsua2JNI.AccountConfig_mediaConfig_set(this.swigCPtr, this, AccountMediaConfig.getCPtr(value), value);
    }

    public AccountMediaConfig getMediaConfig() {
        long cPtr = pjsua2JNI.AccountConfig_mediaConfig_get(this.swigCPtr, this);
        return cPtr == 0L ? null : new AccountMediaConfig(cPtr, false);
    }

    public void setVideoConfig(AccountVideoConfig value) {
        pjsua2JNI.AccountConfig_videoConfig_set(this.swigCPtr, this, AccountVideoConfig.getCPtr(value), value);
    }

    public AccountVideoConfig getVideoConfig() {
        long cPtr = pjsua2JNI.AccountConfig_videoConfig_get(this.swigCPtr, this);
        return cPtr == 0L ? null : new AccountVideoConfig(cPtr, false);
    }

    public void setIpChangeConfig(AccountIpChangeConfig value) {
        pjsua2JNI.AccountConfig_ipChangeConfig_set(this.swigCPtr, this, AccountIpChangeConfig.getCPtr(value), value);
    }

    public AccountIpChangeConfig getIpChangeConfig() {
        long cPtr = pjsua2JNI.AccountConfig_ipChangeConfig_get(this.swigCPtr, this);
        return cPtr == 0L ? null : new AccountIpChangeConfig(cPtr, false);
    }

    public AccountConfig() {
        this(pjsua2JNI.new_AccountConfig(), true);
    }

    @Override
    public void readObject(ContainerNode node) throws Exception {
        pjsua2JNI.AccountConfig_readObject(this.swigCPtr, this, ContainerNode.getCPtr(node), node);
    }

    @Override
    public void writeObject(ContainerNode node) throws Exception {
        pjsua2JNI.AccountConfig_writeObject(this.swigCPtr, this, ContainerNode.getCPtr(node), node);
    }
}

