/*
 * Decompiled with CFR 0.152.
 */
package org.pjsip.pjsua2;

import org.pjsip.pjsua2.ContainerNode;
import org.pjsip.pjsua2.PersistentObject;
import org.pjsip.pjsua2.StringVector;
import org.pjsip.pjsua2.pjsua2JNI;

public class PersistentDocument {
    private transient long swigCPtr;
    protected transient boolean swigCMemOwn;

    protected PersistentDocument(long cPtr, boolean cMemoryOwn) {
        this.swigCMemOwn = cMemoryOwn;
        this.swigCPtr = cPtr;
    }

    protected static long getCPtr(PersistentDocument obj) {
        return obj == null ? 0L : obj.swigCPtr;
    }

    protected void finalize() {
        this.delete();
    }

    public synchronized void delete() {
        if (this.swigCPtr != 0L) {
            if (this.swigCMemOwn) {
                this.swigCMemOwn = false;
                pjsua2JNI.delete_PersistentDocument(this.swigCPtr);
            }
            this.swigCPtr = 0L;
        }
    }

    public void loadFile(String filename) throws Exception {
        pjsua2JNI.PersistentDocument_loadFile(this.swigCPtr, this, filename);
    }

    public void loadString(String input) throws Exception {
        pjsua2JNI.PersistentDocument_loadString(this.swigCPtr, this, input);
    }

    public void saveFile(String filename) throws Exception {
        pjsua2JNI.PersistentDocument_saveFile(this.swigCPtr, this, filename);
    }

    public String saveString() throws Exception {
        return pjsua2JNI.PersistentDocument_saveString(this.swigCPtr, this);
    }

    public ContainerNode getRootContainer() {
        return new ContainerNode(pjsua2JNI.PersistentDocument_getRootContainer(this.swigCPtr, this), false);
    }

    public boolean hasUnread() {
        return pjsua2JNI.PersistentDocument_hasUnread(this.swigCPtr, this);
    }

    public String unreadName() throws Exception {
        return pjsua2JNI.PersistentDocument_unreadName(this.swigCPtr, this);
    }

    public int readInt(String name) throws Exception {
        return pjsua2JNI.PersistentDocument_readInt__SWIG_0(this.swigCPtr, this, name);
    }

    public int readInt() throws Exception {
        return pjsua2JNI.PersistentDocument_readInt__SWIG_1(this.swigCPtr, this);
    }

    public float readNumber(String name) throws Exception {
        return pjsua2JNI.PersistentDocument_readNumber__SWIG_0(this.swigCPtr, this, name);
    }

    public float readNumber() throws Exception {
        return pjsua2JNI.PersistentDocument_readNumber__SWIG_1(this.swigCPtr, this);
    }

    public boolean readBool(String name) throws Exception {
        return pjsua2JNI.PersistentDocument_readBool__SWIG_0(this.swigCPtr, this, name);
    }

    public boolean readBool() throws Exception {
        return pjsua2JNI.PersistentDocument_readBool__SWIG_1(this.swigCPtr, this);
    }

    public String readString(String name) throws Exception {
        return pjsua2JNI.PersistentDocument_readString__SWIG_0(this.swigCPtr, this, name);
    }

    public String readString() throws Exception {
        return pjsua2JNI.PersistentDocument_readString__SWIG_1(this.swigCPtr, this);
    }

    public StringVector readStringVector(String name) throws Exception {
        return new StringVector(pjsua2JNI.PersistentDocument_readStringVector__SWIG_0(this.swigCPtr, this, name), true);
    }

    public StringVector readStringVector() throws Exception {
        return new StringVector(pjsua2JNI.PersistentDocument_readStringVector__SWIG_1(this.swigCPtr, this), true);
    }

    public void readObject(PersistentObject obj) throws Exception {
        pjsua2JNI.PersistentDocument_readObject(this.swigCPtr, this, PersistentObject.getCPtr(obj), obj);
    }

    public ContainerNode readContainer(String name) throws Exception {
        return new ContainerNode(pjsua2JNI.PersistentDocument_readContainer__SWIG_0(this.swigCPtr, this, name), true);
    }

    public ContainerNode readContainer() throws Exception {
        return new ContainerNode(pjsua2JNI.PersistentDocument_readContainer__SWIG_1(this.swigCPtr, this), true);
    }

    public ContainerNode readArray(String name) throws Exception {
        return new ContainerNode(pjsua2JNI.PersistentDocument_readArray__SWIG_0(this.swigCPtr, this, name), true);
    }

    public ContainerNode readArray() throws Exception {
        return new ContainerNode(pjsua2JNI.PersistentDocument_readArray__SWIG_1(this.swigCPtr, this), true);
    }

    public void writeNumber(String name, float num) throws Exception {
        pjsua2JNI.PersistentDocument_writeNumber(this.swigCPtr, this, name, num);
    }

    public void writeInt(String name, int num) throws Exception {
        pjsua2JNI.PersistentDocument_writeInt(this.swigCPtr, this, name, num);
    }

    public void writeBool(String name, boolean value) throws Exception {
        pjsua2JNI.PersistentDocument_writeBool(this.swigCPtr, this, name, value);
    }

    public void writeString(String name, String value) throws Exception {
        pjsua2JNI.PersistentDocument_writeString(this.swigCPtr, this, name, value);
    }

    public void writeStringVector(String name, StringVector arr) throws Exception {
        pjsua2JNI.PersistentDocument_writeStringVector(this.swigCPtr, this, name, StringVector.getCPtr(arr), arr);
    }

    public void writeObject(PersistentObject obj) throws Exception {
        pjsua2JNI.PersistentDocument_writeObject(this.swigCPtr, this, PersistentObject.getCPtr(obj), obj);
    }

    public ContainerNode writeNewContainer(String name) throws Exception {
        return new ContainerNode(pjsua2JNI.PersistentDocument_writeNewContainer(this.swigCPtr, this, name), true);
    }

    public ContainerNode writeNewArray(String name) throws Exception {
        return new ContainerNode(pjsua2JNI.PersistentDocument_writeNewArray(this.swigCPtr, this, name), true);
    }
}

