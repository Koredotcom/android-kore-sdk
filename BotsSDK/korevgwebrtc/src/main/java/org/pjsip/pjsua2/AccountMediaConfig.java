/*
 * Decompiled with CFR 0.152.
 */
package org.pjsip.pjsua2;

import org.pjsip.pjsua2.ContainerNode;
import org.pjsip.pjsua2.PersistentObject;
import org.pjsip.pjsua2.RtcpFbConfig;
import org.pjsip.pjsua2.SrtpOpt;
import org.pjsip.pjsua2.TransportConfig;
import org.pjsip.pjsua2.pjsua2JNI;

public class AccountMediaConfig
extends PersistentObject {
    private transient long swigCPtr;

    protected AccountMediaConfig(long cPtr, boolean cMemoryOwn) {
        super(pjsua2JNI.AccountMediaConfig_SWIGUpcast(cPtr), cMemoryOwn);
        this.swigCPtr = cPtr;
    }

    protected static long getCPtr(AccountMediaConfig obj) {
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
                pjsua2JNI.delete_AccountMediaConfig(this.swigCPtr);
            }
            this.swigCPtr = 0L;
        }
        super.delete();
    }

    public void setTransportConfig(TransportConfig value) {
        pjsua2JNI.AccountMediaConfig_transportConfig_set(this.swigCPtr, this, TransportConfig.getCPtr(value), value);
    }

    public TransportConfig getTransportConfig() {
        long cPtr = pjsua2JNI.AccountMediaConfig_transportConfig_get(this.swigCPtr, this);
        return cPtr == 0L ? null : new TransportConfig(cPtr, false);
    }

    public void setLockCodecEnabled(boolean value) {
        pjsua2JNI.AccountMediaConfig_lockCodecEnabled_set(this.swigCPtr, this, value);
    }

    public boolean getLockCodecEnabled() {
        return pjsua2JNI.AccountMediaConfig_lockCodecEnabled_get(this.swigCPtr, this);
    }

    public void setStreamKaEnabled(boolean value) {
        pjsua2JNI.AccountMediaConfig_streamKaEnabled_set(this.swigCPtr, this, value);
    }

    public boolean getStreamKaEnabled() {
        return pjsua2JNI.AccountMediaConfig_streamKaEnabled_get(this.swigCPtr, this);
    }

    public void setSrtpUse(int value) {
        pjsua2JNI.AccountMediaConfig_srtpUse_set(this.swigCPtr, this, value);
    }

    public int getSrtpUse() {
        return pjsua2JNI.AccountMediaConfig_srtpUse_get(this.swigCPtr, this);
    }

    public void setSrtpSecureSignaling(int value) {
        pjsua2JNI.AccountMediaConfig_srtpSecureSignaling_set(this.swigCPtr, this, value);
    }

    public int getSrtpSecureSignaling() {
        return pjsua2JNI.AccountMediaConfig_srtpSecureSignaling_get(this.swigCPtr, this);
    }

    public void setSrtpOpt(SrtpOpt value) {
        pjsua2JNI.AccountMediaConfig_srtpOpt_set(this.swigCPtr, this, SrtpOpt.getCPtr(value), value);
    }

    public SrtpOpt getSrtpOpt() {
        long cPtr = pjsua2JNI.AccountMediaConfig_srtpOpt_get(this.swigCPtr, this);
        return cPtr == 0L ? null : new SrtpOpt(cPtr, false);
    }

    public void setIpv6Use(int value) {
        pjsua2JNI.AccountMediaConfig_ipv6Use_set(this.swigCPtr, this, value);
    }

    public int getIpv6Use() {
        return pjsua2JNI.AccountMediaConfig_ipv6Use_get(this.swigCPtr, this);
    }

    public void setRtcpMuxEnabled(boolean value) {
        pjsua2JNI.AccountMediaConfig_rtcpMuxEnabled_set(this.swigCPtr, this, value);
    }

    public boolean getRtcpMuxEnabled() {
        return pjsua2JNI.AccountMediaConfig_rtcpMuxEnabled_get(this.swigCPtr, this);
    }

    public void setRtcpFbConfig(RtcpFbConfig value) {
        pjsua2JNI.AccountMediaConfig_rtcpFbConfig_set(this.swigCPtr, this, RtcpFbConfig.getCPtr(value), value);
    }

    public RtcpFbConfig getRtcpFbConfig() {
        long cPtr = pjsua2JNI.AccountMediaConfig_rtcpFbConfig_get(this.swigCPtr, this);
        return cPtr == 0L ? null : new RtcpFbConfig(cPtr, false);
    }

    public AccountMediaConfig() {
        this(pjsua2JNI.new_AccountMediaConfig(), true);
    }

    @Override
    public void readObject(ContainerNode node) throws Exception {
        pjsua2JNI.AccountMediaConfig_readObject(this.swigCPtr, this, ContainerNode.getCPtr(node), node);
    }

    @Override
    public void writeObject(ContainerNode node) throws Exception {
        pjsua2JNI.AccountMediaConfig_writeObject(this.swigCPtr, this, ContainerNode.getCPtr(node), node);
    }
}

