/*
 * Decompiled with CFR 0.152.
 */
package org.pjsip.pjsua2;

import org.pjsip.pjsua2.ContainerNode;
import org.pjsip.pjsua2.PersistentObject;
import org.pjsip.pjsua2.TlsConfig;
import org.pjsip.pjsua2.pj_qos_params;
import org.pjsip.pjsua2.pjsua2JNI;

public class TransportConfig
extends PersistentObject {
    private transient long swigCPtr;

    protected TransportConfig(long cPtr, boolean cMemoryOwn) {
        super(pjsua2JNI.TransportConfig_SWIGUpcast(cPtr), cMemoryOwn);
        this.swigCPtr = cPtr;
    }

    protected static long getCPtr(TransportConfig obj) {
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
                pjsua2JNI.delete_TransportConfig(this.swigCPtr);
            }
            this.swigCPtr = 0L;
        }
        super.delete();
    }

    public void setPort(long value) {
        pjsua2JNI.TransportConfig_port_set(this.swigCPtr, this, value);
    }

    public long getPort() {
        return pjsua2JNI.TransportConfig_port_get(this.swigCPtr, this);
    }

    public void setPortRange(long value) {
        pjsua2JNI.TransportConfig_portRange_set(this.swigCPtr, this, value);
    }

    public long getPortRange() {
        return pjsua2JNI.TransportConfig_portRange_get(this.swigCPtr, this);
    }

    public void setPublicAddress(String value) {
        pjsua2JNI.TransportConfig_publicAddress_set(this.swigCPtr, this, value);
    }

    public String getPublicAddress() {
        return pjsua2JNI.TransportConfig_publicAddress_get(this.swigCPtr, this);
    }

    public void setBoundAddress(String value) {
        pjsua2JNI.TransportConfig_boundAddress_set(this.swigCPtr, this, value);
    }

    public String getBoundAddress() {
        return pjsua2JNI.TransportConfig_boundAddress_get(this.swigCPtr, this);
    }

    public void setTlsConfig(TlsConfig value) {
        pjsua2JNI.TransportConfig_tlsConfig_set(this.swigCPtr, this, TlsConfig.getCPtr(value), value);
    }

    public TlsConfig getTlsConfig() {
        long cPtr = pjsua2JNI.TransportConfig_tlsConfig_get(this.swigCPtr, this);
        return cPtr == 0L ? null : new TlsConfig(cPtr, false);
    }

    public void setQosType(int value) {
        pjsua2JNI.TransportConfig_qosType_set(this.swigCPtr, this, value);
    }

    public int getQosType() {
        return pjsua2JNI.TransportConfig_qosType_get(this.swigCPtr, this);
    }

    public void setQosParams(pj_qos_params value) {
        pjsua2JNI.TransportConfig_qosParams_set(this.swigCPtr, this, pj_qos_params.getCPtr(value), value);
    }

    public pj_qos_params getQosParams() {
        long cPtr = pjsua2JNI.TransportConfig_qosParams_get(this.swigCPtr, this);
        return cPtr == 0L ? null : new pj_qos_params(cPtr, false);
    }

    public TransportConfig() {
        this(pjsua2JNI.new_TransportConfig(), true);
    }

    @Override
    public void readObject(ContainerNode node) throws Exception {
        pjsua2JNI.TransportConfig_readObject(this.swigCPtr, this, ContainerNode.getCPtr(node), node);
    }

    @Override
    public void writeObject(ContainerNode node) throws Exception {
        pjsua2JNI.TransportConfig_writeObject(this.swigCPtr, this, ContainerNode.getCPtr(node), node);
    }
}

