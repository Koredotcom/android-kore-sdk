package kore.botssdk.models;

import java.util.ArrayList;

/**
 * Created by Shiva Krishna on 11/6/2017.
 */

public class BotLineChartDataModel {
    private String title;
    private ArrayList<Float> values;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public ArrayList<Float> getValues() {
        return values;
    }

    public void setValues(ArrayList<Float> values) {
        this.values = values;
    }
}
