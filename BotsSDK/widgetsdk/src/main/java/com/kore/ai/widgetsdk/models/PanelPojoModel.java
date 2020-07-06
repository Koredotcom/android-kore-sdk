package com.kore.ai.widgetsdk.models;

import java.io.Serializable;
import java.util.List;

public class PanelPojoModel implements Serializable
{
    private List<PanelResponseData.Panel> panels = null;

    public void setPanels(List<PanelResponseData.Panel> panels)
    {
        this.panels = panels;
    }

    public List<PanelResponseData.Panel> getPanel() {
        return panels;
    }
}
