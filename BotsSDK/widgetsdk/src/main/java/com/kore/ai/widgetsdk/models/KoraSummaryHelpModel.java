package com.kore.ai.widgetsdk.models;

import java.util.List;

public class KoraSummaryHelpModel {

    private String title;
    private List<ButtonTemplate> body = null;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<ButtonTemplate> getButtons() {
        return body;
    }

    public void setButtons(List<ButtonTemplate> body) {
        this.body = body;
    }

}
