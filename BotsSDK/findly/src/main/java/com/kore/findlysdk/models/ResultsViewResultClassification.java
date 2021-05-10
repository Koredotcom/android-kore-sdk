package com.kore.findlysdk.models;

import java.io.Serializable;

public class ResultsViewResultClassification implements Serializable
{
    private String isEnabled;
    private String sourceType;

    public void setIsEnabled(String isEnabled) {
        this.isEnabled = isEnabled;
    }

    public void setSourceType(String sourceType) {
        this.sourceType = sourceType;
    }
}
