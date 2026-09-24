package artemis.socket;

/** Configuration for the Artemis API-key socket flow. */
public final class ArtemisSocketConfiguration {
    private final String endpoint;
    private final String projectId;
    private final String apiKey;
    private final String channelId;
    private final String channelName;
    private final boolean reconnectionEnabled;
    private final int maxReconnectAttempts;
    private final long reconnectBaseDelayMs;
    private final long reconnectMaxDelayMs;

    private ArtemisSocketConfiguration(Builder builder) {
        endpoint = builder.endpoint;
        projectId = builder.projectId;
        apiKey = builder.apiKey;
        channelId = builder.channelId;
        channelName = builder.channelName;
        reconnectionEnabled = builder.reconnectionEnabled;
        maxReconnectAttempts = builder.maxReconnectAttempts;
        reconnectBaseDelayMs = builder.reconnectBaseDelayMs;
        reconnectMaxDelayMs = builder.reconnectMaxDelayMs;
    }

    public String getEndpoint() {
        return endpoint;
    }

    public String getProjectId() {
        return projectId;
    }

    public String getApiKey() {
        return apiKey;
    }

    public String getChannelId() {
        return channelId;
    }

    public String getChannelName() {
        return channelName;
    }

    public boolean isReconnectionEnabled() {
        return reconnectionEnabled;
    }

    public int getMaxReconnectAttempts() {
        return maxReconnectAttempts;
    }

    public long getReconnectBaseDelayMs() {
        return reconnectBaseDelayMs;
    }

    public long getReconnectMaxDelayMs() {
        return reconnectMaxDelayMs;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private String endpoint = "";
        private String projectId = "";
        private String apiKey = "";
        private String channelId = "";
        private String channelName = "";
        private boolean reconnectionEnabled = true;
        private int maxReconnectAttempts = 5;
        private long reconnectBaseDelayMs = 1000L;
        private long reconnectMaxDelayMs = 30000L;

        public Builder endpoint(String endpoint) {
            this.endpoint = endpoint;
            return this;
        }

        public Builder projectId(String projectId) {
            this.projectId = projectId;
            return this;
        }

        public Builder apiKey(String apiKey) {
            this.apiKey = apiKey;
            return this;
        }

        public Builder channelId(String channelId) {
            this.channelId = channelId;
            return this;
        }

        public Builder channelName(String channelName) {
            this.channelName = channelName;
            return this;
        }

        public Builder reconnection(boolean enabled, int maxAttempts, long baseDelayMs, long maxDelayMs) {
            reconnectionEnabled = enabled;
            maxReconnectAttempts = maxAttempts;
            reconnectBaseDelayMs = baseDelayMs;
            reconnectMaxDelayMs = maxDelayMs;
            return this;
        }

        public ArtemisSocketConfiguration build() {
            require(endpoint, "endpoint");
            require(projectId, "project_id");
            require(apiKey, "api_key");
            if (isBlank(channelId) && isBlank(channelName)) {
                throw new IllegalArgumentException("channel_id or channel_name is required");
            }
            if (maxReconnectAttempts < 0 || reconnectBaseDelayMs < 0 || reconnectMaxDelayMs < reconnectBaseDelayMs) {
                throw new IllegalArgumentException("Invalid reconnection configuration");
            }
            return new ArtemisSocketConfiguration(this);
        }

        private static void require(String value, String name) {
            if (isBlank(value)) {
                throw new IllegalArgumentException(name + " is required");
            }
        }

        private static boolean isBlank(String value) {
            return value == null || value.trim().isEmpty();
        }
    }
}
