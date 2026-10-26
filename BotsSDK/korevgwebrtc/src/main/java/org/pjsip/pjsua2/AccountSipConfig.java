/*
 * Decompiled with CFR 0.152.
 */
package org.pjsip.pjsua2;

import org.pjsip.pjsua2.AuthCredInfoVector;
import org.pjsip.pjsua2.ContainerNode;
import org.pjsip.pjsua2.PersistentObject;
import org.pjsip.pjsua2.StringVector;
import org.pjsip.pjsua2.pjsua2JNI;

public class AccountSipConfig
extends PersistentObject {
    private transient long swigCPtr;

    protected AccountSipConfig(long cPtr, boolean cMemoryOwn) {
        super(pjsua2JNI.AccountSipConfig_SWIGUpcast(cPtr), cMemoryOwn);
        this.swigCPtr = cPtr;
    }

    protected static long getCPtr(AccountSipConfig obj) {
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
                pjsua2JNI.delete_AccountSipConfig(this.swigCPtr);
            }
            this.swigCPtr = 0L;
        }
        super.delete();
    }

    public void setAuthCreds(AuthCredInfoVector value) {
        pjsua2JNI.AccountSipConfig_authCreds_set(this.swigCPtr, this, AuthCredInfoVector.getCPtr(value), value);
    }

    public AuthCredInfoVector getAuthCreds() {
        long cPtr = pjsua2JNI.AccountSipConfig_authCreds_get(this.swigCPtr, this);
        return cPtr == 0L ? null : new AuthCredInfoVector(cPtr, false);
    }

    public void setProxies(StringVector value) {
        pjsua2JNI.AccountSipConfig_proxies_set(this.swigCPtr, this, StringVector.getCPtr(value), value);
    }

    public StringVector getProxies() {
        long cPtr = pjsua2JNI.AccountSipConfig_proxies_get(this.swigCPtr, this);
        return cPtr == 0L ? null : new StringVector(cPtr, false);
    }

    public void setContactForced(String value) {
        pjsua2JNI.AccountSipConfig_contactForced_set(this.swigCPtr, this, value);
    }

    public String getContactForced() {
        return pjsua2JNI.AccountSipConfig_contactForced_get(this.swigCPtr, this);
    }

    public void setContactParams(String value) {
        pjsua2JNI.AccountSipConfig_contactParams_set(this.swigCPtr, this, value);
    }

    public String getContactParams() {
        return pjsua2JNI.AccountSipConfig_contactParams_get(this.swigCPtr, this);
    }

    public void setContactUriParams(String value) {
        pjsua2JNI.AccountSipConfig_contactUriParams_set(this.swigCPtr, this, value);
    }

    public String getContactUriParams() {
        return pjsua2JNI.AccountSipConfig_contactUriParams_get(this.swigCPtr, this);
    }

    public void setAuthInitialEmpty(boolean value) {
        pjsua2JNI.AccountSipConfig_authInitialEmpty_set(this.swigCPtr, this, value);
    }

    public boolean getAuthInitialEmpty() {
        return pjsua2JNI.AccountSipConfig_authInitialEmpty_get(this.swigCPtr, this);
    }

    public void setAuthInitialAlgorithm(String value) {
        pjsua2JNI.AccountSipConfig_authInitialAlgorithm_set(this.swigCPtr, this, value);
    }

    public String getAuthInitialAlgorithm() {
        return pjsua2JNI.AccountSipConfig_authInitialAlgorithm_get(this.swigCPtr, this);
    }

    public void setTransportId(int value) {
        pjsua2JNI.AccountSipConfig_transportId_set(this.swigCPtr, this, value);
    }

    public int getTransportId() {
        return pjsua2JNI.AccountSipConfig_transportId_get(this.swigCPtr, this);
    }

    @Override
    public void readObject(ContainerNode node) throws Exception {
        pjsua2JNI.AccountSipConfig_readObject(this.swigCPtr, this, ContainerNode.getCPtr(node), node);
    }

    @Override
    public void writeObject(ContainerNode node) throws Exception {
        pjsua2JNI.AccountSipConfig_writeObject(this.swigCPtr, this, ContainerNode.getCPtr(node), node);
    }

    public AccountSipConfig() {
        this(pjsua2JNI.new_AccountSipConfig(), true);
    }
}

