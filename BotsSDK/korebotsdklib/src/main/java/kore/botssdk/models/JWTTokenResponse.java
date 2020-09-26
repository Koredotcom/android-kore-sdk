package kore.botssdk.models;

import kore.botssdk.net.RestResponse;

public class JWTTokenResponse{
    private String jwt;
    private String streamId;

    public RestResponse.BotCustomData getCustomData() {
        return customData;
    }

    public void setCustomData(RestResponse.BotCustomData customData) {
        this.customData = customData;
    }

    private String botName;
    private String botsUrl;

    private RestResponse.BotCustomData customData;
    public String getIP() {
        return IP;
    }

    public void setIP(String IP) {
        this.IP = IP;
    }

    private String IP;

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    private String taskId;

    public String getStreamId() {
        return streamId;
    }

    public void setStreamId(String streamId) {
        this.streamId = streamId;
    }

    public String getBotName() {
        return botName;
    }

    public void setBotName(String botName) {
        this.botName = botName;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public void setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
    }

    private String clientId;
    private String clientSecret;

    /**
     * @return
     * The jwt
     */
    public String getJwt() {
        return jwt;
    }
    /**
     * @param jwt
     * The jwt
     */
    public void setJwt(String jwt) {
        this.jwt = jwt;
    }

    public String getBotsUrl() {
        return botsUrl;
    }

    public void setBotsUrl(String botsUrl) {
        this.botsUrl = botsUrl;
    }
}