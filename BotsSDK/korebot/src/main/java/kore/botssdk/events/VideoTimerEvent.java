package kore.botssdk.events;

public class VideoTimerEvent
{
    private double currentPos;
    private long time;

    public void setCurrentPos(double currentPos) {
        this.currentPos = currentPos;
    }

    public double getCurrentPos() {
        return currentPos;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
}
