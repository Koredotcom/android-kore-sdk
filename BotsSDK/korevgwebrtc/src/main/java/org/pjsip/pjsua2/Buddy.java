/*
 * Decompiled with CFR 0.152.
 */
package org.pjsip.pjsua2;

import org.pjsip.pjsua2.Account;
import org.pjsip.pjsua2.BuddyConfig;
import org.pjsip.pjsua2.BuddyInfo;
import org.pjsip.pjsua2.OnBuddyEvSubStateParam;
import org.pjsip.pjsua2.SendInstantMessageParam;
import org.pjsip.pjsua2.SendTypingIndicationParam;
import org.pjsip.pjsua2.pjsua2JNI;

public class Buddy {
    private transient long swigCPtr;
    protected transient boolean swigCMemOwn;

    protected Buddy(long cPtr, boolean cMemoryOwn) {
        this.swigCMemOwn = cMemoryOwn;
        this.swigCPtr = cPtr;
    }

    protected static long getCPtr(Buddy obj) {
        return obj == null ? 0L : obj.swigCPtr;
    }

    protected void finalize() {
        this.delete();
    }

    public synchronized void delete() {
        if (this.swigCPtr != 0L) {
            if (this.swigCMemOwn) {
                this.swigCMemOwn = false;
                pjsua2JNI.delete_Buddy(this.swigCPtr);
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
        pjsua2JNI.Buddy_change_ownership(this, this.swigCPtr, false);
    }

    public void swigTakeOwnership() {
        this.swigCMemOwn = true;
        pjsua2JNI.Buddy_change_ownership(this, this.swigCPtr, true);
    }

    public Buddy() {
        this(pjsua2JNI.new_Buddy(), true);
        pjsua2JNI.Buddy_director_connect(this, this.swigCPtr, this.swigCMemOwn, true);
    }

    public void create(Account acc, BuddyConfig cfg) throws Exception {
        pjsua2JNI.Buddy_create(this.swigCPtr, this, Account.getCPtr(acc), acc, BuddyConfig.getCPtr(cfg), cfg);
    }

    public boolean isValid() {
        return pjsua2JNI.Buddy_isValid(this.swigCPtr, this);
    }

    public BuddyInfo getInfo() throws Exception {
        return new BuddyInfo(pjsua2JNI.Buddy_getInfo(this.swigCPtr, this), true);
    }

    public void subscribePresence(boolean subscribe) throws Exception {
        pjsua2JNI.Buddy_subscribePresence(this.swigCPtr, this, subscribe);
    }

    public void updatePresence() throws Exception {
        pjsua2JNI.Buddy_updatePresence(this.swigCPtr, this);
    }

    public void sendInstantMessage(SendInstantMessageParam prm) throws Exception {
        pjsua2JNI.Buddy_sendInstantMessage(this.swigCPtr, this, SendInstantMessageParam.getCPtr(prm), prm);
    }

    public void sendTypingIndication(SendTypingIndicationParam prm) throws Exception {
        pjsua2JNI.Buddy_sendTypingIndication(this.swigCPtr, this, SendTypingIndicationParam.getCPtr(prm), prm);
    }

    public void onBuddyState() {
        if (this.getClass() == Buddy.class) {
            pjsua2JNI.Buddy_onBuddyState(this.swigCPtr, this);
        } else {
            pjsua2JNI.Buddy_onBuddyStateSwigExplicitBuddy(this.swigCPtr, this);
        }
    }

    public void onBuddyEvSubState(OnBuddyEvSubStateParam prm) {
        if (this.getClass() == Buddy.class) {
            pjsua2JNI.Buddy_onBuddyEvSubState(this.swigCPtr, this, OnBuddyEvSubStateParam.getCPtr(prm), prm);
        } else {
            pjsua2JNI.Buddy_onBuddyEvSubStateSwigExplicitBuddy(this.swigCPtr, this, OnBuddyEvSubStateParam.getCPtr(prm), prm);
        }
    }
}

