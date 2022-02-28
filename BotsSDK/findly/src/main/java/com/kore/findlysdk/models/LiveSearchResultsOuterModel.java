package com.kore.findlysdk.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class LiveSearchResultsOuterModel implements Serializable
{
    private LiveSearchResultsDataModel faq;
    private LiveSearchResultsDataModel web;
    private LiveSearchResultsDataModel task;
    private LiveSearchResultsDataModel file;
    private LiveSearchResultsDataModel default_group;
    private LiveSearchResultsDataModel data;

//    private ArrayList<HashMap <String,Object>> data = new ArrayList<HashMap<String,Object>>();
//
//    public void setData(ArrayList<HashMap<String, Object>> data) {
//        this.data = data;
//    }
//
//    public ArrayList<HashMap<String, Object>> getData() {
//        return data;
//    }


    public LiveSearchResultsDataModel getData() {
        return data;
    }

    public void setData(LiveSearchResultsDataModel data) {
        this.data = data;
    }

    public void setDefault_group(LiveSearchResultsDataModel default_group) {
        this.default_group = default_group;
    }

    public void setFaq(LiveSearchResultsDataModel faq) {
        this.faq = faq;
    }

    public void setFile(LiveSearchResultsDataModel file) {
        this.file = file;
    }

    public void setTask(LiveSearchResultsDataModel task) {
        this.task = task;
    }

    public void setWeb(LiveSearchResultsDataModel web) {
        this.web = web;
    }

    public LiveSearchResultsDataModel getTask() {
        return task;
    }

    public LiveSearchResultsDataModel getDefault_group() {
        return default_group;
    }

    public LiveSearchResultsDataModel getFaq() {
        return faq;
    }

    public LiveSearchResultsDataModel getFile() {
        return file;
    }

    public LiveSearchResultsDataModel getWeb() {
        return web;
    }

}
