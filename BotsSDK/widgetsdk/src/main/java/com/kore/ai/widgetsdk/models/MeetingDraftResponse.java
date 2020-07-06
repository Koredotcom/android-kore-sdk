package com.kore.ai.widgetsdk.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MeetingDraftResponse {

    @SerializedName("status")
    @Expose
    private String status;


    @SerializedName("kId")
    @Expose
    private String kId;

    public String getkId() {
        return kId;
    }

    public void setkId(String kId) {
        this.kId = kId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}