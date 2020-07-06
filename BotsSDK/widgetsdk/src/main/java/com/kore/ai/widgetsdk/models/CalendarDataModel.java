package com.kore.ai.widgetsdk.models;

/**
 * Created by Ramachandra Pradeep on 28-Aug-18.
 */

public class CalendarDataModel {
    public String mId;
    public String hostType;
    public String hostName;
    public String title;
    public boolean append_uttrance;
    public CalendarDataModel(String mId, String hostName, String hostType, String title, boolean append_uttrance){
        this.mId = mId;
        this.hostName = hostName;
        this.hostType = hostType;
        this.title = title;
        this.append_uttrance=append_uttrance;
    }
}
