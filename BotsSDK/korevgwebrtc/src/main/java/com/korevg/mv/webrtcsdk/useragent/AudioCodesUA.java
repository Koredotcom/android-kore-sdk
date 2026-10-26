/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  org.webrtc.PeerConnection$IceServer
 */
package com.korevg.mv.webrtcsdk.useragent;

import android.content.Context;
import com.korevg.mv.webrtcsdk.audio.WebRTCAudioManager;
import com.korevg.mv.webrtcsdk.im.InstanceMessageStatus;
import com.korevg.mv.webrtcsdk.im.InstantMessageCallback;
import com.korevg.mv.webrtcsdk.log.ILog;
import com.korevg.mv.webrtcsdk.log.Log;
import com.korevg.mv.webrtcsdk.log.LogLevel;
import com.korevg.mv.webrtcsdk.session.AudioCodesSession;
import com.korevg.mv.webrtcsdk.session.AudioCodesSessionImpl;
import com.korevg.mv.webrtcsdk.session.InfoAlert;
import com.korevg.mv.webrtcsdk.session.RemoteContact;
import com.korevg.mv.webrtcsdk.sip.ACSipCall;
import com.korevg.mv.webrtcsdk.sip.ACSipManager;
import com.korevg.mv.webrtcsdk.sip.enums.Transport;
import com.korevg.mv.webrtcsdk.sip.listeners.SipEventListener;
import com.korevg.mv.webrtcsdk.sip.pjsip.ACPjSipManager;
import com.korevg.mv.webrtcsdk.useragent.ACConfiguration;
import com.korevg.mv.webrtcsdk.useragent.AudioCodesEventListener;
import com.korevg.mv.webrtcsdk.useragent.Storage;
import com.korevg.mv.webrtcsdk.useragent.WebRTCException;
import com.korevg.mv.webrtcsdk.webrtc.ACWebRTCCall;
import com.korevg.mv.webrtcsdk.webrtc.ACWebRTCManager;
import com.korevg.mv.webrtcsdk.webrtc.SdpUtils;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import org.webrtc.PeerConnection;

public final class AudioCodesUA {
    private static final String TAG = "AudioCodesUA";
    private static final int MAX_SESSIONS = 2;
    private ACSipManager acSipManager;
    private ACWebRTCManager aCWebRTCManager;
    private Hashtable<String, String> inviteHeaders;
    private Hashtable<String, String> registerHeaders;
    private ArrayList<AudioCodesSession> sessionList = new ArrayList();
    private UASipEventListener uaSipEventListener = new UASipEventListener();
    private boolean loginState = false;
    private AudioCodesEventListener eventListener;
    private boolean oauthEnabled = false;
    private boolean autoLogin = false;
    private String accessToken;
    private boolean sdkLoggedIn = false;
    private Context context;
    private static AudioCodesUA instance;
    private InstantMessageCallback instantMessageCallback = new InstantMessageCallback(){

        @Override
        public void onInstantMessage(RemoteContact remoteContact, String message) {
            AudioCodesUA.this.eventListener.onIncomingInstantMessage(remoteContact, message);
        }

        @Override
        public void onInstantMessageStatus(int code, long messageID) {
            InstanceMessageStatus status;
            switch (code) {
                case 200: {
                    status = InstanceMessageStatus.SUCCESS;
                    break;
                }
                case 202: {
                    status = InstanceMessageStatus.ACCEPTED;
                    break;
                }
                case 404: {
                    status = InstanceMessageStatus.NOT_FOUND;
                    break;
                }
                default: {
                    status = InstanceMessageStatus.UNKNOWN_ERROR;
                }
            }
            AudioCodesUA.this.eventListener.onInstantMessageStatus(status, messageID);
        }
    };

    private AudioCodesUA() {
        this.acSipManager = ACSipManager.getSipManager();
        this.aCWebRTCManager = ACWebRTCManager.getInstance();
        Log.d(TAG, "AudioCodesUA created");
    }

    public static synchronized AudioCodesUA getInstance() {
        if (instance == null) {
            instance = new AudioCodesUA();
        }
        return instance;
    }

    public void setServerConfig(String proxyAddress, int port, String domain, Transport transport, List<PeerConnection.IceServer> iceServerList) {
        this.acSipManager.setServerConfig(proxyAddress, port, domain, transport);
        this.aCWebRTCManager.setIceServerList(iceServerList);
    }

    public void setAccount(String username, String displayname, String password) {
        this.setAccount(username, displayname, password, username);
    }

    public void setAccount(String username, String displayname, String password, String authorizationName) {
        this.acSipManager.setAccount(username, displayname, password, authorizationName);
    }

    public void setLogLevel(LogLevel logLevel) {
        Log.setLogLevel(logLevel);
        this.acSipManager.setLogLevel(logLevel);
        this.aCWebRTCManager.setLogLevel(logLevel);
    }

    public void setLogger(ILog log) {
        Log.setLogger(log);
    }

    public void login(Context context) {
        this.login(context, true);
    }

    public void login(Context context, boolean autoLogin) {
        Log.d(TAG, "login");
        this.context = context;
        this.autoLogin = autoLogin;
        ACConfiguration.getConfiguration().init(context);
        this.acSipManager.setSipEventListener(this.uaSipEventListener);
        this.acSipManager.setInstantMessageCallback(this.instantMessageCallback);
        Log.d(TAG, "init sip stack");
        this.acSipManager = ACSipManager.getSipManager();
        this.acSipManager.init(autoLogin);
        Log.d(TAG, "init webrtc");
        this.aCWebRTCManager.initWebRTC(context);
        WebRTCAudioManager.getInstance().init(context);
        this.sdkLoggedIn = true;
    }

    private void initOauthHeaders() {
        if (this.registerHeaders == null) {
            this.registerHeaders = new Hashtable();
        }
        this.registerHeaders.put("Authorization", "Bearer " + this.accessToken);
        this.acSipManager.setRegisterHeaders(this.registerHeaders);
        if (this.inviteHeaders == null) {
            this.inviteHeaders = new Hashtable();
        }
        this.inviteHeaders.put("Authorization", "Bearer " + this.accessToken);
    }

    public void logout() {
        Log.d(TAG, "logout");
        this.sdkLoggedIn = false;
        this.context = null;
        this.acSipManager.deinit();
        this.aCWebRTCManager.deinitWebRTC();
    }

    public void setListener(AudioCodesEventListener listener) {
        this.eventListener = listener;
    }

    public AudioCodesSession call(RemoteContact callTo, boolean withVideo, Hashtable<String, String> inviteHeaders) throws WebRTCException {
        if (!this.sdkLoggedIn) {
            throw new WebRTCException(WebRTCException.WebRTCExceptionCode.LOGGED_OUT);
        }
        if (this.sessionList.size() >= 2) {
            throw new WebRTCException(WebRTCException.WebRTCExceptionCode.MAX_NR_OF_SESSIONS_REACHED);
        }
        for (AudioCodesSession session : this.sessionList) {
            session.hold(true);
        }
        if (this.inviteHeaders != null) {
            if (inviteHeaders == null) {
                inviteHeaders = new Hashtable();
            }
            inviteHeaders.putAll(this.inviteHeaders);
        }
        AudioCodesSessionImpl session = AudioCodesSessionImpl.call(callTo, withVideo, inviteHeaders);
        this.sessionList.add(session);
        session.setSessionID(this.sessionList.indexOf(session));
        return session;
    }

    public void setRegisterExtraHeaders(Hashtable<String, String> headers) {
        this.registerHeaders = headers;
    }

    public void setInviteExtraHeaders(Hashtable<String, String> headers) {
        this.inviteHeaders = headers;
    }

    public Hashtable<String, String> getInviteHeaders() {
        return this.inviteHeaders;
    }

    public Hashtable<String, String> getRegisterHeaders() {
        return this.registerHeaders;
    }

    private AudioCodesSession getCallByCallId(String callId) {
        Iterator<AudioCodesSession> iterator = this.sessionList.iterator();
        if (iterator.hasNext()) {
            AudioCodesSession session = iterator.next();
            AudioCodesSessionImpl impl = (AudioCodesSessionImpl)session;
            if (callId.equals(impl.getAcCall().getCallID())) {
                // empty if block
            }
            return session;
        }
        return null;
    }

    public void removeSession(AudioCodesSession session) {
        this.sessionList.remove(session);
    }

    public void answeringCall(AudioCodesSession audioCodesSession) {
        for (AudioCodesSession session : this.sessionList) {
            if (session.getSessionID() == audioCodesSession.getSessionID()) continue;
            Log.d(TAG, "Putting call on hold: " + session.getSessionID());
            session.hold(true);
        }
    }

    public void handleNetworkChange() {
        Log.d(TAG, "Handle network change");
        if (!this.sdkLoggedIn) {
            Log.d(TAG, "not handling network change since the SDK is not logged in");
            return;
        }
        for (AudioCodesSession session : this.sessionList) {
            AudioCodesSessionImpl impl = (AudioCodesSessionImpl)session;
            impl.handleNetworkChange();
        }
        if (this.acSipManager != null && this.acSipManager.isInitialized()) {
            this.acSipManager.handleNetworkChange();
        }
    }

    public void disconnectOnBrokenConnection(boolean disconnect) {
        Log.d(TAG, "disconnectOnBrokenConnection");
        ACWebRTCCall.setHandleBrokenConnection(!disconnect);
        if (!disconnect) {
            this.setContactRewrite(true);
        }
    }

    public AudioCodesSession getSession(int sessionIndex) {
        if (sessionIndex >= this.sessionList.size()) {
            return null;
        }
        return this.sessionList.get(sessionIndex);
    }

    public boolean isRegistered() {
        return this.loginState;
    }

    public void setUseSessionTimer(boolean use) {
        this.acSipManager.setUseSessionTimer(use);
    }

    public int getRegExpires() {
        return ACPjSipManager.getInstance().getRegExpires();
    }

    public void setRegExpires(int defaultRegExpires) {
        ACPjSipManager.getInstance().setRegExpires(defaultRegExpires);
    }

    public String getUserAgent() {
        return ACPjSipManager.getInstance().getUserAgent();
    }

    public void setUserAgent(String userAgent) {
        ACPjSipManager.getInstance().setUserAgent(userAgent);
    }

    public ArrayList<AudioCodesSession> getSessionList() {
        return this.sessionList;
    }

    public void setContactRewrite(boolean enable) {
        ACPjSipManager.getInstance().setContactRewrite(enable);
    }

    public void setPushToken(Context context, String registrationToken, String projectID) {
        String tmpRegistrationToken = Storage.loadString(context, "PN_REGISTRATION_TOKEN", null);
        String tmpProjectID = Storage.loadString(context, "PN_REGISTRATION_TOKEN", null);
        boolean changed = false;
        if (!this.compare(tmpRegistrationToken, registrationToken)) {
            changed = true;
        }
        if (!this.compare(tmpProjectID, projectID)) {
            changed = true;
        }
        Storage.saveString(context, "PN_REGISTRATION_TOKEN", registrationToken);
        Storage.saveString(context, "PN_PROJECT_ID", projectID);
        Storage.saveString(context, "PN_PROVIDER", "fcm");
        if (changed && this.acSipManager != null) {
            this.acSipManager.sipHeadersChanged();
        }
    }

    public void updatePushToken(Context context, String registrationToken) {
        String tmpRegistrationToken = Storage.loadString(context, "PN_REGISTRATION_TOKEN", null);
        boolean changed = false;
        if (!this.compare(tmpRegistrationToken, registrationToken)) {
            changed = true;
        }
        Storage.saveString(context, "PN_REGISTRATION_TOKEN", registrationToken);
        if (changed && this.acSipManager != null) {
            this.acSipManager.sipHeadersChanged();
        }
    }

    public Context getContext() {
        return this.context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    private boolean compare(String str1, String str2) {
        return str1 == null ? str2 == null : str1.equals(str2);
    }

    public void setVideoCodecHardwareAceleration(boolean videoCodecHwAcceleration) {
        this.aCWebRTCManager.setVideoCodecHwAcceleration(videoCodecHwAcceleration);
    }

    public void setOauthToken(String accessToken) {
        boolean changed = false;
        if (!this.compare(this.accessToken, accessToken)) {
            changed = true;
        }
        this.oauthEnabled = true;
        this.accessToken = accessToken;
        this.initOauthHeaders();
        if (changed && this.acSipManager != null) {
            this.acSipManager.sipHeadersChanged();
        }
    }

    public long sendInstantMessage(RemoteContact contact, String message) {
        return this.acSipManager.sendInstantMessage(contact, message);
    }

    public void setVerifyServer(boolean verifyServer) {
        this.acSipManager.setTlsVerifyServer(verifyServer);
    }

    public void setCaCertFilePath(String path) {
        this.acSipManager.setCaCertFilePath(path);
    }

    public void setSessionExpiresTimeout(int timeout) {
        if (this.acSipManager != null) {
            this.acSipManager.setSessionExpiresTimeout(timeout);
        }
    }

    public void setNameServers(ArrayList<String> nameServersList, boolean useDefaultServers) {
        if (this.acSipManager != null) {
            this.acSipManager.setNameServers(nameServersList, useDefaultServers);
        }
    }

    public void setAllowHeader(String[] events) {
        if (this.acSipManager != null) {
            this.acSipManager.setAllowHeader(events);
        }
    }

    static {
        try {
            System.loadLibrary("pjsua2");
        }
        catch (UnsatisfiedLinkError e) {
            Log.d(TAG, "UnsatisfiedLinkError: " + e.getMessage());
        }
        instance = null;
    }

    private class UASipEventListener
    implements SipEventListener {
        private UASipEventListener() {
        }

        @Override
        public void loginStateChanged(boolean isLogin, int code, String cause) {
            AudioCodesUA.this.loginState = isLogin;
            if (AudioCodesUA.this.eventListener != null) {
                try {
                    AudioCodesUA.this.eventListener.loginStateChanged(isLogin, code, cause);
                }
                catch (Throwable e) {
                    Log.d(AudioCodesUA.TAG, "Error in callback - loginStateChanged. This method is called from the SDK thread, please make sure not to run UI operations on this tread");
                    e.printStackTrace();
                }
            }
        }

        @Override
        public void incomingCall(String remoteSDP, ACSipCall call, InfoAlert infoAlert) {
            boolean hasVideo = SdpUtils.sdpHasVideo(remoteSDP);
            Log.d(AudioCodesUA.TAG, "Incoming call, video: " + hasVideo + " redirect:" + ACConfiguration.getConfiguration().getRedirectEnabled());
            final AudioCodesSessionImpl session = AudioCodesSessionImpl.createIncomingCall(call, hasVideo);
            if (ACConfiguration.getConfiguration().getRedirectEnabled()) {
                Log.d(AudioCodesUA.TAG, "Redirect enabled: call will be answered with redirect to: " + ACConfiguration.getConfiguration().getRedirectContact().getUserName());
                Thread t = new Thread(new Runnable(){

                    @Override
                    public void run() {
                        session.redirect(ACConfiguration.getConfiguration().getRedirectContact(), null);
                    }
                });
                t.start();
                return;
            }
            if (AudioCodesUA.this.sessionList.size() >= 2) {
                session.reject(null);
                return;
            }
            AudioCodesUA.this.sessionList.add(session);
            session.setSessionID(AudioCodesUA.this.sessionList.indexOf(session));
            session.sendRinging();
            if (AudioCodesUA.this.eventListener != null) {
                try {
                    AudioCodesUA.this.eventListener.incomingCall(session, infoAlert);
                }
                catch (Throwable e) {
                    Log.d(AudioCodesUA.TAG, "Error in callback - loginStateChanged. This method is called from the SDK thread, please make sure not to run UI operations on this tread");
                    e.printStackTrace();
                }
            }
        }
    }
}

