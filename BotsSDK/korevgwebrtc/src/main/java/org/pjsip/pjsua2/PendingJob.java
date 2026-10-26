/*
 * Decompiled with CFR 0.152.
 */
package org.pjsip.pjsua2;

import org.pjsip.pjsua2.pjsua2JNI;

public class PendingJob {
    private transient long swigCPtr;
    protected transient boolean swigCMemOwn;

    protected PendingJob(long cPtr, boolean cMemoryOwn) {
        this.swigCMemOwn = cMemoryOwn;
        this.swigCPtr = cPtr;
    }

    protected static long getCPtr(PendingJob obj) {
        return obj == null ? 0L : obj.swigCPtr;
    }

    protected void finalize() {
        this.delete();
    }

    public synchronized void delete() {
        if (this.swigCPtr != 0L) {
            if (this.swigCMemOwn) {
                this.swigCMemOwn = false;
                pjsua2JNI.delete_PendingJob(this.swigCPtr);
            }
            this.swigCPtr = 0L;
        }
    }

    public void execute(boolean is_pending) {
        pjsua2JNI.PendingJob_execute(this.swigCPtr, this, is_pending);
    }
}

