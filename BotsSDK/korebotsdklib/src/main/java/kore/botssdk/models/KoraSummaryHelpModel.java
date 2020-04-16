package kore.botssdk.models;

import java.util.List;

public class KoraSummaryHelpModel {

    private String text;
    private List<ButtonTemplate> buttons = null;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public List<ButtonTemplate> getButtons() {
        return buttons;
    }

    public void setButtons(List<ButtonTemplate> buttons) {
        this.buttons = buttons;
    }

}
