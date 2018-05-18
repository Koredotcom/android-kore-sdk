package kore.botssdk.models;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Shiva Krishna on 11/6/2017.
 */

public class BotLineChartDataModel {
    private String title;
    private List<Float> values;

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
