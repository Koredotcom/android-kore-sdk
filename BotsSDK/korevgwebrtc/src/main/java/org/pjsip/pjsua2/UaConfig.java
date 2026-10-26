/*
 * Decompiled with CFR 0.152.
 */
package org.pjsip.pjsua2;

import org.pjsip.pjsua2.ContainerNode;
import org.pjsip.pjsua2.PersistentObject;
import org.pjsip.pjsua2.StringVector;
import org.pjsip.pjsua2.pjsua2JNI;

public class UaConfig
extends PersistentObject {
    private transient long swigCPtr;

    protected UaConfig(long cPtr, boolean cMemoryOwn) {
        super(pjsua2JNI.UaConfig_SWIGUpcast(cPtr), cMemoryOwn);
        this.swigCPtr = cPtr;
    }

    protected static long getCPtr(UaConfig obj) {
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
                pjsua2JNI.delete_UaConfig(this.swigCPtr);
            }
            this.swigCPtr = 0L;
        }
        super.delete();
    }

    public void setMaxCalls(long value) {
        pjsua2JNI.UaConfig_maxCalls_set(this.swigCPtr, this, value);
    }

    public long getMaxCalls() {
        return pjsua2JNI.UaConfig_maxCalls_get(this.swigCPtr, this);
    }

    public void setThreadCnt(long value) {
        pjsua2JNI.UaConfig_threadCnt_set(this.swigCPtr, this, value);
    }

    public long getThreadCnt() {
        return pjsua2JNI.UaConfig_threadCnt_get(this.swigCPtr, this);
    }

    public void setMainThreadOnly(boolean value) {
        pjsua2JNI.UaConfig_mainThreadOnly_set(this.swigCPtr, this, value);
    }

    public boolean getMainThreadOnly() {
        return pjsua2JNI.UaConfig_mainThreadOnly_get(this.swigCPtr, this);
    }

    public void setNameserver(StringVector value) {
        pjsua2JNI.UaConfig_nameserver_set(this.swigCPtr, this, StringVector.getCPtr(value), value);
    }

    public StringVector getNameserver() {
        long cPtr = pjsua2JNI.UaConfig_nameserver_get(this.swigCPtr, this);
        return cPtr == 0L ? null : new StringVector(cPtr, false);
    }

    public void setOutboundProxies(StringVector value) {
        pjsua2JNI.UaConfig_outboundProxies_set(this.swigCPtr, this, StringVector.getCPtr(value), value);
    }

    public StringVector getOutboundProxies() {
        long cPtr = pjsua2JNI.UaConfig_outboundProxies_get(this.swigCPtr, this);
        return cPtr == 0L ? null : new StringVector(cPtr, false);
    }

    public void setUserAgent(String value) {
        pjsua2JNI.UaConfig_userAgent_set(this.swigCPtr, this, value);
    }

    public String getUserAgent() {
        return pjsua2JNI.UaConfig_userAgent_get(this.swigCPtr, this);
    }

    public void setStunServer(StringVector value) {
        pjsua2JNI.UaConfig_stunServer_set(this.swigCPtr, this, StringVector.getCPtr(value), value);
    }

    public StringVector getStunServer() {
        long cPtr = pjsua2JNI.UaConfig_stunServer_get(this.swigCPtr, this);
        return cPtr == 0L ? null : new StringVector(cPtr, false);
    }

    public void setStunTryIpv6(boolean value) {
        pjsua2JNI.UaConfig_stunTryIpv6_set(this.swigCPtr, this, value);
    }

    public boolean getStunTryIpv6() {
        return pjsua2JNI.UaConfig_stunTryIpv6_get(this.swigCPtr, this);
    }

    public void setStunIgnoreFailure(boolean value) {
        pjsua2JNI.UaConfig_stunIgnoreFailure_set(this.swigCPtr, this, value);
    }

    public boolean getStunIgnoreFailure() {
        return pjsua2JNI.UaConfig_stunIgnoreFailure_get(this.swigCPtr, this);
    }

    public void setNatTypeInSdp(int value) {
        pjsua2JNI.UaConfig_natTypeInSdp_set(this.swigCPtr, this, value);
    }

    public int getNatTypeInSdp() {
        return pjsua2JNI.UaConfig_natTypeInSdp_get(this.swigCPtr, this);
    }

    public void setMwiUnsolicitedEnabled(boolean value) {
        pjsua2JNI.UaConfig_mwiUnsolicitedEnabled_set(this.swigCPtr, this, value);
    }

    public boolean getMwiUnsolicitedEnabled() {
        return pjsua2JNI.UaConfig_mwiUnsolicitedEnabled_get(this.swigCPtr, this);
    }

    public UaConfig() {
        this(pjsua2JNI.new_UaConfig(), true);
    }

    @Override
    public void readObject(ContainerNode node) throws Exception {
        pjsua2JNI.UaConfig_readObject(this.swigCPtr, this, ContainerNode.getCPtr(node), node);
    }

    @Override
    public void writeObject(ContainerNode node) throws Exception {
        pjsua2JNI.UaConfig_writeObject(this.swigCPtr, this, ContainerNode.getCPtr(node), node);
    }
}

