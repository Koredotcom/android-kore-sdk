package com.kore.findlysdk.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class SearchResultsModel implements Serializable
{
    private LiveSearchResultsDataModel faq;
    private LiveSearchResultsDataModel web;
    private LiveSearchResultsDataModel task;
    private LiveSearchResultsDataModel file;
    private ArrayList<LiveSearchResultsModel> data;
    private LiveSearchResultsDataModel default_group;

    public ArrayList<LiveSearchResultsModel> getData() {
        return data;
    }

    public LiveSearchResultsDataModel getWeb() {
        return web;
    }

    public LiveSearchResultsDataModel getFile() {
        return file;
    }

    public LiveSearchResultsDataModel getFaq() {
        return faq;
    }

    public LiveSearchResultsDataModel getTask() {
        return task;
    }

    public void setData(ArrayList<LiveSearchResultsModel> data) {
        this.data = data;
    }

    public void setWeb(LiveSearchResultsDataModel web) {
        this.web = web;
    }

    public void setTask(LiveSearchResultsDataModel task) {
        this.task = task;
    }

    public void setFile(LiveSearchResultsDataModel file) {
        this.file = file;
    }

    public void setFaq(LiveSearchResultsDataModel faq) {
        this.faq = faq;
    }

    public LiveSearchResultsDataModel getDefault_group() {
        return default_group;
    }

    public void setDefault_group(LiveSearchResultsDataModel default_group) {
        this.default_group = default_group;
    }
}
