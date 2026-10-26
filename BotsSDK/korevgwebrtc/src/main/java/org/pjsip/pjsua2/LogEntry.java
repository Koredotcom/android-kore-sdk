/*
 * Decompiled with CFR 0.152.
 */
package org.pjsip.pjsua2;

import org.pjsip.pjsua2.pjsua2JNI;

public class LogEntry {
    private transient long swigCPtr;
    protected transient boolean swigCMemOwn;

    protected LogEntry(long cPtr, boolean cMemoryOwn) {
        this.swigCMemOwn = cMemoryOwn;
        this.swigCPtr = cPtr;
    }

    protected static long getCPtr(LogEntry obj) {
        return obj == null ? 0L : obj.swigCPtr;
    }

    protected void finalize() {
        this.delete();
    }

    public synchronized void delete() {
        if (this.swigCPtr != 0L) {
            if (this.swigCMemOwn) {
                this.swigCMemOwn = false;
                pjsua2JNI.delete_LogEntry(this.swigCPtr);
            }
            this.swigCPtr = 0L;
        }
    }

    public void setLevel(int value) {
        pjsua2JNI.LogEntry_level_set(this.swigCPtr, this, value);
    }

    public int getLevel() {
        return pjsua2JNI.LogEntry_level_get(this.swigCPtr, this);
    }

    public void setMsg(String value) {
        pjsua2JNI.LogEntry_msg_set(this.swigCPtr, this, value);
    }

    public String getMsg() {
        return pjsua2JNI.LogEntry_msg_get(this.swigCPtr, this);
    }

    public void setThreadId(int value) {
        pjsua2JNI.LogEntry_threadId_set(this.swigCPtr, this, value);
    }

    public int getThreadId() {
        return pjsua2JNI.LogEntry_threadId_get(this.swigCPtr, this);
    }

    public void setThreadName(String value) {
        pjsua2JNI.LogEntry_threadName_set(this.swigCPtr, this, value);
    }

    public String getThreadName() {
        return pjsua2JNI.LogEntry_threadName_get(this.swigCPtr, this);
    }

    public LogEntry() {
        this(pjsua2JNI.new_LogEntry(), true);
    }
}

