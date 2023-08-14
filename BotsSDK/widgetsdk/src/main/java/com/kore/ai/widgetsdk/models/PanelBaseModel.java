package com.kore.ai.widgetsdk.models;

import androidx.annotation.NonNull;

import java.io.Serializable;

public class PanelBaseModel implements Serializable,Cloneable {


    @NonNull
    @Override
    public PanelBaseModel clone() throws CloneNotSupportedException {
        PanelBaseModel baseModel = (PanelBaseModel) super.clone();
        baseModel.setData(baseModel.getData().clone());
        return baseModel;
    }

    PanelResponseData.Panel data;



    public PanelResponseData.Panel getData() {
        return data;
    }

    public final void setData(PanelResponseData.Panel data) {
        this.data = data;
    }
}
