/*
 * Decompiled with CFR 0.152.
 */
package org.pjsip.pjsua2;

import org.pjsip.pjsua2.pjsua2JNI;

public class WindowHandle {
    private transient long swigCPtr;
    protected transient boolean swigCMemOwn;

    protected WindowHandle(long cPtr, boolean cMemoryOwn) {
        this.swigCMemOwn = cMemoryOwn;
        this.swigCPtr = cPtr;
    }

    protected static long getCPtr(WindowHandle obj) {
        return obj == null ? 0L : obj.swigCPtr;
    }

    protected void finalize() {
        this.delete();
    }

    public synchronized void delete() {
        if (this.swigCPtr != 0L) {
            if (this.swigCMemOwn) {
                this.swigCMemOwn = false;
                pjsua2JNI.delete_WindowHandle(this.swigCPtr);
            }
            this.swigCPtr = 0L;
        }
    }

    public void setWindow(Object surface) {
        pjsua2JNI.WindowHandle_setWindow(this.swigCPtr, this, surface);
    }

    public WindowHandle() {
        this(pjsua2JNI.new_WindowHandle(), true);
    }
}

