/*
 * Decompiled with CFR 0.152.
 */
package org.pjsip.pjsua2;

import org.pjsip.pjsua2.ContainerNode;
import org.pjsip.pjsua2.IntVector;
import org.pjsip.pjsua2.PersistentObject;
import org.pjsip.pjsua2.pj_qos_params;
import org.pjsip.pjsua2.pjsua2JNI;

public class TlsConfig
extends PersistentObject {
    private transient long swigCPtr;

    protected TlsConfig(long cPtr, boolean cMemoryOwn) {
        super(pjsua2JNI.TlsConfig_SWIGUpcast(cPtr), cMemoryOwn);
        this.swigCPtr = cPtr;
    }

    protected static long getCPtr(TlsConfig obj) {
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
                pjsua2JNI.delete_TlsConfig(this.swigCPtr);
            }
            this.swigCPtr = 0L;
        }
        super.delete();
    }

    public void setCaListFile(String value) {
        pjsua2JNI.TlsConfig_CaListFile_set(this.swigCPtr, this, value);
    }

    public String getCaListFile() {
        return pjsua2JNI.TlsConfig_CaListFile_get(this.swigCPtr, this);
    }

    public void setCertFile(String value) {
        pjsua2JNI.TlsConfig_certFile_set(this.swigCPtr, this, value);
    }

    public String getCertFile() {
        return pjsua2JNI.TlsConfig_certFile_get(this.swigCPtr, this);
    }

    public void setPrivKeyFile(String value) {
        pjsua2JNI.TlsConfig_privKeyFile_set(this.swigCPtr, this, value);
    }

    public String getPrivKeyFile() {
        return pjsua2JNI.TlsConfig_privKeyFile_get(this.swigCPtr, this);
    }

    public void setPassword(String value) {
        pjsua2JNI.TlsConfig_password_set(this.swigCPtr, this, value);
    }

    public String getPassword() {
        return pjsua2JNI.TlsConfig_password_get(this.swigCPtr, this);
    }

    public void setCaBuf(String value) {
        pjsua2JNI.TlsConfig_CaBuf_set(this.swigCPtr, this, value);
    }

    public String getCaBuf() {
        return pjsua2JNI.TlsConfig_CaBuf_get(this.swigCPtr, this);
    }

    public void setCertBuf(String value) {
        pjsua2JNI.TlsConfig_certBuf_set(this.swigCPtr, this, value);
    }

    public String getCertBuf() {
        return pjsua2JNI.TlsConfig_certBuf_get(this.swigCPtr, this);
    }

    public void setPrivKeyBuf(String value) {
        pjsua2JNI.TlsConfig_privKeyBuf_set(this.swigCPtr, this, value);
    }

    public String getPrivKeyBuf() {
        return pjsua2JNI.TlsConfig_privKeyBuf_get(this.swigCPtr, this);
    }

    public void setMethod(int value) {
        pjsua2JNI.TlsConfig_method_set(this.swigCPtr, this, value);
    }

    public int getMethod() {
        return pjsua2JNI.TlsConfig_method_get(this.swigCPtr, this);
    }

    public void setProto(long value) {
        pjsua2JNI.TlsConfig_proto_set(this.swigCPtr, this, value);
    }

    public long getProto() {
        return pjsua2JNI.TlsConfig_proto_get(this.swigCPtr, this);
    }

    public void setCiphers(IntVector value) {
        pjsua2JNI.TlsConfig_ciphers_set(this.swigCPtr, this, IntVector.getCPtr(value), value);
    }

    public IntVector getCiphers() {
        long cPtr = pjsua2JNI.TlsConfig_ciphers_get(this.swigCPtr, this);
        return cPtr == 0L ? null : new IntVector(cPtr, false);
    }

    public void setVerifyServer(boolean value) {
        pjsua2JNI.TlsConfig_verifyServer_set(this.swigCPtr, this, value);
    }

    public boolean getVerifyServer() {
        return pjsua2JNI.TlsConfig_verifyServer_get(this.swigCPtr, this);
    }

    public void setVerifyClient(boolean value) {
        pjsua2JNI.TlsConfig_verifyClient_set(this.swigCPtr, this, value);
    }

    public boolean getVerifyClient() {
        return pjsua2JNI.TlsConfig_verifyClient_get(this.swigCPtr, this);
    }

    public void setRequireClientCert(boolean value) {
        pjsua2JNI.TlsConfig_requireClientCert_set(this.swigCPtr, this, value);
    }

    public boolean getRequireClientCert() {
        return pjsua2JNI.TlsConfig_requireClientCert_get(this.swigCPtr, this);
    }

    public void setMsecTimeout(long value) {
        pjsua2JNI.TlsConfig_msecTimeout_set(this.swigCPtr, this, value);
    }

    public long getMsecTimeout() {
        return pjsua2JNI.TlsConfig_msecTimeout_get(this.swigCPtr, this);
    }

    public void setQosType(int value) {
        pjsua2JNI.TlsConfig_qosType_set(this.swigCPtr, this, value);
    }

    public int getQosType() {
        return pjsua2JNI.TlsConfig_qosType_get(this.swigCPtr, this);
    }

    public void setQosParams(pj_qos_params value) {
        pjsua2JNI.TlsConfig_qosParams_set(this.swigCPtr, this, pj_qos_params.getCPtr(value), value);
    }

    public pj_qos_params getQosParams() {
        long cPtr = pjsua2JNI.TlsConfig_qosParams_get(this.swigCPtr, this);
        return cPtr == 0L ? null : new pj_qos_params(cPtr, false);
    }

    public void setQosIgnoreError(boolean value) {
        pjsua2JNI.TlsConfig_qosIgnoreError_set(this.swigCPtr, this, value);
    }

    public boolean getQosIgnoreError() {
        return pjsua2JNI.TlsConfig_qosIgnoreError_get(this.swigCPtr, this);
    }

    public TlsConfig() {
        this(pjsua2JNI.new_TlsConfig(), true);
    }

    @Override
    public void readObject(ContainerNode node) throws Exception {
        pjsua2JNI.TlsConfig_readObject(this.swigCPtr, this, ContainerNode.getCPtr(node), node);
    }

    @Override
    public void writeObject(ContainerNode node) throws Exception {
        pjsua2JNI.TlsConfig_writeObject(this.swigCPtr, this, ContainerNode.getCPtr(node), node);
    }
}

