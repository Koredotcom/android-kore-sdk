/*
 * Decompiled with CFR 0.152.
 */
package org.pjsip.pjsua2;

import org.pjsip.pjsua2.pjsua2JNI;

public class AccountInfo {
    private transient long swigCPtr;
    protected transient boolean swigCMemOwn;

    protected AccountInfo(long cPtr, boolean cMemoryOwn) {
        this.swigCMemOwn = cMemoryOwn;
        this.swigCPtr = cPtr;
    }

    protected static long getCPtr(AccountInfo obj) {
        return obj == null ? 0L : obj.swigCPtr;
    }

    protected void finalize() {
        this.delete();
    }

    public synchronized void delete() {
        if (this.swigCPtr != 0L) {
            if (this.swigCMemOwn) {
                this.swigCMemOwn = false;
                pjsua2JNI.delete_AccountInfo(this.swigCPtr);
            }
            this.swigCPtr = 0L;
        }
    }

    public void setId(int value) {
        pjsua2JNI.AccountInfo_id_set(this.swigCPtr, this, value);
    }

    public int getId() {
        return pjsua2JNI.AccountInfo_id_get(this.swigCPtr, this);
    }

    public void setIsDefault(boolean value) {
        pjsua2JNI.AccountInfo_isDefault_set(this.swigCPtr, this, value);
    }

    public boolean getIsDefault() {
        return pjsua2JNI.AccountInfo_isDefault_get(this.swigCPtr, this);
    }

    public void setUri(String value) {
        pjsua2JNI.AccountInfo_uri_set(this.swigCPtr, this, value);
    }

    public String getUri() {
        return pjsua2JNI.AccountInfo_uri_get(this.swigCPtr, this);
    }

    public void setRegIsConfigured(boolean value) {
        pjsua2JNI.AccountInfo_regIsConfigured_set(this.swigCPtr, this, value);
    }

    public boolean getRegIsConfigured() {
        return pjsua2JNI.AccountInfo_regIsConfigured_get(this.swigCPtr, this);
    }

    public void setRegIsActive(boolean value) {
        pjsua2JNI.AccountInfo_regIsActive_set(this.swigCPtr, this, value);
    }

    public boolean getRegIsActive() {
        return pjsua2JNI.AccountInfo_regIsActive_get(this.swigCPtr, this);
    }

    public void setRegExpiresSec(long value) {
        pjsua2JNI.AccountInfo_regExpiresSec_set(this.swigCPtr, this, value);
    }

    public long getRegExpiresSec() {
        return pjsua2JNI.AccountInfo_regExpiresSec_get(this.swigCPtr, this);
    }

    public void setRegStatus(int value) {
        pjsua2JNI.AccountInfo_regStatus_set(this.swigCPtr, this, value);
    }

    public int getRegStatus() {
        return pjsua2JNI.AccountInfo_regStatus_get(this.swigCPtr, this);
    }

    public void setRegStatusText(String value) {
        pjsua2JNI.AccountInfo_regStatusText_set(this.swigCPtr, this, value);
    }

    public String getRegStatusText() {
        return pjsua2JNI.AccountInfo_regStatusText_get(this.swigCPtr, this);
    }

    public void setRegLastErr(int value) {
        pjsua2JNI.AccountInfo_regLastErr_set(this.swigCPtr, this, value);
    }

    public int getRegLastErr() {
        return pjsua2JNI.AccountInfo_regLastErr_get(this.swigCPtr, this);
    }

    public void setOnlineStatus(boolean value) {
        pjsua2JNI.AccountInfo_onlineStatus_set(this.swigCPtr, this, value);
    }

    public boolean getOnlineStatus() {
        return pjsua2JNI.AccountInfo_onlineStatus_get(this.swigCPtr, this);
    }

    public void setOnlineStatusText(String value) {
        pjsua2JNI.AccountInfo_onlineStatusText_set(this.swigCPtr, this, value);
    }

    public String getOnlineStatusText() {
        return pjsua2JNI.AccountInfo_onlineStatusText_get(this.swigCPtr, this);
    }

    public AccountInfo() {
        this(pjsua2JNI.new_AccountInfo(), true);
    }
}

