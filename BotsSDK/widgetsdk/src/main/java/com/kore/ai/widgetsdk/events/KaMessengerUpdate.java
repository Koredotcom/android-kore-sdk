package com.kore.ai.widgetsdk.events;

public class KaMessengerUpdate {

    boolean msgUpdate;

    boolean teamUpdate;

    String uri;

    String teamId;

    public String getTeamId() {
        return teamId;
    }

    public void setTeamId(String teamId) {
        this.teamId = teamId;
    }


    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public boolean isMsgUpdate() {
        return msgUpdate;
    }

    public void setMsgUpdate(boolean msgUpdate) {
        this.msgUpdate = msgUpdate;
    }

    public boolean isTeamUpdate() {
        return teamUpdate;
    }

    public void setTeamUpdate(boolean teamUpdate) {
        this.teamUpdate = teamUpdate;
    }
}
