package kore.botssdk.models;

import java.util.ArrayList;

public class ValueModel {
    private String type;
    private int text;
    private ArrayList<BotButtonModel> buttons = null;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getText() {
        return text;
    }

    public void setText(int text) {
        this.text = text;
    }

    public ArrayList<BotButtonModel> getButtons() {
        return buttons;
    }

    public void setButtons(ArrayList<BotButtonModel> buttons) {
        this.buttons = buttons;
    }
}
