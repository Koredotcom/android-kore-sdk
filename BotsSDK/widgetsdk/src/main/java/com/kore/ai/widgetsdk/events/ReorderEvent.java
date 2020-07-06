package com.kore.ai.widgetsdk.events;

import com.kore.ai.widgetsdk.models.PanelBaseModel;

public class ReorderEvent {

    PanelBaseModel panel;
    public ReorderEvent(PanelBaseModel panel)
    {
      this.panel=panel;
    }


 public PanelBaseModel getPanelData()
    {

        return panel;
    }

}
