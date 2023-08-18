package kore.botssdk.events;

public class AdvanceListRefreshEvent {
    private String message;
    private String body;
    private String payLoad;

    public void setBody(String body) {
        this.body = body;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setPayLoad(String payLoad) {
        this.payLoad = payLoad;
    }

    public String getBody() {
        return body;
    }

    public String getMessage() {
        return message;
    }

    public String getPayLoad() {
        return payLoad;
    }
}
