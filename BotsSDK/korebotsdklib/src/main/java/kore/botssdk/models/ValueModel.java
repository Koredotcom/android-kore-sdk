package kore.botssdk.models;

import java.util.ArrayList;

public class ValueModel {
    private String type;
    private int text;
    private ArrayList<Widget.Button> buttons = null;

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

    public ArrayList<Widget.Button> getButtons() {
        return buttons;
    }

    public void setButtons(ArrayList<Widget.Button> buttons) {
        this.buttons = buttons;
    }
}
