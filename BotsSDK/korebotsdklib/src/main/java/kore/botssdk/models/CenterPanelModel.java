package kore.botssdk.models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class CenterPanelModel {
    private String type;
    @SerializedName("data")
    private ArrayList<DataModel> dataModels;

    public String getType() {
        return type;
    }

    public ArrayList<DataModel> getDataModels() {
        return dataModels;
    }
}
