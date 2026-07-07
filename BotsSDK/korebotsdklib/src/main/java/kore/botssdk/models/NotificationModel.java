package kore.botssdk.models;

public class NotificationModel {

    protected String userId;
    protected String accessToken;
    protected String deviceId;

    public NotificationModel(String userId, String accessToken, String deviceId) {
        this.userId = userId;
        this.accessToken = accessToken;
        this.deviceId = deviceId;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public String getUserId() {
        return userId;
    }
}
