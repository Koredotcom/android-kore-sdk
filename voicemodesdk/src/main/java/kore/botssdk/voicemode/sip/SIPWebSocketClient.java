package kore.botssdk.voicemode.sip;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.NonNull;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;

/**
 * WebSocket client for SIP signaling.
 * Handles connection, registration, and SIP message exchange over WebSocket.
 * Supports SIP Digest Authentication.
 */
public class SIPWebSocketClient {

    private static final String TAG = "SIPWebSocketClient";

    private final String webSocketUrl;
    private final String sipUri;
    private final String sipPassword;
    private final String jwtToken; // Store JWT token for custom header
    private final boolean debug;

    private OkHttpClient okHttpClient;
    private WebSocket webSocket;
    private Handler mainHandler;

    private boolean isConnected = false;
    private boolean isRegistered = false;
    private int registerCseq = 0;  // CSeq for REGISTER requests (per Call-ID)
    private int inviteCseq = 0;    // CSeq for INVITE requests (per dialog)
    private String registerCallId;
    private String registerFromTag;
    private String domain;
    private String username;

    private String currentCallId;
    private String currentFromTag;
    private String currentToTag;
    private String currentTarget;
    // Store pending SDP for INVITE retry with auth
    private String pendingSdpOffer;
    
    // Flag to indicate we have valid cached credentials from REGISTER
    private boolean hasValidCredentials = false;

    // Digest auth fields
    private String authRealm;
    private String authNonce;
    private String authQop;
    private int nonceCount = 0;

    // Via host - generated once and reused for all messages (like JsSIP)
    private final String viaHost;
    // Contact user - random 8-char string like JsSIP uses
    private final String contactUser;
    // Instance ID for +sip.instance parameter
    private final String instanceId;

    private SIPClientListener listener;

    public SIPWebSocketClient(String webSocketUrl, String sipUri, String sipPassword, boolean debug) {
        this(webSocketUrl, sipUri, sipPassword, null, debug);
    }
    
    public SIPWebSocketClient(String webSocketUrl, String sipUri, String sipPassword, String jwtToken, boolean debug) {
        this.webSocketUrl = webSocketUrl;
        this.sipUri = sipUri;
        this.sipPassword = sipPassword;
        this.jwtToken = jwtToken;
        this.debug = debug;
        this.mainHandler = new Handler(Looper.getMainLooper());
        this.registerCallId = UUID.randomUUID().toString();
        this.registerFromTag = generateTag();
        // Generate via_host once and reuse (like JsSIP)
        this.viaHost = generateRandomToken(12) + ".invalid";
        // Generate contact user - JsSIP uses 8-char random string, not the actual username
        this.contactUser = generateRandomToken(8);
        // Generate instance ID for +sip.instance
        this.instanceId = UUID.randomUUID().toString();
        
        // Parse domain and username from SIP URI
        String uriWithoutScheme = sipUri.replace("sip:", "");
        int atIndex = uriWithoutScheme.indexOf('@');
        if (atIndex > 0) {
            this.username = uriWithoutScheme.substring(0, atIndex);
            this.domain = uriWithoutScheme.substring(atIndex + 1);
        } else {
            this.username = uriWithoutScheme;
            this.domain = uriWithoutScheme;
        }
    }

    public void setListener(SIPClientListener listener) {
        this.listener = listener;
    }

    public void connect() {
        okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(0, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .pingInterval(30, TimeUnit.SECONDS)
                .build();

        Request request = new Request.Builder()
                .url(webSocketUrl)
                .addHeader("Sec-WebSocket-Protocol", "sip")
                .build();

        logDebug("Connecting to: " + webSocketUrl);
        webSocket = okHttpClient.newWebSocket(request, new SIPWebSocketListener());
    }

    public void disconnect() {
        if (webSocket != null) {
            webSocket.close(1000, "Client disconnect");
            webSocket = null;
        }
        isConnected = false;
        isRegistered = false;
    }

    public boolean isConnected() {
        return isConnected;
    }

    public boolean isRegistered() {
        return isRegistered;
    }

    public void register() {
        if (!isConnected) {
            logError("Cannot register: Not connected");
            return;
        }
        sendRegister(null);
    }

    private void sendRegister(String authorization) {
        registerCseq++;
        // JsSIP uses 7-digit random number for branch
        String branch = "z9hG4bK" + String.format("%07d", (int)(Math.random() * 10000000));
        
        StringBuilder sb = new StringBuilder();
        sb.append("REGISTER sip:").append(domain).append(" SIP/2.0\r\n");
        // JsSIP doesn't include ;rport
        sb.append("Via: SIP/2.0/WSS ").append(viaHost).append(";branch=").append(branch).append("\r\n");
        sb.append("Max-Forwards: 69\r\n");
        // JsSIP has To before From
        sb.append("To: <").append(sipUri).append(">\r\n");
        sb.append("From: <").append(sipUri).append(">;tag=").append(registerFromTag).append("\r\n");
        sb.append("Call-ID: ").append(registerCallId).append("\r\n");
        sb.append("CSeq: ").append(registerCseq).append(" REGISTER\r\n");
        
        if (authorization != null) {
            sb.append("Authorization: ").append(authorization).append("\r\n");
        }
        
        // JsSIP Contact format: random user, +sip.ice, reg-id, +sip.instance
        sb.append("Contact: <sip:").append(contactUser).append("@").append(viaHost).append(";transport=ws>");
        sb.append(";+sip.ice;reg-id=1;+sip.instance=\"<urn:uuid:").append(instanceId).append(">\";expires=600\r\n");
        
        sb.append("Expires: 600\r\n");
        sb.append("Allow: INVITE,ACK,CANCEL,BYE,UPDATE,MESSAGE,OPTIONS,REFER,INFO,NOTIFY,SUBSCRIBE\r\n");
        sb.append("Supported: path,gruu,outbound\r\n");
        sb.append("User-Agent: JsSIP 3.9.0\r\n");
        sb.append("Content-Length: 0\r\n");
        sb.append("\r\n");

        sendRawMessage(sb.toString());
    }

    public void unregister() {
        if (!isConnected) {
            return;
        }

        registerCseq++;
        String branch = "z9hG4bK" + String.format("%07d", (int)(Math.random() * 10000000));
        
        StringBuilder sb = new StringBuilder();
        sb.append("REGISTER sip:").append(domain).append(" SIP/2.0\r\n");
        sb.append("Via: SIP/2.0/WSS ").append(viaHost).append(";branch=").append(branch).append("\r\n");
        sb.append("Max-Forwards: 69\r\n");
        sb.append("To: <").append(sipUri).append(">\r\n");
        sb.append("From: <").append(sipUri).append(">;tag=").append(registerFromTag).append("\r\n");
        sb.append("Call-ID: ").append(registerCallId).append("\r\n");
        sb.append("CSeq: ").append(registerCseq).append(" REGISTER\r\n");
        sb.append("Contact: <sip:").append(contactUser).append("@").append(viaHost).append(";transport=ws>");
        sb.append(";+sip.ice;reg-id=1;+sip.instance=\"<urn:uuid:").append(instanceId).append(">\";expires=0\r\n");
        sb.append("Expires: 0\r\n");
        sb.append("User-Agent: JsSIP 3.9.0\r\n");
        sb.append("Content-Length: 0\r\n");
        sb.append("\r\n");

        sendRawMessage(sb.toString());
        isRegistered = false;
    }

    public String sendInvite(String toUri, String sdpOffer) {
        if (!isConnected || !isRegistered) {
            logError("Cannot send INVITE: Not registered");
            return null;
        }
        
        // Reset INVITE CSeq for new dialog (new Call-ID)
        inviteCseq = 0;
        
        // Proactively include Authorization if we have valid credentials from REGISTER
        // This is what JsSIP does - it caches credentials and reuses them
        String authorization = null;
        if (hasValidCredentials && authRealm != null && authNonce != null) {
            authorization = generateDigestAuth("INVITE", toUri);
            logDebug("Proactively adding Authorization to INVITE");
        }
        
        return sendInviteInternal(toUri, sdpOffer, authorization);
    }

    private String sendInviteInternal(String toUri, String sdpOffer, String authorization) {
        inviteCseq++;
        currentCallId = UUID.randomUUID().toString();
        currentFromTag = generateTag();
        currentTarget = toUri;
        currentToTag = null;
        // Store SDP for potential retry with auth
        pendingSdpOffer = sdpOffer;
        
        String branch = "z9hG4bK" + String.format("%07d", (int)(Math.random() * 10000000));
        byte[] sdpBytes = sdpOffer.getBytes(java.nio.charset.StandardCharsets.UTF_8);
        
        StringBuilder sb = new StringBuilder();
        sb.append("INVITE ").append(toUri).append(" SIP/2.0\r\n");
        sb.append("Via: SIP/2.0/WSS ").append(viaHost).append(";branch=").append(branch).append("\r\n");
        sb.append("Max-Forwards: 69\r\n");
        // JsSIP has To before From
        sb.append("To: <").append(toUri).append(">\r\n");
        sb.append("From: <").append(sipUri).append(">;tag=").append(currentFromTag).append("\r\n");
        sb.append("Call-ID: ").append(currentCallId).append("\r\n");
        sb.append("CSeq: ").append(inviteCseq).append(" INVITE\r\n");
        
        if (authorization != null) {
            sb.append("Authorization: ").append(authorization).append("\r\n");
        }
        
        // JsSIP Contact format with ;ob suffix
        sb.append("Contact: <sip:").append(contactUser).append("@").append(viaHost).append(";transport=ws;ob>\r\n");
        sb.append("Content-Type: application/sdp\r\n");
        sb.append("Session-Expires: 90\r\n");
        sb.append("Allow: INVITE,ACK,CANCEL,BYE,UPDATE,MESSAGE,OPTIONS,REFER,INFO,NOTIFY,SUBSCRIBE\r\n");
        sb.append("Supported: timer,ice,replaces,outbound\r\n");
        sb.append("User-Agent: JsSIP 3.9.0\r\n");
        sb.append("Content-Length: ").append(sdpBytes.length).append("\r\n");
        sb.append("\r\n");
        sb.append(sdpOffer);

        sendRawMessage(sb.toString());
        return currentCallId;
    }
    
    /**
     * Resend INVITE with authentication after receiving 401/407.
     * Keeps the same Call-ID and From-tag, but generates new branch and increments CSeq.
     */
    private void resendInviteWithAuth() {
        if (currentTarget == null || pendingSdpOffer == null) {
            logError("Cannot resend INVITE: missing target or SDP");
            return;
        }
        
        inviteCseq++;
        
        String branch = "z9hG4bK" + String.format("%07d", (int)(Math.random() * 10000000));
        String uri = currentTarget;
        String authorization = generateDigestAuth("INVITE", uri);
        
        byte[] sdpBytes = pendingSdpOffer.getBytes(java.nio.charset.StandardCharsets.UTF_8);
        
        StringBuilder sb = new StringBuilder();
        sb.append("INVITE ").append(currentTarget).append(" SIP/2.0\r\n");
        sb.append("Via: SIP/2.0/WSS ").append(viaHost).append(";branch=").append(branch).append("\r\n");
        sb.append("Max-Forwards: 69\r\n");
        sb.append("To: <").append(currentTarget).append(">\r\n");
        sb.append("From: <").append(sipUri).append(">;tag=").append(currentFromTag).append("\r\n");
        sb.append("Call-ID: ").append(currentCallId).append("\r\n");
        sb.append("CSeq: ").append(inviteCseq).append(" INVITE\r\n");
        
        if (authorization != null) {
            sb.append("Authorization: ").append(authorization).append("\r\n");
        }
        
        sb.append("Contact: <sip:").append(contactUser).append("@").append(viaHost).append(";transport=ws;ob>\r\n");
        sb.append("Content-Type: application/sdp\r\n");
        sb.append("Session-Expires: 90\r\n");
        sb.append("Allow: INVITE,ACK,CANCEL,BYE,UPDATE,MESSAGE,OPTIONS,REFER,INFO,NOTIFY,SUBSCRIBE\r\n");
        sb.append("Supported: timer,ice,replaces,outbound\r\n");
        sb.append("User-Agent: JsSIP 3.9.0\r\n");
        sb.append("Content-Length: ").append(sdpBytes.length).append("\r\n");
        sb.append("\r\n");
        sb.append(pendingSdpOffer);

        logDebug("Retrying INVITE with authentication");
        sendRawMessage(sb.toString());
    }
    
    /**
     * Send ACK for a non-2xx response to INVITE (like 401 Unauthorized).
     * This is required by SIP RFC 3261.
     */
    private void sendAckFor401(String callId, String fromUri, String toUri, String fromTag, String toTag, int ackCseq) {
        String branch = "z9hG4bK" + String.format("%07d", (int)(Math.random() * 10000000));
        
        StringBuilder sb = new StringBuilder();
        sb.append("ACK ").append(toUri).append(" SIP/2.0\r\n");
        sb.append("Via: SIP/2.0/WSS ").append(viaHost).append(";branch=").append(branch).append("\r\n");
        sb.append("Max-Forwards: 69\r\n");
        sb.append("To: <").append(toUri).append(">");
        if (toTag != null && !toTag.isEmpty()) {
            sb.append(";tag=").append(toTag);
        }
        sb.append("\r\n");
        sb.append("From: <").append(fromUri).append(">;tag=").append(fromTag).append("\r\n");
        sb.append("Call-ID: ").append(callId).append("\r\n");
        sb.append("CSeq: ").append(ackCseq).append(" ACK\r\n");
        sb.append("Allow: INVITE,ACK,CANCEL,BYE,UPDATE,MESSAGE,OPTIONS,REFER,INFO,NOTIFY,SUBSCRIBE\r\n");
        sb.append("Supported: outbound\r\n");
        sb.append("User-Agent: JsSIP 3.9.0\r\n");
        sb.append("Content-Length: 0\r\n");
        sb.append("\r\n");

        logDebug("Sending ACK for 401");
        sendRawMessage(sb.toString());
    }

    public void sendAck(String callId, String fromUri, String toUri, String fromTag, String toTag) {
        if (!isConnected) {
            return;
        }

        String branch = "z9hG4bK" + UUID.randomUUID().toString().substring(0, 8);
        
        StringBuilder sb = new StringBuilder();
        sb.append("ACK ").append(toUri).append(" SIP/2.0\r\n");
        sb.append("Via: SIP/2.0/WSS ").append(viaHost).append(";branch=").append(branch).append(";rport\r\n");
        sb.append("Max-Forwards: 70\r\n");
        sb.append("From: <").append(fromUri).append(">;tag=").append(fromTag).append("\r\n");
        sb.append("To: <").append(toUri).append(">;tag=").append(toTag).append("\r\n");
        sb.append("Call-ID: ").append(callId).append("\r\n");
        sb.append("CSeq: ").append(inviteCseq).append(" ACK\r\n");
        sb.append("Content-Length: 0\r\n");
        sb.append("\r\n");

        sendRawMessage(sb.toString());
    }

    public void sendBye(String callId, String fromUri, String toUri, String fromTag, String toTag) {
        if (!isConnected) {
            return;
        }

        inviteCseq++;  // BYE continues the dialog's CSeq
        String branch = "z9hG4bK" + UUID.randomUUID().toString().substring(0, 8);
        
        StringBuilder sb = new StringBuilder();
        sb.append("BYE ").append(toUri).append(" SIP/2.0\r\n");
        sb.append("Via: SIP/2.0/WSS ").append(viaHost).append(";branch=").append(branch).append(";rport\r\n");
        sb.append("Max-Forwards: 70\r\n");
        sb.append("From: <").append(fromUri).append(">;tag=").append(fromTag).append("\r\n");
        sb.append("To: <").append(toUri).append(">;tag=").append(toTag).append("\r\n");
        sb.append("Call-ID: ").append(callId).append("\r\n");
        sb.append("CSeq: ").append(inviteCseq).append(" BYE\r\n");
        sb.append("Content-Length: 0\r\n");
        sb.append("\r\n");

        sendRawMessage(sb.toString());
    }

    public void sendResponse(int statusCode, String statusText, String callId, String fromUri, 
                            String toUri, String fromTag, String toTag, int responseCseq, 
                            String method, String sdpAnswer) {
        String branch = "z9hG4bK" + UUID.randomUUID().toString().substring(0, 8);
        
        StringBuilder sb = new StringBuilder();
        sb.append("SIP/2.0 ").append(statusCode).append(" ").append(statusText).append("\r\n");
        sb.append("Via: SIP/2.0/WSS ").append(viaHost).append(";branch=").append(branch).append(";rport\r\n");
        sb.append("From: <").append(fromUri).append(">;tag=").append(fromTag).append("\r\n");
        sb.append("To: <").append(toUri).append(">;tag=").append(toTag).append("\r\n");
        sb.append("Call-ID: ").append(callId).append("\r\n");
        sb.append("CSeq: ").append(responseCseq).append(" ").append(method).append("\r\n");
        
        if (sdpAnswer != null && !sdpAnswer.isEmpty()) {
            byte[] sdpBytes = sdpAnswer.getBytes();
            sb.append("Contact: <sip:").append(username).append("@").append(viaHost).append(";transport=ws>\r\n");
            sb.append("Content-Type: application/sdp\r\n");
            sb.append("Content-Length: ").append(sdpBytes.length).append("\r\n");
            sb.append("\r\n");
            sb.append(sdpAnswer);
        } else {
            sb.append("Content-Length: 0\r\n");
            sb.append("\r\n");
        }

        sendRawMessage(sb.toString());
    }

    private void sendRawMessage(String message) {
        if (webSocket == null) {
            logError("Cannot send message: WebSocket is null");
            return;
        }

        logDebug("Sending SIP:\n" + message);
        webSocket.send(message);
    }

    private String generateTag() {
        return UUID.randomUUID().toString().substring(0, 8);
    }

    // ==================== Digest Authentication ====================

    private String generateDigestAuth(String method, String uri) {
        if (authRealm == null || authNonce == null) {
            return null;
        }

        nonceCount++;
        String nc = String.format("%08x", nonceCount);
        // JsSIP uses createRandomToken(12) - 12 alphanumeric chars
        String cnonce = generateRandomToken(12);

        // Debug: Log password being used
        logDebug("Using password for digest: '" + sipPassword + "'");
        logDebug("Digest inputs - username: " + username + ", realm: " + authRealm);

        // HA1 = MD5(username:realm:password)
        String ha1Input = username + ":" + authRealm + ":" + sipPassword;
        String ha1 = md5(ha1Input);
        logDebug("HA1 input: " + ha1Input);
        logDebug("HA1: " + ha1);
        
        // HA2 = MD5(method:uri)
        String ha2Input = method + ":" + uri;
        String ha2 = md5(ha2Input);
        logDebug("HA2 input: " + ha2Input);
        logDebug("HA2: " + ha2);

        String response;
        if (authQop != null && authQop.contains("auth")) {
            // response = MD5(HA1:nonce:nc:cnonce:qop:HA2)
            String responseInput = ha1 + ":" + authNonce + ":" + nc + ":" + cnonce + ":auth:" + ha2;
            response = md5(responseInput);
            logDebug("Response input: " + responseInput);
            logDebug("Response: " + response);
            
            // Match JsSIP header order exactly:
            // algorithm, username, realm, nonce, uri, response, qop, cnonce, nc
            return "Digest algorithm=MD5" +
                    ", username=\"" + username + "\"" +
                    ", realm=\"" + authRealm + "\"" +
                    ", nonce=\"" + authNonce + "\"" +
                    ", uri=\"" + uri + "\"" +
                    ", response=\"" + response + "\"" +
                    ", qop=auth" +
                    ", cnonce=\"" + cnonce + "\"" +
                    ", nc=" + nc;
        } else {
            // response = MD5(HA1:nonce:HA2)
            response = md5(ha1 + ":" + authNonce + ":" + ha2);
            
            return "Digest algorithm=MD5" +
                    ", username=\"" + username + "\"" +
                    ", realm=\"" + authRealm + "\"" +
                    ", nonce=\"" + authNonce + "\"" +
                    ", uri=\"" + uri + "\"" +
                    ", response=\"" + response + "\"";
        }
    }
    
    private String generateRandomToken(int length) {
        // Match JsSIP's createRandomToken - alphanumeric characters
        String chars = "0123456789abcdefghijklmnopqrstuvwxyz";
        StringBuilder sb = new StringBuilder();
        java.util.Random random = new java.util.Random();
        for (int i = 0; i < length; i++) {
            sb.append(chars.charAt(random.nextInt(chars.length())));
        }
        return sb.toString();
    }

    private String md5(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            // Use UTF-8 encoding explicitly (matches JavaScript's behavior)
            byte[] digest = md.digest(input.getBytes(java.nio.charset.StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for (byte b : digest) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            logError("MD5 not available: " + e.getMessage());
            return "";
        }
    }

    private void parseWwwAuthenticate(String header) {
        // Parse: Digest realm="xxx", algorithm=MD5, qop="auth", nonce="xxx"
        Pattern realmPattern = Pattern.compile("realm=\"([^\"]+)\"");
        Pattern noncePattern = Pattern.compile("nonce=\"([^\"]+)\"");
        Pattern qopPattern = Pattern.compile("qop=\"([^\"]+)\"");

        Matcher realmMatcher = realmPattern.matcher(header);
        if (realmMatcher.find()) {
            authRealm = realmMatcher.group(1);
        }

        Matcher nonceMatcher = noncePattern.matcher(header);
        if (nonceMatcher.find()) {
            authNonce = nonceMatcher.group(1);
        }

        Matcher qopMatcher = qopPattern.matcher(header);
        if (qopMatcher.find()) {
            authQop = qopMatcher.group(1);
        }

        logDebug("Parsed auth: realm=" + authRealm + ", nonce=" + authNonce + ", qop=" + authQop);
    }

    // ==================== WebSocket Listener ====================

    private void logDebug(String message) {
        if (debug) {
            Log.d(TAG, message);
        }
    }

    private void logError(String message) {
        Log.e(TAG, message);
    }

    private class SIPWebSocketListener extends WebSocketListener {
        @Override
        public void onOpen(@NonNull WebSocket webSocket, @NonNull Response response) {
            logDebug("WebSocket connected");
            isConnected = true;
            mainHandler.post(() -> {
                if (listener != null) {
                    listener.onConnected();
                }
            });
        }

        @Override
        public void onMessage(@NonNull WebSocket webSocket, @NonNull String text) {
            logDebug("Received SIP:\n" + text);
            parseSIPMessage(text);
        }

        @Override
        public void onClosing(@NonNull WebSocket webSocket, int code, @NonNull String reason) {
            logDebug("WebSocket closing: " + code + " " + reason);
        }

        @Override
        public void onClosed(@NonNull WebSocket webSocket, int code, @NonNull String reason) {
            logDebug("WebSocket closed: " + code + " " + reason);
            isConnected = false;
            isRegistered = false;
            mainHandler.post(() -> {
                if (listener != null) {
                    listener.onDisconnected();
                }
            });
        }

        @Override
        public void onFailure(@NonNull WebSocket webSocket, @NonNull Throwable t, Response response) {
            logError("WebSocket failure: " + t.getMessage());
            isConnected = false;
            isRegistered = false;
            mainHandler.post(() -> {
                if (listener != null) {
                    listener.onError("WebSocket failure: " + t.getMessage());
                    listener.onDisconnected();
                }
            });
        }
    }

    private void parseSIPMessage(String message) {
        try {
            String[] lines = message.split("\r\n");
            if (lines.length == 0) return;

            String firstLine = lines[0];
            Map<String, String> headers = new HashMap<>();
            String body = "";

            boolean inBody = false;
            StringBuilder bodyBuilder = new StringBuilder();
            for (int i = 1; i < lines.length; i++) {
                String line = lines[i];
                if (line.isEmpty()) {
                    inBody = true;
                    continue;
                }
                if (inBody) {
                    bodyBuilder.append(line).append("\r\n");
                } else {
                    int colonIndex = line.indexOf(':');
                    if (colonIndex > 0) {
                        String name = line.substring(0, colonIndex).trim().toLowerCase();
                        String value = line.substring(colonIndex + 1).trim();
                        headers.put(name, value);
                    }
                }
            }
            body = bodyBuilder.toString().trim();

            if (firstLine.startsWith("SIP/2.0")) {
                Pattern pattern = Pattern.compile("SIP/2\\.0\\s+(\\d+)\\s+(.*)");
                Matcher matcher = pattern.matcher(firstLine);
                if (matcher.find()) {
                    int statusCode = Integer.parseInt(matcher.group(1));
                    String statusText = matcher.group(2);
                    handleResponse(statusCode, statusText, headers, body);
                }
            } else {
                Pattern pattern = Pattern.compile("(\\w+)\\s+(.+)\\s+SIP/2\\.0");
                Matcher matcher = pattern.matcher(firstLine);
                if (matcher.find()) {
                    String method = matcher.group(1);
                    String requestUri = matcher.group(2);
                    handleRequest(method, requestUri, headers, body);
                }
            }
        } catch (Exception e) {
            logError("Failed to parse SIP message: " + e.getMessage());
        }
    }

    private void handleResponse(int statusCode, String statusText, Map<String, String> headers, String body) {
        String cseqHeader = headers.get("cseq");
        String method = "";
        if (cseqHeader != null) {
            String[] parts = cseqHeader.split("\\s+");
            if (parts.length >= 2) {
                method = parts[1].toUpperCase();
            }
        }

        String toHeader = headers.get("to");
        String toTag = extractTag(toHeader);
        String callId = headers.get("call-id");

        logDebug("Response: " + statusCode + " " + statusText + " for " + method);

        if ("REGISTER".equals(method)) {
            if (statusCode == 401 || statusCode == 407) {
                // Authentication required
                String wwwAuth = headers.get("www-authenticate");
                if (wwwAuth == null) {
                    wwwAuth = headers.get("proxy-authenticate");
                }
                if (wwwAuth != null) {
                    parseWwwAuthenticate(wwwAuth);
                    String auth = generateDigestAuth("REGISTER", "sip:" + domain);
                    if (auth != null) {
                        logDebug("Retrying REGISTER with authentication");
                        sendRegister(auth);
                        return;
                    }
                }
                // If we can't authenticate, report failure
                isRegistered = false;
                hasValidCredentials = false;
                mainHandler.post(() -> {
                    if (listener != null) {
                        listener.onRegistrationFailed(statusCode + " " + statusText);
                    }
                });
            } else if (statusCode >= 200 && statusCode < 300) {
                isRegistered = true;
                // Mark credentials as valid for reuse in INVITE
                hasValidCredentials = (authRealm != null && authNonce != null);
                logDebug("REGISTER successful, hasValidCredentials=" + hasValidCredentials);
                mainHandler.post(() -> {
                    if (listener != null) {
                        listener.onRegistered();
                    }
                });
            } else if (statusCode >= 400) {
                isRegistered = false;
                mainHandler.post(() -> {
                    if (listener != null) {
                        listener.onRegistrationFailed(statusCode + " " + statusText);
                    }
                });
            }
        } else if ("INVITE".equals(method)) {
            if (statusCode == 401 || statusCode == 407) {
                // Authentication required for INVITE
                String wwwAuth = headers.get("www-authenticate");
                if (wwwAuth == null) {
                    wwwAuth = headers.get("proxy-authenticate");
                }
                if (wwwAuth != null && currentTarget != null && pendingSdpOffer != null) {
                    parseWwwAuthenticate(wwwAuth);
                    logDebug("INVITE requires authentication - retrying with auth");
                    
                    // Send ACK for the 401 response (required by SIP for non-2xx responses to INVITE)
                    sendAckFor401(currentCallId, sipUri, currentTarget, currentFromTag, toTag, inviteCseq);
                    
                    // Retry INVITE with authentication
                    resendInviteWithAuth();
                } else {
                    // Cannot retry - report failure
                    mainHandler.post(() -> {
                        if (listener != null) {
                            listener.onCallFailed(currentCallId, statusCode + " " + statusText);
                        }
                    });
                }
            } else if (statusCode == 100) {
                // Trying - ignore
            } else if (statusCode == 180 || statusCode == 183) {
                mainHandler.post(() -> {
                    if (listener != null) {
                        listener.onCallProgress(currentCallId);
                    }
                });
            } else if (statusCode >= 200 && statusCode < 300) {
                currentToTag = toTag;
                final String sdpAnswer = body;
                final String fFromTag = currentFromTag;
                final String fToTag = toTag;
                final String fCallId = currentCallId;
                
                // Debug logging for SDP
                logDebug("SDP Answer length: " + (sdpAnswer != null ? sdpAnswer.length() : "null"));
                if (sdpAnswer != null && sdpAnswer.length() > 0) {
                    logDebug("SDP Answer starts with: " + sdpAnswer.substring(0, Math.min(50, sdpAnswer.length())));
                }
                
                mainHandler.post(() -> {
                    if (listener != null) {
                        listener.onCallAccepted(fCallId, sdpAnswer, fFromTag, fToTag);
                    }
                });
            } else if (statusCode >= 400) {
                mainHandler.post(() -> {
                    if (listener != null) {
                        listener.onCallFailed(currentCallId, statusCode + " " + statusText);
                    }
                });
            }
        } else if ("BYE".equals(method)) {
            if (statusCode >= 200 && statusCode < 300) {
                mainHandler.post(() -> {
                    if (listener != null) {
                        listener.onCallEnded(callId != null ? callId : currentCallId, "BYE confirmed");
                    }
                });
            }
        }
    }

    private void handleRequest(String method, String requestUri, Map<String, String> headers, String body) {
        String callId = headers.get("call-id");
        String fromHeader = headers.get("from");
        String toHeader = headers.get("to");
        String fromTag = extractTag(fromHeader);
        String toTag = extractTag(toHeader);
        String fromUri = extractUri(fromHeader);
        
        String cseqHeader = headers.get("cseq");
        int requestCseq = 1;
        if (cseqHeader != null) {
            String[] parts = cseqHeader.split("\\s+");
            if (parts.length >= 1) {
                try {
                    requestCseq = Integer.parseInt(parts[0]);
                } catch (NumberFormatException e) {
                    // ignore
                }
            }
        }

        logDebug("Request: " + method + " from " + fromUri);

        if ("INVITE".equals(method)) {
            final String fCallId = callId;
            final String fFromUri = fromUri;
            final String fBody = body;
            
            mainHandler.post(() -> {
                if (listener != null) {
                    listener.onIncomingCall(fCallId, fFromUri, fBody);
                }
            });
        } else if ("BYE".equals(method)) {
            if (toTag == null || toTag.isEmpty()) {
                toTag = generateTag();
            }
            sendResponse(200, "OK", callId, fromUri, sipUri, fromTag, toTag, requestCseq, "BYE", null);
            
            mainHandler.post(() -> {
                if (listener != null) {
                    listener.onCallEnded(callId, "Remote hangup");
                }
            });
        } else if ("ACK".equals(method)) {
            mainHandler.post(() -> {
                if (listener != null) {
                    listener.onCallConfirmed(callId);
                }
            });
        } else if ("CANCEL".equals(method)) {
            if (toTag == null || toTag.isEmpty()) {
                toTag = generateTag();
            }
            sendResponse(200, "OK", callId, fromUri, sipUri, fromTag, toTag, requestCseq, "CANCEL", null);
            
            mainHandler.post(() -> {
                if (listener != null) {
                    listener.onCallEnded(callId, "Cancelled");
                }
            });
        } else if ("OPTIONS".equals(method)) {
            if (toTag == null || toTag.isEmpty()) {
                toTag = generateTag();
            }
            sendResponse(200, "OK", callId, fromUri, sipUri, fromTag, toTag, requestCseq, "OPTIONS", null);
        }
    }

    private String extractTag(String header) {
        if (header == null) return null;
        Pattern pattern = Pattern.compile("tag=([^;>\\s]+)");
        Matcher matcher = pattern.matcher(header);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return null;
    }

    private String extractUri(String header) {
        if (header == null) return null;
        Pattern pattern = Pattern.compile("<([^>]+)>");
        Matcher matcher = pattern.matcher(header);
        if (matcher.find()) {
            return matcher.group(1);
        }
        int semicolonIndex = header.indexOf(';');
        if (semicolonIndex > 0) {
            return header.substring(0, semicolonIndex).trim();
        }
        return header.trim();
    }

    public interface SIPClientListener {
        void onConnected();
        void onDisconnected();
        void onRegistered();
        void onRegistrationFailed(String error);
        void onCallProgress(String callId);
        void onCallAccepted(String callId, String sdpAnswer, String fromTag, String toTag);
        void onCallConfirmed(String callId);
        void onCallEnded(String callId, String reason);
        void onCallFailed(String callId, String reason);
        void onIncomingCall(String callId, String fromUri, String sdpOffer);
        void onError(String error);
    }
}
