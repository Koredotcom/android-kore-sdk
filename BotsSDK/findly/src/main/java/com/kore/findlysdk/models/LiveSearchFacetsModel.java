package com.kore.findlysdk.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LiveSearchFacetsModel
{
    private int faq;
    private int page;
    private int web;
    private int file;
    private int task;
    private int document;
    private int data;
    @SerializedName("all results")
    @Expose
    private String all_results;

    public int getFaq() {
        return faq;
    }

    public int getPage() {
        return page;
    }

    public int getTask() {
        return task;
    }

    public void setFaq(int faq) {
        this.faq = faq;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public void setTask(int task) {
        this.task = task;
    }

    public int getDocument() {
        return document;
    }

    public void setDocument(int document) {
        this.document = document;
    }

    public void setAll_results(String all_results) {
        this.all_results = all_results;
    }

    public String getAll_results() {
        return all_results;
    }

    public void setWeb(int web) {
        this.web = web;
    }

    public void setFile(int file) {
        this.file = file;
    }

    public void setData(int data) {
        this.data = data;
    }

    public int getData() {
        return data;
    }

    public int getFile() {
        return file;
    }

    public int getWeb() {
        return web;
    }
}
