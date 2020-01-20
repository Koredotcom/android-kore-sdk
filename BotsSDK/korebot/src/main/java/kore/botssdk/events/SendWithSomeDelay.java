package kore.botssdk.events;

public class SendWithSomeDelay {
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getPayload() {
        return payload;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public boolean isScrollUpNeeded() {
        return isScrollUpNeeded;
    }

    public void setScrollUpNeeded(boolean scrollUpNeeded) {
        isScrollUpNeeded = scrollUpNeeded;
    }

    private String message;
    private String payload;
    private long time;
    private boolean isScrollUpNeeded;


    public SendWithSomeDelay(String message, String payload, long time, boolean isScrollUpNeeded){
        this.message = message;
        this.payload = payload;
        this.time = time;
        this.isScrollUpNeeded = isScrollUpNeeded;
    }

}
