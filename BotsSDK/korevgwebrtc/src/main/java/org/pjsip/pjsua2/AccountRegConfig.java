/*
 * Decompiled with CFR 0.152.
 */
package org.pjsip.pjsua2;

import org.pjsip.pjsua2.ContainerNode;
import org.pjsip.pjsua2.PersistentObject;
import org.pjsip.pjsua2.SipHeaderVector;
import org.pjsip.pjsua2.pjsua2JNI;

public class AccountRegConfig
extends PersistentObject {
    private transient long swigCPtr;

    protected AccountRegConfig(long cPtr, boolean cMemoryOwn) {
        super(pjsua2JNI.AccountRegConfig_SWIGUpcast(cPtr), cMemoryOwn);
        this.swigCPtr = cPtr;
    }

    protected static long getCPtr(AccountRegConfig obj) {
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
                pjsua2JNI.delete_AccountRegConfig(this.swigCPtr);
            }
            this.swigCPtr = 0L;
        }
        super.delete();
    }

    public void setRegistrarUri(String value) {
        pjsua2JNI.AccountRegConfig_registrarUri_set(this.swigCPtr, this, value);
    }

    public String getRegistrarUri() {
        return pjsua2JNI.AccountRegConfig_registrarUri_get(this.swigCPtr, this);
    }

    public void setRegisterOnAdd(boolean value) {
        pjsua2JNI.AccountRegConfig_registerOnAdd_set(this.swigCPtr, this, value);
    }

    public boolean getRegisterOnAdd() {
        return pjsua2JNI.AccountRegConfig_registerOnAdd_get(this.swigCPtr, this);
    }

    public void setHeaders(SipHeaderVector value) {
        pjsua2JNI.AccountRegConfig_headers_set(this.swigCPtr, this, SipHeaderVector.getCPtr(value), value);
    }

    public SipHeaderVector getHeaders() {
        long cPtr = pjsua2JNI.AccountRegConfig_headers_get(this.swigCPtr, this);
        return cPtr == 0L ? null : new SipHeaderVector(cPtr, false);
    }

    public void setContactParams(String value) {
        pjsua2JNI.AccountRegConfig_contactParams_set(this.swigCPtr, this, value);
    }

    public String getContactParams() {
        return pjsua2JNI.AccountRegConfig_contactParams_get(this.swigCPtr, this);
    }

    public void setTimeoutSec(long value) {
        pjsua2JNI.AccountRegConfig_timeoutSec_set(this.swigCPtr, this, value);
    }

    public long getTimeoutSec() {
        return pjsua2JNI.AccountRegConfig_timeoutSec_get(this.swigCPtr, this);
    }

    public void setRetryIntervalSec(long value) {
        pjsua2JNI.AccountRegConfig_retryIntervalSec_set(this.swigCPtr, this, value);
    }

    public long getRetryIntervalSec() {
        return pjsua2JNI.AccountRegConfig_retryIntervalSec_get(this.swigCPtr, this);
    }

    public void setFirstRetryIntervalSec(long value) {
        pjsua2JNI.AccountRegConfig_firstRetryIntervalSec_set(this.swigCPtr, this, value);
    }

    public long getFirstRetryIntervalSec() {
        return pjsua2JNI.AccountRegConfig_firstRetryIntervalSec_get(this.swigCPtr, this);
    }

    public void setRandomRetryIntervalSec(long value) {
        pjsua2JNI.AccountRegConfig_randomRetryIntervalSec_set(this.swigCPtr, this, value);
    }

    public long getRandomRetryIntervalSec() {
        return pjsua2JNI.AccountRegConfig_randomRetryIntervalSec_get(this.swigCPtr, this);
    }

    public void setDelayBeforeRefreshSec(long value) {
        pjsua2JNI.AccountRegConfig_delayBeforeRefreshSec_set(this.swigCPtr, this, value);
    }

    public long getDelayBeforeRefreshSec() {
        return pjsua2JNI.AccountRegConfig_delayBeforeRefreshSec_get(this.swigCPtr, this);
    }

    public void setDropCallsOnFail(boolean value) {
        pjsua2JNI.AccountRegConfig_dropCallsOnFail_set(this.swigCPtr, this, value);
    }

    public boolean getDropCallsOnFail() {
        return pjsua2JNI.AccountRegConfig_dropCallsOnFail_get(this.swigCPtr, this);
    }

    public void setUnregWaitMsec(long value) {
        pjsua2JNI.AccountRegConfig_unregWaitMsec_set(this.swigCPtr, this, value);
    }

    public long getUnregWaitMsec() {
        return pjsua2JNI.AccountRegConfig_unregWaitMsec_get(this.swigCPtr, this);
    }

    public void setProxyUse(long value) {
        pjsua2JNI.AccountRegConfig_proxyUse_set(this.swigCPtr, this, value);
    }

    public long getProxyUse() {
        return pjsua2JNI.AccountRegConfig_proxyUse_get(this.swigCPtr, this);
    }

    @Override
    public void readObject(ContainerNode node) throws Exception {
        pjsua2JNI.AccountRegConfig_readObject(this.swigCPtr, this, ContainerNode.getCPtr(node), node);
    }

    @Override
    public void writeObject(ContainerNode node) throws Exception {
        pjsua2JNI.AccountRegConfig_writeObject(this.swigCPtr, this, ContainerNode.getCPtr(node), node);
    }

    public AccountRegConfig() {
        this(pjsua2JNI.new_AccountRegConfig(), true);
    }
}

