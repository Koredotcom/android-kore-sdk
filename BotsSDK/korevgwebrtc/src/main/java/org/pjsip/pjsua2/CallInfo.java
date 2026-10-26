/*
 * Decompiled with CFR 0.152.
 */
package org.pjsip.pjsua2;

import org.pjsip.pjsua2.CallMediaInfoVector;
import org.pjsip.pjsua2.CallSetting;
import org.pjsip.pjsua2.TimeVal;
import org.pjsip.pjsua2.pjsua2JNI;

public class CallInfo {
    private transient long swigCPtr;
    protected transient boolean swigCMemOwn;

    protected CallInfo(long cPtr, boolean cMemoryOwn) {
        this.swigCMemOwn = cMemoryOwn;
        this.swigCPtr = cPtr;
    }

    protected static long getCPtr(CallInfo obj) {
        return obj == null ? 0L : obj.swigCPtr;
    }

    protected void finalize() {
        this.delete();
    }

    public synchronized void delete() {
        if (this.swigCPtr != 0L) {
            if (this.swigCMemOwn) {
                this.swigCMemOwn = false;
                pjsua2JNI.delete_CallInfo(this.swigCPtr);
            }
            this.swigCPtr = 0L;
        }
    }

    public void setId(int value) {
        pjsua2JNI.CallInfo_id_set(this.swigCPtr, this, value);
    }

    public int getId() {
        return pjsua2JNI.CallInfo_id_get(this.swigCPtr, this);
    }

    public void setRole(int value) {
        pjsua2JNI.CallInfo_role_set(this.swigCPtr, this, value);
    }

    public int getRole() {
        return pjsua2JNI.CallInfo_role_get(this.swigCPtr, this);
    }

    public void setAccId(int value) {
        pjsua2JNI.CallInfo_accId_set(this.swigCPtr, this, value);
    }

    public int getAccId() {
        return pjsua2JNI.CallInfo_accId_get(this.swigCPtr, this);
    }

    public void setLocalUri(String value) {
        pjsua2JNI.CallInfo_localUri_set(this.swigCPtr, this, value);
    }

    public String getLocalUri() {
        return pjsua2JNI.CallInfo_localUri_get(this.swigCPtr, this);
    }

    public void setLocalContact(String value) {
        pjsua2JNI.CallInfo_localContact_set(this.swigCPtr, this, value);
    }

    public String getLocalContact() {
        return pjsua2JNI.CallInfo_localContact_get(this.swigCPtr, this);
    }

    public void setRemoteUri(String value) {
        pjsua2JNI.CallInfo_remoteUri_set(this.swigCPtr, this, value);
    }

    public String getRemoteUri() {
        return pjsua2JNI.CallInfo_remoteUri_get(this.swigCPtr, this);
    }

    public void setRemoteContact(String value) {
        pjsua2JNI.CallInfo_remoteContact_set(this.swigCPtr, this, value);
    }

    public String getRemoteContact() {
        return pjsua2JNI.CallInfo_remoteContact_get(this.swigCPtr, this);
    }

    public void setCallIdString(String value) {
        pjsua2JNI.CallInfo_callIdString_set(this.swigCPtr, this, value);
    }

    public String getCallIdString() {
        return pjsua2JNI.CallInfo_callIdString_get(this.swigCPtr, this);
    }

    public void setSetting(CallSetting value) {
        pjsua2JNI.CallInfo_setting_set(this.swigCPtr, this, CallSetting.getCPtr(value), value);
    }

    public CallSetting getSetting() {
        long cPtr = pjsua2JNI.CallInfo_setting_get(this.swigCPtr, this);
        return cPtr == 0L ? null : new CallSetting(cPtr, false);
    }

    public void setState(int value) {
        pjsua2JNI.CallInfo_state_set(this.swigCPtr, this, value);
    }

    public int getState() {
        return pjsua2JNI.CallInfo_state_get(this.swigCPtr, this);
    }

    public void setStateText(String value) {
        pjsua2JNI.CallInfo_stateText_set(this.swigCPtr, this, value);
    }

    public String getStateText() {
        return pjsua2JNI.CallInfo_stateText_get(this.swigCPtr, this);
    }

    public void setLastStatusCode(int value) {
        pjsua2JNI.CallInfo_lastStatusCode_set(this.swigCPtr, this, value);
    }

    public int getLastStatusCode() {
        return pjsua2JNI.CallInfo_lastStatusCode_get(this.swigCPtr, this);
    }

    public void setLastReason(String value) {
        pjsua2JNI.CallInfo_lastReason_set(this.swigCPtr, this, value);
    }

    public String getLastReason() {
        return pjsua2JNI.CallInfo_lastReason_get(this.swigCPtr, this);
    }

    public void setMedia(CallMediaInfoVector value) {
        pjsua2JNI.CallInfo_media_set(this.swigCPtr, this, CallMediaInfoVector.getCPtr(value), value);
    }

    public CallMediaInfoVector getMedia() {
        long cPtr = pjsua2JNI.CallInfo_media_get(this.swigCPtr, this);
        return cPtr == 0L ? null : new CallMediaInfoVector(cPtr, false);
    }

    public void setProvMedia(CallMediaInfoVector value) {
        pjsua2JNI.CallInfo_provMedia_set(this.swigCPtr, this, CallMediaInfoVector.getCPtr(value), value);
    }

    public CallMediaInfoVector getProvMedia() {
        long cPtr = pjsua2JNI.CallInfo_provMedia_get(this.swigCPtr, this);
        return cPtr == 0L ? null : new CallMediaInfoVector(cPtr, false);
    }

    public void setConnectDuration(TimeVal value) {
        pjsua2JNI.CallInfo_connectDuration_set(this.swigCPtr, this, TimeVal.getCPtr(value), value);
    }

    public TimeVal getConnectDuration() {
        long cPtr = pjsua2JNI.CallInfo_connectDuration_get(this.swigCPtr, this);
        return cPtr == 0L ? null : new TimeVal(cPtr, false);
    }

    public void setTotalDuration(TimeVal value) {
        pjsua2JNI.CallInfo_totalDuration_set(this.swigCPtr, this, TimeVal.getCPtr(value), value);
    }

    public TimeVal getTotalDuration() {
        long cPtr = pjsua2JNI.CallInfo_totalDuration_get(this.swigCPtr, this);
        return cPtr == 0L ? null : new TimeVal(cPtr, false);
    }

    public void setRemOfferer(boolean value) {
        pjsua2JNI.CallInfo_remOfferer_set(this.swigCPtr, this, value);
    }

    public boolean getRemOfferer() {
        return pjsua2JNI.CallInfo_remOfferer_get(this.swigCPtr, this);
    }

    public void setRemAudioCount(long value) {
        pjsua2JNI.CallInfo_remAudioCount_set(this.swigCPtr, this, value);
    }

    public long getRemAudioCount() {
        return pjsua2JNI.CallInfo_remAudioCount_get(this.swigCPtr, this);
    }

    public void setRemVideoCount(long value) {
        pjsua2JNI.CallInfo_remVideoCount_set(this.swigCPtr, this, value);
    }

    public long getRemVideoCount() {
        return pjsua2JNI.CallInfo_remVideoCount_get(this.swigCPtr, this);
    }

    public CallInfo() {
        this(pjsua2JNI.new_CallInfo(), true);
    }
}

