package com.kore.ai.widgetsdk.models;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Shiva Krishna on 11/6/2017.
 */

public class BotLineChartDataModel {
    private String title;
    private List<Float> values;
    private List<String> displayValues;

    public void setValues(List<Float> values) {
        this.values = values;
    }

    public List<String> getDisplayValues() {
        return displayValues;
    }

    public void setDisplayValues(List<String> displayValues) {
        this.displayValues = displayValues;
    }



    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<Float> getValues() {
        return values;
    }

    public void setValues(ArrayList<Float> values) {
        this.values = values;
    }
}
