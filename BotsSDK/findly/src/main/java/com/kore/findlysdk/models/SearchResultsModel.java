package com.kore.findlysdk.models;

import java.io.Serializable;
import java.util.ArrayList;

public class SearchResultsModel implements Serializable
{
    private ArrayList<LiveSearchResultsModel> faq;
    private ArrayList<LiveSearchResultsModel> page;
    private ArrayList<LiveSearchResultsModel> task;
    private ArrayList<LiveSearchResultsModel> document;

    public ArrayList<LiveSearchResultsModel> getFaq() {
        return faq;
    }

    public ArrayList<LiveSearchResultsModel> getPage() {
        return page;
    }

    public void setPage(ArrayList<LiveSearchResultsModel> page) {
        this.page = page;
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

    public ArrayList<LiveSearchResultsModel> getDocument() {
        return document;
    }

    public void setDocument(ArrayList<LiveSearchResultsModel> document) {
        this.document = document;
    }
}
