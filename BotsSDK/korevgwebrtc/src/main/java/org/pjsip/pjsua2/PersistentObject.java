/*
 * Decompiled with CFR 0.152.
 */
package org.pjsip.pjsua2;

import org.pjsip.pjsua2.ContainerNode;
import org.pjsip.pjsua2.pjsua2JNI;

public class PersistentObject {
    private transient long swigCPtr;
    protected transient boolean swigCMemOwn;

    protected PersistentObject(long cPtr, boolean cMemoryOwn) {
        this.swigCMemOwn = cMemoryOwn;
        this.swigCPtr = cPtr;
    }

    protected static long getCPtr(PersistentObject obj) {
        return obj == null ? 0L : obj.swigCPtr;
    }

    protected void finalize() {
        this.delete();
    }

    public synchronized void delete() {
        if (this.swigCPtr != 0L) {
            if (this.swigCMemOwn) {
                this.swigCMemOwn = false;
                pjsua2JNI.delete_PersistentObject(this.swigCPtr);
            }
            this.swigCPtr = 0L;
        }
    }

    public void readObject(ContainerNode node) throws Exception {
        pjsua2JNI.PersistentObject_readObject(this.swigCPtr, this, ContainerNode.getCPtr(node), node);
    }

    public void writeObject(ContainerNode node) throws Exception {
        pjsua2JNI.PersistentObject_writeObject(this.swigCPtr, this, ContainerNode.getCPtr(node), node);
    }
}

