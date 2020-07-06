package com.kore.ai.widgetsdk.models;

import java.util.List;

/**
 * Created by Ramachandra Pradeep on 11-Apr-18.
 */

public class BotBarChartDataModel {
    private String title;
    private List<Float> values;
    private List<String> displayValues;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<Float> getValues() {
        return values;
    }

    public void setValues(List<Float> values) {
        this.values = values;
    }

    public List<String> getDisplayValues() {
        return displayValues;
    }

    public void setDisplayValues(List<String> displayValues) {
        this.displayValues = displayValues;
    }


}
