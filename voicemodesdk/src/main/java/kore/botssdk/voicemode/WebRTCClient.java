package kore.botssdk.voicemode;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import org.webrtc.AudioSource;
import org.webrtc.AudioTrack;
import org.webrtc.DataChannel;
import org.webrtc.DefaultVideoDecoderFactory;
import org.webrtc.DefaultVideoEncoderFactory;
import org.webrtc.EglBase;
import org.webrtc.IceCandidate;
import org.webrtc.MediaConstraints;
import org.webrtc.MediaStream;
import org.webrtc.PeerConnection;
import org.webrtc.PeerConnectionFactory;
import org.webrtc.RtpReceiver;
import org.webrtc.SdpObserver;
import org.webrtc.SessionDescription;
import org.webrtc.VideoDecoderFactory;
import org.webrtc.VideoEncoderFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kore.botssdk.voicemode.audio.AudioSessionManager;
import kore.botssdk.voicemode.net.JWTService;
import kore.botssdk.voicemode.sip.SIPWebSocketClient;
import kore.botssdk.voicemode.utils.LogHelper;
import kore.botssdk.voicemode.utils.PermissionHelper;

/**
 * Main WebRTC client class for making SIP-based voice calls.
 * This is the primary interface for the WebRTC SDK.
 * 
 * <p>Usage example:
 * <pre>
 * WebRTCConfig config = new WebRTCConfig.Builder()
 *     .botId("your-bot-id")
 *     .clientId("your-client-id")
 *     .clientSecret("your-client-secret")
 *     .identity("user@example.com")
 *     .webSocketUrl("wss://sip.example.com:8443/")
 *     .jwtServiceUrl("https://jwt.example.com/token")
 *     .serverUrl("https://platform.example.com")
 *     .sipDomain("sip.example.com")
 *     .build();
 * 
 * WebRTCClient client = new WebRTCClient(context);
 * client.setListener(new WebRTCListener() { ... });
 * client.initWithCredentials(config);
 * </pre>
 */
public class WebRTCClient {

    private static final String TAG = "WebRTCClient";

    private static final List<PeerConnection.IceServer> ICE_SERVERS = new ArrayList<PeerConnection.IceServer>() {{
        add(PeerConnection.IceServer.builder("stun:stun.l.google.com:19302").createIceServer());
    }};

    private final Context context;
    private final Handler mainHandler;
    private final JWTService jwtService;
    private final AudioSessionManager audioSessionManager;

    private PeerConnectionFactory peerConnectionFactory;
    private EglBase eglBase;
    private SIPWebSocketClient sipClient;
    private WebRTCConfig config;
    private WebRTCListener listener;

    private CallSession activeSession;
    private MediaStream localStream;
    private AudioSource audioSource;
    private AudioTrack localAudioTrack;

    private boolean isInitialized = false;
    private boolean isRegistered = false;

    private Map<String, CallSession> sessions = new HashMap<>();
    private Map<String, String> callTags = new HashMap<>();

    public WebRTCClient(Context context) {
        this.context = context.getApplicationContext();
        this.mainHandler = new Handler(Looper.getMainLooper());
        this.jwtService = new JWTService();
        this.audioSessionManager = new AudioSessionManager(context);
    }

    /**
     * Set the listener for WebRTC events.
     * @param listener The listener implementation
     */
    public void setListener(WebRTCListener listener) {
        this.listener = listener;
    }

    /**
     * Get the audio session manager for controlling speaker, volume, etc.
     * @return AudioSessionManager instance
     */
    public AudioSessionManager getAudioSessionManager() {
        return audioSessionManager;
    }

    /**
     * Get the current configuration.
     * @return WebRTCConfig or null if not initialized
     */
    public WebRTCConfig getConfig() {
        return config;
    }

    /**
     * Check if the client is registered with SIP server.
     * @return true if registered
     */
    public boolean isRegistered() {
        return isRegistered;
    }

    /**
     * Get the current active call session.
     * @return CallSession or null if no active call
     */
    public CallSession getActiveSession() {
        return activeSession;
    }

    /**
     * Check if permissions are available.
     * @return true if audio permissions are granted
     */
    public boolean hasPermissions() {
        return PermissionHelper.hasAudioPermissions(context);
    }

    /**
     * Enable or disable debug logging.
     * @param enabled true to enable
     */
    public void setDebugEnabled(boolean enabled) {
        LogHelper.setDebugEnabled(enabled);
    }

    /**
     * Initialize the WebRTC client with credentials.
     * This will:
     * 1. Fetch JWT token
     * 2. Get user ID from JWT grant
     * 3. Connect to SIP WebSocket
     * 4. Register with SIP server
     * 5. Optionally make an auto-call
     * 
     * @param config The WebRTC configuration
     */
    public void initWithCredentials(WebRTCConfig config) {
        this.config = config;
        
        if (config.isDebug()) {
            LogHelper.setDebugEnabled(true);
        }

        LogHelper.d(TAG, "Initializing with credentials...");

        if (!hasPermissions()) {
            notifyError("Audio permissions not granted");
            return;
        }

        initializePeerConnectionFactory();

        jwtService.fetchJwtToken(
                config.getJwtServiceUrl(),
                config.getClientId(),
                config.getClientSecret(),
                config.getIdentity(),
                new JWTService.JWTCallback() {
                    @Override
                    public void onSuccess(String jwtToken) {
                        LogHelper.d(TAG, "JWT token obtained");
                        config.setJwtToken(jwtToken);
                        fetchUserIdAndRegister();
                    }

                    @Override
                    public void onError(String error) {
                        notifyError("JWT token fetch failed: " + error);
                    }
                }
        );
    }

    private void fetchUserIdAndRegister() {
        jwtService.fetchUserId(
                config.getServerUrl(),
                config.getJwtToken(),
                config.getBotId(),
                new JWTService.UserIdCallback() {
                    @Override
                    public void onSuccess(String userId) {
                        LogHelper.d(TAG, "User ID obtained: " + userId);
                        config.setUserId(userId);
                        
                        String sipUri = config.buildSipUri();
                        config.setSipUri(sipUri);
                        
                        connectAndRegister();
                    }

                    @Override
                    public void onError(String error) {
                        notifyError("User ID fetch failed: " + error);
                    }
                }
        );
    }

    private void connectAndRegister() {
        // JsSIP uses literal "secret" as the SIP password
        // The JWT token is passed as a custom header for Kore-specific auth
        String jwtToken = config.getJwtToken();
        String sipPassword = "secret"; // JsSIP uses literal "secret" as password
        
        LogHelper.d(TAG, "JWT Token available: " + (jwtToken != null && !jwtToken.isEmpty()));
        LogHelper.d(TAG, "JWT Token length: " + (jwtToken != null ? jwtToken.length() : 0));
        LogHelper.d(TAG, "Using SIP password: 'secret' (as in JsSIP)");
        
        sipClient = new SIPWebSocketClient(
                config.getWebSocketUrl(),
                config.getSipUri(),
                sipPassword,
                jwtToken,
                config.isDebug()
        );

        sipClient.setListener(new SIPClientListenerImpl());
        sipClient.connect();
    }

    /**
     * Initialize without auto-fetch of JWT and user ID.
     * Use this if you already have the SIP URI.
     * @param webSocketUrl WebSocket URL
     * @param sipUri Full SIP URI (e.g., sip:user@domain)
     * @param autoRegister Whether to auto-register
     */
    public void init(String webSocketUrl, String sipUri, boolean autoRegister) {
        LogHelper.d(TAG, "Initializing with SIP URI...");

        if (!hasPermissions()) {
            notifyError("Audio permissions not granted");
            return;
        }

        initializePeerConnectionFactory();

        if (config == null) {
            config = new WebRTCConfig.Builder()
                    .botId("")
                    .clientId("")
                    .clientSecret("")
                    .identity("")
                    .webSocketUrl(webSocketUrl)
                    .jwtServiceUrl("")
                    .serverUrl("")
                    .sipDomain(extractDomain(sipUri))
                    .autoRegister(autoRegister)
                    .build();
        }
        config.setSipUri(sipUri);

        // JsSIP uses literal "secret" as the SIP password
        String jwtToken = config.getJwtToken();
        String sipPassword = "secret"; // JsSIP always uses "secret" as password
        
        sipClient = new SIPWebSocketClient(webSocketUrl, sipUri, sipPassword, jwtToken, LogHelper.isDebugEnabled());
        sipClient.setListener(new SIPClientListenerImpl());
        sipClient.connect();
    }

    private void initializePeerConnectionFactory() {
        if (peerConnectionFactory != null) {
            return;
        }

        eglBase = EglBase.create();

        PeerConnectionFactory.InitializationOptions initOptions = PeerConnectionFactory.InitializationOptions.builder(context)
                .setEnableInternalTracer(true)
                .createInitializationOptions();
        PeerConnectionFactory.initialize(initOptions);

        VideoEncoderFactory encoderFactory = new DefaultVideoEncoderFactory(
                eglBase.getEglBaseContext(), true, true);
        VideoDecoderFactory decoderFactory = new DefaultVideoDecoderFactory(eglBase.getEglBaseContext());

        peerConnectionFactory = PeerConnectionFactory.builder()
                .setVideoEncoderFactory(encoderFactory)
                .setVideoDecoderFactory(decoderFactory)
                .createPeerConnectionFactory();

        isInitialized = true;
        LogHelper.d(TAG, "PeerConnectionFactory initialized");
    }

    /**
     * Register with SIP server.
     */
    public void register() {
        if (sipClient != null && sipClient.isConnected()) {
            sipClient.register();
        }
    }

    /**
     * Unregister from SIP server.
     */
    public void unregister() {
        if (sipClient != null) {
            sipClient.unregister();
        }
        isRegistered = false;
    }

    /**
     * Make a call to the configured target (bot).
     * @return CallSession for the call, or null if failed
     */
    public CallSession call() {
        if (config != null) {
            return call(config.getCallTarget());
        }
        notifyError("No call target configured");
        return null;
    }

    /**
     * Make a call to a specific target.
     * @param target SIP URI to call (e.g., sip:bot@domain)
     * @return CallSession for the call, or null if failed
     */
    public CallSession call(String target) {
        if (!isRegistered) {
            notifyError("Cannot make call: Not registered");
            return null;
        }

        if (activeSession != null && !activeSession.isEnded()) {
            notifyError("Cannot make call: Already in a call");
            return null;
        }

        LogHelper.d(TAG, "Making call to: " + target);

        createLocalStream();

        PeerConnection peerConnection = createPeerConnection();
        if (peerConnection == null) {
            notifyError("Failed to create peer connection");
            return null;
        }

        // Use addTrack instead of addStream for Unified Plan SDP semantics
        if (localAudioTrack != null) {
            java.util.List<String> streamIds = java.util.Collections.singletonList("localStream");
            peerConnection.addTrack(localAudioTrack, streamIds);
        }

        String callId = java.util.UUID.randomUUID().toString();
        CallSession session = new CallSession(callId, target);
        session.setPeerConnection(peerConnection);
        session.setLocalStream(localStream);
        session.setState(CallSession.State.CONNECTING);
        session.setOutgoing(true);

        sessions.put(callId, session);
        activeSession = session;

        createOffer(peerConnection, target);

        return session;
    }

    /**
     * Hangup the active call.
     */
    public void hangup() {
        if (activeSession != null) {
            hangup(activeSession);
        }
    }

    /**
     * Hangup a specific call session.
     * @param session The session to hangup
     */
    public void hangup(CallSession session) {
        if (session == null || session.isEnded()) {
            return;
        }

        LogHelper.d(TAG, "Hanging up call: " + session.getCallId());

        String fromTag = callTags.get(session.getCallId() + "_from");
        String toTag = callTags.get(session.getCallId() + "_to");

        if (sipClient != null) {
            sipClient.sendBye(session.getCallId(), config.getSipUri(), session.getRemoteUri(), fromTag, toTag);
        }

        cleanupSession(session, "Local hangup");
    }

    /**
     * Mute the active call.
     */
    public void mute() {
        if (activeSession != null) {
            activeSession.mute();
        }
    }

    /**
     * Unmute the active call.
     */
    public void unmute() {
        if (activeSession != null) {
            activeSession.unmute();
        }
    }

    /**
     * Toggle mute state.
     * @return true if now muted
     */
    public boolean toggleMute() {
        if (activeSession != null) {
            return activeSession.toggleMute();
        }
        return false;
    }

    /**
     * Toggle speaker.
     * @return true if speaker is now on
     */
    public boolean toggleSpeaker() {
        return audioSessionManager.toggleSpeaker();
    }

    /**
     * Set speaker enabled.
     * @param enabled true to enable speaker
     */
    public void setSpeakerEnabled(boolean enabled) {
        audioSessionManager.setSpeakerEnabled(enabled);
    }

    /**
     * Check if speaker is enabled.
     * @return true if speaker is on
     */
    public boolean isSpeakerEnabled() {
        return audioSessionManager.isSpeakerEnabled();
    }

    /**
     * Check if call is muted.
     * @return true if muted
     */
    public boolean isMuted() {
        return activeSession != null && activeSession.isMuted();
    }

    /**
     * Disconnect and cleanup all resources.
     */
    public void disconnect() {
        LogHelper.d(TAG, "Disconnecting...");

        if (activeSession != null && !activeSession.isEnded()) {
            hangup(activeSession);
        }

        if (sipClient != null) {
            sipClient.disconnect();
            sipClient = null;
        }

        cleanupLocalStream();

        if (peerConnectionFactory != null) {
            peerConnectionFactory.dispose();
            peerConnectionFactory = null;
        }

        if (eglBase != null) {
            eglBase.release();
            eglBase = null;
        }

        audioSessionManager.stopAudioSession();

        isInitialized = false;
        isRegistered = false;
        activeSession = null;
        sessions.clear();
        callTags.clear();
    }

    private void createLocalStream() {
        if (localStream != null) {
            return;
        }

        localStream = peerConnectionFactory.createLocalMediaStream("localStream");

        MediaConstraints audioConstraints = new MediaConstraints();
        audioConstraints.mandatory.add(new MediaConstraints.KeyValuePair("googEchoCancellation", "true"));
        audioConstraints.mandatory.add(new MediaConstraints.KeyValuePair("googAutoGainControl", "true"));
        audioConstraints.mandatory.add(new MediaConstraints.KeyValuePair("googHighpassFilter", "true"));
        audioConstraints.mandatory.add(new MediaConstraints.KeyValuePair("googNoiseSuppression", "true"));

        audioSource = peerConnectionFactory.createAudioSource(audioConstraints);
        localAudioTrack = peerConnectionFactory.createAudioTrack("audioTrack", audioSource);
        localAudioTrack.setEnabled(true);

        localStream.addTrack(localAudioTrack);
    }

    private void cleanupLocalStream() {
        if (localAudioTrack != null) {
            localAudioTrack.setEnabled(false);
            localAudioTrack.dispose();
            localAudioTrack = null;
        }

        if (audioSource != null) {
            audioSource.dispose();
            audioSource = null;
        }

        if (localStream != null) {
            localStream.dispose();
            localStream = null;
        }
    }

    private PeerConnection createPeerConnection() {
        PeerConnection.RTCConfiguration rtcConfig = new PeerConnection.RTCConfiguration(ICE_SERVERS);
        rtcConfig.sdpSemantics = PeerConnection.SdpSemantics.UNIFIED_PLAN;

        return peerConnectionFactory.createPeerConnection(rtcConfig, new PeerConnectionObserver());
    }

    private void createOffer(PeerConnection peerConnection, String target) {
        MediaConstraints constraints = new MediaConstraints();
        constraints.mandatory.add(new MediaConstraints.KeyValuePair("OfferToReceiveAudio", "true"));
        constraints.mandatory.add(new MediaConstraints.KeyValuePair("OfferToReceiveVideo", "false"));

        peerConnection.createOffer(new SdpObserver() {
            @Override
            public void onCreateSuccess(SessionDescription sessionDescription) {
                peerConnection.setLocalDescription(new SdpObserver() {
                    @Override
                    public void onCreateSuccess(SessionDescription sd) {}

                    @Override
                    public void onSetSuccess() {
                        String callId = sipClient.sendInvite(target, sessionDescription.description);
                        if (activeSession != null && callId != null) {
                            String oldId = activeSession.getCallId();
                            sessions.remove(oldId);
                            sessions.put(callId, activeSession);
                        }
                    }

                    @Override
                    public void onCreateFailure(String s) {
                        notifyError("Failed to create offer: " + s);
                    }

                    @Override
                    public void onSetFailure(String s) {
                        notifyError("Failed to set local description: " + s);
                    }
                }, sessionDescription);
            }

            @Override
            public void onSetSuccess() {}

            @Override
            public void onCreateFailure(String s) {
                notifyError("Failed to create offer: " + s);
            }

            @Override
            public void onSetFailure(String s) {}
        }, constraints);
    }

    private void handleAnswer(String callId, String sdpAnswer, String fromTag, String toTag) {
        LogHelper.d(TAG, "handleAnswer called with callId: " + callId);
        LogHelper.d(TAG, "SDP Answer is null: " + (sdpAnswer == null));
        LogHelper.d(TAG, "SDP Answer length: " + (sdpAnswer != null ? sdpAnswer.length() : 0));
        if (sdpAnswer != null && sdpAnswer.length() > 0) {
            LogHelper.d(TAG, "SDP Answer first 100 chars: " + sdpAnswer.substring(0, Math.min(100, sdpAnswer.length())));
        }
        
        CallSession session = sessions.get(callId);
        if (session == null) {
            session = activeSession;
        }
        if (session == null) {
            LogHelper.e(TAG, "No session found for callId: " + callId);
            return;
        }

        callTags.put(callId + "_from", fromTag);
        callTags.put(callId + "_to", toTag);

        if (sdpAnswer == null || sdpAnswer.isEmpty()) {
            notifyError("SDP Answer is null or empty");
            return;
        }

        // Ensure SDP uses proper line endings (\r\n) as required by SDP spec
        String normalizedSdp = sdpAnswer.replace("\r\n", "\n").replace("\r", "\n").replace("\n", "\r\n");
        // Ensure SDP ends with CRLF
        if (!normalizedSdp.endsWith("\r\n")) {
            normalizedSdp = normalizedSdp + "\r\n";
        }
        
        // Add missing SDP attributes required by WebRTC Unified Plan if not present
        // The server's SDP answer may be missing BUNDLE group and msid-semantic
        normalizedSdp = fixSdpAnswer(normalizedSdp);
        
        LogHelper.d(TAG, "Normalized SDP length: " + normalizedSdp.length());
        LogHelper.d(TAG, "Full SDP Answer:\n" + normalizedSdp);
        
        SessionDescription answer = new SessionDescription(SessionDescription.Type.ANSWER, normalizedSdp);
        LogHelper.d(TAG, "SessionDescription created: " + (answer != null));
        LogHelper.d(TAG, "SessionDescription type: " + (answer != null ? answer.type : "null"));
        LogHelper.d(TAG, "SessionDescription description is null: " + (answer != null && answer.description == null));
        
        PeerConnection pc = session.getPeerConnection();
        LogHelper.d(TAG, "PeerConnection is null: " + (pc == null));
        LogHelper.d(TAG, "PeerConnection state: " + (pc != null ? pc.signalingState() : "null"));
        
        if (pc != null) {
            pc.setRemoteDescription(new SdpObserver() {
                @Override
                public void onCreateSuccess(SessionDescription sd) {}

                @Override
                public void onSetSuccess() {
                    LogHelper.d(TAG, "Remote description set successfully");
                    sipClient.sendAck(callId, config.getSipUri(), activeSession.getRemoteUri(), fromTag, toTag);
                }

                @Override
                public void onCreateFailure(String s) {}

                @Override
                public void onSetFailure(String s) {
                    notifyError("Failed to set remote description: " + s);
                }
            }, answer);
        }
    }

    private void cleanupSession(CallSession session, String reason) {
        if (session == null) {
            return;
        }

        session.setState(CallSession.State.ENDED);

        PeerConnection pc = session.getPeerConnection();
        if (pc != null) {
            pc.close();
        }

        sessions.remove(session.getCallId());
        callTags.remove(session.getCallId() + "_from");
        callTags.remove(session.getCallId() + "_to");

        if (activeSession == session) {
            activeSession = null;
            audioSessionManager.stopAudioSession();
        }

        mainHandler.post(() -> {
            if (listener != null) {
                listener.onCallEnded(reason);
            }
        });
    }
    
    /**
     * Fix SDP answer to be compatible with WebRTC Unified Plan.
     * Some SIP servers return SDP that's missing required WebRTC attributes.
     */
    private String fixSdpAnswer(String sdp) {
        StringBuilder fixedSdp = new StringBuilder();
        String[] lines = sdp.split("\r\n");
        
        boolean hasBundle = false;
        boolean hasMsidSemantic = false;
        String mid = null;
        
        // First pass: check what's present and extract mid value
        for (String line : lines) {
            if (line.startsWith("a=group:BUNDLE")) {
                hasBundle = true;
            }
            if (line.startsWith("a=msid-semantic")) {
                hasMsidSemantic = true;
            }
            if (line.startsWith("a=mid:")) {
                mid = line.substring(6).trim();
            }
        }
        
        LogHelper.d(TAG, "SDP fix: hasBundle=" + hasBundle + ", hasMsidSemantic=" + hasMsidSemantic + ", mid=" + mid);
        
        // Second pass: rebuild SDP with missing attributes
        boolean addedSessionAttributes = false;
        for (String line : lines) {
            fixedSdp.append(line).append("\r\n");
            
            // After t= line, add missing session-level attributes
            if (line.startsWith("t=") && !addedSessionAttributes) {
                if (!hasBundle && mid != null) {
                    fixedSdp.append("a=group:BUNDLE ").append(mid).append("\r\n");
                    LogHelper.d(TAG, "Added missing a=group:BUNDLE " + mid);
                }
                if (!hasMsidSemantic) {
                    fixedSdp.append("a=msid-semantic: WMS *\r\n");
                    LogHelper.d(TAG, "Added missing a=msid-semantic");
                }
                addedSessionAttributes = true;
            }
        }
        
        return fixedSdp.toString();
    }

    private void notifyError(String error) {
        LogHelper.e(TAG, error);
        mainHandler.post(() -> {
            if (listener != null) {
                listener.onError(error);
            }
        });
    }

    private String extractDomain(String uri) {
        if (uri == null) return "";
        String withoutSip = uri.replace("sip:", "");
        int atIndex = withoutSip.indexOf('@');
        if (atIndex >= 0) {
            return withoutSip.substring(atIndex + 1);
        }
        return withoutSip;
    }

    private class SIPClientListenerImpl implements SIPWebSocketClient.SIPClientListener {
        @Override
        public void onConnected() {
            LogHelper.d(TAG, "SIP WebSocket connected");
            mainHandler.post(() -> {
                if (listener != null) {
                    listener.onConnected();
                }
            });

            if (config != null && config.isAutoRegister()) {
                sipClient.register();
            }
        }

        @Override
        public void onDisconnected() {
            LogHelper.d(TAG, "SIP WebSocket disconnected");
            isRegistered = false;
            mainHandler.post(() -> {
                if (listener != null) {
                    listener.onDisconnected();
                }
            });
        }

        @Override
        public void onRegistered() {
            LogHelper.d(TAG, "SIP registered");
            isRegistered = true;
            mainHandler.post(() -> {
                if (listener != null) {
                    listener.onRegistered();
                }

                if (config != null && config.isAutoCall()) {
                    call();
                }
            });
        }

        @Override
        public void onRegistrationFailed(String error) {
            LogHelper.e(TAG, "SIP registration failed: " + error);
            isRegistered = false;
            mainHandler.post(() -> {
                if (listener != null) {
                    listener.onRegistrationFailed(error);
                }
            });
        }

        @Override
        public void onCallProgress(String callId) {
            LogHelper.d(TAG, "Call progress: " + callId);
            CallSession session = sessions.get(callId);
            if (session != null) {
                session.setState(CallSession.State.RINGING);
            }
            mainHandler.post(() -> {
                if (listener != null) {
                    listener.onCallProgress();
                }
            });
        }

        @Override
        public void onCallAccepted(String callId, String sdpAnswer, String fromTag, String toTag) {
            LogHelper.d(TAG, "Call accepted: " + callId);
            CallSession session = sessions.get(callId);
            if (session == null) {
                session = activeSession;
            }
            if (session != null) {
                session.setState(CallSession.State.IN_PROGRESS);
            }
            
            handleAnswer(callId, sdpAnswer, fromTag, toTag);

            mainHandler.post(() -> {
                if (listener != null) {
                    listener.onCallAccepted();
                }
            });

            audioSessionManager.startAudioSession();
        }

        @Override
        public void onCallConfirmed(String callId) {
            LogHelper.d(TAG, "Call confirmed: " + callId);
            mainHandler.post(() -> {
                if (listener != null) {
                    listener.onCallConfirmed();
                }
            });
        }

        @Override
        public void onCallEnded(String callId, String reason) {
            LogHelper.d(TAG, "Call ended: " + callId + " - " + reason);
            CallSession session = sessions.get(callId);
            if (session == null) {
                session = activeSession;
            }
            cleanupSession(session, reason);
        }

        @Override
        public void onCallFailed(String callId, String reason) {
            LogHelper.e(TAG, "Call failed: " + callId + " - " + reason);
            CallSession session = sessions.get(callId);
            if (session == null) {
                session = activeSession;
            }
            if (session != null) {
                session.setState(CallSession.State.FAILED);
                cleanupSession(session, reason);
            }
            mainHandler.post(() -> {
                if (listener != null) {
                    listener.onCallFailed(reason);
                }
            });
        }

        @Override
        public void onIncomingCall(String callId, String fromUri, String sdpOffer) {
            LogHelper.d(TAG, "Incoming call: " + callId + " from " + fromUri);
            
            CallSession session = new CallSession(callId, fromUri);
            session.setState(CallSession.State.RINGING);
            session.setOutgoing(false);
            sessions.put(callId, session);

            mainHandler.post(() -> {
                if (listener != null) {
                    listener.onIncomingCall(session);
                }
            });
        }

        @Override
        public void onError(String error) {
            notifyError(error);
        }
    }

    private class PeerConnectionObserver implements PeerConnection.Observer {
        @Override
        public void onSignalingChange(PeerConnection.SignalingState signalingState) {
            LogHelper.d(TAG, "Signaling state: " + signalingState);
        }

        @Override
        public void onIceConnectionChange(PeerConnection.IceConnectionState iceConnectionState) {
            LogHelper.d(TAG, "ICE connection state: " + iceConnectionState);
            
            if (iceConnectionState == PeerConnection.IceConnectionState.CONNECTED) {
                mainHandler.post(() -> {
                    if (listener != null && activeSession != null) {
                        listener.onCallConfirmed();
                    }
                });
            } else if (iceConnectionState == PeerConnection.IceConnectionState.DISCONNECTED ||
                       iceConnectionState == PeerConnection.IceConnectionState.FAILED) {
                if (activeSession != null && !activeSession.isEnded()) {
                    cleanupSession(activeSession, "ICE " + iceConnectionState.name().toLowerCase());
                }
            }
        }

        @Override
        public void onIceConnectionReceivingChange(boolean b) {}

        @Override
        public void onIceGatheringChange(PeerConnection.IceGatheringState iceGatheringState) {
            LogHelper.d(TAG, "ICE gathering state: " + iceGatheringState);
        }

        @Override
        public void onIceCandidate(IceCandidate iceCandidate) {
            LogHelper.d(TAG, "ICE candidate: " + iceCandidate.sdp);
        }

        @Override
        public void onIceCandidatesRemoved(IceCandidate[] iceCandidates) {}

        @Override
        public void onAddStream(MediaStream mediaStream) {
            LogHelper.d(TAG, "Remote stream added");
            if (activeSession != null) {
                activeSession.setRemoteStream(mediaStream);
            }
        }

        @Override
        public void onRemoveStream(MediaStream mediaStream) {
            LogHelper.d(TAG, "Remote stream removed");
        }

        @Override
        public void onDataChannel(DataChannel dataChannel) {}

        @Override
        public void onRenegotiationNeeded() {
            LogHelper.d(TAG, "Renegotiation needed");
        }

        @Override
        public void onAddTrack(RtpReceiver rtpReceiver, MediaStream[] mediaStreams) {
            LogHelper.d(TAG, "Track added");
        }
    }
}
