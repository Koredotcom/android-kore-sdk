/*
 * Decompiled with CFR 0.152.
 */
package org.pjsip.pjsua2;

import org.pjsip.pjsua2.ContainerNode;
import org.pjsip.pjsua2.PersistentObject;
import org.pjsip.pjsua2.pjsua2JNI;

public class AccountVideoConfig
extends PersistentObject {
    private transient long swigCPtr;

    protected AccountVideoConfig(long cPtr, boolean cMemoryOwn) {
        super(pjsua2JNI.AccountVideoConfig_SWIGUpcast(cPtr), cMemoryOwn);
        this.swigCPtr = cPtr;
    }

    protected static long getCPtr(AccountVideoConfig obj) {
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
                pjsua2JNI.delete_AccountVideoConfig(this.swigCPtr);
            }
            this.swigCPtr = 0L;
        }
        super.delete();
    }

    public void setAutoShowIncoming(boolean value) {
        pjsua2JNI.AccountVideoConfig_autoShowIncoming_set(this.swigCPtr, this, value);
    }

    public boolean getAutoShowIncoming() {
        return pjsua2JNI.AccountVideoConfig_autoShowIncoming_get(this.swigCPtr, this);
    }

    public void setAutoTransmitOutgoing(boolean value) {
        pjsua2JNI.AccountVideoConfig_autoTransmitOutgoing_set(this.swigCPtr, this, value);
    }

    public boolean getAutoTransmitOutgoing() {
        return pjsua2JNI.AccountVideoConfig_autoTransmitOutgoing_get(this.swigCPtr, this);
    }

    public void setWindowFlags(long value) {
        pjsua2JNI.AccountVideoConfig_windowFlags_set(this.swigCPtr, this, value);
    }

    public long getWindowFlags() {
        return pjsua2JNI.AccountVideoConfig_windowFlags_get(this.swigCPtr, this);
    }

    public void setDefaultCaptureDevice(int value) {
        pjsua2JNI.AccountVideoConfig_defaultCaptureDevice_set(this.swigCPtr, this, value);
    }

    public int getDefaultCaptureDevice() {
        return pjsua2JNI.AccountVideoConfig_defaultCaptureDevice_get(this.swigCPtr, this);
    }

    public void setDefaultRenderDevice(int value) {
        pjsua2JNI.AccountVideoConfig_defaultRenderDevice_set(this.swigCPtr, this, value);
    }

    public int getDefaultRenderDevice() {
        return pjsua2JNI.AccountVideoConfig_defaultRenderDevice_get(this.swigCPtr, this);
    }

    public void setRateControlMethod(int value) {
        pjsua2JNI.AccountVideoConfig_rateControlMethod_set(this.swigCPtr, this, value);
    }

    public int getRateControlMethod() {
        return pjsua2JNI.AccountVideoConfig_rateControlMethod_get(this.swigCPtr, this);
    }

    public void setRateControlBandwidth(long value) {
        pjsua2JNI.AccountVideoConfig_rateControlBandwidth_set(this.swigCPtr, this, value);
    }

    public long getRateControlBandwidth() {
        return pjsua2JNI.AccountVideoConfig_rateControlBandwidth_get(this.swigCPtr, this);
    }

    public void setStartKeyframeCount(long value) {
        pjsua2JNI.AccountVideoConfig_startKeyframeCount_set(this.swigCPtr, this, value);
    }

    public long getStartKeyframeCount() {
        return pjsua2JNI.AccountVideoConfig_startKeyframeCount_get(this.swigCPtr, this);
    }

    public void setStartKeyframeInterval(long value) {
        pjsua2JNI.AccountVideoConfig_startKeyframeInterval_set(this.swigCPtr, this, value);
    }

    public long getStartKeyframeInterval() {
        return pjsua2JNI.AccountVideoConfig_startKeyframeInterval_get(this.swigCPtr, this);
    }

    public AccountVideoConfig() {
        this(pjsua2JNI.new_AccountVideoConfig(), true);
    }

    @Override
    public void readObject(ContainerNode node) throws Exception {
        pjsua2JNI.AccountVideoConfig_readObject(this.swigCPtr, this, ContainerNode.getCPtr(node), node);
    }

    @Override
    public void writeObject(ContainerNode node) throws Exception {
        pjsua2JNI.AccountVideoConfig_writeObject(this.swigCPtr, this, ContainerNode.getCPtr(node), node);
    }
}

