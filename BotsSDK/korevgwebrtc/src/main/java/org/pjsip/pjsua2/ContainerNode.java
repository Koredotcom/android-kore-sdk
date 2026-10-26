/*
 * Decompiled with CFR 0.152.
 */
package org.pjsip.pjsua2;

import org.pjsip.pjsua2.PersistentObject;
import org.pjsip.pjsua2.StringVector;
import org.pjsip.pjsua2.pjsua2JNI;

public class ContainerNode {
    private transient long swigCPtr;
    protected transient boolean swigCMemOwn;

    protected ContainerNode(long cPtr, boolean cMemoryOwn) {
        this.swigCMemOwn = cMemoryOwn;
        this.swigCPtr = cPtr;
    }

    protected static long getCPtr(ContainerNode obj) {
        return obj == null ? 0L : obj.swigCPtr;
    }

    protected void finalize() {
        this.delete();
    }

    public synchronized void delete() {
        if (this.swigCPtr != 0L) {
            if (this.swigCMemOwn) {
                this.swigCMemOwn = false;
                pjsua2JNI.delete_ContainerNode(this.swigCPtr);
            }
            this.swigCPtr = 0L;
        }
    }

    public boolean hasUnread() {
        return pjsua2JNI.ContainerNode_hasUnread(this.swigCPtr, this);
    }

    public String unreadName() throws Exception {
        return pjsua2JNI.ContainerNode_unreadName(this.swigCPtr, this);
    }

    public int readInt(String name) throws Exception {
        return pjsua2JNI.ContainerNode_readInt__SWIG_0(this.swigCPtr, this, name);
    }

    public int readInt() throws Exception {
        return pjsua2JNI.ContainerNode_readInt__SWIG_1(this.swigCPtr, this);
    }

    public float readNumber(String name) throws Exception {
        return pjsua2JNI.ContainerNode_readNumber__SWIG_0(this.swigCPtr, this, name);
    }

    public float readNumber() throws Exception {
        return pjsua2JNI.ContainerNode_readNumber__SWIG_1(this.swigCPtr, this);
    }

    public boolean readBool(String name) throws Exception {
        return pjsua2JNI.ContainerNode_readBool__SWIG_0(this.swigCPtr, this, name);
    }

    public boolean readBool() throws Exception {
        return pjsua2JNI.ContainerNode_readBool__SWIG_1(this.swigCPtr, this);
    }

    public String readString(String name) throws Exception {
        return pjsua2JNI.ContainerNode_readString__SWIG_0(this.swigCPtr, this, name);
    }

    public String readString() throws Exception {
        return pjsua2JNI.ContainerNode_readString__SWIG_1(this.swigCPtr, this);
    }

    public StringVector readStringVector(String name) throws Exception {
        return new StringVector(pjsua2JNI.ContainerNode_readStringVector__SWIG_0(this.swigCPtr, this, name), true);
    }

    public StringVector readStringVector() throws Exception {
        return new StringVector(pjsua2JNI.ContainerNode_readStringVector__SWIG_1(this.swigCPtr, this), true);
    }

    public void readObject(PersistentObject obj) throws Exception {
        pjsua2JNI.ContainerNode_readObject(this.swigCPtr, this, PersistentObject.getCPtr(obj), obj);
    }

    public ContainerNode readContainer(String name) throws Exception {
        return new ContainerNode(pjsua2JNI.ContainerNode_readContainer__SWIG_0(this.swigCPtr, this, name), true);
    }

    public ContainerNode readContainer() throws Exception {
        return new ContainerNode(pjsua2JNI.ContainerNode_readContainer__SWIG_1(this.swigCPtr, this), true);
    }

    public ContainerNode readArray(String name) throws Exception {
        return new ContainerNode(pjsua2JNI.ContainerNode_readArray__SWIG_0(this.swigCPtr, this, name), true);
    }

    public ContainerNode readArray() throws Exception {
        return new ContainerNode(pjsua2JNI.ContainerNode_readArray__SWIG_1(this.swigCPtr, this), true);
    }

    public void writeNumber(String name, float num) throws Exception {
        pjsua2JNI.ContainerNode_writeNumber(this.swigCPtr, this, name, num);
    }

    public void writeInt(String name, int num) throws Exception {
        pjsua2JNI.ContainerNode_writeInt(this.swigCPtr, this, name, num);
    }

    public void writeBool(String name, boolean value) throws Exception {
        pjsua2JNI.ContainerNode_writeBool(this.swigCPtr, this, name, value);
    }

    public void writeString(String name, String value) throws Exception {
        pjsua2JNI.ContainerNode_writeString(this.swigCPtr, this, name, value);
    }

    public void writeStringVector(String name, StringVector arr) throws Exception {
        pjsua2JNI.ContainerNode_writeStringVector(this.swigCPtr, this, name, StringVector.getCPtr(arr), arr);
    }

    public void writeObject(PersistentObject obj) throws Exception {
        pjsua2JNI.ContainerNode_writeObject(this.swigCPtr, this, PersistentObject.getCPtr(obj), obj);
    }

    public ContainerNode writeNewContainer(String name) throws Exception {
        return new ContainerNode(pjsua2JNI.ContainerNode_writeNewContainer(this.swigCPtr, this, name), true);
    }

    public ContainerNode writeNewArray(String name) throws Exception {
        return new ContainerNode(pjsua2JNI.ContainerNode_writeNewArray(this.swigCPtr, this, name), true);
    }

    public ContainerNode() {
        this(pjsua2JNI.new_ContainerNode(), true);
    }
}

