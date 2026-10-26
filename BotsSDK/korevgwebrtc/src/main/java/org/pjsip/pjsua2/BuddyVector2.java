/*
 * Decompiled with CFR 0.152.
 */
package org.pjsip.pjsua2;

import org.pjsip.pjsua2.Buddy;
import org.pjsip.pjsua2.pjsua2JNI;

public class BuddyVector2 {
    private transient long swigCPtr;
    protected transient boolean swigCMemOwn;

    protected BuddyVector2(long cPtr, boolean cMemoryOwn) {
        this.swigCMemOwn = cMemoryOwn;
        this.swigCPtr = cPtr;
    }

    protected static long getCPtr(BuddyVector2 obj) {
        return obj == null ? 0L : obj.swigCPtr;
    }

    protected void finalize() {
        this.delete();
    }

    public synchronized void delete() {
        if (this.swigCPtr != 0L) {
            if (this.swigCMemOwn) {
                this.swigCMemOwn = false;
                pjsua2JNI.delete_BuddyVector2(this.swigCPtr);
            }
            this.swigCPtr = 0L;
        }
    }

    public BuddyVector2() {
        this(pjsua2JNI.new_BuddyVector2__SWIG_0(), true);
    }

    public BuddyVector2(long n) {
        this(pjsua2JNI.new_BuddyVector2__SWIG_1(n), true);
    }

    public long size() {
        return pjsua2JNI.BuddyVector2_size(this.swigCPtr, this);
    }

    public long capacity() {
        return pjsua2JNI.BuddyVector2_capacity(this.swigCPtr, this);
    }

    public void reserve(long n) {
        pjsua2JNI.BuddyVector2_reserve(this.swigCPtr, this, n);
    }

    public boolean isEmpty() {
        return pjsua2JNI.BuddyVector2_isEmpty(this.swigCPtr, this);
    }

    public void clear() {
        pjsua2JNI.BuddyVector2_clear(this.swigCPtr, this);
    }

    public void add(Buddy x) {
        pjsua2JNI.BuddyVector2_add(this.swigCPtr, this, Buddy.getCPtr(x), x);
    }

    public Buddy get(int i) {
        return new Buddy(pjsua2JNI.BuddyVector2_get(this.swigCPtr, this, i), false);
    }

    public void set(int i, Buddy val) {
        pjsua2JNI.BuddyVector2_set(this.swigCPtr, this, i, Buddy.getCPtr(val), val);
    }
}

