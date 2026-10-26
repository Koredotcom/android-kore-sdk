/*
 * Decompiled with CFR 0.152.
 */
package org.pjsip.pjsua2;

import org.pjsip.pjsua2.ContainerNode;
import org.pjsip.pjsua2.PersistentDocument;
import org.pjsip.pjsua2.pjsua2JNI;

public class JsonDocument
extends PersistentDocument {
    private transient long swigCPtr;

    protected JsonDocument(long cPtr, boolean cMemoryOwn) {
        super(pjsua2JNI.JsonDocument_SWIGUpcast(cPtr), cMemoryOwn);
        this.swigCPtr = cPtr;
    }

    protected static long getCPtr(JsonDocument obj) {
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
                pjsua2JNI.delete_JsonDocument(this.swigCPtr);
            }
            this.swigCPtr = 0L;
        }
        super.delete();
    }

    public JsonDocument() {
        this(pjsua2JNI.new_JsonDocument(), true);
    }

    @Override
    public void loadFile(String filename) throws Exception {
        pjsua2JNI.JsonDocument_loadFile(this.swigCPtr, this, filename);
    }

    @Override
    public void loadString(String input) throws Exception {
        pjsua2JNI.JsonDocument_loadString(this.swigCPtr, this, input);
    }

    @Override
    public void saveFile(String filename) throws Exception {
        pjsua2JNI.JsonDocument_saveFile(this.swigCPtr, this, filename);
    }

    @Override
    public String saveString() throws Exception {
        return pjsua2JNI.JsonDocument_saveString(this.swigCPtr, this);
    }

    @Override
    public ContainerNode getRootContainer() {
        return new ContainerNode(pjsua2JNI.JsonDocument_getRootContainer(this.swigCPtr, this), false);
    }
}

