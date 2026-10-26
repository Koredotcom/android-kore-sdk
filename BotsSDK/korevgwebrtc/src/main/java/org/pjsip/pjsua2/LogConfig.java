/*
 * Decompiled with CFR 0.152.
 */
package org.pjsip.pjsua2;

import org.pjsip.pjsua2.ContainerNode;
import org.pjsip.pjsua2.LogWriter;
import org.pjsip.pjsua2.PersistentObject;
import org.pjsip.pjsua2.pjsua2JNI;

public class LogConfig
extends PersistentObject {
    private transient long swigCPtr;

    protected LogConfig(long cPtr, boolean cMemoryOwn) {
        super(pjsua2JNI.LogConfig_SWIGUpcast(cPtr), cMemoryOwn);
        this.swigCPtr = cPtr;
    }

    protected static long getCPtr(LogConfig obj) {
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
                pjsua2JNI.delete_LogConfig(this.swigCPtr);
            }
            this.swigCPtr = 0L;
        }
        super.delete();
    }

    public void setMsgLogging(long value) {
        pjsua2JNI.LogConfig_msgLogging_set(this.swigCPtr, this, value);
    }

    public long getMsgLogging() {
        return pjsua2JNI.LogConfig_msgLogging_get(this.swigCPtr, this);
    }

    public void setLevel(long value) {
        pjsua2JNI.LogConfig_level_set(this.swigCPtr, this, value);
    }

    public long getLevel() {
        return pjsua2JNI.LogConfig_level_get(this.swigCPtr, this);
    }

    public void setConsoleLevel(long value) {
        pjsua2JNI.LogConfig_consoleLevel_set(this.swigCPtr, this, value);
    }

    public long getConsoleLevel() {
        return pjsua2JNI.LogConfig_consoleLevel_get(this.swigCPtr, this);
    }

    public void setDecor(long value) {
        pjsua2JNI.LogConfig_decor_set(this.swigCPtr, this, value);
    }

    public long getDecor() {
        return pjsua2JNI.LogConfig_decor_get(this.swigCPtr, this);
    }

    public void setFilename(String value) {
        pjsua2JNI.LogConfig_filename_set(this.swigCPtr, this, value);
    }

    public String getFilename() {
        return pjsua2JNI.LogConfig_filename_get(this.swigCPtr, this);
    }

    public void setFileFlags(long value) {
        pjsua2JNI.LogConfig_fileFlags_set(this.swigCPtr, this, value);
    }

    public long getFileFlags() {
        return pjsua2JNI.LogConfig_fileFlags_get(this.swigCPtr, this);
    }

    public void setWriter(LogWriter value) {
        pjsua2JNI.LogConfig_writer_set(this.swigCPtr, this, LogWriter.getCPtr(value), value);
    }

    public LogWriter getWriter() {
        long cPtr = pjsua2JNI.LogConfig_writer_get(this.swigCPtr, this);
        return cPtr == 0L ? null : new LogWriter(cPtr, false);
    }

    public LogConfig() {
        this(pjsua2JNI.new_LogConfig(), true);
    }

    @Override
    public void readObject(ContainerNode node) throws Exception {
        pjsua2JNI.LogConfig_readObject(this.swigCPtr, this, ContainerNode.getCPtr(node), node);
    }

    @Override
    public void writeObject(ContainerNode node) throws Exception {
        pjsua2JNI.LogConfig_writeObject(this.swigCPtr, this, ContainerNode.getCPtr(node), node);
    }
}

