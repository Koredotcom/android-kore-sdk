package kore.botssdk.models;

import com.google.gson.annotations.SerializedName;

public class AnswerPayloadModel {
    @SerializedName("center_panel")
    private CenterPanelModel centerPanel;

    public CenterPanelModel getCenterPanel() {
        return centerPanel;
    }
}
