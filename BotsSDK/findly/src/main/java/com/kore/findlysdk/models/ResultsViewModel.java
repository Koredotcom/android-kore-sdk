package com.kore.findlysdk.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class ResultsViewModel implements Serializable
{
    private ArrayList<ResultsViewSetting> settings;

    public void setSettings(ArrayList<ResultsViewSetting> settings) {
        this.settings = settings;
    }

    public ArrayList<ResultsViewSetting> getSettings() {
        return settings;
    }
}

