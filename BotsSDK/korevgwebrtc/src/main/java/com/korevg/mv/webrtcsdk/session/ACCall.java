/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.app.Activity
 *  android.os.Handler
 *  org.webrtc.SurfaceViewRenderer
 */
package com.korevg.mv.webrtcsdk.session;

import android.app.Activity;
import android.os.Handler;
import com.korevg.mv.webrtcsdk.audio.WebRTCAudioManager;
import com.korevg.mv.webrtcsdk.log.Log;
import com.korevg.mv.webrtcsdk.session.ACCallStatistics;
import com.korevg.mv.webrtcsdk.session.CallState;
import com.korevg.mv.webrtcsdk.session.CallTermination;
import com.korevg.mv.webrtcsdk.session.CallTransferState;
import com.korevg.mv.webrtcsdk.session.DTMF;
import com.korevg.mv.webrtcsdk.session.NotifyEvent;
import com.korevg.mv.webrtcsdk.session.RemoteContact;
import com.korevg.mv.webrtcsdk.session.TerminationInfo;
import com.korevg.mv.webrtcsdk.sip.ACSipCall;
import com.korevg.mv.webrtcsdk.sip.ACSipManager;
import com.korevg.mv.webrtcsdk.sip.CallDialog;
import com.korevg.mv.webrtcsdk.sip.listeners.IHoldCompleteListener;
import com.korevg.mv.webrtcsdk.sip.listeners.SessionEventListener;
import com.korevg.mv.webrtcsdk.useragent.ACConfiguration;
import com.korevg.mv.webrtcsdk.useragent.WebRTCException;
import com.korevg.mv.webrtcsdk.webrtc.ACWebRTCCall;
import com.korevg.mv.webrtcsdk.webrtc.ACWebRTCManager;
import com.korevg.mv.webrtcsdk.webrtc.SdpUtils;
import com.korevg.mv.webrtcsdk.webrtc.listeners.WebRTCCallEventListener;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import org.webrtc.SurfaceViewRenderer;

public class ACCall {
    private static final String TAG = "ACCall";
    private boolean videoEnabled = true;
    private boolean videoMuted = false;
    private boolean remoteHold;
    private boolean localHold;
    private boolean reinviteWithVideo;
    private boolean isOutgoing = false;
    private boolean isIceComplete = false;
    private boolean isInNetworkchange = false;
    private boolean isDelayedOffer = false;
    private int timeout = ACConfiguration.getConfiguration().getIceCandidateWaitPeriod();
    private ACSipCall sipCall;
    private HashMap<Integer, ACWebRTCCall> webRTCCallHashMap = new HashMap();
    public String remoteSDP;
    public String localSDP;
    private ACCall instance;
    private RequestSDP requestSDPListener = new RequestSDP();
    private CallEventListener callEventListener = new CallEventListener();
    private SessionEventListener sessionEventListener;
    private WebRTCCallEventListener webRTCCallEventListener;
    private CallState callState = null;
    private Hashtable<String, String> inviteHeaders;
    private CallTermination termination = null;
    private Activity videoActivity;
    private Handler videoUiHandler;
    private ACCall conferencePartner = null;
    private IHoldCompleteListener holdCompleteListener;
    private boolean clearingWebRtclist;

    public CallTermination getTermination() {
        return this.termination;
    }

    public void setTermination(CallTermination termination) {
        this.termination = termination;
    }

    public ACCall getConferencePartner() {
        return this.conferencePartner;
    }

    public void setConferencePartner(ACCall conferencePartner) {
        this.conferencePartner = conferencePartner;
    }

    public void setHoldCompleteListener(IHoldCompleteListener holdCompleteListener) {
        this.holdCompleteListener = holdCompleteListener;
    }

    public CallState getCallState() {
        return this.callState;
    }

    public void setCallState(CallState callState) {
        Log.d(TAG, "setCallState: " + (Object)((Object)callState));
        this.callState = callState;
    }

    private ACCall() {
    }

    public ACCall(SessionEventListener listener, WebRTCCallEventListener webRTCCallEventListener, Hashtable<String, String> inviteHeaders) {
        this(listener, webRTCCallEventListener, null, inviteHeaders);
    }

    public ACCall(SessionEventListener listener, WebRTCCallEventListener webRTCCallEventListener, ACSipCall call, Hashtable<String, String> inviteHeaders) {
        this.instance = this;
        this.sessionEventListener = listener;
        this.webRTCCallEventListener = webRTCCallEventListener;
        if (call != null) {
            this.sipCall = call;
            this.sipCall.setInviteHeaders(inviteHeaders);
        }
        this.inviteHeaders = inviteHeaders;
    }

    public void setLocaLRenderPosition(int x, int y) {
        ACWebRTCCall webrtcCall;
        if (this.webRTCCallHashMap.size() <= 0) {
            try {
                Thread.sleep(200L);
            }
            catch (Exception exception) {
                // empty catch block
            }
        }
        if ((webrtcCall = this.getWebRTCCall()) != null) {
            webrtcCall.setLocaLRenderPosition(x, y);
        }
    }

    public void sendDTMF(DTMF dtmf) {
        ACWebRTCCall webrtcCall = this.getWebRTCCall();
        switch (ACConfiguration.getConfiguration().getDtmfOptions().dtmfMethod) {
            case SIP_INFO: {
                this.sipCall.sendDTMF(dtmf);
                break;
            }
            case WEBRTC: {
                webrtcCall.sendDTMF(dtmf);
            }
        }
    }

    public void sendInfo(String info) {
        this.sipCall.sendInfo(info);
    }

    public void switchCamera() {
        ACWebRTCCall webrtcCall = this.getWebRTCCall();
        webrtcCall.switchCamera();
    }

    public void muteAudio(boolean mute) {
        ACWebRTCCall webrtcCall = this.getWebRTCCall();
        webrtcCall.muteAudio(mute);
    }

    public void muteVideo(boolean mute) {
        ACWebRTCCall webrtcCall = this.getWebRTCCall();
        webrtcCall.muteVideo(mute);
    }

    public void stopVideo() {
        ACWebRTCCall webrtcCall = this.getWebRTCCall();
        webrtcCall.stopVideo();
    }

    public boolean isLocalHold() {
        return this.localHold;
    }

    public boolean isDelayedOffer() {
        return this.isDelayedOffer;
    }

    public void addToConference(ACCall acCall) {
        acCall.setHoldCompleteListener(this.holdCompleteListener);
        acCall.setCallState(CallState.CONFERENCE_CONNECTING);
        acCall.setConferencePartner(this);
        this.setConferencePartner(acCall);
        acCall.hold(false);
    }

    public void handleNetworkChange() {
        Log.d(TAG, "Handle network change");
        if (this.webRTCCallHashMap.size() <= 0) {
            return;
        }
        this.isInNetworkchange = true;
        this.localSDP = null;
        boolean hold = this.remoteHold || this.localHold;
        ACWebRTCCall webrtcCall = this.getWebRTCCall();
        webrtcCall.createOfferForNetworkChange(hold);
    }

    public boolean isIceGatheringComplete() {
        return this.isIceComplete;
    }

    public void setIceGatheringComplete(boolean complete) {
        Log.d(TAG, "setIceGatheringComplete: " + complete);
        this.isIceComplete = complete;
    }

    public void onIceGatheringComplete() {
        this.setIceGatheringComplete(true);
        if (this.callState != CallState.CONNECTED || !this.reinviteWithVideo) {
            // empty if block
        }
    }

    public void terminateCall(int dialogId, CallTermination termination) {
        Log.d(TAG, "terminateCall: " + dialogId);
        if (this.sipCall != null) {
            if (this.isInNetworkchange) {
                Log.d(TAG, "Ending of call during network change is not permitted");
                return;
            }
            if (this.sipCall.getActiveDialog().getId() == dialogId) {
                this.sipCall.terminateCall(termination);
            }
        }
        this.clearWebRTCCall(dialogId);
    }

    public void sendRinging() {
        this.sipCall.sendRinging();
    }

    public boolean isAudioEnabled() {
        if (this.webRTCCallHashMap.size() <= 0) {
            Log.d(TAG, "Webrtc call null, meaning before call - audio should be considered enabled");
            return true;
        }
        ACWebRTCCall webrtcCall = this.getWebRTCCall();
        return !webrtcCall.isAudioEnabled();
    }

    public boolean isVideoEnabled() {
        if (this.webRTCCallHashMap.size() <= 0) {
            return this.videoEnabled;
        }
        ACWebRTCCall webrtcCall = this.getWebRTCCall();
        if (webrtcCall != null) {
            return !webrtcCall.isVideoEnabled();
        }
        return false;
    }

    public boolean isRemoteHold() {
        return this.remoteHold;
    }

    public RemoteContact getTransferContact() {
        return this.sipCall.getTransferContact();
    }

    public ACSipCall getTransferCall() {
        return this.sipCall.getTransferCall();
    }

    public CallTransferState getTransferState() {
        if (this.sipCall == null) {
            return CallTransferState.NONE;
        }
        return this.sipCall.getTransferState();
    }

    public CallDialog.TransferState getDialogTransferState() {
        if (this.sipCall == null) {
            return CallDialog.TransferState.NONE;
        }
        return this.sipCall.getDialogTransferState();
    }

    public CallDialog.TransferMode getDialogTransferMode() {
        if (this.sipCall == null) {
            return CallDialog.TransferMode.NONE;
        }
        return this.sipCall.getDialogTransferMode();
    }

    public boolean hasVideo() {
        return this.videoEnabled;
    }

    public RemoteContact getRemoteNumber() {
        if (this.sipCall == null) {
            Log.d(TAG, "Sipcall is null: no remoteNumber");
            return null;
        }
        return this.sipCall.getRemoteNumber();
    }

    public void setRemoteNumber(RemoteContact remoteNumber) {
    }

    public void setWithVideo(boolean withVideo) {
        this.videoEnabled = withVideo;
    }

    public void answer(Hashtable<String, String> inviteHeaders, boolean withVideo) {
        Log.d(TAG, "Answer, withVideo:" + withVideo + " videoEnabled:" + this.videoEnabled);
        ACWebRTCCall webrtcCall = this.getWebRTCCall();
        this.isDelayedOffer = false;
        if (this.sipCall.getRemoteSDP() == null) {
            this.isDelayedOffer = true;
            Log.d(TAG, "Answer, delayed offer");
            this.videoEnabled = true;
            if (webrtcCall == null) {
                webrtcCall = ACWebRTCManager.getInstance().initCall(this.videoEnabled, this.webRTCCallEventListener);
                this.addWebRTCCall(webrtcCall);
            } else if (!this.videoEnabled) {
                webrtcCall.removeVideoTrack();
            }
            webrtcCall.createOffer(false);
        } else {
            Log.d(TAG, "Answer, standard");
            if (webrtcCall == null) {
                webrtcCall = ACWebRTCManager.getInstance().initCall(this.videoEnabled, this.webRTCCallEventListener);
                this.addWebRTCCall(webrtcCall);
            }
            this.videoMuted = !withVideo;
            webrtcCall.createAnswer(this.videoMuted, this.sipCall.getRemoteSDP(), false);
        }
        this.sipCall.setiCallEventListener(this.callEventListener);
        this.sipCall.setRequestSDPListener(this.requestSDPListener);
        this.sipCall.answer(this.videoEnabled);
        this.sipCall.setInviteHeaders(inviteHeaders);
        WebRTCAudioManager.getInstance().startCall();
    }

    public void reject(Hashtable<String, String> inviteHeaders) {
        this.sipCall.setiCallEventListener(this.callEventListener);
        this.sipCall.setInviteHeaders(inviteHeaders);
        this.sipCall.reject();
    }

    public void redirect(RemoteContact contact, Hashtable<String, String> inviteHeaders) {
        this.sipCall.setiCallEventListener(this.callEventListener);
        this.sipCall.setInviteHeaders(inviteHeaders);
        this.sipCall.redirect(contact);
    }

    public void prepareIncomingCall() {
        this.isOutgoing = false;
        this.sipCall.setiCallEventListener(this.callEventListener);
    }

    public void call(RemoteContact remoteNumber) throws WebRTCException {
        Log.d(TAG, "Calling");
        this.isOutgoing = true;
        this.callEventListener.callProgress(CallState.CALLING);
        ACWebRTCCall webrtcCall = ACWebRTCManager.getInstance().initCall(this.videoEnabled, this.webRTCCallEventListener);
        this.addWebRTCCall(webrtcCall);
        webrtcCall.createOffer(false);
        this.sipCall = ACSipManager.getSipManager().call(remoteNumber, this.videoEnabled, this.requestSDPListener, this.callEventListener, this.inviteHeaders);
        WebRTCAudioManager.getInstance().startCall();
    }

    public void showVideo(Activity activity) throws WebRTCException {
        Log.d(TAG, "showVideo Click");
        if (this.sipCall == null) {
            Log.d(TAG, "sipCall not set, not displaying video");
            return;
        }
        if (this.callEventListener == null) {
            Log.d(TAG, "callEventListener not set, not displaying video");
            return;
        }
        ACWebRTCCall webrtcCall = null;
        if (this.sipCall != null) {
            webrtcCall = this.getWebRTCCall();
        }
        if (webrtcCall == null) {
            webrtcCall = ACWebRTCManager.getInstance().initCall(true, this.webRTCCallEventListener);
            this.addWebRTCCall(webrtcCall);
        }
        this.videoUiHandler = new Handler();
        this.videoActivity = activity;
        webrtcCall.showVideo(activity, this.videoUiHandler);
    }

    public void showVideo(SurfaceViewRenderer localView, SurfaceViewRenderer remoteView) throws WebRTCException {
        ACWebRTCCall webrtcCall = null;
        if (this.sipCall != null) {
            webrtcCall = this.getWebRTCCall();
        }
        if (webrtcCall == null) {
            webrtcCall = ACWebRTCManager.getInstance().initCall(true, this.webRTCCallEventListener);
            this.addWebRTCCall(webrtcCall);
        }
        this.videoUiHandler = new Handler();
        webrtcCall.showVideo(localView, remoteView, this.videoUiHandler);
    }

    public String getCallID() {
        if (this.sipCall != null) {
            return this.sipCall.getCallId();
        }
        Log.d(TAG, "sipcall is null");
        return null;
    }

    public void terminateCall() {
        if (this.sipCall != null) {
            this.sipCall.terminateCall();
        }
        this.clearWebRTCCallHashMap();
    }

    public int getCallDuration() {
        return this.sipCall.getCallDuration();
    }

    public long getCallStartTime() {
        return this.sipCall.getCallStartTime();
    }

    public void reinviteWithVideo() {
        ACWebRTCCall webrtcCall = this.getWebRTCCall();
        if (!this.videoEnabled) {
            this.reinviteWithVideo = true;
        }
        this.videoEnabled = true;
        this.localSDP = null;
        webrtcCall.setVideoEnabled(true);
        webrtcCall.setVideoMuted(false);
        this.videoMuted = false;
        this.timeout = 500;
        webrtcCall.createOffer(false);
        if (this.sipCall != null) {
            this.sipCall.hold(false);
        }
    }

    public boolean hold(boolean hold) {
        boolean oldLocalHold = this.localHold;
        this.localHold = hold;
        if (hold && (this.remoteHold || oldLocalHold)) {
            Log.d(TAG, "Call is already on hold, cannot hold call");
            return false;
        }
        if (!hold && this.remoteHold && !oldLocalHold) {
            Log.d(TAG, "Call has been set on hold by remote, cannot unhold call");
            return false;
        }
        ACWebRTCCall webrtcCall = this.getWebRTCCall();
        if (webrtcCall != null) {
            webrtcCall.createOffer(hold);
        }
        if (this.sipCall != null) {
            this.sipCall.hold(hold);
        }
        return true;
    }

    public boolean isOutgoing() {
        return this.isOutgoing;
    }

    public void setOutgoing(boolean outgoing) {
        this.isOutgoing = outgoing;
    }

    public ACCallStatistics getStats() {
        ACWebRTCCall webrtcCall = this.getWebRTCCall();
        if (webrtcCall == null) {
            return null;
        }
        return webrtcCall.getStats();
    }

    public RemoteContact getRedirectContact() {
        return this.sipCall.getRedirectContact();
    }

    public void transferCall(RemoteContact transferTo) {
        this.sipCall.transferCall(transferTo);
    }

    public void transferCall(final ACCall transferToACCall) {
        Log.d(TAG, "transferCall");
        if (!transferToACCall.isLocalHold() && !transferToACCall.isRemoteHold()) {
            Log.d(TAG, "transferCall hold");
            transferToACCall.setHoldCompleteListener(new IHoldCompleteListener(){

                @Override
                public void holdComplete(boolean succes) {
                    Log.d(ACCall.TAG, "holdComplete");
                    ACCall.this.sipCall.transferCall(transferToACCall.sipCall);
                    transferToACCall.setHoldCompleteListener(null);
                }
            });
            transferToACCall.hold(true);
        } else {
            this.sipCall.transferCall(transferToACCall.sipCall);
        }
    }

    private void addWebRTCCall(ACWebRTCCall webrtcCall) {
        int dialogID = -1;
        if (this.sipCall != null) {
            dialogID = this.sipCall.getActiveDialogID();
        }
        Log.d(TAG, "Adding Webrtcall for dialog id:" + dialogID);
        if (this.webRTCCallHashMap.containsKey(dialogID)) {
            Log.d(TAG, "Replacing webrtcall???");
        } else {
            Log.d(TAG, "New webrtcall");
        }
        this.putInWebRTCCallHashMap(dialogID, webrtcCall);
    }

    private void removeWebRTCCall(int dialogID) {
        Log.d(TAG, "Removing webrtcall for dialog id:" + dialogID);
        if (!this.webRTCCallHashMap.containsKey(dialogID)) {
            Log.d(TAG, "Webrtcall does not exist???");
        } else {
            this.removeFromWebRTCCallHashMap(dialogID);
            Log.d(TAG, "Removed webrtcall");
        }
    }

    private ACWebRTCCall getWebRTCCall() {
        if (this.sipCall == null) {
            Log.d(TAG, "Getting Webrtcall: sipcall is null");
            return null;
        }
        return this.getWebRTCCall(this.sipCall.getActiveDialog().getId());
    }

    private ACWebRTCCall getWebRTCCall(int dialogID) {
        Log.d(TAG, "Getting Webrtcall for dialog id:" + dialogID);
        ACWebRTCCall tmpCall = this.getFromWebRTCCallHashMap(dialogID);
        if (tmpCall == null) {
            Log.d(TAG, "Call does not exist for this dialog id");
            tmpCall = this.getFromWebRTCCallHashMap(-1);
            if (tmpCall != null) {
                Log.d(TAG, "Call found on -1");
                this.removeFromWebRTCCallHashMap(-1);
                this.putInWebRTCCallHashMap(dialogID, tmpCall);
            }
        }
        return tmpCall;
    }

    private void putInWebRTCCallHashMap(int dialogID, ACWebRTCCall webRTCCall) {
        while (this.clearingWebRtclist) {
        }
        if (webRTCCall == null) {
            Log.d("WEBRTCDialog", "putInWebRTCCallHashMap webRTCCall null!!!");
        }
        Log.d("WEBRTCDialog", "putInWebRTCCallHashMap: " + dialogID);
        webRTCCall.setDialogId(dialogID);
        this.webRTCCallHashMap.put(dialogID, webRTCCall);
    }

    private void removeFromWebRTCCallHashMap(int dialogID) {
        while (this.clearingWebRtclist) {
        }
        Log.d("WEBRTCDialog", "removeFromWebRTCCallHashMap: " + dialogID);
        ACWebRTCCall webRTCCall = this.webRTCCallHashMap.remove(dialogID);
        if (webRTCCall == null) {
            Log.d("WEBRTCDialog", "removeFromWebRTCCallHashMap webRTCCall null!!!");
        }
    }

    private ACWebRTCCall getFromWebRTCCallHashMap(int dialogID) {
        while (this.clearingWebRtclist) {
        }
        Log.d("WEBRTCDialog", "getFromWebRTCCallHashMap: " + dialogID);
        ACWebRTCCall webRTCCall = this.webRTCCallHashMap.get(dialogID);
        if (webRTCCall == null) {
            Log.d("WEBRTCDialog", "getFromWebRTCCallHashMap webRTCCall null!!!");
        }
        return webRTCCall;
    }

    private void clearWebRTCCall(int dialogID) {
        Log.d("WEBRTCDialog", "clearWebRTCCall: " + dialogID);
        ACWebRTCCall webrtcCall = this.getFromWebRTCCallHashMap(dialogID);
        if (webrtcCall != null) {
            Log.d("WEBRTCDialog", "clearing: " + dialogID);
            webrtcCall.terminateCall();
            this.removeFromWebRTCCallHashMap(dialogID);
        }
    }

    private synchronized void clearWebRTCCallHashMap() {
        while (this.clearingWebRtclist) {
        }
        this.clearingWebRtclist = true;
        Log.d("WEBRTCDialog", "clearWebRTCCallHashMap: size " + this.webRTCCallHashMap.size());
        for (Map.Entry<Integer, ACWebRTCCall> pair : this.webRTCCallHashMap.entrySet()) {
            Log.d("WEBRTCDialog", "Terminating webrtcall for dialogID: " + pair.getKey());
            ACWebRTCCall webrtcCall = pair.getValue();
            webrtcCall.terminateCall();
        }
        this.webRTCCallHashMap.clear();
        this.clearingWebRtclist = false;
    }

    private void reShowVideo(final ACWebRTCCall webrtcCall) {
        Log.d(TAG, "reShowVideo: " + webrtcCall.getDialogID() + ", " + this.videoEnabled);
        if (this.videoEnabled) {
            Log.d(TAG, "videoActivity null: " + (this.videoActivity == null));
            if (this.videoActivity != null) {
                if (this.videoUiHandler == null) {
                    return;
                }
                this.videoUiHandler.post(new Runnable(){

                    @Override
                    public void run() {
                        try {
                            webrtcCall.showVideo(ACCall.this.videoActivity, ACCall.this.videoUiHandler);
                        }
                        catch (WebRTCException e) {
                            Log.d(ACCall.TAG, "showVideo error", e);
                        }
                    }
                });
            }
        }
    }

    public void dispose() {
        Log.d(TAG, "dispose ACCall");
        this.videoUiHandler = null;
        this.videoActivity = null;
    }

    public class RequestSDP
    implements ACSipCall.IRequestSDP {
        @Override
        public String requestLocalSDP(String newRemoteSDP, int dialogID) {
            Log.d(ACCall.TAG, "requestLocalSDP: " + dialogID);
            ACWebRTCCall webrtcCall = ACCall.this.getWebRTCCall(dialogID);
            if (webrtcCall == null) {
                Log.d(ACCall.TAG, "requestLocalSDP: webrtcCall is null! call might be terminated");
                return null;
            }
            ACCall.this.remoteSDP = newRemoteSDP;
            ACCall.this.setIceGatheringComplete(false);
            Log.d(ACCall.TAG, "icegathering set");
            if (ACCall.this.sipCall == null) {
                Log.d(ACCall.TAG, "Sipcall is null, probably before makecall");
            } else {
                Log.d(ACCall.TAG, "SDP getCallState: " + (Object)((Object)ACCall.this.sipCall.getCallState()));
                Log.d(ACCall.TAG, "SDP getDialogTransferMode: " + (Object)((Object)ACCall.this.sipCall.getDialogTransferMode()));
            }
            Log.d(ACCall.TAG, "getIceCandidateWaitPeriod: " + ACCall.this.timeout);
            int stepTime = 100;
            for (int i = 0; i < ACCall.this.timeout; i += stepTime) {
                try {
                    Thread.sleep(stepTime);
                }
                catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (ACCall.this.isIceGatheringComplete()) break;
            }
            Log.d(ACCall.TAG, "end wait period isIceGatheringComplete: " + ACCall.this.isIceGatheringComplete());
            ACCall.this.localSDP = webrtcCall.getLocalSDP();
            if (ACCall.this.localSDP == null) {
                Log.d(ACCall.TAG, "get created local SDP");
                ACCall.this.localSDP = webrtcCall.getCreatedLocalSDP();
            }
            if (ACCall.this.localSDP.length() > 4000) {
                Log.d(ACCall.TAG, "local sdp 1: " + ACCall.this.localSDP.substring(0, 4000));
                Log.d(ACCall.TAG, "local sdp 2: " + ACCall.this.localSDP.substring(4000, ACCall.this.localSDP.length()));
            } else {
                Log.d(ACCall.TAG, "localSDP: " + ACCall.this.localSDP);
            }
            return ACCall.this.localSDP;
        }

        @Override
        public void incomingReinvite(String remoteSDP) {
            ACWebRTCCall webrtcCall = ACCall.this.getWebRTCCall();
            if (webrtcCall == null) {
                Log.d(ACCall.TAG, "Webrtcall null: call might have been terminated");
                return;
            }
            boolean hold = this.checkForHold(remoteSDP);
            ACCall.this.timeout = 500;
            boolean newVideoEnabled = SdpUtils.sdpHasVideo(remoteSDP);
            Log.d(ACCall.TAG, "newVideoEnabled: " + newVideoEnabled);
            Log.d(ACCall.TAG, "videoEnabled: " + ACCall.this.videoEnabled);
            if (!ACCall.this.videoEnabled && newVideoEnabled) {
                ACCall.this.videoEnabled = true;
                ACCall.this.videoMuted = true;
                webrtcCall.setVideoEnabled(true);
                webrtcCall.setVideoMuted(true);
                ACCall.this.callEventListener.reinviteWithVideoCallback();
            }
            ACCall.this.localSDP = null;
            webrtcCall.createAnswer(ACCall.this.videoMuted, remoteSDP, hold);
        }

        private boolean checkForHold(String remoteSDP) {
            boolean hold;
            ACCall.this.remoteHold = SdpUtils.sdpIsHold(remoteSDP);
            boolean bl = hold = ACCall.this.remoteHold || ACCall.this.localHold;
            if (hold) {
                ACCall.this.callEventListener.callProgress(CallState.HOLD);
                ACCall.this.sipCall.checkForTransfer();
            } else {
                ACCall.this.callEventListener.callProgress(CallState.CONNECTED);
            }
            return hold;
        }

        @Override
        public void onCallAnswered(String remoteSDP) {
            Log.d(ACCall.TAG, "onCallAnswered");
            this.checkForHold(remoteSDP);
            ACWebRTCCall webrtcCall = ACCall.this.getWebRTCCall();
            if (webrtcCall == null) {
                Log.d(ACCall.TAG, "webrtcCall is null, probably terminated, not handlinh this any further");
                return;
            }
            webrtcCall.setRemoteSDPOnAnswer(remoteSDP);
            if (ACCall.this.videoEnabled) {
                ACCall.this.reShowVideo(webrtcCall);
            }
        }

        @Override
        public void onCallReplaced(String remoteSDP, int newDialogID) {
            boolean hold = this.checkForHold(remoteSDP);
            Log.d(ACCall.TAG, "onCallReplaced: hold " + hold);
            ACCall.this.timeout = 500;
            boolean newVideoEnabled = SdpUtils.sdpHasVideo(remoteSDP);
            Log.d(ACCall.TAG, "newVideoEnabled: " + newVideoEnabled);
            Log.d(ACCall.TAG, "videoEnabled: " + ACCall.this.videoEnabled);
            ACWebRTCCall webrtcCall = ACWebRTCManager.getInstance().initCall(ACCall.this.videoEnabled, ACCall.this.webRTCCallEventListener);
            ACCall.this.putInWebRTCCallHashMap(newDialogID, webrtcCall);
            if (!ACCall.this.videoEnabled && newVideoEnabled) {
                ACCall.this.videoEnabled = true;
                ACCall.this.videoMuted = true;
                webrtcCall.setVideoEnabled(true);
                webrtcCall.setVideoMuted(true);
                ACCall.this.callEventListener.reinviteWithVideoCallback();
            }
            ACCall.this.localSDP = null;
            webrtcCall.createAnswer(ACCall.this.videoMuted, remoteSDP, hold);
            if (ACCall.this.videoEnabled) {
                Log.d(ACCall.TAG, "onCallReplaced: show video");
                ACCall.this.reShowVideo(webrtcCall);
            }
        }

        @Override
        public void onTransferHold(boolean hold) {
            ACCall.this.hold(hold);
        }
    }

    public class CallEventListener
    implements ACSipCall.ICallEventListener {
        @Override
        public void callTerminated(TerminationInfo terminationInfo) {
            Log.d(ACCall.TAG, "callTerminated: " + (Object)((Object)terminationInfo.callTermination));
            Log.d(ACCall.TAG, "terminateCall webrtc");
            ACCall.this.setTermination(terminationInfo.callTermination);
            WebRTCAudioManager.getInstance().stopCall();
            ACCall.this.clearWebRTCCallHashMap();
            if (ACCall.this.sessionEventListener != null) {
                Log.d(ACCall.TAG, "Calling session event listener");
                ACCall.this.sessionEventListener.callTerminated(ACCall.this.instance, terminationInfo);
            } else {
                Log.d(ACCall.TAG, "No session event listener has been set");
            }
            ACCall.this.clearWebRTCCallHashMap();
            ACCall.this.sipCall = null;
        }

        @Override
        public void callProgress(CallState callState) {
            Log.d(ACCall.TAG, "callProgress: " + (Object)((Object)callState));
            if (ACCall.this.holdCompleteListener != null && callState == CallState.HOLD) {
                ACCall.this.holdCompleteListener.holdComplete(true);
            }
            if (ACCall.this.reinviteWithVideo && callState == CallState.CONNECTED) {
                ACCall.this.videoEnabled = true;
                ACCall.this.sessionEventListener.reinviteWithVideoCallback();
                ACCall.this.reinviteWithVideo = false;
            }
            if (callState == CallState.CONNECTED) {
                ACCall.this.isInNetworkchange = false;
            }
            ACCall.this.setCallState(callState);
            if (ACCall.this.sessionEventListener != null) {
                ACCall.this.sessionEventListener.callProgress(ACCall.this.instance, callState);
            }
        }

        @Override
        public void reinviteWithVideoCallback() {
            Log.d(ACCall.TAG, "reinviteWithVideoCallback");
            if (ACCall.this.sessionEventListener != null) {
                ACCall.this.sessionEventListener.reinviteWithVideoCallback();
            }
        }

        @Override
        public void incomingTransfer() {
            Log.d(ACCall.TAG, "incomingTransfer");
            ACCall.this.remoteHold = false;
            ACCall.this.timeout = 500;
            ACWebRTCCall webrtcCall = ACWebRTCManager.getInstance().initCall(ACCall.this.videoEnabled, ACCall.this.webRTCCallEventListener);
            ACCall.this.reShowVideo(webrtcCall);
            webrtcCall.createOffer(false);
            ACCall.this.putInWebRTCCallHashMap(-1, webrtcCall);
        }

        @Override
        public void transferDialogTerminated(int dialogId, int newDIalogID) {
            ACWebRTCCall webrtc;
            Log.d(ACCall.TAG, "transferDialogTerminated: " + dialogId);
            Log.d(ACCall.TAG, "newDIalogID: " + newDIalogID);
            Log.d(ACCall.TAG, "webRTCCallHashMap size: " + ACCall.this.webRTCCallHashMap.size());
            ACWebRTCCall webrtcCall = ACCall.this.getFromWebRTCCallHashMap(dialogId);
            if (webrtcCall != null) {
                Log.d(ACCall.TAG, "removing webrtc call");
                webrtcCall.terminateCall();
                ACCall.this.removeWebRTCCall(dialogId);
            }
            if ((webrtc = ACCall.this.getFromWebRTCCallHashMap(newDIalogID)) != null) {
                ACCall.this.reShowVideo(webrtc);
            }
        }

        @Override
        public void incomingNotify(NotifyEvent notifyEvent, String dtmfValue) {
            Log.d(ACCall.TAG, "incomingNotify");
            if (ACCall.this.sessionEventListener != null) {
                ACCall.this.sessionEventListener.incomingNotify(notifyEvent, dtmfValue);
            }
        }
    }

    public class HoldCompleteListener
    implements IHoldCompleteListener {
        @Override
        public void holdComplete(boolean succes) {
            Log.d(ACCall.TAG, "Hold completed");
        }
    }
}

