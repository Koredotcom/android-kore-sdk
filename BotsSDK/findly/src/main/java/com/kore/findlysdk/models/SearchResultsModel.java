package com.kore.findlysdk.models;

import java.io.Serializable;
import java.util.ArrayList;

public class SearchResultsModel implements Serializable
{
    private ArrayList<LiveSearchResultsModel> faq;
    private ArrayList<LiveSearchResultsModel> web;
    private ArrayList<LiveSearchResultsModel> task;
    private ArrayList<LiveSearchResultsModel> file;
    private ArrayList<LiveSearchResultsModel> data;

    public ArrayList<LiveSearchResultsModel> getFaq() {
        return faq;
    }

    public ArrayList<LiveSearchResultsModel> getWeb() { return web; }

    public void setWeb(ArrayList<LiveSearchResultsModel> web) {
        this.web = web;
    }

    public void setFaq(ArrayList<LiveSearchResultsModel> faq) {
        this.faq = faq;
    }

    public void setTask(ArrayList<LiveSearchResultsModel> task) {
        this.task = task;
    }

    public ArrayList<LiveSearchResultsModel> getTask() {
        return task;
    }

    public ArrayList<LiveSearchResultsModel> getFile() {
        return file;
    }

    public void setFile(ArrayList<LiveSearchResultsModel> file) {
        this.file = file;
    }

    public void setData(ArrayList<LiveSearchResultsModel> data) {
        this.data = data;
    }

    public ArrayList<LiveSearchResultsModel> getData() {
        return data;
    }
}
