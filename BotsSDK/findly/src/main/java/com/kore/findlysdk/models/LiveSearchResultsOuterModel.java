package com.kore.findlysdk.models;

import java.io.Serializable;
import java.util.ArrayList;

public class LiveSearchResultsOuterModel implements Serializable
{
    private ArrayList<LiveSearchResultsModel> faq;
    private ArrayList<LiveSearchResultsModel> page;
    private ArrayList<LiveSearchResultsModel> task;
    private ArrayList<LiveSearchResultsModel> document;
    private ArrayList<LiveSearchResultsModel> object;

    public ArrayList<LiveSearchResultsModel> getDocument() {
        return document;
    }

    public ArrayList<LiveSearchResultsModel> getFaq() {
        return faq;
    }

    public ArrayList<LiveSearchResultsModel> getObject() {
        return object;
    }

    public ArrayList<LiveSearchResultsModel> getPage() {
        return page;
    }

    public ArrayList<LiveSearchResultsModel> getTask() {
        return task;
    }

    public void setDocument(ArrayList<LiveSearchResultsModel> document) {
        this.document = document;
    }

    public void setFaq(ArrayList<LiveSearchResultsModel> faq) {
        this.faq = faq;
    }

    public void setObject(ArrayList<LiveSearchResultsModel> object) {
        this.object = object;
    }

    public void setPage(ArrayList<LiveSearchResultsModel> page) {
        this.page = page;
    }

    public void setTask(ArrayList<LiveSearchResultsModel> task) {
        this.task = task;
    }
}
