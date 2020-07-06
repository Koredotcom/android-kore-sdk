package com.kore.ai.widgetsdk.models;

import java.io.Serializable;
import java.util.List;

public class WidgetsDataModel implements Serializable
{
    private List<BaseChartModel> data;

    public List<BaseChartModel> getData()
    {
        return data;
    }

    public void setData(List<BaseChartModel> data)
    {
        this.data = data;
    }
}
