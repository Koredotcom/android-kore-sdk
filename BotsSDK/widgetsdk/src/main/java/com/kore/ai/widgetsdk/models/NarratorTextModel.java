package com.kore.ai.widgetsdk.models;

public class NarratorTextModel {
    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getComposeText() {
        return composeText;
    }

    public void setComposeText(String composeText) {
        this.composeText = composeText;
    }

    public String getHandFocus() {
        return handFocus;
    }

    public void setHandFocus(String handFocus) {
        this.handFocus = handFocus;
    }

    String text, composeText, handFocus;
}
