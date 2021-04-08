package kore.botssdk.events;

public class ProfileColorUpdateEvent {
    public String getProfColor() {
        return profColor;
    }

    public void setProfColor(String profColor) {
        this.profColor = profColor;
    }

    private String profColor;

    public ProfileColorUpdateEvent(String profColor){
        this.profColor = profColor;
    }

}
