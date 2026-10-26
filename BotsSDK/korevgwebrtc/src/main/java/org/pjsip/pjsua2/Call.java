/*
 * Decompiled with CFR 0.152.
 */
package org.pjsip.pjsua2;

import org.pjsip.pjsua2.Account;
import org.pjsip.pjsua2.AudioMedia;
import org.pjsip.pjsua2.CallInfo;
import org.pjsip.pjsua2.CallOpParam;
import org.pjsip.pjsua2.CallSendDtmfParam;
import org.pjsip.pjsua2.CallSendRequestParam;
import org.pjsip.pjsua2.CallVidSetStreamParam;
import org.pjsip.pjsua2.Media;
import org.pjsip.pjsua2.MediaTransportInfo;
import org.pjsip.pjsua2.OnCallAnsweredParam;
import org.pjsip.pjsua2.OnCallMediaEventParam;
import org.pjsip.pjsua2.OnCallMediaStateParam;
import org.pjsip.pjsua2.OnCallMediaTransportStateParam;
import org.pjsip.pjsua2.OnCallRedirectedParam;
import org.pjsip.pjsua2.OnCallReplaceRequestParam;
import org.pjsip.pjsua2.OnCallReplacedParam;
import org.pjsip.pjsua2.OnCallRxOfferParam;
import org.pjsip.pjsua2.OnCallRxReinviteParam;
import org.pjsip.pjsua2.OnCallSdpCreatedParam;
import org.pjsip.pjsua2.OnCallStateParam;
import org.pjsip.pjsua2.OnCallTransferRequestParam;
import org.pjsip.pjsua2.OnCallTransferStatusParam;
import org.pjsip.pjsua2.OnCallTsxStateParam;
import org.pjsip.pjsua2.OnCallTxOfferParam;
import org.pjsip.pjsua2.OnCreateMediaTransportParam;
import org.pjsip.pjsua2.OnCreateMediaTransportSrtpParam;
import org.pjsip.pjsua2.OnDtmfDigitParam;
import org.pjsip.pjsua2.OnInstantMessageParam;
import org.pjsip.pjsua2.OnInstantMessageStatusParam;
import org.pjsip.pjsua2.OnStreamCreatedParam;
import org.pjsip.pjsua2.OnStreamDestroyedParam;
import org.pjsip.pjsua2.OnTypingIndicationParam;
import org.pjsip.pjsua2.SWIGTYPE_p_void;
import org.pjsip.pjsua2.SendInstantMessageParam;
import org.pjsip.pjsua2.SendTypingIndicationParam;
import org.pjsip.pjsua2.StreamInfo;
import org.pjsip.pjsua2.StreamStat;
import org.pjsip.pjsua2.VideoMedia;
import org.pjsip.pjsua2.pjsua2JNI;

public class Call {
    private transient long swigCPtr;
    protected transient boolean swigCMemOwn;

    protected Call(long cPtr, boolean cMemoryOwn) {
        this.swigCMemOwn = cMemoryOwn;
        this.swigCPtr = cPtr;
    }

    protected static long getCPtr(Call obj) {
        return obj == null ? 0L : obj.swigCPtr;
    }

    protected void finalize() {
        this.delete();
    }

    public synchronized void delete() {
        if (this.swigCPtr != 0L) {
            if (this.swigCMemOwn) {
                this.swigCMemOwn = false;
                pjsua2JNI.delete_Call(this.swigCPtr);
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
        pjsua2JNI.Call_change_ownership(this, this.swigCPtr, false);
    }

    public void swigTakeOwnership() {
        this.swigCMemOwn = true;
        pjsua2JNI.Call_change_ownership(this, this.swigCPtr, true);
    }

    public Call(Account acc, int call_id) {
        this(pjsua2JNI.new_Call__SWIG_0(Account.getCPtr(acc), acc, call_id), true);
        pjsua2JNI.Call_director_connect(this, this.swigCPtr, this.swigCMemOwn, true);
    }

    public Call(Account acc) {
        this(pjsua2JNI.new_Call__SWIG_1(Account.getCPtr(acc), acc), true);
        pjsua2JNI.Call_director_connect(this, this.swigCPtr, this.swigCMemOwn, true);
    }

    public CallInfo getInfo() throws Exception {
        return new CallInfo(pjsua2JNI.Call_getInfo(this.swigCPtr, this), true);
    }

    public boolean isActive() {
        return pjsua2JNI.Call_isActive(this.swigCPtr, this);
    }

    public int getId() {
        return pjsua2JNI.Call_getId(this.swigCPtr, this);
    }

    public static Call lookup(int call_id) {
        long cPtr = pjsua2JNI.Call_lookup(call_id);
        return cPtr == 0L ? null : new Call(cPtr, false);
    }

    public boolean hasMedia() {
        return pjsua2JNI.Call_hasMedia(this.swigCPtr, this);
    }

    public Media getMedia(long med_idx) {
        long cPtr = pjsua2JNI.Call_getMedia(this.swigCPtr, this, med_idx);
        return cPtr == 0L ? null : new Media(cPtr, false);
    }

    public AudioMedia getAudioMedia(int med_idx) throws Exception {
        return new AudioMedia(pjsua2JNI.Call_getAudioMedia(this.swigCPtr, this, med_idx), true);
    }

    public VideoMedia getEncodingVideoMedia(int med_idx) throws Exception {
        return new VideoMedia(pjsua2JNI.Call_getEncodingVideoMedia(this.swigCPtr, this, med_idx), true);
    }

    public VideoMedia getDecodingVideoMedia(int med_idx) throws Exception {
        return new VideoMedia(pjsua2JNI.Call_getDecodingVideoMedia(this.swigCPtr, this, med_idx), true);
    }

    public int remoteHasCap(int htype, String hname, String token) {
        return pjsua2JNI.Call_remoteHasCap(this.swigCPtr, this, htype, hname, token);
    }

    public void setUserData(SWIGTYPE_p_void user_data) {
        pjsua2JNI.Call_setUserData(this.swigCPtr, this, SWIGTYPE_p_void.getCPtr(user_data));
    }

    public SWIGTYPE_p_void getUserData() {
        long cPtr = pjsua2JNI.Call_getUserData(this.swigCPtr, this);
        return cPtr == 0L ? null : new SWIGTYPE_p_void(cPtr, false);
    }

    public int getRemNatType() throws Exception {
        return pjsua2JNI.Call_getRemNatType(this.swigCPtr, this);
    }

    public void makeCall(String dst_uri, CallOpParam prm) throws Exception {
        pjsua2JNI.Call_makeCall(this.swigCPtr, this, dst_uri, CallOpParam.getCPtr(prm), prm);
    }

    public void answer(CallOpParam prm) throws Exception {
        pjsua2JNI.Call_answer(this.swigCPtr, this, CallOpParam.getCPtr(prm), prm);
    }

    public void hangup(CallOpParam prm) throws Exception {
        pjsua2JNI.Call_hangup(this.swigCPtr, this, CallOpParam.getCPtr(prm), prm);
    }

    public void setHold(CallOpParam prm) throws Exception {
        pjsua2JNI.Call_setHold(this.swigCPtr, this, CallOpParam.getCPtr(prm), prm);
    }

    public void reinvite(CallOpParam prm) throws Exception {
        pjsua2JNI.Call_reinvite(this.swigCPtr, this, CallOpParam.getCPtr(prm), prm);
    }

    public void update(CallOpParam prm) throws Exception {
        pjsua2JNI.Call_update(this.swigCPtr, this, CallOpParam.getCPtr(prm), prm);
    }

    public void xfer(String dest, CallOpParam prm) throws Exception {
        pjsua2JNI.Call_xfer(this.swigCPtr, this, dest, CallOpParam.getCPtr(prm), prm);
    }

    public void xferReplaces(Call dest_call, CallOpParam prm) throws Exception {
        pjsua2JNI.Call_xferReplaces(this.swigCPtr, this, Call.getCPtr(dest_call), dest_call, CallOpParam.getCPtr(prm), prm);
    }

    public void processRedirect(int cmd) throws Exception {
        pjsua2JNI.Call_processRedirect(this.swigCPtr, this, cmd);
    }

    public void dialDtmf(String digits) throws Exception {
        pjsua2JNI.Call_dialDtmf(this.swigCPtr, this, digits);
    }

    public void sendDtmf(CallSendDtmfParam param) throws Exception {
        pjsua2JNI.Call_sendDtmf(this.swigCPtr, this, CallSendDtmfParam.getCPtr(param), param);
    }

    public void sendInstantMessage(SendInstantMessageParam prm) throws Exception {
        pjsua2JNI.Call_sendInstantMessage(this.swigCPtr, this, SendInstantMessageParam.getCPtr(prm), prm);
    }

    public void sendTypingIndication(SendTypingIndicationParam prm) throws Exception {
        pjsua2JNI.Call_sendTypingIndication(this.swigCPtr, this, SendTypingIndicationParam.getCPtr(prm), prm);
    }

    public void sendRequest(CallSendRequestParam prm) throws Exception {
        pjsua2JNI.Call_sendRequest(this.swigCPtr, this, CallSendRequestParam.getCPtr(prm), prm);
    }

    public String dump(boolean with_media, String indent) throws Exception {
        return pjsua2JNI.Call_dump(this.swigCPtr, this, with_media, indent);
    }

    public int vidGetStreamIdx() {
        return pjsua2JNI.Call_vidGetStreamIdx(this.swigCPtr, this);
    }

    public boolean vidStreamIsRunning(int med_idx, int dir) {
        return pjsua2JNI.Call_vidStreamIsRunning(this.swigCPtr, this, med_idx, dir);
    }

    public void vidSetStream(int op, CallVidSetStreamParam param) throws Exception {
        pjsua2JNI.Call_vidSetStream(this.swigCPtr, this, op, CallVidSetStreamParam.getCPtr(param), param);
    }

    public StreamInfo getStreamInfo(long med_idx) throws Exception {
        return new StreamInfo(pjsua2JNI.Call_getStreamInfo(this.swigCPtr, this, med_idx), true);
    }

    public StreamStat getStreamStat(long med_idx) throws Exception {
        return new StreamStat(pjsua2JNI.Call_getStreamStat(this.swigCPtr, this, med_idx), true);
    }

    public MediaTransportInfo getMedTransportInfo(long med_idx) throws Exception {
        return new MediaTransportInfo(pjsua2JNI.Call_getMedTransportInfo(this.swigCPtr, this, med_idx), true);
    }

    public void processMediaUpdate(OnCallMediaStateParam prm) {
        pjsua2JNI.Call_processMediaUpdate(this.swigCPtr, this, OnCallMediaStateParam.getCPtr(prm), prm);
    }

    public void processStateChange(OnCallStateParam prm) {
        pjsua2JNI.Call_processStateChange(this.swigCPtr, this, OnCallStateParam.getCPtr(prm), prm);
    }

    public void onCallState(OnCallStateParam prm) {
        if (this.getClass() == Call.class) {
            pjsua2JNI.Call_onCallState(this.swigCPtr, this, OnCallStateParam.getCPtr(prm), prm);
        } else {
            pjsua2JNI.Call_onCallStateSwigExplicitCall(this.swigCPtr, this, OnCallStateParam.getCPtr(prm), prm);
        }
    }

    public void onCallTsxState(OnCallTsxStateParam prm) {
        if (this.getClass() == Call.class) {
            pjsua2JNI.Call_onCallTsxState(this.swigCPtr, this, OnCallTsxStateParam.getCPtr(prm), prm);
        } else {
            pjsua2JNI.Call_onCallTsxStateSwigExplicitCall(this.swigCPtr, this, OnCallTsxStateParam.getCPtr(prm), prm);
        }
    }

    public void onCallMediaState(OnCallMediaStateParam prm) {
        if (this.getClass() == Call.class) {
            pjsua2JNI.Call_onCallMediaState(this.swigCPtr, this, OnCallMediaStateParam.getCPtr(prm), prm);
        } else {
            pjsua2JNI.Call_onCallMediaStateSwigExplicitCall(this.swigCPtr, this, OnCallMediaStateParam.getCPtr(prm), prm);
        }
    }

    public void onCallSdpCreated(OnCallSdpCreatedParam prm) {
        if (this.getClass() == Call.class) {
            pjsua2JNI.Call_onCallSdpCreated(this.swigCPtr, this, OnCallSdpCreatedParam.getCPtr(prm), prm);
        } else {
            pjsua2JNI.Call_onCallSdpCreatedSwigExplicitCall(this.swigCPtr, this, OnCallSdpCreatedParam.getCPtr(prm), prm);
        }
    }

    public void onStreamCreated(OnStreamCreatedParam prm) {
        if (this.getClass() == Call.class) {
            pjsua2JNI.Call_onStreamCreated(this.swigCPtr, this, OnStreamCreatedParam.getCPtr(prm), prm);
        } else {
            pjsua2JNI.Call_onStreamCreatedSwigExplicitCall(this.swigCPtr, this, OnStreamCreatedParam.getCPtr(prm), prm);
        }
    }

    public void onStreamDestroyed(OnStreamDestroyedParam prm) {
        if (this.getClass() == Call.class) {
            pjsua2JNI.Call_onStreamDestroyed(this.swigCPtr, this, OnStreamDestroyedParam.getCPtr(prm), prm);
        } else {
            pjsua2JNI.Call_onStreamDestroyedSwigExplicitCall(this.swigCPtr, this, OnStreamDestroyedParam.getCPtr(prm), prm);
        }
    }

    public void onDtmfDigit(OnDtmfDigitParam prm) {
        if (this.getClass() == Call.class) {
            pjsua2JNI.Call_onDtmfDigit(this.swigCPtr, this, OnDtmfDigitParam.getCPtr(prm), prm);
        } else {
            pjsua2JNI.Call_onDtmfDigitSwigExplicitCall(this.swigCPtr, this, OnDtmfDigitParam.getCPtr(prm), prm);
        }
    }

    public void onCallTransferRequest(OnCallTransferRequestParam prm) {
        if (this.getClass() == Call.class) {
            pjsua2JNI.Call_onCallTransferRequest(this.swigCPtr, this, OnCallTransferRequestParam.getCPtr(prm), prm);
        } else {
            pjsua2JNI.Call_onCallTransferRequestSwigExplicitCall(this.swigCPtr, this, OnCallTransferRequestParam.getCPtr(prm), prm);
        }
    }

    public void onCallTransferStatus(OnCallTransferStatusParam prm) {
        if (this.getClass() == Call.class) {
            pjsua2JNI.Call_onCallTransferStatus(this.swigCPtr, this, OnCallTransferStatusParam.getCPtr(prm), prm);
        } else {
            pjsua2JNI.Call_onCallTransferStatusSwigExplicitCall(this.swigCPtr, this, OnCallTransferStatusParam.getCPtr(prm), prm);
        }
    }

    public void onCallReplaceRequest(OnCallReplaceRequestParam prm) {
        if (this.getClass() == Call.class) {
            pjsua2JNI.Call_onCallReplaceRequest(this.swigCPtr, this, OnCallReplaceRequestParam.getCPtr(prm), prm);
        } else {
            pjsua2JNI.Call_onCallReplaceRequestSwigExplicitCall(this.swigCPtr, this, OnCallReplaceRequestParam.getCPtr(prm), prm);
        }
    }

    public void onCallReplaced(OnCallReplacedParam prm) {
        if (this.getClass() == Call.class) {
            pjsua2JNI.Call_onCallReplaced(this.swigCPtr, this, OnCallReplacedParam.getCPtr(prm), prm);
        } else {
            pjsua2JNI.Call_onCallReplacedSwigExplicitCall(this.swigCPtr, this, OnCallReplacedParam.getCPtr(prm), prm);
        }
    }

    public void onCallRxOffer(OnCallRxOfferParam prm) {
        if (this.getClass() == Call.class) {
            pjsua2JNI.Call_onCallRxOffer(this.swigCPtr, this, OnCallRxOfferParam.getCPtr(prm), prm);
        } else {
            pjsua2JNI.Call_onCallRxOfferSwigExplicitCall(this.swigCPtr, this, OnCallRxOfferParam.getCPtr(prm), prm);
        }
    }

    public void onCallAnswered(OnCallAnsweredParam prm) {
        if (this.getClass() == Call.class) {
            pjsua2JNI.Call_onCallAnswered(this.swigCPtr, this, OnCallAnsweredParam.getCPtr(prm), prm);
        } else {
            pjsua2JNI.Call_onCallAnsweredSwigExplicitCall(this.swigCPtr, this, OnCallAnsweredParam.getCPtr(prm), prm);
        }
    }

    public void onCallRxReinvite(OnCallRxReinviteParam prm) {
        if (this.getClass() == Call.class) {
            pjsua2JNI.Call_onCallRxReinvite(this.swigCPtr, this, OnCallRxReinviteParam.getCPtr(prm), prm);
        } else {
            pjsua2JNI.Call_onCallRxReinviteSwigExplicitCall(this.swigCPtr, this, OnCallRxReinviteParam.getCPtr(prm), prm);
        }
    }

    public void onCallTxOffer(OnCallTxOfferParam prm) {
        if (this.getClass() == Call.class) {
            pjsua2JNI.Call_onCallTxOffer(this.swigCPtr, this, OnCallTxOfferParam.getCPtr(prm), prm);
        } else {
            pjsua2JNI.Call_onCallTxOfferSwigExplicitCall(this.swigCPtr, this, OnCallTxOfferParam.getCPtr(prm), prm);
        }
    }

    public void onInstantMessage(OnInstantMessageParam prm) {
        if (this.getClass() == Call.class) {
            pjsua2JNI.Call_onInstantMessage(this.swigCPtr, this, OnInstantMessageParam.getCPtr(prm), prm);
        } else {
            pjsua2JNI.Call_onInstantMessageSwigExplicitCall(this.swigCPtr, this, OnInstantMessageParam.getCPtr(prm), prm);
        }
    }

    public void onInstantMessageStatus(OnInstantMessageStatusParam prm) {
        if (this.getClass() == Call.class) {
            pjsua2JNI.Call_onInstantMessageStatus(this.swigCPtr, this, OnInstantMessageStatusParam.getCPtr(prm), prm);
        } else {
            pjsua2JNI.Call_onInstantMessageStatusSwigExplicitCall(this.swigCPtr, this, OnInstantMessageStatusParam.getCPtr(prm), prm);
        }
    }

    public void onTypingIndication(OnTypingIndicationParam prm) {
        if (this.getClass() == Call.class) {
            pjsua2JNI.Call_onTypingIndication(this.swigCPtr, this, OnTypingIndicationParam.getCPtr(prm), prm);
        } else {
            pjsua2JNI.Call_onTypingIndicationSwigExplicitCall(this.swigCPtr, this, OnTypingIndicationParam.getCPtr(prm), prm);
        }
    }

    public int onCallRedirected(OnCallRedirectedParam prm) {
        return this.getClass() == Call.class ? pjsua2JNI.Call_onCallRedirected(this.swigCPtr, this, OnCallRedirectedParam.getCPtr(prm), prm) : pjsua2JNI.Call_onCallRedirectedSwigExplicitCall(this.swigCPtr, this, OnCallRedirectedParam.getCPtr(prm), prm);
    }

    public void onCallMediaTransportState(OnCallMediaTransportStateParam prm) {
        if (this.getClass() == Call.class) {
            pjsua2JNI.Call_onCallMediaTransportState(this.swigCPtr, this, OnCallMediaTransportStateParam.getCPtr(prm), prm);
        } else {
            pjsua2JNI.Call_onCallMediaTransportStateSwigExplicitCall(this.swigCPtr, this, OnCallMediaTransportStateParam.getCPtr(prm), prm);
        }
    }

    public void onCallMediaEvent(OnCallMediaEventParam prm) {
        if (this.getClass() == Call.class) {
            pjsua2JNI.Call_onCallMediaEvent(this.swigCPtr, this, OnCallMediaEventParam.getCPtr(prm), prm);
        } else {
            pjsua2JNI.Call_onCallMediaEventSwigExplicitCall(this.swigCPtr, this, OnCallMediaEventParam.getCPtr(prm), prm);
        }
    }

    public void onCreateMediaTransport(OnCreateMediaTransportParam prm) {
        if (this.getClass() == Call.class) {
            pjsua2JNI.Call_onCreateMediaTransport(this.swigCPtr, this, OnCreateMediaTransportParam.getCPtr(prm), prm);
        } else {
            pjsua2JNI.Call_onCreateMediaTransportSwigExplicitCall(this.swigCPtr, this, OnCreateMediaTransportParam.getCPtr(prm), prm);
        }
    }

    public void onCreateMediaTransportSrtp(OnCreateMediaTransportSrtpParam prm) {
        if (this.getClass() == Call.class) {
            pjsua2JNI.Call_onCreateMediaTransportSrtp(this.swigCPtr, this, OnCreateMediaTransportSrtpParam.getCPtr(prm), prm);
        } else {
            pjsua2JNI.Call_onCreateMediaTransportSrtpSwigExplicitCall(this.swigCPtr, this, OnCreateMediaTransportSrtpParam.getCPtr(prm), prm);
        }
    }
}

