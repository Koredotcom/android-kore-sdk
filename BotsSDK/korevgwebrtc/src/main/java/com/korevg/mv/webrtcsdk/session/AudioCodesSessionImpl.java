/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.app.Activity
 *  org.webrtc.SurfaceViewRenderer
 */
package com.korevg.mv.webrtcsdk.session;

import android.app.Activity;
import com.korevg.mv.webrtcsdk.log.Log;
import com.korevg.mv.webrtcsdk.session.ACCall;
import com.korevg.mv.webrtcsdk.session.ACCallStatistics;
import com.korevg.mv.webrtcsdk.session.AudioCodesSession;
import com.korevg.mv.webrtcsdk.session.AudioCodesSessionEventListener;
import com.korevg.mv.webrtcsdk.session.CallState;
import com.korevg.mv.webrtcsdk.session.CallTermination;
import com.korevg.mv.webrtcsdk.session.CallTransferState;
import com.korevg.mv.webrtcsdk.session.DTMF;
import com.korevg.mv.webrtcsdk.session.NotifyEvent;
import com.korevg.mv.webrtcsdk.session.RemoteContact;
import com.korevg.mv.webrtcsdk.session.TerminationInfo;
import com.korevg.mv.webrtcsdk.sip.ACSipCall;
import com.korevg.mv.webrtcsdk.sip.listeners.SessionEventListener;
import com.korevg.mv.webrtcsdk.useragent.AudioCodesUA;
import com.korevg.mv.webrtcsdk.useragent.WebRTCException;
import com.korevg.mv.webrtcsdk.webrtc.listeners.WebRTCCallEventListener;
import java.util.ArrayList;
import java.util.Hashtable;
import org.webrtc.SurfaceViewRenderer;

public final class AudioCodesSessionImpl
implements AudioCodesSession {
    private static final String TAG = "AudioCodesSessionImpl";
    private int sessionID;
    private Object userData = null;
    private AudioCodesSessionImpl instance = null;
    private CallSessionEventListener callSessionEventListener = new CallSessionEventListener();
    private WebRTCEventListener webRTCEventListener = new WebRTCEventListener();
    private ArrayList<AudioCodesSessionEventListener> sessionEventListenerList = new ArrayList();
    private ACCall acCall;

    @Override
    public void addSessionEventListener(AudioCodesSessionEventListener sessionEventListener) {
        if (!this.sessionEventListenerList.contains(sessionEventListener)) {
            this.sessionEventListenerList.add(sessionEventListener);
        }
    }

    @Override
    public void removeSessionEventListener(AudioCodesSessionEventListener sessionEventListener) {
        this.sessionEventListenerList.remove(sessionEventListener);
    }

    @Override
    public CallState getCallState() {
        return this.acCall.getCallState();
    }

    @Override
    public CallTermination getTermination() {
        return this.acCall.getTermination();
    }

    public ACCall getAcCall() {
        return this.acCall;
    }

    @Override
    public int getSessionID() {
        return this.sessionID;
    }

    public void setSessionID(int sessionID) {
        this.sessionID = sessionID;
    }

    @Override
    public void setLocaLRenderPosition(int xPercentage, int yPercentage) {
        this.acCall.setLocaLRenderPosition(xPercentage, yPercentage);
    }

    private AudioCodesSessionImpl() {
        this.instance = this;
    }

    public static synchronized AudioCodesSessionImpl createIncomingCall(ACSipCall call, boolean hasVideo) {
        AudioCodesSessionImpl session = new AudioCodesSessionImpl();
        ACCall acCall = new ACCall(session.callSessionEventListener, session.webRTCEventListener, call, null);
        acCall.setWithVideo(hasVideo);
        acCall.prepareIncomingCall();
        session.setCallObject(acCall);
        return session;
    }

    public static synchronized AudioCodesSessionImpl call(RemoteContact callTo, boolean withVideo, Hashtable<String, String> inviteHeaders) throws WebRTCException {
        AudioCodesSessionImpl session = new AudioCodesSessionImpl();
        ACCall acCall = new ACCall(session.callSessionEventListener, session.webRTCEventListener, inviteHeaders);
        session.setCallObject(acCall);
        acCall.setWithVideo(withVideo);
        acCall.call(callTo);
        return session;
    }

    private void setCallObject(ACCall acCall) {
        this.acCall = acCall;
    }

    @Override
    public void answer(Hashtable<String, String> inviteHeaders, boolean withVideo) {
        AudioCodesUA.getInstance().answeringCall(this);
        this.acCall.answer(inviteHeaders, withVideo);
    }

    @Override
    public void reject(Hashtable<String, String> inviteHeaders) {
        this.acCall.reject(inviteHeaders);
    }

    @Override
    public void redirect(RemoteContact contact, Hashtable<String, String> inviteHeaders) {
        this.acCall.redirect(contact, inviteHeaders);
    }

    @Override
    public void terminate() {
        this.acCall.terminateCall();
    }

    @Override
    public void muteAudio(boolean mute) {
        this.acCall.muteAudio(mute);
    }

    @Override
    public void muteVideo(boolean mute) {
        if (!this.hasVideo()) {
            return;
        }
        this.acCall.muteVideo(mute);
    }

    private void addToConference(AudioCodesSessionImpl session) throws WebRTCException {
        if (session.getCallState() != CallState.HOLD || !session.isLocalHold()) {
            throw new WebRTCException(WebRTCException.WebRTCExceptionCode.CONFERENCE_2ND_SESSION_NOT_ON_HOLD);
        }
        if (this.getCallState() != CallState.CONNECTED) {
            throw new WebRTCException(WebRTCException.WebRTCExceptionCode.CONFERENCE_1ST_SESSION_NOT_ACTIVE);
        }
        this.acCall.addToConference(session.acCall);
    }

    @Override
    public boolean isAudioMuted() {
        return this.acCall.isAudioEnabled();
    }

    @Override
    public boolean isVideoMuted() {
        return this.acCall.isVideoEnabled();
    }

    @Override
    public void sendDTMF(DTMF dtmf) {
        this.acCall.sendDTMF(dtmf);
    }

    @Override
    public void sendInfo(String info) {
        this.acCall.sendInfo(info);
    }

    @Override
    public boolean isOutgoing() {
        return this.acCall.isOutgoing();
    }

    @Override
    public boolean hasVideo() {
        return this.acCall.hasVideo();
    }

    @Override
    public RemoteContact getRemoteNumber() {
        return this.acCall.getRemoteNumber();
    }

    @Override
    public void setWithVideo(boolean withVideo) {
        this.acCall.setWithVideo(withVideo);
    }

    @Override
    public void showVideo(Activity activity) throws WebRTCException {
        this.acCall.showVideo(activity);
    }

    @Override
    public void showVideo(SurfaceViewRenderer localView, SurfaceViewRenderer remoteView) throws WebRTCException {
        this.acCall.showVideo(localView, remoteView);
    }

    @Override
    public void switchCamera() {
        this.acCall.switchCamera();
    }

    @Override
    public boolean hold(boolean hold) {
        return this.acCall.hold(hold);
    }

    @Override
    public void reinviteWithVideo() {
        this.acCall.reinviteWithVideo();
    }

    @Override
    public void stopVideo() {
        this.acCall.stopVideo();
    }

    @Override
    public boolean isLocalHold() {
        return this.acCall.isLocalHold();
    }

    @Override
    public boolean isDelayedOffer() {
        return this.acCall.isDelayedOffer();
    }

    @Override
    public boolean isRemoteHold() {
        return this.acCall.isRemoteHold();
    }

    @Override
    public ACCallStatistics getStats() {
        return this.acCall.getStats();
    }

    public void handleNetworkChange() {
        this.acCall.handleNetworkChange();
    }

    @Override
    public int duration() {
        return this.acCall.getCallDuration();
    }

    @Override
    public long getCallStartTime() {
        return this.acCall.getCallStartTime();
    }

    @Override
    public void setUserData(Object data) {
        this.userData = data;
    }

    @Override
    public Object getUserData() {
        return this.userData;
    }

    public void sendRinging() {
        this.acCall.sendRinging();
    }

    @Override
    public RemoteContact getRedirectContact() {
        return this.acCall.getRedirectContact();
    }

    @Override
    public boolean transferCall(RemoteContact transferTo) {
        if (this.acCall.getCallState() != CallState.CONNECTED) {
            Log.d(TAG, "Trying to call transfer in wrong call state: " + (Object)((Object)this.acCall.getCallState()));
            return false;
        }
        this.acCall.transferCall(transferTo);
        return true;
    }

    @Override
    public boolean transferCall(AudioCodesSession transferToSession) {
        if (this.acCall.getCallState() != CallState.CONNECTED && this.acCall.getCallState() != CallState.HOLD) {
            Log.d(TAG, "Trying to call transfer in wrong call state: " + (Object)((Object)this.acCall.getCallState()));
            return false;
        }
        if (this.acCall.getCallState() != CallState.HOLD) {
            Log.d(TAG, "First session should be in hold state");
            return false;
        }
        if (transferToSession.getCallState() != CallState.CONNECTED && transferToSession.getCallState() != CallState.HOLD) {
            Log.d(TAG, "Trying to call transfer when transferToSession is in wrong call state: " + (Object)((Object)transferToSession.getCallState()));
            return false;
        }
        this.acCall.transferCall(((AudioCodesSessionImpl)transferToSession).getACCall());
        return true;
    }

    @Override
    public RemoteContact getTransferContact() {
        return this.acCall.getTransferContact();
    }

    @Override
    public CallTransferState getTransferState() {
        return this.acCall.getTransferState();
    }

    protected ACCall getACCall() {
        return this.acCall;
    }

    private class CallSessionEventListener
    implements SessionEventListener {
        private CallSessionEventListener() {
        }

        @Override
        public void callTerminated(ACCall call, TerminationInfo terminationInfo) {
            Log.d(AudioCodesSessionImpl.TAG, "callTerminated");
            String callID = call.getCallID();
            Log.d(AudioCodesSessionImpl.TAG, "callTerminated callID = " + callID);
            Log.d(AudioCodesSessionImpl.TAG, "call transfer state = " + (Object)((Object)call.getDialogTransferState()));
            Log.d(AudioCodesSessionImpl.TAG, "call transfer mode = " + (Object)((Object)call.getDialogTransferMode()));
            if (callID != null) {
                for (AudioCodesSessionEventListener sessionEventListener : AudioCodesSessionImpl.this.sessionEventListenerList) {
                    Log.d(AudioCodesSessionImpl.TAG, "sessionEventListener session termination; " + (Object)((Object)AudioCodesSessionImpl.this.acCall.getTermination()));
                    try {
                        sessionEventListener.callTerminated(AudioCodesSessionImpl.this.instance, terminationInfo);
                    }
                    catch (Throwable e) {
                        Log.d(AudioCodesSessionImpl.TAG, "Error in callback - callTerminated. This method is called from the SDK thread, please make sure not to run UI operations on this tread");
                        e.printStackTrace();
                    }
                }
                Log.d(AudioCodesSessionImpl.TAG, "removing session");
                call.dispose();
                AudioCodesUA.getInstance().removeSession(AudioCodesSessionImpl.this.instance);
                AudioCodesSessionImpl.this.sessionEventListenerList.clear();
                AudioCodesSessionImpl.this.callSessionEventListener = null;
                AudioCodesSessionImpl.this.userData = null;
            }
        }

        @Override
        public void callProgress(ACCall call, CallState callState) {
            Log.d(AudioCodesSessionImpl.TAG, "callProgress: " + (Object)((Object)callState));
            for (AudioCodesSessionEventListener sessionEventListener : AudioCodesSessionImpl.this.sessionEventListenerList) {
                Log.d(AudioCodesSessionImpl.TAG, "sessionEventListener session callstate; " + (Object)((Object)callState));
                try {
                    sessionEventListener.callProgress(AudioCodesSessionImpl.this.instance);
                }
                catch (Throwable e) {
                    Log.d(AudioCodesSessionImpl.TAG, "Error in callback - callProgress. This method is called from the SDK thread, please make sure not to run UI operations on this tread");
                    e.printStackTrace();
                }
            }
        }

        @Override
        public void reinviteWithVideoCallback() {
            Log.d(AudioCodesSessionImpl.TAG, "reinviteWithVideoCallback");
            for (AudioCodesSessionEventListener sessionEventListener : AudioCodesSessionImpl.this.sessionEventListenerList) {
                Log.d(AudioCodesSessionImpl.TAG, "sessionEventListener reinviteWithVideoCallback");
                try {
                    sessionEventListener.reinviteWithVideoCallback(AudioCodesSessionImpl.this.instance);
                }
                catch (Throwable e) {
                    Log.d(AudioCodesSessionImpl.TAG, "Error in callback - reinviteWithVideoCallback. This method is called from the SDK thread, please make sure not to run UI operations on this tread");
                    e.printStackTrace();
                }
            }
        }

        @Override
        public void incomingNotify(NotifyEvent notifyEvent, String value) {
            Log.d(AudioCodesSessionImpl.TAG, "incomingNotify");
            for (AudioCodesSessionEventListener sessionEventListener : AudioCodesSessionImpl.this.sessionEventListenerList) {
                Log.d(AudioCodesSessionImpl.TAG, "sessionEventListener incomingNotify");
                try {
                    Log.d(AudioCodesSessionImpl.TAG, "incomingNotify: " + (Object)((Object)notifyEvent));
                    Log.d(AudioCodesSessionImpl.TAG, "incomingNotify value: " + value);
                    sessionEventListener.incomingNotify(notifyEvent, value);
                }
                catch (Throwable e) {
                    Log.d(AudioCodesSessionImpl.TAG, "Error in callback - incomingNotify. This method is called from the SDK thread, please make sure not to run UI operations on this tread");
                    e.printStackTrace();
                }
            }
        }
    }

    private class WebRTCEventListener
    implements WebRTCCallEventListener {
        private WebRTCEventListener() {
        }

        @Override
        public void cameraSwitched(boolean frontCamera) {
            for (AudioCodesSessionEventListener sessionEventListener : AudioCodesSessionImpl.this.sessionEventListenerList) {
                Log.d(AudioCodesSessionImpl.TAG, "WebRTCEventListener frontCamera: " + frontCamera);
                try {
                    sessionEventListener.cameraSwitched(frontCamera);
                }
                catch (Throwable e) {
                    Log.d(AudioCodesSessionImpl.TAG, "Error in callback - cameraSwitched. This method is called from the SDK thread, please make sure not to run UI operations on this tread");
                    e.printStackTrace();
                }
            }
        }

        @Override
        public void onIceGatheringComlete() {
            AudioCodesSessionImpl.this.acCall.onIceGatheringComplete();
        }

        @Override
        public void callFailed(int dialogId) {
            AudioCodesSessionImpl.this.acCall.terminateCall(dialogId, CallTermination.TERMINATED_MEDIA_FAILED);
        }

        @Override
        public void mediaFailed() {
            for (AudioCodesSessionEventListener sessionEventListener : AudioCodesSessionImpl.this.sessionEventListenerList) {
                Log.d(AudioCodesSessionImpl.TAG, "WebRTCEventListener mediaFailed ");
                try {
                    AudioCodesSessionImpl session = AudioCodesSessionImpl.this.instance;
                    sessionEventListener.mediaFailed(session);
                }
                catch (Throwable e) {
                    Log.d(AudioCodesSessionImpl.TAG, "Error in callback - mediaFailed. This method is called from the SDK thread, please make sure not to run UI operations on this tread");
                    e.printStackTrace();
                }
            }
        }
    }
}

