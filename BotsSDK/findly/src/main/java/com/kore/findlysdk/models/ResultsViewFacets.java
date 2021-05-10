package com.kore.findlysdk.models;

import java.io.Serializable;

public class ResultsViewFacets implements Serializable
{
    private String aligned;
    private String isEnabled;

    public void setIsEnabled(String isEnabled) {
        this.isEnabled = isEnabled;
    }

    public void setAligned(String aligned) {
        this.aligned = aligned;
    }

    public String getAligned() {
        return aligned;
    }

    public String getIsEnabled() {
        return isEnabled;
    }

}
