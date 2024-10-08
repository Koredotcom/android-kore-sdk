package kore.botssdk.models;

public class CardTemplateButtonModel {
    private String title;
    private String type;
    private String payload;
    private HeaderStyles buttonStyles;

    public String getTitle() {
        return title;
    }

    public String getPayload() {
        return payload;
    }

    public String getType() {
        return type;
    }

    public HeaderStyles getButtonStyles() {
        return buttonStyles;
    }
}
