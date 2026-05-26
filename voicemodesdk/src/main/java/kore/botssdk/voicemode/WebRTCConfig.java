package kore.botssdk.voicemode;

/**
 * Configuration class for WebRTC SDK.
 * Holds all configuration parameters needed for SIP registration and calls.
 */
public class WebRTCConfig {

    private String botId;
    private String clientId;
    private String clientSecret;
    private String identity;
    private String webSocketUrl;
    private String jwtServiceUrl;
    private String serverUrl;
    private String sipDomain;
    private String jwtToken;
    private String userId;
    private String sipUri;
    private String callTarget;
    private boolean debug = false;
    private boolean autoRegister = true;
    private boolean autoCall = true;

    private WebRTCConfig(Builder builder) {
        this.botId = builder.botId;
        this.clientId = builder.clientId;
        this.clientSecret = builder.clientSecret;
        this.identity = builder.identity;
        this.webSocketUrl = builder.webSocketUrl;
        this.jwtServiceUrl = builder.jwtServiceUrl;
        this.serverUrl = builder.serverUrl;
        this.sipDomain = builder.sipDomain;
        this.debug = builder.debug;
        this.autoRegister = builder.autoRegister;
        this.autoCall = builder.autoCall;
    }

    public String getBotId() {
        return botId;
    }

    public String getClientId() {
        return clientId;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public String getIdentity() {
        return identity;
    }

    public String getWebSocketUrl() {
        return webSocketUrl;
    }

    public String getJwtServiceUrl() {
        return jwtServiceUrl;
    }

    public String getServerUrl() {
        return serverUrl;
    }

    public String getSipDomain() {
        return sipDomain;
    }

    public String getJwtToken() {
        return jwtToken;
    }

    public void setJwtToken(String jwtToken) {
        this.jwtToken = jwtToken;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getSipUri() {
        return sipUri;
    }

    public void setSipUri(String sipUri) {
        this.sipUri = sipUri;
    }

    public String getCallTarget() {
        if (callTarget != null && !callTarget.isEmpty()) {
            return callTarget;
        }
        return "sip:" + botId + "@" + sipDomain;
    }

    public void setCallTarget(String callTarget) {
        this.callTarget = callTarget;
    }

    public boolean isDebug() {
        return debug;
    }

    public boolean isAutoRegister() {
        return autoRegister;
    }

    public boolean isAutoCall() {
        return autoCall;
    }

    public String buildSipUri() {
        if (userId != null && sipDomain != null) {
            return "sip:" + userId + "@" + sipDomain;
        }
        return null;
    }

    public static class Builder {
        private String botId;
        private String clientId;
        private String clientSecret;
        private String identity;
        private String webSocketUrl;
        private String jwtServiceUrl;
        private String serverUrl;
        private String sipDomain;
        private boolean debug = false;
        private boolean autoRegister = true;
        private boolean autoCall = true;

        public Builder botId(String botId) {
            this.botId = botId;
            return this;
        }

        public Builder clientId(String clientId) {
            this.clientId = clientId;
            return this;
        }

        public Builder clientSecret(String clientSecret) {
            this.clientSecret = clientSecret;
            return this;
        }

        public Builder identity(String identity) {
            this.identity = identity;
            return this;
        }

        public Builder webSocketUrl(String webSocketUrl) {
            this.webSocketUrl = webSocketUrl;
            return this;
        }

        public Builder jwtServiceUrl(String jwtServiceUrl) {
            this.jwtServiceUrl = jwtServiceUrl;
            return this;
        }

        public Builder serverUrl(String serverUrl) {
            this.serverUrl = serverUrl;
            return this;
        }

        public Builder sipDomain(String sipDomain) {
            this.sipDomain = sipDomain;
            return this;
        }

        public Builder debug(boolean debug) {
            this.debug = debug;
            return this;
        }

        public Builder autoRegister(boolean autoRegister) {
            this.autoRegister = autoRegister;
            return this;
        }

        public Builder autoCall(boolean autoCall) {
            this.autoCall = autoCall;
            return this;
        }

        public WebRTCConfig build() {
            if (botId == null || botId.isEmpty()) {
                throw new IllegalArgumentException("botId is required");
            }
            if (clientId == null || clientId.isEmpty()) {
                throw new IllegalArgumentException("clientId is required");
            }
            if (clientSecret == null || clientSecret.isEmpty()) {
                throw new IllegalArgumentException("clientSecret is required");
            }
            if (identity == null || identity.isEmpty()) {
                throw new IllegalArgumentException("identity is required");
            }
            if (webSocketUrl == null || webSocketUrl.isEmpty()) {
                throw new IllegalArgumentException("webSocketUrl is required");
            }
            if (jwtServiceUrl == null || jwtServiceUrl.isEmpty()) {
                throw new IllegalArgumentException("jwtServiceUrl is required");
            }
            if (serverUrl == null || serverUrl.isEmpty()) {
                throw new IllegalArgumentException("serverUrl is required");
            }
            if (sipDomain == null || sipDomain.isEmpty()) {
                throw new IllegalArgumentException("sipDomain is required");
            }
            return new WebRTCConfig(this);
        }
    }
}
