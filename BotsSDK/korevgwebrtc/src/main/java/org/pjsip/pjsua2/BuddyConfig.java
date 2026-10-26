/*
 * Decompiled with CFR 0.152.
 */
package org.pjsip.pjsua2;

import org.pjsip.pjsua2.ContainerNode;
import org.pjsip.pjsua2.PersistentObject;
import org.pjsip.pjsua2.pjsua2JNI;

public class BuddyConfig
extends PersistentObject {
    private transient long swigCPtr;

    protected BuddyConfig(long cPtr, boolean cMemoryOwn) {
        super(pjsua2JNI.BuddyConfig_SWIGUpcast(cPtr), cMemoryOwn);
        this.swigCPtr = cPtr;
    }

    protected static long getCPtr(BuddyConfig obj) {
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
                pjsua2JNI.delete_BuddyConfig(this.swigCPtr);
            }
            this.swigCPtr = 0L;
        }
        super.delete();
    }

    public void setUri(String value) {
        pjsua2JNI.BuddyConfig_uri_set(this.swigCPtr, this, value);
    }

    public String getUri() {
        return pjsua2JNI.BuddyConfig_uri_get(this.swigCPtr, this);
    }

    public void setSubscribe(boolean value) {
        pjsua2JNI.BuddyConfig_subscribe_set(this.swigCPtr, this, value);
    }

    public boolean getSubscribe() {
        return pjsua2JNI.BuddyConfig_subscribe_get(this.swigCPtr, this);
    }

    @Override
    public void readObject(ContainerNode node) throws Exception {
        pjsua2JNI.BuddyConfig_readObject(this.swigCPtr, this, ContainerNode.getCPtr(node), node);
    }

    @Override
    public void writeObject(ContainerNode node) throws Exception {
        pjsua2JNI.BuddyConfig_writeObject(this.swigCPtr, this, ContainerNode.getCPtr(node), node);
    }

    public BuddyConfig() {
        this(pjsua2JNI.new_BuddyConfig(), true);
    }
}

