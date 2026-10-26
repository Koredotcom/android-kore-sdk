/*
 * Decompiled with CFR 0.152.
 */
package org.pjsip.pjsua2;

import org.pjsip.pjsua2.ContainerNode;
import org.pjsip.pjsua2.PersistentObject;
import org.pjsip.pjsua2.RtcpFbCapVector;
import org.pjsip.pjsua2.pjsua2JNI;

public class RtcpFbConfig
extends PersistentObject {
    private transient long swigCPtr;

    protected RtcpFbConfig(long cPtr, boolean cMemoryOwn) {
        super(pjsua2JNI.RtcpFbConfig_SWIGUpcast(cPtr), cMemoryOwn);
        this.swigCPtr = cPtr;
    }

    protected static long getCPtr(RtcpFbConfig obj) {
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
                pjsua2JNI.delete_RtcpFbConfig(this.swigCPtr);
            }
            this.swigCPtr = 0L;
        }
        super.delete();
    }

    public void setDontUseAvpf(boolean value) {
        pjsua2JNI.RtcpFbConfig_dontUseAvpf_set(this.swigCPtr, this, value);
    }

    public boolean getDontUseAvpf() {
        return pjsua2JNI.RtcpFbConfig_dontUseAvpf_get(this.swigCPtr, this);
    }

    public void setCaps(RtcpFbCapVector value) {
        pjsua2JNI.RtcpFbConfig_caps_set(this.swigCPtr, this, RtcpFbCapVector.getCPtr(value), value);
    }

    public RtcpFbCapVector getCaps() {
        long cPtr = pjsua2JNI.RtcpFbConfig_caps_get(this.swigCPtr, this);
        return cPtr == 0L ? null : new RtcpFbCapVector(cPtr, false);
    }

    public RtcpFbConfig() {
        this(pjsua2JNI.new_RtcpFbConfig(), true);
    }

    @Override
    public void readObject(ContainerNode node) throws Exception {
        pjsua2JNI.RtcpFbConfig_readObject(this.swigCPtr, this, ContainerNode.getCPtr(node), node);
    }

    @Override
    public void writeObject(ContainerNode node) throws Exception {
        pjsua2JNI.RtcpFbConfig_writeObject(this.swigCPtr, this, ContainerNode.getCPtr(node), node);
    }
}

