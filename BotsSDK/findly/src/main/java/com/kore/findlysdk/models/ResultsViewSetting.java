package com.kore.findlysdk.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class ResultsViewSetting implements Serializable
{
    private String _id;
    private String view;
    private ResultsViewResultClassification resultClassification;
    private int maxResultsAllowed;
    @SerializedName("interface")
    @Expose
    private String myInterface;
    private ArrayList<ResultsViewAppearance> appearance;
    private String orderBasedOnRelevance;
    private ResultsViewFacets facets;
    private String createdOn;
    private String streamId;
    private String searchIndexId;
    private String indexPipelineId;

    public void setStreamId(String streamId) {
        this.streamId = streamId;
    }

    public void setSearchIndexId(String searchIndexId) {
        this.searchIndexId = searchIndexId;
    }

    public void setIndexPipelineId(String indexPipelineId) {
        this.indexPipelineId = indexPipelineId;
    }

    public void setCreatedOn(String createdOn) {
        this.createdOn = createdOn;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public void setAppearance(ArrayList<ResultsViewAppearance> appearance) {
        this.appearance = appearance;
    }

    public void setFacets(ResultsViewFacets facets) {
        this.facets = facets;
    }

    public void setInterfaces(String interfaces) {
        this.myInterface = interfaces;
    }

    public void setMaxResultsAllowed(int maxResultsAllowed) {
        this.maxResultsAllowed = maxResultsAllowed;
    }

    public void setOrderBasedOnRelevance(String orderBasedOnRelevance) {
        this.orderBasedOnRelevance = orderBasedOnRelevance;
    }

    public void setResultClassification(ResultsViewResultClassification resultClassification) {
        this.resultClassification = resultClassification;
    }

    public void setView(String view) {
        this.view = view;
    }

    public String getStreamId() {
        return streamId;
    }

    public String getSearchIndexId() {
        return searchIndexId;
    }

    public String getOrderBasedOnRelevance() {
        return orderBasedOnRelevance;
    }

    public String getIndexPipelineId() {
        return indexPipelineId;
    }

    public String getCreatedOn() {
        return createdOn;
    }

    public String get_id() {
        return _id;
    }

    public int getMaxResultsAllowed() {
        return maxResultsAllowed;
    }

    public ArrayList<ResultsViewAppearance> getAppearance() {
        return appearance;
    }

    public ResultsViewFacets getFacets() {
        return facets;
    }

    public ResultsViewResultClassification getResultClassification() {
        return resultClassification;
    }

    public String getInterfaces() {
        return myInterface;
    }

    public String getView() {
        return view;
    }
}
