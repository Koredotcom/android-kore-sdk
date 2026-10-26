/*
 * Decompiled with CFR 0.152.
 */
package org.pjsip.pjsua2;

import org.pjsip.pjsua2.ContainerNode;
import org.pjsip.pjsua2.LogConfig;
import org.pjsip.pjsua2.MediaConfig;
import org.pjsip.pjsua2.PersistentObject;
import org.pjsip.pjsua2.UaConfig;
import org.pjsip.pjsua2.pjsua2JNI;

public class EpConfig
extends PersistentObject {
    private transient long swigCPtr;

    protected EpConfig(long cPtr, boolean cMemoryOwn) {
        super(pjsua2JNI.EpConfig_SWIGUpcast(cPtr), cMemoryOwn);
        this.swigCPtr = cPtr;
    }

    protected static long getCPtr(EpConfig obj) {
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
                pjsua2JNI.delete_EpConfig(this.swigCPtr);
            }
            this.swigCPtr = 0L;
        }
        super.delete();
    }

    public void setUaConfig(UaConfig value) {
        pjsua2JNI.EpConfig_uaConfig_set(this.swigCPtr, this, UaConfig.getCPtr(value), value);
    }

    public UaConfig getUaConfig() {
        long cPtr = pjsua2JNI.EpConfig_uaConfig_get(this.swigCPtr, this);
        return cPtr == 0L ? null : new UaConfig(cPtr, false);
    }

    public void setLogConfig(LogConfig value) {
        pjsua2JNI.EpConfig_logConfig_set(this.swigCPtr, this, LogConfig.getCPtr(value), value);
    }

    public LogConfig getLogConfig() {
        long cPtr = pjsua2JNI.EpConfig_logConfig_get(this.swigCPtr, this);
        return cPtr == 0L ? null : new LogConfig(cPtr, false);
    }

    public void setMedConfig(MediaConfig value) {
        pjsua2JNI.EpConfig_medConfig_set(this.swigCPtr, this, MediaConfig.getCPtr(value), value);
    }

    public MediaConfig getMedConfig() {
        long cPtr = pjsua2JNI.EpConfig_medConfig_get(this.swigCPtr, this);
        return cPtr == 0L ? null : new MediaConfig(cPtr, false);
    }

    @Override
    public void readObject(ContainerNode node) throws Exception {
        pjsua2JNI.EpConfig_readObject(this.swigCPtr, this, ContainerNode.getCPtr(node), node);
    }

    @Override
    public void writeObject(ContainerNode node) throws Exception {
        pjsua2JNI.EpConfig_writeObject(this.swigCPtr, this, ContainerNode.getCPtr(node), node);
    }

    public EpConfig() {
        this(pjsua2JNI.new_EpConfig(), true);
    }
}

