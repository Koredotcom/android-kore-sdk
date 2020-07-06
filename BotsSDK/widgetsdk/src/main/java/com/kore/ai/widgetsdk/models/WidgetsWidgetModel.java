package com.kore.ai.widgetsdk.models;

import java.io.Serializable;
import java.util.List;

public class WidgetsWidgetModel implements Serializable
{
    private List<Widget> data;

    public List<Widget> getData()
    {
        return data;
    }

    public void setData(List<Widget> data)
    {
        this.data = data;
    }
}
