/*
 * Decompiled with CFR 0.152.
 */
package org.pjsip.pjsua2;

import org.pjsip.pjsua2.AccountConfig;
import org.pjsip.pjsua2.AccountInfo;
import org.pjsip.pjsua2.Buddy;
import org.pjsip.pjsua2.BuddyVector2;
import org.pjsip.pjsua2.OnIncomingCallParam;
import org.pjsip.pjsua2.OnIncomingSubscribeParam;
import org.pjsip.pjsua2.OnInstantMessageParam;
import org.pjsip.pjsua2.OnInstantMessageStatusParam;
import org.pjsip.pjsua2.OnMwiInfoParam;
import org.pjsip.pjsua2.OnRegStartedParam;
import org.pjsip.pjsua2.OnRegStateParam;
import org.pjsip.pjsua2.OnTypingIndicationParam;
import org.pjsip.pjsua2.PresNotifyParam;
import org.pjsip.pjsua2.PresenceStatus;
import org.pjsip.pjsua2.pjsua2JNI;

public class Account {
    private transient long swigCPtr;
    protected transient boolean swigCMemOwn;

    protected Account(long cPtr, boolean cMemoryOwn) {
        this.swigCMemOwn = cMemoryOwn;
        this.swigCPtr = cPtr;
    }

    protected static long getCPtr(Account obj) {
        return obj == null ? 0L : obj.swigCPtr;
    }

    protected void finalize() {
        this.delete();
    }

    public synchronized void delete() {
        if (this.swigCPtr != 0L) {
            if (this.swigCMemOwn) {
                this.swigCMemOwn = false;
                pjsua2JNI.delete_Account(this.swigCPtr);
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
        pjsua2JNI.Account_change_ownership(this, this.swigCPtr, false);
    }

    public void swigTakeOwnership() {
        this.swigCMemOwn = true;
        pjsua2JNI.Account_change_ownership(this, this.swigCPtr, true);
    }

    public Account() {
        this(pjsua2JNI.new_Account(), true);
        pjsua2JNI.Account_director_connect(this, this.swigCPtr, this.swigCMemOwn, true);
    }

    public void create(AccountConfig cfg, boolean make_default) throws Exception {
        pjsua2JNI.Account_create__SWIG_0(this.swigCPtr, this, AccountConfig.getCPtr(cfg), cfg, make_default);
    }

    public void create(AccountConfig cfg) throws Exception {
        pjsua2JNI.Account_create__SWIG_1(this.swigCPtr, this, AccountConfig.getCPtr(cfg), cfg);
    }

    public void shutdown() {
        pjsua2JNI.Account_shutdown(this.swigCPtr, this);
    }

    public void modify(AccountConfig cfg) throws Exception {
        pjsua2JNI.Account_modify(this.swigCPtr, this, AccountConfig.getCPtr(cfg), cfg);
    }

    public boolean isValid() {
        return pjsua2JNI.Account_isValid(this.swigCPtr, this);
    }

    public void setDefault() throws Exception {
        pjsua2JNI.Account_setDefault(this.swigCPtr, this);
    }

    public boolean isDefault() {
        return pjsua2JNI.Account_isDefault(this.swigCPtr, this);
    }

    public int getId() {
        return pjsua2JNI.Account_getId(this.swigCPtr, this);
    }

    public static Account lookup(int acc_id) {
        long cPtr = pjsua2JNI.Account_lookup(acc_id);
        return cPtr == 0L ? null : new Account(cPtr, false);
    }

    public AccountInfo getInfo() throws Exception {
        return new AccountInfo(pjsua2JNI.Account_getInfo(this.swigCPtr, this), true);
    }

    public void setRegistration(boolean renew) throws Exception {
        pjsua2JNI.Account_setRegistration(this.swigCPtr, this, renew);
    }

    public void setOnlineStatus(PresenceStatus pres_st) throws Exception {
        pjsua2JNI.Account_setOnlineStatus(this.swigCPtr, this, PresenceStatus.getCPtr(pres_st), pres_st);
    }

    public void setTransport(int tp_id) throws Exception {
        pjsua2JNI.Account_setTransport(this.swigCPtr, this, tp_id);
    }

    public void presNotify(PresNotifyParam prm) throws Exception {
        pjsua2JNI.Account_presNotify(this.swigCPtr, this, PresNotifyParam.getCPtr(prm), prm);
    }

    public BuddyVector2 enumBuddies2() throws Exception {
        return new BuddyVector2(pjsua2JNI.Account_enumBuddies2(this.swigCPtr, this), true);
    }

    public Buddy findBuddy2(String uri) throws Exception {
        return new Buddy(pjsua2JNI.Account_findBuddy2(this.swigCPtr, this, uri), true);
    }

    public void onIncomingCall(OnIncomingCallParam prm) {
        if (this.getClass() == Account.class) {
            pjsua2JNI.Account_onIncomingCall(this.swigCPtr, this, OnIncomingCallParam.getCPtr(prm), prm);
        } else {
            pjsua2JNI.Account_onIncomingCallSwigExplicitAccount(this.swigCPtr, this, OnIncomingCallParam.getCPtr(prm), prm);
        }
    }

    public void onRegStarted(OnRegStartedParam prm) {
        if (this.getClass() == Account.class) {
            pjsua2JNI.Account_onRegStarted(this.swigCPtr, this, OnRegStartedParam.getCPtr(prm), prm);
        } else {
            pjsua2JNI.Account_onRegStartedSwigExplicitAccount(this.swigCPtr, this, OnRegStartedParam.getCPtr(prm), prm);
        }
    }

    public void onRegState(OnRegStateParam prm) {
        if (this.getClass() == Account.class) {
            pjsua2JNI.Account_onRegState(this.swigCPtr, this, OnRegStateParam.getCPtr(prm), prm);
        } else {
            pjsua2JNI.Account_onRegStateSwigExplicitAccount(this.swigCPtr, this, OnRegStateParam.getCPtr(prm), prm);
        }
    }

    public void onIncomingSubscribe(OnIncomingSubscribeParam prm) {
        if (this.getClass() == Account.class) {
            pjsua2JNI.Account_onIncomingSubscribe(this.swigCPtr, this, OnIncomingSubscribeParam.getCPtr(prm), prm);
        } else {
            pjsua2JNI.Account_onIncomingSubscribeSwigExplicitAccount(this.swigCPtr, this, OnIncomingSubscribeParam.getCPtr(prm), prm);
        }
    }

    public void onInstantMessage(OnInstantMessageParam prm) {
        if (this.getClass() == Account.class) {
            pjsua2JNI.Account_onInstantMessage(this.swigCPtr, this, OnInstantMessageParam.getCPtr(prm), prm);
        } else {
            pjsua2JNI.Account_onInstantMessageSwigExplicitAccount(this.swigCPtr, this, OnInstantMessageParam.getCPtr(prm), prm);
        }
    }

    public void onInstantMessageStatus(OnInstantMessageStatusParam prm) {
        if (this.getClass() == Account.class) {
            pjsua2JNI.Account_onInstantMessageStatus(this.swigCPtr, this, OnInstantMessageStatusParam.getCPtr(prm), prm);
        } else {
            pjsua2JNI.Account_onInstantMessageStatusSwigExplicitAccount(this.swigCPtr, this, OnInstantMessageStatusParam.getCPtr(prm), prm);
        }
    }

    public void onTypingIndication(OnTypingIndicationParam prm) {
        if (this.getClass() == Account.class) {
            pjsua2JNI.Account_onTypingIndication(this.swigCPtr, this, OnTypingIndicationParam.getCPtr(prm), prm);
        } else {
            pjsua2JNI.Account_onTypingIndicationSwigExplicitAccount(this.swigCPtr, this, OnTypingIndicationParam.getCPtr(prm), prm);
        }
    }

    public void onMwiInfo(OnMwiInfoParam prm) {
        if (this.getClass() == Account.class) {
            pjsua2JNI.Account_onMwiInfo(this.swigCPtr, this, OnMwiInfoParam.getCPtr(prm), prm);
        } else {
            pjsua2JNI.Account_onMwiInfoSwigExplicitAccount(this.swigCPtr, this, OnMwiInfoParam.getCPtr(prm), prm);
        }
    }
}

