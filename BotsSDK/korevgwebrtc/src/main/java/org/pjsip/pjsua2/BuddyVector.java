/*
 * Decompiled with CFR 0.152.
 */
package org.pjsip.pjsua2;

import org.pjsip.pjsua2.Buddy;
import org.pjsip.pjsua2.pjsua2JNI;

public class BuddyVector {
    private transient long swigCPtr;
    protected transient boolean swigCMemOwn;

    protected BuddyVector(long cPtr, boolean cMemoryOwn) {
        this.swigCMemOwn = cMemoryOwn;
        this.swigCPtr = cPtr;
    }

    protected static long getCPtr(BuddyVector obj) {
        return obj == null ? 0L : obj.swigCPtr;
    }

    protected void finalize() {
        this.delete();
    }

    public synchronized void delete() {
        if (this.swigCPtr != 0L) {
            if (this.swigCMemOwn) {
                this.swigCMemOwn = false;
                pjsua2JNI.delete_BuddyVector(this.swigCPtr);
            }
            this.swigCPtr = 0L;
        }
    }

    public BuddyVector() {
        this(pjsua2JNI.new_BuddyVector__SWIG_0(), true);
    }

    public BuddyVector(long n) {
        this(pjsua2JNI.new_BuddyVector__SWIG_1(n), true);
    }

    public long size() {
        return pjsua2JNI.BuddyVector_size(this.swigCPtr, this);
    }

    public long capacity() {
        return pjsua2JNI.BuddyVector_capacity(this.swigCPtr, this);
    }

    public void reserve(long n) {
        pjsua2JNI.BuddyVector_reserve(this.swigCPtr, this, n);
    }

    public boolean isEmpty() {
        return pjsua2JNI.BuddyVector_isEmpty(this.swigCPtr, this);
    }

    public void clear() {
        pjsua2JNI.BuddyVector_clear(this.swigCPtr, this);
    }

    public void add(Buddy x) {
        pjsua2JNI.BuddyVector_add(this.swigCPtr, this, Buddy.getCPtr(x), x);
    }

    public Buddy get(int i) {
        long cPtr = pjsua2JNI.BuddyVector_get(this.swigCPtr, this, i);
        return cPtr == 0L ? null : new Buddy(cPtr, false);
    }

    public void set(int i, Buddy val) {
        pjsua2JNI.BuddyVector_set(this.swigCPtr, this, i, Buddy.getCPtr(val), val);
    }
}

