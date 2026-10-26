/*
 * Decompiled with CFR 0.152.
 */
package org.pjsip.pjsua2;

import org.pjsip.pjsua2.Buddy;
import org.pjsip.pjsua2.pjsua2JNI;

public class FindBuddyMatch {
    private transient long swigCPtr;
    protected transient boolean swigCMemOwn;

    protected FindBuddyMatch(long cPtr, boolean cMemoryOwn) {
        this.swigCMemOwn = cMemoryOwn;
        this.swigCPtr = cPtr;
    }

    protected static long getCPtr(FindBuddyMatch obj) {
        return obj == null ? 0L : obj.swigCPtr;
    }

    protected void finalize() {
        this.delete();
    }

    public synchronized void delete() {
        if (this.swigCPtr != 0L) {
            if (this.swigCMemOwn) {
                this.swigCMemOwn = false;
                pjsua2JNI.delete_FindBuddyMatch(this.swigCPtr);
            }
            this.swigCPtr = 0L;
        }
    }

    protected void swigDirectorDisconnect() {
        this.swigCMemOwn = false;
        this.delete();
    }

    public void swigReleaseOwnership() {
        this.swigCMemOwn = false;
        pjsua2JNI.FindBuddyMatch_change_ownership(this, this.swigCPtr, false);
    }

    public void swigTakeOwnership() {
        this.swigCMemOwn = true;
        pjsua2JNI.FindBuddyMatch_change_ownership(this, this.swigCPtr, true);
    }

    public boolean match(String token, Buddy buddy) {
        return this.getClass() == FindBuddyMatch.class ? pjsua2JNI.FindBuddyMatch_match(this.swigCPtr, this, token, Buddy.getCPtr(buddy), buddy) : pjsua2JNI.FindBuddyMatch_matchSwigExplicitFindBuddyMatch(this.swigCPtr, this, token, Buddy.getCPtr(buddy), buddy);
    }

    public FindBuddyMatch() {
        this(pjsua2JNI.new_FindBuddyMatch(), true);
        pjsua2JNI.FindBuddyMatch_director_connect(this, this.swigCPtr, this.swigCMemOwn, true);
    }
}

