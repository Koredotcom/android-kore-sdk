/*
 * Decompiled with CFR 0.152.
 */
package org.pjsip.pjsua2;

import org.pjsip.pjsua2.ContainerNode;
import org.pjsip.pjsua2.PersistentObject;
import org.pjsip.pjsua2.SipHeaderVector;
import org.pjsip.pjsua2.pjsua2JNI;

public class AccountPresConfig
extends PersistentObject {
    private transient long swigCPtr;

    protected AccountPresConfig(long cPtr, boolean cMemoryOwn) {
        super(pjsua2JNI.AccountPresConfig_SWIGUpcast(cPtr), cMemoryOwn);
        this.swigCPtr = cPtr;
    }

    protected static long getCPtr(AccountPresConfig obj) {
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
                pjsua2JNI.delete_AccountPresConfig(this.swigCPtr);
            }
            this.swigCPtr = 0L;
        }
        super.delete();
    }

    public void setHeaders(SipHeaderVector value) {
        pjsua2JNI.AccountPresConfig_headers_set(this.swigCPtr, this, SipHeaderVector.getCPtr(value), value);
    }

    public SipHeaderVector getHeaders() {
        long cPtr = pjsua2JNI.AccountPresConfig_headers_get(this.swigCPtr, this);
        return cPtr == 0L ? null : new SipHeaderVector(cPtr, false);
    }

    public void setPublishEnabled(boolean value) {
        pjsua2JNI.AccountPresConfig_publishEnabled_set(this.swigCPtr, this, value);
    }

    public boolean getPublishEnabled() {
        return pjsua2JNI.AccountPresConfig_publishEnabled_get(this.swigCPtr, this);
    }

    public void setPublishQueue(boolean value) {
        pjsua2JNI.AccountPresConfig_publishQueue_set(this.swigCPtr, this, value);
    }

    public boolean getPublishQueue() {
        return pjsua2JNI.AccountPresConfig_publishQueue_get(this.swigCPtr, this);
    }

    public void setPublishShutdownWaitMsec(long value) {
        pjsua2JNI.AccountPresConfig_publishShutdownWaitMsec_set(this.swigCPtr, this, value);
    }

    public long getPublishShutdownWaitMsec() {
        return pjsua2JNI.AccountPresConfig_publishShutdownWaitMsec_get(this.swigCPtr, this);
    }

    public void setPidfTupleId(String value) {
        pjsua2JNI.AccountPresConfig_pidfTupleId_set(this.swigCPtr, this, value);
    }

    public String getPidfTupleId() {
        return pjsua2JNI.AccountPresConfig_pidfTupleId_get(this.swigCPtr, this);
    }

    @Override
    public void readObject(ContainerNode node) throws Exception {
        pjsua2JNI.AccountPresConfig_readObject(this.swigCPtr, this, ContainerNode.getCPtr(node), node);
    }

    @Override
    public void writeObject(ContainerNode node) throws Exception {
        pjsua2JNI.AccountPresConfig_writeObject(this.swigCPtr, this, ContainerNode.getCPtr(node), node);
    }

    public AccountPresConfig() {
        this(pjsua2JNI.new_AccountPresConfig(), true);
    }
}

