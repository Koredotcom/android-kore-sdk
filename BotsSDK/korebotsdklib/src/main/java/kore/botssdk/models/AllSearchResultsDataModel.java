package kore.botssdk.models;

import java.util.ArrayList;

public class AllSearchResultsDataModel
{
    private ArrayList<LiveSearchResultsModel> data;
    private int doc_count;

    public ArrayList<LiveSearchResultsModel> getData() {
        return data;
    }

    public void setData(ArrayList<LiveSearchResultsModel> data) {
        this.data = data;
    }

    public int getDoc_count() {
        return doc_count;
    }

    public void setDoc_count(int doc_count) {
        this.doc_count = doc_count;
    }
}
