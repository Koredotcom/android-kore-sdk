package com.kore.ai.widgetsdk.models;

import java.io.Serializable;
import java.util.List;

public class PanelWidgetsDataModel implements Serializable  {

    private List<WidgetsModel> data = null;

    public List<WidgetsModel> getData() {
        return data;
    }

    public void setData(List<WidgetsModel> data) {
        this.data = data;
    }
}
