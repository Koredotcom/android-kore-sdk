package kore.botssdk.event;

public class RTMConnectionEvent {

    public boolean isConnected() {
        return isConnected;
    }

    public void setConnected(boolean connected) {
        isConnected = connected;
    }

    private boolean isConnected;

    public RTMConnectionEvent(boolean isConnected){
        this.isConnected = isConnected;
    }
}
