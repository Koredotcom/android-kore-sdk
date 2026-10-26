/*
 * Decompiled with CFR 0.152.
 */
package org.pjsip.pjsua2;

import org.pjsip.pjsua2.ContainerNode;
import org.pjsip.pjsua2.IntVector;
import org.pjsip.pjsua2.PersistentObject;
import org.pjsip.pjsua2.SrtpCryptoVector;
import org.pjsip.pjsua2.pjsua2JNI;

public class SrtpOpt
extends PersistentObject {
    private transient long swigCPtr;

    protected SrtpOpt(long cPtr, boolean cMemoryOwn) {
        super(pjsua2JNI.SrtpOpt_SWIGUpcast(cPtr), cMemoryOwn);
        this.swigCPtr = cPtr;
    }

    protected static long getCPtr(SrtpOpt obj) {
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
                pjsua2JNI.delete_SrtpOpt(this.swigCPtr);
            }
            this.swigCPtr = 0L;
        }
        super.delete();
    }

    public void setCryptos(SrtpCryptoVector value) {
        pjsua2JNI.SrtpOpt_cryptos_set(this.swigCPtr, this, SrtpCryptoVector.getCPtr(value), value);
    }

    public SrtpCryptoVector getCryptos() {
        long cPtr = pjsua2JNI.SrtpOpt_cryptos_get(this.swigCPtr, this);
        return cPtr == 0L ? null : new SrtpCryptoVector(cPtr, false);
    }

    public void setKeyings(IntVector value) {
        pjsua2JNI.SrtpOpt_keyings_set(this.swigCPtr, this, IntVector.getCPtr(value), value);
    }

    public IntVector getKeyings() {
        long cPtr = pjsua2JNI.SrtpOpt_keyings_get(this.swigCPtr, this);
        return cPtr == 0L ? null : new IntVector(cPtr, false);
    }

    public SrtpOpt() {
        this(pjsua2JNI.new_SrtpOpt(), true);
    }

    @Override
    public void readObject(ContainerNode node) throws Exception {
        pjsua2JNI.SrtpOpt_readObject(this.swigCPtr, this, ContainerNode.getCPtr(node), node);
    }

    @Override
    public void writeObject(ContainerNode node) throws Exception {
        pjsua2JNI.SrtpOpt_writeObject(this.swigCPtr, this, ContainerNode.getCPtr(node), node);
    }
}

